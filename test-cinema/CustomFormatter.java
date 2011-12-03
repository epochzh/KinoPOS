import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

// http://javablog.co.uk/2008/07/12/logging-with-javautillogging/

@SuppressWarnings("all")
public class CustomFormatter extends Formatter {
    // milliseconds can be nice for rough performance numbers
    // private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS Z";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    // private static final String DEFAULT_FORMAT = "%L: %t %E %m";
    private static final String DEFAULT_FORMAT = "%L | %t | %E | %m";
    private final boolean needsExpensiveEclipseFormat;
    private final MessageFormat messageFormat;
    private final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    public CustomFormatter() {
        super();
        // load the format from logging.properties
        final String propName = this.getClass().getName() + ".format";
        String format = LogManager.getLogManager().getProperty(propName);
        if ((format == null) || (format.trim().length() == 0)) {
            format = DEFAULT_FORMAT;
        }
        if (format.contains("{") || format.contains("}")) {
            throw new IllegalArgumentException("curly braces not allowed");
        }
        // convert it into the MessageFormat form
        format = format
        .replace("%L", "{0}")
        .replace("%m", "{1}")
        .replace("%M", "{2}")
        .replace("%t", "{3}")
        .replace("%c", "{4}")
        .replace("%T", "{5}")
        .replace("%n", "{6}")
        .replace("%C", "{7}")
        .replace("%E", "{8}") + "\n";
        this.needsExpensiveEclipseFormat = format.contains("{8}");
        this.messageFormat = new MessageFormat(format);
    }
    @Override
    public String format(final LogRecord record) {
        final String[] arguments = new String[9];
        // %L
        arguments[0] = record.getLevel().toString();
        // %m
        arguments[1] = record.getMessage();
        // sometimes the message is empty, but there is a throwable
        if ((arguments[1] == null) || (arguments[1].length() == 0)) {
            final Throwable thrown = record.getThrown();
            if (thrown != null) {
                arguments[1] = thrown.getMessage();
            }
        }
        // %M
        if (record.getSourceMethodName() != null) {
            arguments[2] = record.getSourceMethodName();
        } else {
            arguments[2] = "?";
        }
        // %t
        final Date date = new Date(record.getMillis());
        synchronized (this.dateFormat) {
            arguments[3] = this.dateFormat.format(date);
        }
        // %c
        if (record.getSourceClassName() != null) {
            arguments[4] = record.getSourceClassName();
        } else {
            arguments[4] = "?";
        }
        // %T
        arguments[5] = Integer.valueOf(record.getThreadID()).toString();
        // %n
        arguments[6] = record.getLoggerName();
        // %C
        final int start = arguments[4].lastIndexOf(".") + 1;
        if ((start > 0) && (start < arguments[4].length())) {
            arguments[7] = arguments[4].substring(start);
        } else {
            arguments[7] = arguments[4];
        }
        // %E
        if(this.needsExpensiveEclipseFormat){
            arguments[8] = getEclipseFormat(); // gets a stackTrace to generate.
        } else {
            arguments[8] = "(?:?)";
        }
        synchronized (this.messageFormat) {
            return this.messageFormat.format(arguments);
        }
    }
    /**
     * Returns caller location information in eclipse format eg (Filename.java:23)
     * WARNING Generating caller location information is extremely slow. 
     * It's use should be avoided unless execution speed is not an issue.
     * @return the eclipse format
     */
    public static String getEclipseFormat() {
        // getStackTrace can be expensive
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        final StringBuilder sb = new StringBuilder();
        // Here is an example of the typical stack trace we get back.
        //        level 0 (Thread.java:1436) getStackTrace
        //        level 1 (CustomFormatter.java:139) getFileLineNumber
        //        level 2 (CustomFormatter.java:128) format
        //        level 3 (StreamHandler.java:179) publish
        //        level 4 (ConsoleHandler.java:88) publish
        //        level 5 (Logger.java:458) log
        //        level 6 (Logger.java:480) doLog
        //        level 7 (Logger.java:503) log
        //        level 8 (YourCodeHere.java:26) someMethod
        if(stackTrace.length >= 9) {
            String fileName = stackTrace[8].getFileName();
            int lineNumber = stackTrace[8].getLineNumber();
                // Each of these calls back into logger.logp with the appropriate 
                // level and message. This adds one extra level to the stack trace
                //              logger.finest("finest message");
                //              logger.finer("finer message");
                //              logger.fine("fine message");
                //              logger.info("info message");
                //              logger.warning("warning message");
                //              logger.severe("severe message");
                //              logger.entering("SomeClass", "aMethod");
                //              logger.exiting("SomeClass", "aMethod");
                //              logger.exiting("SomeClass", "aMethod", "returnval");
                //              logger.config("config message");
                //
                // Here is an example stack trace from a call to exiting
                //              level 0 (Thread.java:1436) getStackTrace
                //              level 1 (CustomFormatter.java:139) getFileLineNumber
                //              level 2 (CustomFormatter.java:128) format
                //              level 3 (StreamHandler.java:179) publish
                //              level 4 (ConsoleHandler.java:88) publish
                //              level 5 (Logger.java:458) log
                //              level 6 (Logger.java:480) doLog
                //              level 7 (Logger.java:623) logp
                //              level 8 (Logger.java:938) exiting
                //              level 9 (YourCodeHere.java:27) someMethod
                // 
                // We could check the name of the method we are in and only go one level deeper
                // if it is one of "finest", "finer", "fine", "info", "warning",
                // "severe", "config", "entering" or "exiting" but that would be too much trouble.
                // If the stack is in Logger.java and we can go one level deeper - do it.
            if((stackTrace.length >= 10) && "Logger.java".equals(fileName)) {
                fileName = stackTrace[9].getFileName();
                lineNumber = stackTrace[9].getLineNumber();
            }
            // In Eclipse console, text like (CustomFormatter.java:179) becomes 
            // a blue link that jumps to that line in that file when clicked.
            sb.append('(');
            sb.append(fileName);
            sb.append(':');
            sb.append(lineNumber);
            sb.append(')');
        }
        return sb.toString();
    }
}

//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.data.loader;

import com.openbravo.basic.BasicException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BatchSentence extends BaseSentence {

    protected Session m_s;

    protected HashMap<String, String> m_parameters;

    /** Creates a new instance of BatchSentence */
    public BatchSentence(final Session s) {
        this.m_s = s;
        this.m_parameters = new HashMap<String, String>();
    }

    public void putParameter(final String name, final String replacement) {
        this.m_parameters.put(name, replacement);
    }

    protected abstract Reader getReader() throws BasicException;

    public class ExceptionsResultSet implements DataResultSet {

        List l;

        int m_iIndex;

        public ExceptionsResultSet(final List l) {
            this.l = l;
            this.m_iIndex = -1;
        }

        // XXX: CINEMA
        @Override
        public Byte getByte(final int columnIndex) throws BasicException {
            throw new BasicException(LocalRes
                .getIntString("exception.nodataset"));
        }

        // XXX: CINEMA
        @Override
        public Character getCharacter(final int columnIndex)
        throws BasicException {
            throw new BasicException(LocalRes
                .getIntString("exception.nodataset"));
        }

        // XXX: CINEMA
        @Override
        public Long getLong(final int columnIndex) throws BasicException {
            throw new BasicException(LocalRes
                .getIntString("exception.nodataset"));
        }

        // XXX: CINEMA
        @Override
        public Short getShort(final int columnIndex) throws BasicException {
            throw new BasicException(LocalRes
                .getIntString("exception.nodataset"));
        }

        @Override
        public Integer getInt(final int columnIndex) throws BasicException {
            throw new BasicException(LocalRes
                .getIntString("exception.nodataset"));
        }

        @Override
        public String getString(final int columnIndex) throws BasicException {
            throw new BasicException(LocalRes
                .getIntString("exception.nodataset"));
        }

        @Override
        public Double getDouble(final int columnIndex) throws BasicException {
            throw new BasicException(LocalRes
                .getIntString("exception.nodataset"));
        }

        @Override
        public Boolean getBoolean(final int columnIndex) throws BasicException {
            throw new BasicException(LocalRes
                .getIntString("exception.nodataset"));
        }

        @Override
        public java.util.Date getTimestamp(final int columnIndex)
        throws BasicException {
            throw new BasicException(LocalRes
                .getIntString("exception.nodataset"));
        }

        // public java.io.InputStream getBinaryStream(int columnIndex) throws
        // DataException;
        @Override
        public byte[] getBytes(final int columnIndex) throws BasicException {
            throw new BasicException(LocalRes
                .getIntString("exception.nodataset"));
        }

        @Override
        public Object getObject(final int columnIndex) throws BasicException {
            throw new BasicException(LocalRes
                .getIntString("exception.nodataset"));
        }

        // public int getColumnCount() throws DataException;
        @Override
        public DataField[] getDataField() throws BasicException {
            throw new BasicException(LocalRes
                .getIntString("exception.nodataset"));
        }

        @Override
        public Object getCurrent() throws BasicException {
            if ((this.m_iIndex < 0) || (this.m_iIndex >= this.l.size())) {
                throw new BasicException(LocalRes
                    .getIntString("exception.outofbounds"));
            } else {
                return this.l.get(this.m_iIndex);
            }
        }

        @Override
        public boolean next() throws BasicException {
            return ++this.m_iIndex < this.l.size();
        }

        @Override
        public void close() throws BasicException {
        }

        @Override
        public int updateCount() {
            return 0;
        }
    }

    @Override
    public final void closeExec() throws BasicException {
    }

    @Override
    public final DataResultSet moreResults() throws BasicException {
        return null;
    }

    @Override
    public DataResultSet openExec(final Object params) throws BasicException {

        final BufferedReader br = new BufferedReader(this.getReader());

        String sLine;
        StringBuffer sSentence = new StringBuffer();
        final List aExceptions = new ArrayList();

        try {
            while ((sLine = br.readLine()) != null) {
                sLine = sLine.trim();
                if (!sLine.equals("") && !sLine.startsWith("--")) {
                    // No es un comentario ni linea vacia
                    if (sLine.endsWith(";")) {
                        // ha terminado la sentencia
                        sSentence
                            .append(sLine.substring(0, sLine.length() - 1));

                        // File parameters
                        final Pattern pattern =
                            Pattern.compile("\\$(\\w+)\\{([^}]*)\\}");
                        final Matcher matcher =
                            pattern.matcher(sSentence.toString());
                        final List paramlist = new ArrayList();

                        // Replace all occurrences of pattern in input
                        final StringBuffer buf = new StringBuffer();
                        while (matcher.find()) {
                            if ("FILE".equals(matcher.group(1))) {
                                paramlist.add(ImageUtils
                                    .getBytesFromResource(matcher.group(2)));
                                matcher.appendReplacement(buf, "?");
                            } else {
                                final String replacement =
                                    this.m_parameters.get(matcher.group(1));
                                if (replacement == null) {
                                    matcher.appendReplacement(buf, Matcher
                                        .quoteReplacement(matcher.group(0)));
                                } else {
                                    paramlist.add(replacement);
                                    matcher.appendReplacement(buf, "?");
                                }
                            }
                        }
                        matcher.appendTail(buf);

                        // La disparo
                        try {
                            BaseSentence sent;
                            if (paramlist.size() == 0) {
                                sent =
                                    new StaticSentence(this.m_s, buf.toString());
                                sent.exec();
                            } else {
                                sent =
                                    new PreparedSentence(this.m_s, buf
                                        .toString(),
                                        SerializerWriteBuilder.INSTANCE);
                                sent.exec(new VarParams(paramlist));
                            }
                        } catch (final BasicException eD) {
                            aExceptions.add(eD);
                        }
                        sSentence = new StringBuffer();

                    } else {
                        // la sentencia continua en la linea siguiente
                        sSentence.append(sLine);
                    }
                }
            }

            br.close();

        } catch (final IOException eIO) {
            throw new BasicException(LocalRes
                .getIntString("exception.noreadfile"), eIO);
        }

        if (sSentence.length() > 0) {
            // ha quedado una sentencia inacabada
            aExceptions.add(new BasicException(LocalRes
                .getIntString("exception.nofinishedfile")));
        }

        return new ExceptionsResultSet(aExceptions);
    }

    private static class VarParams implements SerializableWrite {

        private final List l;

        public VarParams(final List l) {
            this.l = l;
        }

        @Override
        public void writeValues(final DataWrite dp) throws BasicException {
            for (int i = 0; i < this.l.size(); i++) {
                final Object v = this.l.get(i);
                if (v instanceof String) {
                    dp.setString(i + 1, (String) v);
                } else if (v instanceof byte[]) {
                    dp.setBytes(i + 1, (byte[]) this.l.get(i));
                } else {
                    dp.setObject(i + 1, v);
                }
            }
        }
    }
}

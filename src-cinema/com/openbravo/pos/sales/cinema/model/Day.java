package com.openbravo.pos.sales.cinema.model;

/**
 */
public enum Day {

    /**
     */
    SUNDAY("sunday"),

    /**
     */
    MONDAY("monday"),

    /**
     */
    TUESDAY("tuesday"),

    /**
     */
    WEDNESDAY("wednesday"),

    /**
     */
    THURSDAY("thursday"),

    /**
     */
    FRIDAY("friday"),

    /**
     */
    SATURDAY("saturday");

    /**
     * @param name
     * @return the {@link Day} associated to the specified name
     */
    public static Day fromName(final String name) {
        for (final Day day : values()) {
            if (day.name.equals(name)) {
                return day;
            }
        }

        throw new IllegalArgumentException("name: " + name);
    }

    /**
     */
    private final String name;

    /**
     * @param name
     */
    private Day(final String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }
}

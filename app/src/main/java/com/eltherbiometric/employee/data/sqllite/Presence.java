package com.eltherbiometric.employee.data.sqllite;

public class Presence {
    public static final String TABLE_NAME = "presence";
    public static final String COLUMN_NIK = "nik";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_METHOD = "method";
    public static final String COLUMN_LATITUDE = "lat";
    public static final String COLUMN_LONGITUDE = "long";
    public static final String COLUMN_STATUS = "status";

    public static String createTable(){
        String[] fields = new String[]{
                TABLE_NAME,
                COLUMN_NIK,
                COLUMN_DATE,
                COLUMN_TIME,
                COLUMN_METHOD,
                COLUMN_LATITUDE,
                COLUMN_LONGITUDE,
                COLUMN_STATUS,
        };
        return String.format("CREATE TABLE %s (%s TEXT, " +
                        "%s TEXT NULL, " +
                        "%s TEXT NULL, " +
                        "%s TEXT NULL, " +
                        "%s TEXT NULL, " +
                        "%s TEXT NULL, " +
                        "%s TEXT NULL);",
                fields);
    }

    public static String dropTable(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}

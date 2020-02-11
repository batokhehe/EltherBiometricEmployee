package com.eltherbiometric.employee.data.sqllite;

public class User {
    public static final String TABLE_NAME = "user";
    public static final String COLUMN_NIK = "nik";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DIVISION = "division";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    public static String createTable(){
        String[] fields = new String[]{
                TABLE_NAME,
                COLUMN_NIK,
                COLUMN_NAME,
                COLUMN_DIVISION,
                COLUMN_USERNAME,
                COLUMN_PASSWORD
        };
        return String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY, " +
                        "%s TEXT NULL, %s TEXT NULL, %s TEXT NULL, %s TEXT NULL);",
                fields);
    }

    public static String dropTable(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}

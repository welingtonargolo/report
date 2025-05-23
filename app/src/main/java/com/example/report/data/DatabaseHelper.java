package com.example.report.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cityreport.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_PROBLEMS = "problems";

    // Common Column Names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";

    // Users Table Columns
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // Categories Table Columns
    public static final String COLUMN_DESCRIPTION = "description";

    // Problems Table Columns
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_DATETIME = "datetime";
    public static final String COLUMN_STATUS = "status";

    // Create Table Statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT NOT NULL,"
            + COLUMN_EMAIL + " TEXT UNIQUE NOT NULL,"
            + COLUMN_PASSWORD + " TEXT NOT NULL"
            + ")";

    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT NOT NULL,"
            + COLUMN_DESCRIPTION + " TEXT"
            + ")";

    private static final String CREATE_TABLE_PROBLEMS = "CREATE TABLE " + TABLE_PROBLEMS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CATEGORY_ID + " INTEGER NOT NULL,"
            + COLUMN_USER_ID + " INTEGER NOT NULL,"
            + COLUMN_DESCRIPTION + " TEXT NOT NULL,"
            + COLUMN_PHOTO + " BLOB,"
            + COLUMN_LATITUDE + " REAL NOT NULL,"
            + COLUMN_LONGITUDE + " REAL NOT NULL,"
            + COLUMN_DATETIME + " TEXT NOT NULL,"
            + COLUMN_STATUS + " TEXT NOT NULL,"
            + "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_ID + "),"
            + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_PROBLEMS);

        // Initialize default categories
        initializeDefaultCategories(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROBLEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }

    private void initializeDefaultCategories(SQLiteDatabase db) {
        String[][] defaultCategories = {
            {"Buraco na Via", "Buracos, rachaduras e problemas no asfalto"},
            {"Iluminação", "Problemas com postes e iluminação pública"},
            {"Lixo", "Acúmulo de lixo e problemas de coleta"},
            {"Calçada", "Problemas em calçadas e passeios"},
            {"Sinalização", "Problemas com placas e sinais de trânsito"},
            {"Alagamento", "Pontos de alagamento e problemas de drenagem"},
            {"Poluição", "Problemas ambientais e poluição"},
            {"Outros", "Outros problemas não categorizados"}
        };

        for (String[] category : defaultCategories) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, category[0]);
            values.put(COLUMN_DESCRIPTION, category[1]);
            db.insert(TABLE_CATEGORIES, null, values);
        }
    }
}

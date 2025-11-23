package com.example.labubas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "labubas.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_REPAIR = "repair_requests";
    private static final String TABLE_PAINTING = "painting_requests";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_OWNER_NAME = "ownerName";
    private static final String COLUMN_PHONE = "phoneNumber";
    private static final String COLUMN_CAR_MODEL = "carModel";
    private static final String COLUMN_ISSUE = "issueDescription";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_COMPLETION_DATE = "completionDate";
    private static final String COLUMN_COMPLETION_TIME = "completionTime";
    private static final String COLUMN_COLOR = "color";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRepairTable = "CREATE TABLE " + TABLE_REPAIR + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_OWNER_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_CAR_MODEL + " TEXT, " +
                COLUMN_ISSUE + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_STATUS + " TEXT DEFAULT 'в работе', " +
                COLUMN_COMPLETION_DATE + " TEXT, " +
                COLUMN_COMPLETION_TIME + " TEXT" +
                ")";

        String createPaintingTable = "CREATE TABLE " + TABLE_PAINTING + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_OWNER_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_CAR_MODEL + " TEXT, " +
                COLUMN_COLOR + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_STATUS + " TEXT DEFAULT 'в работе', " +
                COLUMN_COMPLETION_DATE + " TEXT" +
                ")";

        db.execSQL(createRepairTable);
        db.execSQL(createPaintingTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPAIR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAINTING);
        onCreate(db);
    }

    public long insertRepairRequest(Map<String, String> request) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OWNER_NAME, request.get(COLUMN_OWNER_NAME));
        values.put(COLUMN_PHONE, request.get(COLUMN_PHONE));
        values.put(COLUMN_CAR_MODEL, request.get(COLUMN_CAR_MODEL));
        values.put(COLUMN_ISSUE, request.get(COLUMN_ISSUE));
        values.put(COLUMN_DATE, request.get(COLUMN_DATE));
        values.put(COLUMN_TIME, request.get(COLUMN_TIME));
        return db.insert(TABLE_REPAIR, null, values);
    }

    public long insertPaintingRequest(Map<String, String> request) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OWNER_NAME, request.get(COLUMN_OWNER_NAME));
        values.put(COLUMN_PHONE, request.get(COLUMN_PHONE));
        values.put(COLUMN_CAR_MODEL, request.get(COLUMN_CAR_MODEL));
        values.put(COLUMN_COLOR, request.get(COLUMN_COLOR));
        values.put(COLUMN_DATE, request.get(COLUMN_DATE));
        values.put(COLUMN_TIME, request.get("time"));
        return db.insert(TABLE_PAINTING, null, values);
    }

    public List<Map<String, String>> getRepairRequests() {
        List<Map<String, String>> requests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REPAIR, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Map<String, String> request = new HashMap<>();
                request.put(COLUMN_ID, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                request.put(COLUMN_OWNER_NAME, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OWNER_NAME)));
                request.put(COLUMN_PHONE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                request.put(COLUMN_CAR_MODEL, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CAR_MODEL)));
                request.put(COLUMN_ISSUE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ISSUE)));
                request.put(COLUMN_DATE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                request.put(COLUMN_TIME, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)));
                request.put(COLUMN_STATUS, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                request.put(COLUMN_COMPLETION_DATE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPLETION_DATE)));
                request.put(COLUMN_COMPLETION_TIME, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPLETION_TIME)));
                requests.add(request);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return requests;
    }

    public List<Map<String, String>> getPaintingRequests() {
        List<Map<String, String>> requests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PAINTING, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Map<String, String> request = new HashMap<>();
                request.put(COLUMN_ID, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                request.put(COLUMN_OWNER_NAME, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OWNER_NAME)));
                request.put(COLUMN_PHONE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                request.put(COLUMN_CAR_MODEL, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CAR_MODEL)));
                request.put(COLUMN_COLOR, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLOR)));
                request.put(COLUMN_DATE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                request.put(COLUMN_STATUS, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                requests.add(request);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return requests;
    }

    public void updateRepairStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        if ("выполнено".equals(status)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            values.put(COLUMN_COMPLETION_DATE, dateFormat.format(new Date()));
            values.put(COLUMN_COMPLETION_TIME, timeFormat.format(new Date()));
        }
        db.update(TABLE_REPAIR, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updatePaintingStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);

        if ("выполнено".equalsIgnoreCase(status)) { // используем equalsIgnoreCase
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            values.put(COLUMN_COMPLETION_DATE, dateFormat.format(new Date()));
        }

        db.update(TABLE_PAINTING, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteRepairRequest(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REPAIR, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deletePaintingRequest(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PAINTING, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateRepairRequest(int id, Map<String, String> request) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OWNER_NAME, request.get(COLUMN_OWNER_NAME));
        values.put(COLUMN_PHONE, request.get(COLUMN_PHONE));
        values.put(COLUMN_CAR_MODEL, request.get(COLUMN_CAR_MODEL));
        values.put(COLUMN_ISSUE, request.get(COLUMN_ISSUE));
        values.put(COLUMN_DATE, request.get(COLUMN_DATE));
        values.put(COLUMN_TIME, request.get(COLUMN_TIME));
        db.update(TABLE_REPAIR, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updatePaintingRequest(int id, Map<String, String> request) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OWNER_NAME, request.get(COLUMN_OWNER_NAME));
        values.put(COLUMN_PHONE, request.get(COLUMN_PHONE));
        values.put(COLUMN_CAR_MODEL, request.get(COLUMN_CAR_MODEL));
        values.put(COLUMN_COLOR, request.get(COLUMN_COLOR));
        values.put(COLUMN_DATE, request.get(COLUMN_DATE));
        values.put(COLUMN_TIME, request.get("time"));
        db.update(TABLE_PAINTING, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REPAIR, null, null);
        db.delete(TABLE_PAINTING, null, null);
        db.close();
    }
}
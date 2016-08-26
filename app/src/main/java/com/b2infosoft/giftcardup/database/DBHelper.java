package com.b2infosoft.giftcardup.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.b2infosoft.giftcardup.model.State;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rajesh on 8/2/2016.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "gift_card_up.db";
    Schema SC = Schema.getInstance();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String T1 = "CREATE TABLE " + SC.STATE_TABLE + "(" + SC.STATE_ID_COLUMN + " int," + SC.STATE_NAME_COLUMN + " text," + SC.STATE_ABBREVIATION_COLUMN + " text)";
        db.execSQL(T1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SC.STATE_TABLE);
    }

    /* ----------------- STATE PART START --------------------- */
    public void setState(State state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SC.STATE_ID_COLUMN, state.getId());
        values.put(SC.STATE_NAME_COLUMN, state.getName());
        values.put(SC.STATE_ABBREVIATION_COLUMN, state.getAbbreviation());
        db.insert(SC.STATE_TABLE, null, values);
    }

    public void deleteState() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SC.STATE_TABLE);
    }

    public State getStateByName(String stateName) {
        State state = new State();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SC.STATE_TABLE + " WHERE " + SC.STATE_NAME_COLUMN + " = '" + stateName + "' LIMIT 1", null);
        while (cursor.moveToNext()) {
            state.setId(cursor.getString(cursor.getColumnIndex(SC.STATE_ID_COLUMN)));
            state.setName(cursor.getString(cursor.getColumnIndex(SC.STATE_NAME_COLUMN)));
            state.setAbbreviation(cursor.getString(cursor.getColumnIndex(SC.STATE_ABBREVIATION_COLUMN)));
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return state;
    }
    public Map<String,State> getStateMap() {
        Map<String,State> stateMap = new HashMap<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SC.STATE_TABLE, null);
        while (cursor.moveToNext()) {
            State state = new State();
            state.setId(cursor.getString(cursor.getColumnIndex(SC.STATE_ID_COLUMN)));
            state.setName(cursor.getString(cursor.getColumnIndex(SC.STATE_NAME_COLUMN)));
            state.setAbbreviation(cursor.getString(cursor.getColumnIndex(SC.STATE_ABBREVIATION_COLUMN)));
            stateMap.put(state.getName(),state);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return stateMap;
    }
    public State getStateByAbbreviation(String abbreviation) {
        State state = new State();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SC.STATE_TABLE + " WHERE " + SC.STATE_ABBREVIATION_COLUMN + " = '" + abbreviation + "' LIMIT 1", null);
        while (cursor.moveToNext()) {
            state.setId(cursor.getString(cursor.getColumnIndex(SC.STATE_ID_COLUMN)));
            state.setName(cursor.getString(cursor.getColumnIndex(SC.STATE_NAME_COLUMN)));
            state.setAbbreviation(cursor.getString(cursor.getColumnIndex(SC.STATE_ABBREVIATION_COLUMN)));
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return state;
    }

    /* ----------------- SESSION PART END --------------------- */

}

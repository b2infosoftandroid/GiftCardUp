package com.b2infosoft.giftcardup.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.b2infosoft.giftcardup.model.CompanyCategory;
import com.b2infosoft.giftcardup.model.ControlPanel;
import com.b2infosoft.giftcardup.model.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
        String TABLE_STATE = "CREATE TABLE " + SC.STATE_TABLE + "(" + SC.STATE_ID_COLUMN + " int," + SC.STATE_NAME_COLUMN + " text," + SC.STATE_ABBREVIATION_COLUMN + " text)";
        db.execSQL(TABLE_STATE);
        String TABLE_CATEGORIES = "CREATE TABLE " + SC.CATEGORY_TABLE + "(" + SC.CATEGORY_ID_COLUMN + " int," + SC.CATEGORY_NAME_COLUMN + " text)";
        db.execSQL(TABLE_CATEGORIES);
        String TABLE_CONTROL_PANEL = "CREATE TABLE " + SC.CONTROL_PANEL_TABLE + "(" + SC.CONTROL_PANEL_ID + " int," + SC.CONTROL_PANEL_GENERAL_COMMISSION + " text," + SC.CONTROL_PANEL_SHIPPING_CHARGE + " text,"
                + SC.CONTROL_PANEL_SELLING_PERCENTAGE + " int," + SC.CONTROL_PANEL_COMPANY_NAME + " text," + SC.CONTROL_PANEL_PHONE_NO + " text," + SC.CONTROL_PANEL_EMAIL + " text,"
                + SC.CONTROL_PANEL_CITY + " text," + SC.CONTROL_PANEL_ADDRESS + " text," + SC.CONTROL_PANEL_STATE + " text," + SC.CONTROL_PANEL_ZIP_CODE + " int,"
                + SC.CONTROL_PANEL_PROCESS_TIME + " text," + SC.CONTROL_PANEL_CARD_ATTEMPT_TIME + " int," + SC.CONTROL_PANEL_IMAGE + " text," + SC.CONTROL_PANEL_FIRST_CLASS_PRICE + " text," + SC.CONTROL_PANEL_PRIORITY_PRICE + " text,"
                + SC.CONTROL_PANEL_EXPRESS_PRICE + " text,"+ SC.CONTROL_PANEL_MIN_SCORE + " text,"+ SC.CONTROL_PANEL_MAX_SCORE + " text,"+ SC.CONTROL_PANEL_REFFERAL_AMOUNT + " int)";
        db.execSQL(TABLE_CONTROL_PANEL);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SC.STATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SC.CATEGORY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SC.CONTROL_PANEL_TABLE);
    }

    /* ----------------- STATE PART START --------------------- */
    public void setState(State state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SC.STATE_ID_COLUMN, state.getId());
        values.put(SC.STATE_NAME_COLUMN, state.getName().toUpperCase(Locale.getDefault()));
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
        String sql ="SELECT * FROM " + SC.STATE_TABLE + " WHERE " + SC.STATE_NAME_COLUMN + " = '" + stateName.toUpperCase(Locale.getDefault()) + "' LIMIT 1";
        Cursor cursor = database.rawQuery(sql, null);
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

    public Map<String, State> getStateMap() {
        Map<String, State> stateMap = new HashMap<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SC.STATE_TABLE, null);
        while (cursor.moveToNext()) {
            State state = new State();
            state.setId(cursor.getString(cursor.getColumnIndex(SC.STATE_ID_COLUMN)));
            state.setName(cursor.getString(cursor.getColumnIndex(SC.STATE_NAME_COLUMN)));
            state.setAbbreviation(cursor.getString(cursor.getColumnIndex(SC.STATE_ABBREVIATION_COLUMN)));
            stateMap.put(state.getName(), state);
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

    /* ----------------- STATE PART END --------------------- */
    /* ----------------- CATEGORIES PART START -------------- */
    public void setCategory(CompanyCategory category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SC.CATEGORY_ID_COLUMN, category.getCategoryID());
        values.put(SC.CATEGORY_NAME_COLUMN, category.getCategoryName());
        db.insert(SC.CATEGORY_TABLE, null, values);
    }

    public List<CompanyCategory> getCategories() {
        List<CompanyCategory> categories = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SC.CATEGORY_TABLE, null);
        while (cursor.moveToNext()) {
            CompanyCategory category = new CompanyCategory();
            category.setCategoryID(cursor.getInt(cursor.getColumnIndex(SC.CATEGORY_ID_COLUMN)));
            category.setCategoryName(cursor.getString(cursor.getColumnIndex(SC.CATEGORY_NAME_COLUMN)));
            categories.add(category);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return categories;
    }

    public void deleteCategories() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SC.CATEGORY_TABLE);
    }
    /* ----------------- CATEGORIES PART END ---------------- */

    /* ------------------ CONTROL PANEL PART START ------------------*/
    public void setControlPanel(ControlPanel panel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SC.CONTROL_PANEL_ID, panel.getId());
        values.put(SC.CONTROL_PANEL_GENERAL_COMMISSION, panel.getGeneralCommission());
        values.put(SC.CONTROL_PANEL_SHIPPING_CHARGE, panel.getShippingCharge());
        values.put(SC.CONTROL_PANEL_SELLING_PERCENTAGE, panel.getSellingPercentage());
        values.put(SC.CONTROL_PANEL_COMPANY_NAME, panel.getCompanyName());
        values.put(SC.CONTROL_PANEL_PHONE_NO, panel.getPhoneNumber());
        values.put(SC.CONTROL_PANEL_EMAIL, panel.getEmail());
        values.put(SC.CONTROL_PANEL_CITY, panel.getCity());
        values.put(SC.CONTROL_PANEL_ADDRESS, panel.getAddress());
        values.put(SC.CONTROL_PANEL_STATE, panel.getState());
        values.put(SC.CONTROL_PANEL_ZIP_CODE, panel.getZipCode());
        values.put(SC.CONTROL_PANEL_PROCESS_TIME, panel.getProcessTime());
        values.put(SC.CONTROL_PANEL_CARD_ATTEMPT_TIME, panel.getCardAttemptTime());
        values.put(SC.CONTROL_PANEL_IMAGE, panel.getImage());
        values.put(SC.CONTROL_PANEL_FIRST_CLASS_PRICE, panel.getFirstClassPrice());
        values.put(SC.CONTROL_PANEL_PRIORITY_PRICE, panel.getPriorityPrice());
        values.put(SC.CONTROL_PANEL_EXPRESS_PRICE, panel.getExpressPrice());
        values.put(SC.CONTROL_PANEL_MIN_SCORE, panel.getMinimumScore());
        values.put(SC.CONTROL_PANEL_MAX_SCORE, panel.getMaximumScore());
        values.put(SC.CONTROL_PANEL_REFFERAL_AMOUNT, panel.getReferralAmount());
        db.insert(SC.CONTROL_PANEL_TABLE, null, values);
    }

    public ControlPanel getControlPanel() {
        ControlPanel panel = new ControlPanel();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SC.CONTROL_PANEL_TABLE, null);
        while (cursor.moveToNext()) {
            panel.setId(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_ID)));
            panel.setGeneralCommission(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_GENERAL_COMMISSION)));
            panel.setShippingCharge(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_SHIPPING_CHARGE)));
            panel.setSellingPercentage(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_SELLING_PERCENTAGE)));
            panel.setCompanyName(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_COMPANY_NAME)));
            panel.setPhoneNumber(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_PHONE_NO)));
            panel.setEmail(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_EMAIL)));
            panel.setCity(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_CITY)));
            panel.setAddress(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_ADDRESS)));
            panel.setState(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_STATE)));
            panel.setZipCode(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_ZIP_CODE)));
            panel.setProcessTime(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_PROCESS_TIME)));
            panel.setCardAttemptTime(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_CARD_ATTEMPT_TIME)));
            panel.setImage(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_IMAGE)));
            panel.setFirstClassPrice(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_FIRST_CLASS_PRICE)));
            panel.setPriorityPrice(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_PRIORITY_PRICE)));
            panel.setExpressPrice(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_EXPRESS_PRICE)));
            panel.setMinimumScore(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_MIN_SCORE)));
            panel.setMaximumScore(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_MAX_SCORE)));
            panel.setReferralAmount(cursor.getString(cursor.getColumnIndex(SC.CONTROL_PANEL_REFFERAL_AMOUNT)));
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return panel;
    }

    public void deleteControlPanel() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SC.CONTROL_PANEL_TABLE);
    }
    /* ------------------ CONTROL PANEL END ------------------*/

}

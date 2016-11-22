package com.example.a24814.qzalog.components;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.a24814.qzalog.models.Category;
import com.example.a24814.qzalog.models.Region;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataBaseAdapter {

    protected static final String TAG = "DataBaseAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public DataBaseAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext, null);
    }

    public DataBaseAdapter(Context context, Boolean inited)
    {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext, inited);
    }


    public void close()
    {
        mDbHelper.close();
    }


    public List<Category> getCategories(List<Category> categories){
        final String TABLE_NAME = "categories";

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db  = mDbHelper.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                categories.add(
                        new Category(
                                cursor.getString(cursor.getColumnIndex("name")),
                                null,
                                cursor.getInt(cursor.getColumnIndex("category_id")))
                );
            } while (cursor.moveToNext());
        }
        cursor.close();

        return categories;
    }

    public List<Region> getRegions(List<Region> regions, Integer parent){
        final String TABLE_NAME = "regions";
        String selectQuery;
        if(parent == null)
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE parent is null ORDER BY position asc";
        else
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE parent = " + parent + "  ORDER BY position asc";

        SQLiteDatabase db  = mDbHelper.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                regions.add(
                        new Region(
                                cursor.getInt(cursor.getColumnIndex("id")),
                                cursor.getString(cursor.getColumnIndex("name")),
                                cursor.getInt(cursor.getColumnIndex("position")),
                                cursor.getInt(cursor.getColumnIndex("parent")))
                );
            } while (cursor.moveToNext());
        }
        cursor.close();

        return regions;
    }

    public Boolean  checkChildren(List<Region> regions, Integer parent){
        final String TABLE_NAME = "regions";
        String selectQuery;
        selectQuery = "SELECT  COUNT(*) as amount FROM " + TABLE_NAME + " WHERE parent = " + parent;

        SQLiteDatabase db  = mDbHelper.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        Integer amount = cursor.getInt(cursor.getColumnIndex("amount"));

        cursor.close();
        if(amount > 0){
            return true;
        }
        return false;


    }

    public JSONObject getForm(Integer category_id){
        final String TABLE_NAME = "categories";

        String selectQuery = "SELECT form FROM " + TABLE_NAME + " WHERE category_id = " + category_id;
        SQLiteDatabase db  = mDbHelper.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            String jsonString = cursor.getString(cursor.getColumnIndex("form"));
            try {
                JSONObject obj = new JSONObject(jsonString);
                return obj;
            } catch (Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        }
        cursor.close();
        return null;
    }

    public Boolean manageLiked(Integer objectId) {
        final String TABLE_NAME = "liked";

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE object_id = " + objectId;
        Cursor cursor      = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            try {
                db.delete(TABLE_NAME, "object_id = " + String.valueOf(objectId), null);

            } catch (Throwable t) {
                Log.d(TAG, t.getMessage());
            }
            return false;
        }else{
            ContentValues values = new ContentValues();
            values.put("object_id", objectId); // Contact Name
            db.insert(TABLE_NAME, null, values);
            return true;
        }


    }

    public Boolean getLiked(Integer objectId) {
        final String TABLE_NAME = "liked";

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE object_id = " + objectId;
        Cursor cursor      = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public List<Integer> getLikedAll() {
        List<Integer> selectedObjects = new ArrayList<Integer>();
        final String TABLE_NAME = "liked";

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor      = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Integer i =  cursor.getInt(cursor.getColumnIndex("object_id"));
                selectedObjects.add(i);
            } while (cursor.moveToNext());

        }
        return selectedObjects;
    }





}

package com.example.a24814.qzalog.components;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.a24814.qzalog.models.Category;
import com.example.a24814.qzalog.models.Region;

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


}

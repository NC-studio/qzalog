package com.example.a24814.qzalog.components;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.a24814.qzalog.models.Category;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


public class DataBaseHelper extends SQLiteOpenHelper
{
    private static String TAG = "DataBaseHelper";

    private static String DB_PATH = "";
    private static String DB_NAME ="qzalog.db";
    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private static final int DATABASE_VERSION = 11;
    private static boolean mDataBaseExist;

    public DataBaseHelper(Context context)
    {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
    }



    public void createDataBase() throws IOException
    {
        mDataBaseExist = checkDataBase();
        this.getReadableDatabase();
        this.close();
    }

    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        Log.d(TAG, dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException
    {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //Open the database, so we can query it
    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        Log.v(TAG, "openDataBase" + mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    @Override
    public synchronized void close()
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        try
        {
            if(!mDataBaseExist) {
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
            }
        }
        catch (IOException mIOException)
        {
            Log.e(TAG,  mIOException.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade");
        try
        {
            if(mDataBaseExist) {
                File dbFile = new File(DB_PATH + DB_NAME);
                dbFile.delete();
            }
            copyDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG,  mIOException.getMessage());
        }
    }

    public List<Category> getCategories(List<Category> categories){
        final String TABLE_NAME = "categories";

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db  = this.getReadableDatabase();
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
}
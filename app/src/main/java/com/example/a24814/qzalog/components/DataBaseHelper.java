package com.example.a24814.qzalog.components;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DataBaseHelper extends SQLiteOpenHelper
{
    private static String TAG = "DataBaseHelper";

    private static String DB_PATH = "";
    private static String DB_NAME ="qzalog.db";
    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private static final int DATABASE_VERSION = 50;
    private static boolean mDataBaseExist;

    public DataBaseHelper(Context context, Boolean inited)
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

        if(inited == true){
            mDataBaseExist = true;
        }else{
            mDataBaseExist = checkDataBase();
        }

        if (mDataBaseExist) {
            openDataBase();
        } else {
            try
            {
                createDataBase();
            }
            catch (IOException mIOException)
            {
                Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
                throw new Error("UnableToCreateDatabase");
            }
        }

    }





    public void createDataBase() throws IOException
    {
        copyDataBase();
        /*mDataBaseExist = checkDataBase();
        if(!mDataBaseExist) {
            copyDataBase();
            Log.e(TAG, "createDatabase database created");
        }*/

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
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);

        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    @Override
    public synchronized void close()
    {
        Log.d(TAG, "closed");
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade");
        try
        {
            if(mDataBaseExist) {
                File dbFile = new File(DB_PATH + DB_NAME);
                dbFile.delete();
                Log.d(TAG, "onUpgrade Deleted");
            }
            copyDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG,  mIOException.getMessage());
        }
    }





}
package com.ncgoup.a24814.qzalog.components;

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
    private static final int DATABASE_VERSION = 61;
    private static boolean mDataBaseExist;
    private static File dir;
    private static File file;
    private File dbFile;

    public DataBaseHelper(Context context, Boolean inited)
    {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.mContext = context;


        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }



        mDataBaseExist = checkDataBase();

        if (mDataBaseExist) {
            openDataBase();
        } else {
            try
            {
                this.getReadableDatabase();
                createDataBase();
                this.close();
            }
            catch (IOException mIOException)
            {
                Log.e("testio", mIOException.getMessage());
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
        dbFile = new File(DB_PATH + DB_NAME);
        Log.d(TAG, dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException
    {

        OutputStream mOutput = new FileOutputStream(dbFile);
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mInput.close();
        mOutput.flush();
        mOutput.close();

    }

    //Open the database, so we can query it
    public boolean openDataBase() throws SQLException
    {


        String mPath = mContext.getDatabasePath(DB_NAME).getAbsolutePath();
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
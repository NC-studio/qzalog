package com.example.a24814.qzalog.components;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


public class Helpers {

    public static void showToast(Context context,  String msg )
    {
        Toast.makeText( context, msg, Toast.LENGTH_LONG ).show();
    }

    public static String getStringByUrl(String urlSite) {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urlSite);
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                String server_response = Helpers.readStream(urlConnection.getInputStream());
                return server_response;
            }

        } catch (MalformedURLException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
        return null;

    }

    public static void selectSpinnerValue(Spinner spinner, String myString)
    {
        if(myString != null){

            int index = 0;
            for(int i = 0; i < spinner.getCount(); i++){
                if(spinner.getItemAtPosition(i).toString().equals(myString)){
                    spinner.setSelection(i);
                    break;
                }
            }
        }

    }


    public static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }



    public static String randomString(Integer max_length)
    {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(max_length);
        char tempChar;
        for (int i = 0; i < max_length; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public static String getRealPathFromURI(Activity activity, Uri contentURI) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(contentURI, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        //cursor.close();
        return filePath;
    }


   public static Bitmap rotateImageIfRequired(Bitmap bitmap, String photoPath) throws IOException {

       ExifInterface ei = new ExifInterface(photoPath);
       int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
               ExifInterface.ORIENTATION_UNDEFINED);

       switch(orientation) {
           case ExifInterface.ORIENTATION_ROTATE_90:
               return rotateImage(bitmap, 90);

           case ExifInterface.ORIENTATION_ROTATE_180:
               return rotateImage(bitmap, 180);

           case ExifInterface.ORIENTATION_ROTATE_270:
               return rotateImage(bitmap, 270);

           case ExifInterface.ORIENTATION_NORMAL:
           default:
               return bitmap;

       }
   }

    public static Bitmap rotateImage(Bitmap source, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }



}

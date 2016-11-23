package com.ncgoup.a24814.qzalog.components;

import android.os.AsyncTask;

import java.util.concurrent.TimeUnit;

//АСИНХРОНЫЙ ГЕНЕРИК КЛАСС
public class BackendAsynh<T> extends AsyncTask<Void, Integer, String> {

    protected T result;

    public String errorMessage;

    public void setValue (T value) {
        this.result = value;
    }

    public T getValue () {
        return result;
    }

    public void setErrorMessage (String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage () {
        return errorMessage;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        handleResponse(getValue());
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

    }

    public void handleResponse( T response )
    {

    }

}
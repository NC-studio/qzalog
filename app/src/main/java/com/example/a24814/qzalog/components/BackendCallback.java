package com.example.a24814.qzalog.components;

import android.app.ProgressDialog;
import android.content.Context;


public class BackendCallback<T> extends BackendAsynh<T>{

    private Context context;
    public ProgressDialog progressDialog;

    public BackendCallback( Context context )
    {
        this.context = context;
        progressDialog = ProgressDialog.show( context, "", "Загружаю...", true );

    }

    public BackendCallback( Context context, String message )
    {
        this.context = context;
progressDialog = ProgressDialog.show( context, "", message, true );

    }



    public BackendCallback( Context context, Boolean flag )
    {
        this.context = context;

    }

    @Override
    public void handleResponse( T response )
    {
        try{
            if(progressDialog != null)
                progressDialog.cancel();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

   /* @Override
    public void handleFault( BackendlessFault fault )
    {
        progressDialog.cancel();
        Toast.makeText( context, fault.getMessage(), Toast.LENGTH_LONG ).show();
    }*/
}

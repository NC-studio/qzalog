package com.example.a24814.qzalog.components;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.a24814.qzalog.CategoryFragment;
import com.example.a24814.qzalog.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class Backend {

    public static void getCategoriesInfo(final Context context, final Activity activity, final CategoryFragment fragment) {

        new BackendCallback<Boolean>(context, false){
            @Override
            public String doInBackground(Void... params )
            {
                setValue(true);
                HashMap<String, Category> categoriesList = new HashMap<>();
                String jsonResponse = Helpers.getStringByUrl("http://qzalog.kz/_mobile_category");
                if(jsonResponse != null){
                    try {
                        JSONObject jsonObj = new JSONObject(jsonResponse);
                        // Getting JSON Array node
                        JSONArray categories = jsonObj.getJSONArray("categories");
                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject c = categories.getJSONObject(i);
                            String id = c.getString("id");
                            String amount = c.getString("amount");
                            String title = c.getString("title");

                            Category category =  new Category(title, Integer.valueOf(amount), Integer.valueOf(id));
                            categoriesList.put(id, category);
                        }
                    } catch (final JSONException e) {
                        setValue(false);
                        Log.e("BACKEND TEST", "Json parsing error: " + e.getMessage());
                    }

                    List<Category> categoriesApp =  ((BaseFile) activity.getApplication()).getCategories();
                    for (Category object: categoriesApp) {
                        Category categoryApp = categoriesList.get(String.valueOf(object.getObjectId()));
                        if(categoryApp != null){
                            object.setObjAmount(Integer.valueOf(categoryApp.getObjAmount()));
                        }
                    }
                }
                return null;
            }
            @Override
            public void handleResponse( Boolean success )
            {
                super.handleResponse( success );

                //if(success == false){
                  //  Helpers.showToast(context, "Сервис недоступен. Проверьте соеденение с интернетом");
               // }

                fragment.backendResponse();

            }
        }.execute();



    }



}

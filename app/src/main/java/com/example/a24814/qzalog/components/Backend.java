package com.example.a24814.qzalog.components;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.a24814.qzalog.CategoryFragment;
import com.example.a24814.qzalog.CategoryObjectsFragment;
import com.example.a24814.qzalog.models.Category;
import com.example.a24814.qzalog.models.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
                        Log.d("test", jsonObj.toString());
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

    public static void getObjects(final Activity activity, final CategoryObjectsFragment fragment, final String url, final Integer page) {

        new BackendCallback<List<Objects>>(activity, false){
            @Override
            public String doInBackground(Void... params )
            {
               // setValue(false);

                //Integer page = ((BaseFile) activity.getApplication()).getPage();
                String jsonResponse = Helpers.getStringByUrl(url + "&page=" + String.valueOf(page));

               /// Log.d("test", url);
               // Log.d("test", jsonResponse);

                if(jsonResponse != null){
                    List<Objects> objects = ((BaseFile) activity.getApplication()).getObjects();

                   // List<Objects> clones = Helpers.copyList(objects);
                    List<Objects> clones = new ArrayList<Objects>();

                    try {
                        JSONObject jsonObj = new JSONObject(jsonResponse);

                        // Getting JSON Array node
                        JSONArray objectsArray = jsonObj.getJSONArray("objects");
                        for (int i = 0; i < objectsArray.length(); i++) {
                            JSONObject c = objectsArray.getJSONObject(i);
                            Integer id = c.getInt("id");
                            String title = c.getString("title");
                            String image = c.getString("image");
                            String region = c.getString("region");
                            String price = c.getString("price");
                            String discount = c.getString("discount");
                            String info = c.getString("info");



                            Objects obj = new Objects(id, title, image, region, price, discount, info);
                            clones.add(obj);

                        }
                        //if(objectsArray.length() < 10){
                               //setValue(true);
                       // }




                    } catch (final JSONException e) {
                       // setValue(true);
                        Log.e("BACKEND TEST", "Json parsing error: " + e.getMessage());
                    }
                    setValue(clones);


                   // ((BaseFile) activity.getApplication()).setObjects(objects);;
                   // ((BaseFile) activity.getApplication()).setPage(page + 1);

                }


                return null;
            }
            @Override
            public void handleResponse( List<Objects> clones )
            {
                Log.d("testtest", String.valueOf(clones.size()));
                super.handleResponse( clones );
                fragment.backendResponse(clones);


            }
        }.execute();
    }





}

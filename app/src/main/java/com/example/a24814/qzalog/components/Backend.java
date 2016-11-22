package com.example.a24814.qzalog.components;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.a24814.qzalog.CategoryFragment;
import com.example.a24814.qzalog.CategoryObjectsFragment;
import com.example.a24814.qzalog.LikedObjectsFragment;
import com.example.a24814.qzalog.MapFragment;
import com.example.a24814.qzalog.ObjectDetailFragment;
import com.example.a24814.qzalog.R;
import com.example.a24814.qzalog.SelectedObjectsFragment;
import com.example.a24814.qzalog.models.Category;
import com.example.a24814.qzalog.models.MapObject;
import com.example.a24814.qzalog.models.MapProperty;
import com.example.a24814.qzalog.models.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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

    public static BackendCallback<List<Objects>> getObjects(final Activity activity, final CategoryObjectsFragment fragment, final String url, final Integer page) {
        return new BackendCallback<List<Objects>>(activity, false){
            @Override
            public String doInBackground(Void... params )
            {
                String jsonResponse = Helpers.getStringByUrl(url + "&page=" + String.valueOf(page));
                if(jsonResponse != null){
                    //List<Objects> objects = ((BaseFile) activity.getApplication()).getObjects();
                    List<Objects> clones = new ArrayList<Objects>();
                    try {
                        JSONObject jsonObj = new JSONObject(jsonResponse);
                        JSONArray objectsArray = jsonObj.getJSONArray("objects");
                        DataBaseAdapter myDatabaseHelper = new DataBaseAdapter(activity, true);
                        for (int i = 0; i < objectsArray.length(); i++) {
                            JSONObject c = objectsArray.getJSONObject(i);
                            Integer id = c.getInt("id");
                            String title = c.getString("title");
                            String image = c.getString("image");
                            String region = c.getString("region");
                            String price = c.getString("price");
                            String discount = c.getString("discount");
                            String info = c.getString("info");

                            Boolean isLiked = myDatabaseHelper.getLiked(id);
                            Drawable d;
                            if(isLiked){
                                d = activity.getResources().getDrawable(R.drawable.ic_liked_active);
                            }else{
                                d = activity.getResources().getDrawable(R.drawable.ic_liked);
                            }

                            Objects obj = new Objects(id, title, image, region, price, discount, info, d, isLiked);
                            clones.add(obj);
                        }
                        myDatabaseHelper.close();
                    } catch (final JSONException e) {
                        Log.e("BACKEND TEST", "Json parsing error: " + e.getMessage());
                    }
                    setValue(clones);
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
        };
    }

    public static void loadObject(final Activity context, final Integer objId, final ObjectDetailFragment fragment){
        new BackendCallback<Objects>(context, false){
            @Override
            public String doInBackground(Void... params )
            {
                String url = Defaults.OBJ_PATH + String.valueOf(objId);
                String jsonResponse = Helpers.getStringByUrl(url);
                if(jsonResponse != null){
                    try {
                        JSONObject jsonObj = new JSONObject(jsonResponse);
                        JSONObject c = jsonObj.getJSONObject("object");
                        Integer id = c.getInt("id");
                        String title = c.getString("title");
                        String complex = c.getString("complex");
                        String dateCreated = c.getString("dateCreated");
                        String address = c.getString("address");
                        String price = c.getString("price");
                        String discount = c.getString("discount");
                        String phones = c.getString("phones");
                        String images = c.getString("images");
                        String infoArray = c.getString("infoArray");
                        String description = c.getString("description");
                        JSONObject phonesObj = new JSONObject(phones);
                        String coordX = c.getString("coordX");
                        String coordY = c.getString("coordY");
                        String zoom = c.getString("zoom");


                        Object json = new JSONTokener(images).nextValue();
                        JSONObject imagesObj =  new  JSONObject();
                        if (json instanceof JSONObject)
                           imagesObj = new JSONObject(images);

                        json = new JSONTokener(infoArray).nextValue();
                        JSONObject infoArrayObj = new  JSONObject();
                        if (json instanceof JSONObject)
                            infoArrayObj = new JSONObject(infoArray);

                        DataBaseAdapter myDatabaseHelper = new DataBaseAdapter(context, true);
                        Boolean isLiked = myDatabaseHelper.getLiked(id);
                        myDatabaseHelper.close();



                        Objects obj = new Objects(id, title, complex, dateCreated,address, price, discount, phonesObj, imagesObj, infoArrayObj, description, coordX, coordY, zoom, isLiked);
                        setValue(obj);
                    } catch (final JSONException e) {
                        Log.e("BACKEND TEST", "Json parsing error: " + e.getMessage());
                    }
                }
                return null;
            }
            @Override
            public void handleResponse( Objects object )
            {
                super.handleResponse( object );
                fragment.backendResponse(object);
            }
        }.execute();
    }

    public static BackendCallback<List<MapObject>> getMapObjects(final Activity activity, final MapFragment fragment, final String url) {
        return new BackendCallback<List<MapObject>>(activity, false){
            @Override
            public String doInBackground(Void... params )
            {
                String jsonResponse = Helpers.getStringByUrl(url);
                if(jsonResponse != null){
                    List<MapObject> mapObjects = new ArrayList<MapObject>();
                    try {
                        JSONObject jsonObj = new JSONObject(jsonResponse);
                        JSONArray objectsArray = jsonObj.getJSONArray("objects");
                        for (int i = 0; i < objectsArray.length(); i++) {
                            JSONObject c = objectsArray.getJSONObject(i);
                            Integer id = c.getInt("id");
                            String coordX = c.getString("coord_x");
                            String coordY = c.getString("coord_y");

                            if(!coordX.isEmpty()) {
                                MapObject obj = new MapObject(id, coordX, coordY);
                                mapObjects.add(obj);
                            }
                        }

                        JSONObject objectMap = jsonObj.getJSONObject("mapProperty");

                        String coordX = objectMap.getString("coord_x");
                        String coordY = objectMap.getString("coord_y");
                        String zoom = objectMap.getString("zoom");

                        MapProperty mapProperty = new MapProperty(coordX, coordY, zoom);
                        ((BaseFile) activity.getApplication()).setMapProperty(mapProperty);

                    } catch (final JSONException e) {
                        Log.e("BACKEND TEST", "Json parsing error: " + e.getMessage());
                    }
                    setValue(mapObjects);




                }
                return null;
            }
            @Override
            public void handleResponse( List<MapObject> mapObjects )
            {
                super.handleResponse( mapObjects );
                fragment.backendResponse(mapObjects);
            }
        };
    }

    public static BackendCallback<List<Objects>> getSelectedObjects(final Activity activity, final SelectedObjectsFragment fragment, final List<Integer> objectsList) {
        return new BackendCallback<List<Objects>>(activity, false){
            @Override
            public String doInBackground(Void... params )
            {
                Integer categoryId = ((BaseFile)  activity.getApplication()).getCategoryId();
                String url = Defaults.SELECTED_OBJ_PATH + "?category=" + String.valueOf(categoryId);

                for (int i = 0; i < objectsList.size(); i++) {
                    url = url + "&" + "objects["+String.valueOf(i)+"]="+String.valueOf(objectsList.get(i));
                }

                String jsonResponse = Helpers.getStringByUrl(url);
                if(jsonResponse != null){
                    List<Objects> clones = new ArrayList<Objects>();
                    try {
                        JSONObject jsonObj = new JSONObject(jsonResponse);
                        JSONArray objectsArray = jsonObj.getJSONArray("objects");
                        DataBaseAdapter myDatabaseHelper = new DataBaseAdapter(activity, true);
                        for (int i = 0; i < objectsArray.length(); i++) {
                            JSONObject c = objectsArray.getJSONObject(i);
                            Integer id = c.getInt("id");
                            String title = c.getString("title");
                            String image = c.getString("image");
                            String region = c.getString("region");
                            String price = c.getString("price");
                            String discount = c.getString("discount");
                            String info = c.getString("info");

                            Boolean isLiked = myDatabaseHelper.getLiked(id);
                            Drawable d;
                            if(isLiked){
                                d = activity.getResources().getDrawable(R.drawable.ic_liked_active);
                            }else{
                                d = activity.getResources().getDrawable(R.drawable.ic_liked);
                            }

                            Objects obj = new Objects(id, title, image, region, price, discount, info, d, isLiked);
                            clones.add(obj);
                        }
                        myDatabaseHelper.close();
                    } catch (final JSONException e) {
                        Log.e("BACKEND TEST", "Json parsing error: " + e.getMessage());
                    }
                    setValue(clones);
                }
                return null;
            }
            @Override
            public void handleResponse( List<Objects> clones )
            {

                super.handleResponse( clones );
                fragment.backendResponse(clones);


            }
        };
    }

    public static BackendCallback<List<Objects>> getLikedObjects(final Activity activity, final LikedObjectsFragment fragment, final List<Integer> objectsList) {
        return new BackendCallback<List<Objects>>(activity, false){
            @Override
            public String doInBackground(Void... params )
            {
                String url = Defaults.SELECTED_OBJ_PATH;

                String devider = "?";
                for (int i = 0; i < objectsList.size(); i++) {
                    url = url + devider + "objects["+String.valueOf(i)+"]="+String.valueOf(objectsList.get(i));
                    devider = "&";
                }

                String jsonResponse = Helpers.getStringByUrl(url);
                if(jsonResponse != null){
                    List<Objects> clones = new ArrayList<Objects>();
                    try {
                        JSONObject jsonObj = new JSONObject(jsonResponse);
                        JSONArray objectsArray = jsonObj.getJSONArray("objects");
                        DataBaseAdapter myDatabaseHelper = new DataBaseAdapter(activity, true);
                        for (int i = 0; i < objectsArray.length(); i++) {
                            JSONObject c = objectsArray.getJSONObject(i);
                            Integer id = c.getInt("id");
                            String title = c.getString("title");
                            String image = c.getString("image");
                            String region = c.getString("region");
                            String price = c.getString("price");
                            String discount = c.getString("discount");

                            Boolean isLiked = myDatabaseHelper.getLiked(id);
                            Drawable d;
                            if(isLiked){
                                d = activity.getResources().getDrawable(R.drawable.ic_liked_active);
                            }else{
                                d = activity.getResources().getDrawable(R.drawable.ic_liked);
                            }

                            Objects obj = new Objects(id, title, image, region, price, discount, null, d, isLiked);
                            clones.add(obj);
                        }
                        myDatabaseHelper.close();
                    } catch (final JSONException e) {
                        Log.e("BACKEND TEST", "Json parsing error: " + e.getMessage());
                    }
                    setValue(clones);
                }
                return null;
            }
            @Override
            public void handleResponse( List<Objects> clones )
            {

                super.handleResponse( clones );
                fragment.backendResponse(clones);


            }
        };
    }

}

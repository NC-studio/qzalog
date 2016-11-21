package com.example.a24814.qzalog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a24814.qzalog.components.Backend;
import com.example.a24814.qzalog.models.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ObjectDetailFragment extends Fragment {

    private final String TAG = "ObjectDetailFragment";

    private ObjectDetailFragment fragment;

    private List<Objects> objects = new ArrayList<Objects>();;

    private View view;

    private RelativeLayout loader;

    private Integer objId;

    private LinearLayout objContainer;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragment = this;
        view = inflater.inflate(R.layout.activity_object_detail,
                container, false);


        initView();
        initIbject();

        return view;
    }



    private void initView(){
        loader = (RelativeLayout) view.findViewById(R.id.loader);
        objContainer = (LinearLayout) view.findViewById(R.id.objContainer);
    }

    private void initIbject(){
        objId = ((ObjectDetailActivity)getActivity()).getObjId();
        Backend.loadObject(getActivity(), objId, this);
    }

    public void backendResponse(Objects object){
        loader.setVisibility(View.GONE);
        objContainer.setVisibility(View.VISIBLE);
        (((ObjectDetailActivity)getActivity()).getMapIcon()).setVisible(true);
        (((ObjectDetailActivity)getActivity()).getLikeIcon()).setVisible(true);

        String price = object.getPrice();

        ((TextView)view.findViewById(R.id.price)).setText(price);


        if(object.getDiscount() != null && !object.getDiscount().isEmpty()) {
            TextView textView = (TextView) view.findViewById(R.id.price);
            textView.setVisibility(View.VISIBLE);
            textView.setText(object.getDiscount());
        }
        ((TextView)view.findViewById(R.id.dateCreated)).setText(object.getDateCreated());

        ((TextView)view.findViewById(R.id.detailTitle)).setText(object.getName());

        if(object.getComplex() != null && !object.getComplex().isEmpty()) {
            TextView textView = (TextView) view.findViewById(R.id.detailComplex);
            textView.setVisibility(View.VISIBLE);
            textView.setText(object.getComplex());
        }

        ((TextView)view.findViewById(R.id.detailAddress)).setText(object.getRegion());

        if(object.getDscription() != null && !object.getDscription().isEmpty()) {
            TextView textView = (TextView) view.findViewById(R.id.description);
            LinearLayout block = (LinearLayout) view.findViewById(R.id.descriptionBlock);
            block.setVisibility(View.VISIBLE);
            textView.setText(object.getDscription());
        }


        FrameLayout dynamicParams = (FrameLayout) view.findViewById(R.id.dynamicParams);

        JSONObject info = object.getInfoJson();
        try {
            Iterator<String> temp = info.keys();
            while (temp.hasNext()) {
                String key = temp.next();
                JSONObject value = info.getJSONObject(key);

                JSONObject c = value.getJSONObject("value");

                Log.d("testio",c.toString());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }





    }






}

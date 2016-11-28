package com.ncgoup.a24814.qzalog;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ncgoup.a24814.qzalog.components.Backend;
import com.ncgoup.a24814.qzalog.components.BaseFile;
import com.ncgoup.a24814.qzalog.models.Objects;
import com.ncgoup.a24814.qzalog.models.SimpleImageModel;
import com.ncgoup.a24814.qzalog.models.SimpleValueOrder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private LayoutInflater inflater;


    private ArrayList<SimpleImageModel> horizontalList;

    private RecyclerView horizontal_recycler_view;

    private HorizontalAdapter horizontalAdapter;

    private TextView imageAmountTextView;

    private Integer imageAmount;

    private LinearLayoutManager horizontalLayoutManagaer;

    private int previusPosition = 0;

    private ImageView leftArrow;

    private ImageView rightArrow;

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
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loader = (RelativeLayout) view.findViewById(R.id.loader);
        objContainer = (LinearLayout) view.findViewById(R.id.objContainer);

        horizontal_recycler_view= (RecyclerView) view.findViewById(R.id.horizontal_recycler_view);
        imageAmountTextView = (TextView) view.findViewById(R.id.imageAmount);

        leftArrow = (ImageView) view.findViewById(R.id.leftArrow);
        rightArrow = (ImageView) view.findViewById(R.id.rightArrow);
    }

    private void initIbject(){
        objId = ((ObjectDetailActivity)getActivity()).getObjId();
        Backend.loadObject(getActivity(), objId, this);
    }

    public void backendResponse(Objects object) {

        if (object != null) {
            ((BaseFile) getActivity().getApplication()).setObjectModel(object);

            loader.setVisibility(View.GONE);
            objContainer.setVisibility(View.VISIBLE);

            Resources resources = getActivity().getResources();
            if(object.getLiked()){
                int resourceId = resources.getIdentifier("ic_star_active", "drawable",
                        getActivity().getPackageName());
                (((ObjectDetailActivity) getActivity()).getLikeIcon()).setIcon(resources.getDrawable(resourceId));
            }else{

                int resourceId = resources.getIdentifier("ic_star", "drawable",
                        getActivity().getPackageName());
                (((ObjectDetailActivity) getActivity()).getLikeIcon()).setIcon(resources.getDrawable(resourceId));
            }

            (((ObjectDetailActivity) getActivity()).getLikeIcon()).setVisible(true);


            if(object.getCoordX() != null && !object.getCoordX().isEmpty())
                (((ObjectDetailActivity) getActivity()).getMapIcon()).setVisible(true);

            String price = object.getPrice();

            ((TextView) view.findViewById(R.id.price)).setText(price);


            if (object.getDiscount() != null && !object.getDiscount().isEmpty()) {
                TextView textView = (TextView) view.findViewById(R.id.discount);
                textView.setVisibility(View.VISIBLE);
                textView.setText(object.getDiscount());
            }
            ((TextView) view.findViewById(R.id.dateCreated)).setText(object.getDateCreated());

            ((TextView) view.findViewById(R.id.detailTitle)).setText(object.getName());

            if (object.getComplex() != null && !object.getComplex().isEmpty()) {
                TextView textView = (TextView) view.findViewById(R.id.detailComplex);
                textView.setVisibility(View.VISIBLE);
                textView.setText(object.getComplex());
            }

            ((TextView) view.findViewById(R.id.detailAddress)).setText(object.getRegion());

            if (object.getDscription() != null && !object.getDscription().isEmpty()) {
                TextView textView = (TextView) view.findViewById(R.id.description);
                LinearLayout block = (LinearLayout) view.findViewById(R.id.descriptionBlock);
                block.setVisibility(View.VISIBLE);
                textView.setText(object.getDscription());
            }

            LinearLayout dynamicParams = (LinearLayout) view.findViewById(R.id.dynamicParams);
            LinearLayout dynamicParamsBlock = (LinearLayout) view.findViewById(R.id.parametres);

            JSONObject info = object.getInfoJson();
            ArrayList<SimpleValueOrder> valueOrderList = new ArrayList<SimpleValueOrder>();
            try {

                Iterator<String> temp = info.keys();
                int i = 0;
                while (temp.hasNext()) {
                    i++;
                    String key = temp.next();
                    JSONObject value = info.getJSONObject(key);
                    valueOrderList.add(new SimpleValueOrder(value.get("title").toString(), value.get("value").toString(), Integer.valueOf(key)));
                }
                Collections.sort(valueOrderList, new Comparator<SimpleValueOrder>(){
                    public int compare(SimpleValueOrder emp1, SimpleValueOrder emp2) {
                        return emp1.getPosition().compareTo(emp2.getPosition());
                    }
                });

                for (SimpleValueOrder valueModel: valueOrderList) {
                    View v = inflater.inflate(R.layout.object_detail_dynamic_params, null);
                    ((TextView) v.findViewById(R.id.paramsName)).setText(valueModel.getName());
                    ((TextView) v.findViewById(R.id.paramsValue)).setText(valueModel.getValue());
                    dynamicParams.addView(v);
                }

                if (i == 0) {
                    dynamicParamsBlock.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                Log.d("testio", e.getMessage());
            }

            LinearLayout phonesBlock = (LinearLayout) view.findViewById(R.id.phoneContainerMain);
            JSONObject phones = object.getPhones();
            try {

                Iterator<String> temp = phones.keys();
                while (temp.hasNext()) {
                    View v = inflater.inflate(R.layout.phone, null);
                    String key = temp.next();
                    final String value = phones.getString(key);
                    ((TextView) v.findViewById(R.id.phoneNumber)).setText(value);
                    LinearLayout phoneContainer = (LinearLayout) v.findViewById(R.id.phoneContainer);
                    phoneContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + value));
                            startActivity(intent);
                        }
                    });
                    phonesBlock.addView(v);

                }

            } catch (JSONException e) {
                Log.d("testio", e.getMessage());
            }

            JSONObject images = object.getImages();
            horizontalList = new ArrayList<SimpleImageModel>();
            try {
                Iterator<String> temp = images.keys();
                while (temp.hasNext()) {
                    String key = temp.next();
                    JSONObject value = images.getJSONObject(key);
                    SimpleImageModel simpleImage = new SimpleImageModel(value.get("little").toString(), Integer.valueOf(key));
                    horizontalList.add(simpleImage);
                }

                Collections.sort(horizontalList, new Comparator<SimpleImageModel>(){
                    public int compare(SimpleImageModel emp1, SimpleImageModel emp2) {
                        return emp1.getPosition().compareTo(emp2.getPosition());
                    }
                });

                imageAmount = horizontalList.size();
                if (imageAmount == 0) {
                    view.findViewById(R.id.imagesContainer).setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                Log.d("testio", e.getMessage());
            }
            ((BaseFile) getActivity().getApplication()).setImages(images);
            ((BaseFile) getActivity().getApplication()).setImagesAmount(imageAmount);

            imageAmountTextView.setText(String.valueOf(previusPosition + 1) + "/" + String.valueOf(imageAmount));
            if(imageAmount > 1){
                rightArrow.setVisibility(View.VISIBLE);
            }


            horizontalAdapter = new HorizontalAdapter(horizontalList);
            horizontalLayoutManagaer
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);
            horizontal_recycler_view.setAdapter(horizontalAdapter);

            horizontal_recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (previusPosition != horizontalLayoutManagaer.findFirstVisibleItemPosition()) {
                        previusPosition = horizontalLayoutManagaer.findFirstVisibleItemPosition();
                        int currPosition = previusPosition + 1;
                        imageAmountTextView.setText(String.valueOf(currPosition) + "/" + String.valueOf(imageAmount));
                        if(currPosition == imageAmount ){
                            rightArrow.setVisibility(View.GONE);
                        }else{
                            rightArrow.setVisibility(View.VISIBLE);
                            if(currPosition == 1 ){
                                leftArrow.setVisibility(View.GONE);
                            }else{
                                leftArrow.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });

            leftArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(previusPosition > 0) {
                        horizontal_recycler_view.scrollToPosition(previusPosition - 1);
                    }
                }
            });
            rightArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(previusPosition < imageAmount) {
                        horizontal_recycler_view.scrollToPosition(previusPosition + 1);
                    }
                }
            });


        }
    }


    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        private List<SimpleImageModel> horizontalList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public MyViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.objectImage);
            }
        }
        public HorizontalAdapter(List<SimpleImageModel> horizontalList) {
            this.horizontalList = horizontalList;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gallery_item, parent, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            Picasso.with(getActivity())
                    .load(horizontalList.get(position).getName())
                    .tag("image")
                    .placeholder(R.drawable.trick )
                    .into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), GalleryActivity.class);
                    intent.putExtra("objId", objId);
                    intent.putExtra("position", previusPosition);
                    startActivity(intent);
                }
            });

        }
        @Override
        public int getItemCount() {
            return horizontalList.size();
        }
    }





}

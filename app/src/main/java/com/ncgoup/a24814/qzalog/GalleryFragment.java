package com.ncgoup.a24814.qzalog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.ncgoup.a24814.qzalog.components.BaseFile;
import com.ncgoup.a24814.qzalog.models.SimpleImageModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class GalleryFragment extends Fragment {

    private GalleryFragment fragment;

    private View view;

    private JSONObject images;

    private Integer imagesAmount;

    private ArrayList<SimpleImageModel> horizontalList;

    private RecyclerView horizontal_recycler_view;

    private HorizontalAdapter horizontalAdapter;

    private LinearLayoutManager horizontalLayoutManagaer;

    private int previusPosition = 0;

    private TextView toolbarText;

    private ImageView leftArrow;

    private ImageView rightArrow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragment = this;
        view = inflater.inflate(R.layout.activity_gallery,
                container, false);

        images = ((BaseFile) getActivity().getApplication()).getImages();
        imagesAmount = ((BaseFile) getActivity().getApplication()).getImagesAmount();

        previusPosition = ((GalleryActivity)getActivity()).getPosition();

        initView();

        horizontalList=new ArrayList<SimpleImageModel>();
        try {
            Iterator<String> temp = images.keys();
            while (temp.hasNext()) {
                String key = temp.next();
                JSONObject value = images.getJSONObject(key);
                horizontalList.add(new SimpleImageModel(value.get("big").toString(), Integer.valueOf(key)));
            }
            Collections.sort(horizontalList, new Comparator<SimpleImageModel>(){
                public int compare(SimpleImageModel emp1, SimpleImageModel emp2) {
                    return emp1.getPosition().compareTo(emp2.getPosition());
                }
            });


        } catch (JSONException e) {

        }

        //Setting starting visibility of arrows
        if(previusPosition < imagesAmount && imagesAmount > 1){
            rightArrow.setVisibility(View.VISIBLE);
        }
        if(previusPosition > 0){
            leftArrow.setVisibility(View.VISIBLE);
        }


        toolbarText = ((GalleryActivity)getActivity()).getToolbarText();
        horizontalAdapter=new HorizontalAdapter(horizontalList);
        horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);
        horizontal_recycler_view.setAdapter(horizontalAdapter);
        horizontal_recycler_view.scrollToPosition(previusPosition);

        horizontal_recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(previusPosition != horizontalLayoutManagaer.findFirstVisibleItemPosition()){
                    previusPosition = horizontalLayoutManagaer.findFirstVisibleItemPosition();
                    toolbarText.setText("Изображение " + String.valueOf(previusPosition + 1) +  "/" + String.valueOf(imagesAmount));

                    int currPosition = previusPosition + 1;
                    if(currPosition == imagesAmount ){
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
                if(previusPosition < imagesAmount) {
                    horizontal_recycler_view.scrollToPosition(previusPosition + 1);
                }
            }
        });



        return view;
    }

    private void initView(){
        horizontal_recycler_view = (RecyclerView) view.findViewById(R.id.horizontal_recycler_view);
        leftArrow = (ImageView) view.findViewById(R.id.leftArrow);
        rightArrow = (ImageView) view.findViewById(R.id.rightArrow);
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

        }
        @Override
        public int getItemCount() {
            return horizontalList.size();
        }
    }




}

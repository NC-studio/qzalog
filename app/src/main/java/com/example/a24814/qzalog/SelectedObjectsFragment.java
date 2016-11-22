package com.example.a24814.qzalog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a24814.qzalog.components.Backend;
import com.example.a24814.qzalog.components.BackendCallback;
import com.example.a24814.qzalog.components.BaseFile;
import com.example.a24814.qzalog.components.DataBaseAdapter;
import com.example.a24814.qzalog.models.Objects;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class SelectedObjectsFragment extends Fragment {

    private final String TAG = "CategoryObjects Fragment";

    private SelectedObjectsFragment fragment;

    private List<Objects> objects = new ArrayList<Objects>();;

    private ListView objectsList;

    private View loadingFooter;

    private View view;

    private ArrayAdapter<Objects> adapter;

    private Boolean isLoading = false;

    private Boolean isUploaded = false;

    private String urlRequest;


    private Integer objNumber = 0;

    private List<Integer> selectedObjects;

    private BackendCallback<List<Objects>> backendAsync;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = this;
        view = inflater.inflate(R.layout.objects_list,
                container, false);
        initView();
        initAdapter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        if(backendAsync != null){
            backendAsync.cancel(true);
        }
        super.onPause();
    }



    private void initView(){
        objectsList = (ListView) view.findViewById(R.id.objectsList);
        final LayoutInflater factory = getActivity().getLayoutInflater();
        loadingFooter = factory.inflate(R.layout.list_loader, null);
        objectsList.addFooterView(loadingFooter);

    }



    private void initAdapter(){

        selectedObjects = ((BaseFile) getActivity().getApplication()).getMapObjects();

        adapter = new ObjectAdapter(getActivity(), R.layout.objects_list_item, objects);
        objectsList.setAdapter(adapter);

        objectsList.setOnScrollListener(new SampleScrollListener(getActivity()));
        objectsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, final View arg1, int arg2,
                                    long arg3) {
                backendAsync.cancel(true);
                Objects obj = objects.get(arg2);
                Intent intent = new Intent(getActivity(), ObjectDetailActivity.class);
                intent.putExtra("objId", obj.getId());
                startActivity(intent);
            }
        });
        isLoading = true;


        backendAsync =  Backend.getSelectedObjects(getActivity(), fragment, selectedObjects);
        backendAsync.execute();

    }

    public class SampleScrollListener implements AbsListView.OnScrollListener {
        private final Context context;
       // private final Object scrollTag = new Object(); // this can be static or not, depending what u want to achieve

        public SampleScrollListener(Context context) {
            this.context = context;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            final Picasso picasso = Picasso.with(context);
            if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                picasso.resumeTag("image");
            } else {
                picasso.pauseTag("image");
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {

        }
    }





    private static class ViewHolder {
        TextView title;
        ImageView img;
       // ProgressBar progress;
        TextView region;
        TextView price;
        TextView discount;
        TextView info;
        ImageView likeCheker;
    }

    public class ObjectAdapter extends ArrayAdapter<Objects>
    {
        Context context;
        int layoutResourceId;
        List<Objects> data = null;

        public ObjectAdapter(Context context, int layoutResourceId, List<Objects> data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);
                holder.img = (ImageView) convertView.findViewById(R.id.objectImage);
               // holder.progress = (ProgressBar)  convertView.findViewById(R.id.progress);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.region = (TextView) convertView.findViewById(R.id.address);
                holder.price = (TextView) convertView.findViewById(R.id.price);
                holder.discount = (TextView) convertView.findViewById(R.id.discount);
                holder.info = (TextView) convertView.findViewById(R.id.additionalInfo);
                holder.likeCheker = (ImageView) convertView.findViewById(R.id.likeCheker);

                holder.likeCheker.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Objects object = getItem(position);
                        DataBaseAdapter myDatabaseHelper = new DataBaseAdapter(getActivity(), true);
                        Boolean liked = myDatabaseHelper.manageLiked(object.getId());
                        myDatabaseHelper.close();

                        if(liked){
                            object.setLikerIcon(getResources().getDrawable(R.drawable.ic_liked_active));
                        }else{
                            object.setLikerIcon(getResources().getDrawable(R.drawable.ic_liked));
                        }
                        holder.likeCheker.setImageDrawable(object.getLikerIcon());

                    }
                });



                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            Objects object = getItem(position);
            holder.likeCheker.setImageDrawable(object.getLikerIcon());
            //holder.progress.setVisibility(View.VISIBLE);
            Picasso.with(getActivity())
                    .load(object.getImgUrl())
                    .tag("image")
                    .placeholder(R.drawable.trick )
                    .into(holder.img);

            holder.title.setText(object.getName());
            holder.region.setText(object.getRegion());
            holder.price.setText(object.getPrice());
            holder.info.setText(object.getInfo());
            holder.discount.setText(object.getDiscount());
            holder.discount.setVisibility(object.getVisibility());

            return convertView;
        }
    }

    public void backendResponse(List<Objects> clones){

       //loadingFooter.setVisibility(View.GONE);
        for (Objects item : clones) {
            objects.add(item);
        }
        adapter.notifyDataSetChanged();
        objectsList.requestLayout();
        objectsList.removeFooterView(loadingFooter);
        isUploaded = true;
        isLoading = false;




    }




}

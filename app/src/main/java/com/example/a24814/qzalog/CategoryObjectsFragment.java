package com.example.a24814.qzalog;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.a24814.qzalog.components.Backend;
import com.example.a24814.qzalog.components.BaseFile;
import com.example.a24814.qzalog.models.Objects;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class CategoryObjectsFragment extends Fragment {

    private final String TAG = "CategoryObjects Fragment";

    private CategoryObjectsFragment fragment;

    private List<Objects> objects = new ArrayList<Objects>();;

    private ListView objectsList;

    private View loadingFooter;

    private View view;

    private ArrayAdapter<Objects> adapter;

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    private Boolean isLoading = false;

    private Boolean isUploaded = false;

    private Boolean pauseLoading = false;

    private String urlRequest;

    private Integer page = 1;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragment = this;
        view = inflater.inflate(R.layout.objects_list,
                container, false);

        urlRequest = ((BaseFile) getActivity().getApplication()).getUrl();
        objects = ((BaseFile) getActivity().getApplication()).getObjects();

        initView();
        initAdapter();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first




    }




    private void initView(){
        objectsList = (ListView) view.findViewById(R.id.objectsList);
        final LayoutInflater factory = getActivity().getLayoutInflater();
        loadingFooter = factory.inflate(R.layout.list_loader, null);
        objectsList.addFooterView(loadingFooter);
    }



    private void initAdapter(){


        adapter = new ObjectAdapter(getActivity(), R.layout.objects_list_item, objects);
        objectsList.setAdapter(adapter);

        objectsList.setOnScrollListener(new SampleScrollListener(getActivity()));
        objectsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, final View arg1, int arg2,
                                    long arg3) {

            }
        });
        isLoading = true;
        Backend.getObjects(getActivity(), fragment, urlRequest, page);

    }

    public class SampleScrollListener implements AbsListView.OnScrollListener {
        private final Context context;
        private final Object scrollTag = new Object(); // this can be static or not, depending what u want to achieve

        public SampleScrollListener(Context context) {
            this.context = context;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            final Picasso picasso = Picasso.with(context);
            if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                picasso.resumeTag(scrollTag);
            } else {
                picasso.pauseTag(scrollTag);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen == totalItemCount) {
                if(isUploaded == false && isLoading==false) {
                    isLoading = true;
                    loadingFooter.setVisibility(View.VISIBLE);

                    Backend.getObjects(getActivity(), fragment, urlRequest, page);
                }
            }
        }
    }



    private static class ViewHolder {
        TextView title;
        ImageView img;
        ProgressBar progress;

        TextView region;
        TextView price;
        TextView discount;
        TextView info;

    }

    public class ObjectAdapter extends ArrayAdapter<Objects>
    {
        Context context;
        int layoutResourceId;
        List<Objects> data = null;


        public  ObjectAdapter(Context context, int layoutResourceId, List<Objects> data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }


        public View getView(int position, View convertView, ViewGroup parent)
        {
            final ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);

                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.img = (ImageView) convertView.findViewById(R.id.objectImage);
                holder.progress = (ProgressBar)  convertView.findViewById(R.id.progress);

                holder.region = (TextView) convertView.findViewById(R.id.address);
                holder.price = (TextView) convertView.findViewById(R.id.price);
                holder.info = (TextView) convertView.findViewById(R.id.additionalInfo);


                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            Objects object = getItem(position);

            String image_url = object.getImgUrl();

            if (holder.img.getTag() == null || !holder.img.getTag().equals(image_url)) {
                holder.progress.setVisibility(View.VISIBLE);
                Picasso.with(getActivity())
                        .load(object.getImgUrl())
                        .into(holder.img, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                holder.progress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                holder.img.setTag(image_url);
            }
            holder.title.setText(object.getName());
            holder.region.setText(object.getRegion());
            holder.price.setText(object.getPrice());
            holder.info.setText(object.getInfo());






            return convertView;
        }
    }

    public void backendResponse(Boolean isUploaded){
        page = page + 1;

        loadingFooter.setVisibility(View.GONE);


        this.isUploaded = isUploaded;
        isLoading = false;

        objects = ((BaseFile) getActivity().getApplication()).getObjects();
        adapter.notifyDataSetChanged();
    }




}

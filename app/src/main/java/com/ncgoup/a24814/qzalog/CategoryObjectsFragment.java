package com.ncgoup.a24814.qzalog;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a24814.qzalog.R;
import com.ncgoup.a24814.qzalog.components.Backend;
import com.ncgoup.a24814.qzalog.components.BackendCallback;
import com.ncgoup.a24814.qzalog.components.BaseFile;
import com.ncgoup.a24814.qzalog.components.DataBaseAdapter;
import com.ncgoup.a24814.qzalog.models.Objects;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class CategoryObjectsFragment extends Fragment {

    private final String TAG = "CategoryObjects Fragment";

    private CategoryObjectsFragment fragment;

    private List<Objects> objects = new ArrayList<Objects>();

    private ListView objectsList;

    private View loadingFooter;

    private View view;

    private RelativeLayout noFoundBlock;

    private ArrayAdapter<Objects> adapter;

    private Boolean isLoading = false;

    private Boolean isUploaded = false;

    private Boolean pauseLoading = false;

    private String urlRequest;

    private Integer page = 1;

    private Integer objNumber = 0;

    private BackendCallback<List<Objects>> backendAsync;

    private FrameLayout filterDate;

    private FrameLayout filterPriceUp;

    private FrameLayout filterPriceDown;

    private Integer filterButtonActivePos = 0;

    private LayoutInflater factory;

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
        factory = getActivity().getLayoutInflater();
        loadingFooter = factory.inflate(R.layout.list_loader, null);
        objectsList.addFooterView(loadingFooter);
        noFoundBlock = (RelativeLayout) view.findViewById(R.id.noFoundBlock);


        view.findViewById(R.id.filterContainer).setVisibility(View.VISIBLE);
        filterDate = (FrameLayout) view.findViewById(R.id.filterDate);
        filterPriceUp = (FrameLayout) view.findViewById(R.id.filterPriceUp);
        filterPriceDown = (FrameLayout) view.findViewById(R.id.filterPriceDown);

        filterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterButtonActivePos != 0){
                    filterButtonActivePos = 0;
                    ((TextView) filterDate.findViewById(R.id.filterDateText)).setTextColor(getResources().getColor(R.color.white));
                    ((TextView) filterPriceUp.findViewById(R.id.filterPriceUpText)).setTextColor(getResources().getColor(R.color.appBlue));
                    ((TextView) filterPriceDown.findViewById(R.id.filterPriceDownText)).setTextColor(getResources().getColor(R.color.appBlue));
                    filterDate.setBackgroundResource(R.drawable.border_filter_left_active);
                    filterPriceUp.setBackgroundResource(R.drawable.border_filter_center);
                    filterPriceDown.setBackgroundResource(R.drawable.border_filter_right);
                    sortRequest("");
                }
            }
        });

        filterPriceUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterButtonActivePos != 1){
                    filterButtonActivePos = 1;
                    ((TextView) filterDate.findViewById(R.id.filterDateText)).setTextColor(getResources().getColor(R.color.appBlue));
                    ((TextView) filterPriceUp.findViewById(R.id.filterPriceUpText)).setTextColor(getResources().getColor(R.color.white));
                    ((TextView) filterPriceDown.findViewById(R.id.filterPriceDownText)).setTextColor(getResources().getColor(R.color.appBlue));
                    filterDate.setBackgroundResource(R.drawable.border_filter_left);
                    filterPriceUp.setBackgroundResource(R.drawable.border_filter_center_active);
                    filterPriceDown.setBackgroundResource(R.drawable.border_filter_right);
                    sortRequest("&sort=cost_out");
                }
            }
        });

        filterPriceDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterButtonActivePos != 2){
                    filterButtonActivePos = 2;
                    ((TextView) filterDate.findViewById(R.id.filterDateText)).setTextColor(getResources().getColor(R.color.appBlue));
                    ((TextView) filterPriceUp.findViewById(R.id.filterPriceUpText)).setTextColor(getResources().getColor(R.color.appBlue));
                    ((TextView) filterPriceDown.findViewById(R.id.filterPriceDownText)).setTextColor(getResources().getColor(R.color.white));
                    filterDate.setBackgroundResource(R.drawable.border_filter_left);
                    filterPriceUp.setBackgroundResource(R.drawable.border_filter_center);
                    filterPriceDown.setBackgroundResource(R.drawable.border_filter_right_active);
                    sortRequest("&sort=-cost_out");
                }
            }
        });

    }

    private void sortRequest(String sortString){
        if(isLoading == true){
            backendAsync.cancel(true);
        }
        objects = new ArrayList<Objects>();
        adapter.clear();
        adapter = new ObjectAdapter(getActivity(), R.layout.objects_list_item, objects);
        objectsList.setAdapter(adapter);

        page = 1;
        if(isUploaded == true){
            isUploaded = false;
            loadingFooter = factory.inflate(R.layout.list_loader, null);
            objectsList.addFooterView(loadingFooter);
        }

        loadingFooter.setVisibility(View.VISIBLE);
        isLoading = true;
        backendAsync = Backend.getObjects(getActivity(), fragment, urlRequest + sortString, page);
        backendAsync.execute();

    }



    private void initAdapter(){


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
                intent.putExtra("objPos", arg2);
                startActivity(intent);
            }
        });
        isLoading = true;
        backendAsync =  Backend.getObjects(getActivity(), fragment, urlRequest, page);
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
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen == totalItemCount) {
                if(isUploaded == false && isLoading==false) {
                    isLoading = true;
                    loadingFooter.setVisibility(View.VISIBLE);

                    backendAsync = Backend.getObjects(getActivity(), fragment, urlRequest, page);
                    backendAsync.execute();
                }
            }
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
                        //object.setLiked(liked);
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
                    /*.into(holder.img, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                           // holder.progress.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError() {
                        }
                    });*/

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
        page = page + 1;
        loadingFooter.setVisibility(View.GONE);

        for (Objects item : clones) {
            objects.add(item);
        }
        adapter.notifyDataSetChanged();
        objectsList.requestLayout();
        if(objects != null) {
            ((BaseFile) getActivity().getApplication()).setObjects(objects);

            if (clones.size() % 10 > 0 || objNumber == objects.size()) {
                if (objects.size() == 0) {
                    noFoundBlock.setVisibility(View.VISIBLE);
                    objectsList.setVisibility(View.GONE);
                }
                this.isUploaded = true;

                objectsList.removeFooterView(loadingFooter);
            } else {
                objNumber = objects.size();
            }
        }

        isLoading = false;
        //List<Objects> objects1 = ((BaseFile) getActivity().getApplication()).getObjects();
       // objects =  ((BaseFile) getActivity().getApplication()).getObjects();
    }




}

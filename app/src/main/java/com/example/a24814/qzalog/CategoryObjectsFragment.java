package com.example.a24814.qzalog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a24814.qzalog.components.BaseFile;
import com.example.a24814.qzalog.models.Objects;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CategoryObjectsFragment extends Fragment {

    private final String TAG = "CategoryObjects Fragment";

    private List<Objects> objects;

    private ListView objectsList;

    private View loadingFooter;

    private View view;

    private ArrayAdapter<Objects> adapter;

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.objects_list,
                container, false);

        initView();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initImageLoader();

        initAdapter();

    }

    private void initView(){
        objectsList = (ListView) view.findViewById(R.id.objectsList);
        final LayoutInflater factory = getActivity().getLayoutInflater();
        loadingFooter = factory.inflate(R.layout.list_loader, null);
    }


    private void initImageLoader(){
        // UNIVERSAL IMAGE LOADER SETUP
        options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                //.showImageForEmptyUri(R.drawable.avatar)
                //.showImageOnFail(R.drawable.avatar)
                //.showImageOnLoading(R.drawable.photo_24dip_xxxhdpi)
                .imageScaleType(ImageScaleType.EXACTLY).displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext())
                .defaultDisplayImageOptions(options).memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private void initAdapter(){
        objects = ((BaseFile) getActivity().getApplication()).getObjects();
        objects.add(new Objects(1, "asda", "http://qzalog.kz/uploads/objects/344669/thumbnails/DQVFWIrJ6HIgfK1W1v4dGjCWRJoJ05LB.jpg"));
        objects.add(new Objects(1, "asda2", "http://qzalog.kz/uploads/objects/302174/thumbnails/hxX291BjE7HyeuS0Mta9SSE0Tz8HCiE4.jpg"));
        objects.add(new Objects(1, "asda3", "http://qzalog.kz/uploads/objects/42442662/thumbnails/eYcH35pMt5Ka9OTx8YYRZDZepbq6R8Jr.JPG"));

        adapter = new ObjectAdapter(getActivity(), R.layout.objects_list_item, objects);
        objectsList.setAdapter(adapter);
        // categoryList.addFooterView(loadingFooter);

        objectsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, final View arg1, int arg2,
                                    long arg3) {

            }
        });

    }

    private static class ViewHolder {
        TextView title;
        ImageView img;

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
            int listViewItemType = getItemViewType(position);

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);

                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.img = (ImageView) convertView.findViewById(R.id.objectImage);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            Objects object = getItem(position);

            String image_url = object.getImgUrl();

            if (holder.img.getTag() == null || !holder.img.getTag().equals(image_url)) {
                Picasso.with(getActivity())
                        .load(object.getImgUrl())
                        .into(holder.img);
                holder.img.setTag(image_url);
            }





           // holder.title.setText(object.getName());


            return convertView;
        }
    }



}

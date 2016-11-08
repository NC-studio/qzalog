package com.example.a24814.qzalog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.a24814.qzalog.models.Category;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


public class CategoryObjectsFragment extends Fragment {

    private final String TAG = "CategoryObjects Fragment";

    private View loadingFooter;

    private View view;

    private ArrayAdapter<Category> adapter;

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

    }

    private void initView(){
        //categoryList = (ListView) view.findViewById(R.id.categorylist);
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



}

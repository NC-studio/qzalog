package com.example.a24814.qzalog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a24814.qzalog.components.Backend;
import com.example.a24814.qzalog.components.BaseFile;
import com.example.a24814.qzalog.components.DataBaseAdapter;
import com.example.a24814.qzalog.models.Category;

import java.util.List;


public class CategoryFragment extends Fragment {

    private final String TAG = "Category Fragment";

    private List<Category> categories;

    private ListView categoryList;

    //private View loadingFooter;

    private View view;

    private ArrayAdapter<Category> adapter;




   // private ImageLoader imageLoader;

   // private DisplayImageOptions options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.category_item_list,
                container, false);

        initView();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



       // initImageLoader();
        initAdapter();
    }

    private void initView(){
        categoryList = (ListView) view.findViewById(R.id.categorylist);
        final LayoutInflater factory = getActivity().getLayoutInflater();
        //loadingFooter = factory.inflate(R.layout.list_loader, null);
    }


   /* private void initImageLoader(){
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
*/
    private void initAdapter(){
        categories = ((BaseFile) getActivity().getApplication()).getCategories();
        getCategories();

        adapter = new CategoryAdapter(getActivity(), R.layout.category_item, categories);
        categoryList.setAdapter(adapter);
       // categoryList.addFooterView(loadingFooter);

        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, final View arg1, int arg2,
                                    long arg3) {
                try {
                    Intent intent = new Intent(getActivity(), CategoryActivity.class);
                    intent.putExtra("categoryId", arg2);
                    startActivity(intent);
                } catch ( IndexOutOfBoundsException e ) {
                    Log.d(TAG, e.getMessage());
                }
            }
        });

    }

    private static class ViewHolder {
        TextView name;
        TextView amount;
        ImageView icon;
       // Integer position;

    }

    public class CategoryAdapter extends ArrayAdapter<Category>
    {
        Context context;
        int layoutResourceId;
        List<Category> data = null;


        public  CategoryAdapter(Context context, int layoutResourceId, List<Category> data) {
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

                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.amount = (TextView) convertView.findViewById(R.id.amount);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            Category category = getItem(position);
            holder.name.setText(category.getName());
            holder.amount.setText(category.getObjAmount());

            int id = getResources().getIdentifier("ic_category_" + String.valueOf(category.getObjectId()), "drawable", getActivity().getPackageName());
            holder.icon.setImageDrawable(getResources().getDrawable(id));

            return convertView;
        }
    }

    private void  getCategories(){
        DataBaseAdapter myDatabaseHelper = new DataBaseAdapter(getActivity(), true);
        categories = myDatabaseHelper.getCategories(categories);
        myDatabaseHelper.close();
        Backend.getCategoriesInfo(getActivity(), getActivity(), this);
    }

    public void  backendResponse(){
        adapter.notifyDataSetChanged();
    }




}

package com.ncgoup.a24814.qzalog;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.ncgoup.a24814.qzalog.components.Backend;
import com.ncgoup.a24814.qzalog.components.BaseFile;
import com.ncgoup.a24814.qzalog.components.DataBaseAdapter;
import com.ncgoup.a24814.qzalog.models.Category;

import java.util.List;


public class CategoryFragment extends Fragment {

    private final String TAG = "Category Fragment";

    private List<Category> categories;

    private ListView categoryList;

    private View view;

    private ArrayAdapter<Category> adapter;

    private Boolean uploaded = false;

    private FrameLayout pageLink;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.category_item_list,
                container, false);
        initView();

        if (savedInstanceState != null)
        {
            //Log.v("Main", savedInstanceState.getString("loaded"));
        }
        initView();
        initAdapter();


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        //savedInstanceState.putInt("loaded", 1);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void initView(){
        categoryList = (ListView) view.findViewById(R.id.categorylist);
        final LayoutInflater factory = getActivity().getLayoutInflater();
        //loadingFooter = factory.inflate(R.layout.list_loader, null);
    }

    private void initAdapter(){
        if(uploaded == false) {
            categories = ((BaseFile) getActivity().getApplication()).getCategories();
            if(categories.size() == 0) {
                categories.add(new Category("Cпособы приобритения имущества"));
                getCategories();
            }

            adapter = new CategoryAdapter(getActivity(), R.layout.category_item, categories);
            categoryList.setAdapter(adapter);

            categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, final View arg1, int arg2,
                                        long arg3) {
                    Category category = adapter.getItem(arg2);
                    if(category.getType() == 0){
                        try {
                            Intent intent = new Intent(getActivity(), CategoryActivity.class);
                            intent.putExtra("category", arg2);
                            startActivity(intent);
                        } catch (IndexOutOfBoundsException e) {
                            Log.d(TAG, e.getMessage());
                        }
                    }else{
                        Intent intent = new Intent(getActivity(), Info2Activity.class);
                        startActivity(intent);
                    }

                }
            });
        }

    }

    private static class ViewHolder {
        TextView name;
        TextView amount;
        ImageView icon;
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

        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).getType();
        }


        public View getView(int position, View convertView, ViewGroup parent)
        {
            final ViewHolder holder;


            int listViewItemType = getItemViewType(position);


            if (convertView == null) {
                holder = new ViewHolder();
                if (listViewItemType == 1) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_item_top, parent, false);
                } else {
                    convertView = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);
                }


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
        uploaded = true;
        adapter.notifyDataSetChanged();
    }




}

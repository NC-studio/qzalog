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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a24814.qzalog.R;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.category_item_list,
                container, false);

        initView();
        initAdapter();

        return view;
    }



    private void initView(){
        categoryList = (ListView) view.findViewById(R.id.categorylist);
        final LayoutInflater factory = getActivity().getLayoutInflater();
        //loadingFooter = factory.inflate(R.layout.list_loader, null);
    }

    private void initAdapter(){
        if(uploaded == false) {
            categories = ((BaseFile) getActivity().getApplication()).getCategories();

            getCategories();

            adapter = new CategoryAdapter(getActivity(), R.layout.category_item, categories);
            categoryList.setAdapter(adapter);

            categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, final View arg1, int arg2,
                                        long arg3) {
                    try {
                        Intent intent = new Intent(getActivity(), CategoryActivity.class);
                        intent.putExtra("category", arg2);
                        startActivity(intent);
                    } catch (IndexOutOfBoundsException e) {
                        Log.d(TAG, e.getMessage());
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


        public View getView(int position, View convertView, ViewGroup parent)
        {
            final ViewHolder holder;

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
        uploaded = true;
        adapter.notifyDataSetChanged();
    }




}
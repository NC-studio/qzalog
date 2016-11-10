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
import android.widget.ListView;
import android.widget.TextView;

import com.example.a24814.qzalog.components.BaseFile;
import com.example.a24814.qzalog.components.DataBaseHelper;
import com.example.a24814.qzalog.models.Region;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegionFragment extends Fragment {
    private final String TAG = "Region Fragment";

    private List<Region> regions = new ArrayList<Region>();

    private ListView regionsList;

    private View view;

    private ArrayAdapter<Region> adapter;

    private JSONObject formRegion;

    private Integer numberOfPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_list,
                container, false);


        initView();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAdapter();
    }

    private void initView(){
        regionsList = (ListView) view.findViewById(R.id.customList);
    }

    private void initAdapter(){
        //Get JSONObject of selected Region


        if(((ListActivity)getActivity()).numberOfPage == 2){
            formRegion = ((BaseFile) getActivity().getApplication()).getFormRegion();
        }else{
            formRegion = new JSONObject();
        }

        getRegions(formRegion);


        adapter = new RegionAdapter(getActivity(), R.layout.activity_list_item, regions);
        regionsList.setAdapter(adapter);
        // categoryList.addFooterView(loadingFooter);

        regionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, final View arg1, int arg2,
                                    long arg3) {
                getActivity().finish();
                try {
                    if(arg2 != 0){
                        Intent intent = new Intent(getActivity(), ListActivity.class);
                        Region regionSelected = regions.get(arg2);
                        formRegion = new JSONObject();
                        try {
                            formRegion.put("id", regionSelected.getId());
                            formRegion.put("name", regionSelected.getName());
                            formRegion.put("position", regionSelected.getPosition());
                            formRegion.put("parent", regionSelected.getId());
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        ((BaseFile) getActivity().getApplication()).setFormRegion(formRegion);

                        if(((ListActivity) getActivity()).numberOfPage == 2 || checkChildren(regionSelected.getId()) == false){
                            getActivity().finish();
                        }else{
                            intent.putExtra("numberOfPage", ((ListActivity)getActivity()).numberOfPage);
                            startActivity(intent);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        }



                    }

                } catch ( IndexOutOfBoundsException e ) {
                    Log.d(TAG, e.getMessage());
                }

            }
        });

    }

    private static class ViewHolder {
        TextView name;
    }

    public class RegionAdapter extends ArrayAdapter<Region>
    {
        Context context;
        int layoutResourceId;
        List<Region> data = null;


        public  RegionAdapter(Context context, int layoutResourceId, List<Region> data) {
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

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            Region region = getItem(position);
            holder.name.setText(region.getName());

            return convertView;
        }
    }

    private void  getRegions(JSONObject formRegion){



        DataBaseHelper myDatabaseHelper = new DataBaseHelper(getActivity());
        myDatabaseHelper.openDataBase();
        Integer parent = null;
        try {
            parent = formRegion.getInt("parent");
        }catch (JSONException e){
            Log.d(TAG, e.getMessage());
        }
        regions.add(new Region(0, "Все", 0, parent));
        regions = myDatabaseHelper.getRegions(regions, parent);

        myDatabaseHelper.close();
    }


        private Boolean  checkChildren(Integer objectId){
            DataBaseHelper myDatabaseHelper = new DataBaseHelper(getActivity());
            myDatabaseHelper.openDataBase();


            Boolean check = myDatabaseHelper.checkChildren(regions, objectId);

            myDatabaseHelper.close();

            return check;
        }





}

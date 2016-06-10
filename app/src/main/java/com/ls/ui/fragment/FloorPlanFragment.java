package com.ls.ui.fragment;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.data.FloorPlan;
import com.ls.util.L;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 09.06.2016.
 */
public class FloorPlanFragment  extends Fragment
{
    public static final String TAG = "FloorPlanFragment";
    private Spinner floorSelector;
    private List<FloorPlan>plans;
    private View actionbarLayout;
    private ImageView floorImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fr_floor_plan, null);
        actionbarLayout = inflater.inflate(R.layout.fr_floor_plan_action_bar,null);
        floorSelector = (Spinner)actionbarLayout.findViewById(R.id.spinner);
        floorSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                new LoadPlanImageTask().execute(plans.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        floorImage = (ImageView)result.findViewById(R.id.floor_plan_image);

        new LoadPlansTask().execute();
        return result;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity)this.getActivity();
        if(activity != null && activity.getSupportActionBar() != null) {
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(actionbarLayout,
                    new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    public void onPause()
    {
        AppCompatActivity activity = (AppCompatActivity)this.getActivity();
        if(activity != null && activity.getSupportActionBar() != null) {
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setCustomView(null);
        }
        super.onPause();
    }

    private class LoadPlansTask extends AsyncTask<Void,Void,List<FloorPlan>>{

        @Override
        protected List<FloorPlan> doInBackground(Void... params)
        {
            return Model.instance().getFloorPlansManager().getFloorPlans();
        }

        @Override
        protected void onPostExecute(List<FloorPlan> floorPlans)
        {
            super.onPostExecute(floorPlans);
            plans = floorPlans;

            List<String>names = new ArrayList<>(floorPlans.size());
            for(FloorPlan plan:plans){
              names.add(plan.getName());
            }

            ArrayAdapter<String> floorsAdapter = new ArrayAdapter<>(floorSelector.getContext(), android.R.layout.simple_spinner_item, names);
            floorSelector.setAdapter(floorsAdapter);
        }
    }

    private class LoadPlanImageTask extends AsyncTask<FloorPlan,Void,Drawable>{

        @Override
        protected void onPreExecute()
        {
            floorSelector.setEnabled(false);
            floorSelector.setClickable(false);
            floorImage.setImageDrawable(null);
            super.onPreExecute();
        }

        @Override
        protected Drawable doInBackground(FloorPlan... params)
        {
            Bitmap planImage =  Model.instance().getFloorPlansManager().getImageForPlan(params[0],1000,1000);
            if(planImage != null){
                return new BitmapDrawable(null,planImage);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Drawable drawable)
        {
            super.onPostExecute(drawable);
            floorSelector.setEnabled(true);
            floorSelector.setClickable(true);
            floorImage.setImageDrawable(drawable);
        }
    }

}

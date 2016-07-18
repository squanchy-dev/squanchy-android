package com.ls.ui.fragment;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.data.FloorPlan;
import com.ls.ui.adapter.FloorSelectorAdapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 09.06.2016.
 */
public class FloorPlanFragment  extends Fragment
{
    public static int REDCOMMENDED_FLOOR_IMAGE_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels * 2;
    public static int REDCOMMENDED_FLOOR_IMAGE_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels * 2;

    public static final String TAG = "FloorPlanFragment";
    private Spinner floorSelector;
    private List<FloorPlan>plans;
    private ImageView floorImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fr_floor_plan, null);
        floorSelector = (Spinner)result.findViewById(R.id.spinner);
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

        return result;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        new LoadPlansTask().execute();
    }

//    void resolveTitleVisibility(){
//        AppCompatActivity activity = (AppCompatActivity)this.getActivity();
//        if(activity != null && activity.getSupportActionBar() != null) {
//            ActionBar actionBar = activity.getSupportActionBar();
//            if(this.isResumed()) {
//                if (plans != null && !plans.isEmpty()) {
//                    actionBar.setTitle("");
//                    actionBar.setCustomView(actionbarLayout);
//                } else {
//                    actionBar.setTitle(R.string.floor_plan);
//                    actionBar.setCustomView(null);
//                }
//            }
//        }
//    }

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

            FloorSelectorAdapter floorsAdapter = new FloorSelectorAdapter(floorSelector.getContext(),names);
            floorSelector.setAdapter(floorsAdapter);

            floorSelector.setVisibility(plans.isEmpty() ? View.INVISIBLE : View.VISIBLE);
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
            Bitmap planImage =  Model.instance().getFloorPlansManager().getImageForPlan(params[0],
                    REDCOMMENDED_FLOOR_IMAGE_WIDTH,REDCOMMENDED_FLOOR_IMAGE_HEIGHT);
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

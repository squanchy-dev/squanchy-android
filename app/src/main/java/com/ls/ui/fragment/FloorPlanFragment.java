package com.ls.ui.fragment;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.data.FloorPlan;
import com.ls.util.L;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import java.util.List;

/**
 * Created on 09.06.2016.
 */
public class FloorPlanFragment  extends Fragment
{
    public static final String TAG = "FloorPlanFragment";
    private SpinnerAdapter floorsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        new LoadPlansTask().execute();
        return inflater.inflate(R.layout.fr_floor_plan, null);
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

            L.e("Plans:" + floorPlans);
        }
    }

}

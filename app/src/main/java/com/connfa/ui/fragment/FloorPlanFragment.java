package com.connfa.ui.fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.connfa.R;
import com.connfa.model.Model;
import com.connfa.model.UpdatesManager;
import com.connfa.model.data.FloorPlan;
import com.connfa.ui.adapter.FloorSelectorAdapter;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;
import java.util.List;

public class FloorPlanFragment extends Fragment {

    private static int RECOMMENDED_FLOOR_IMAGE_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels * 2;
    private static int RECOMMENDED_FLOOR_IMAGE_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels * 2;

    public static final String TAG = "FloorPlanFragment";

    private View mLayoutContent, mLayoutPlaceholder;
    private Spinner floorSelector;
    private List<FloorPlan> plans;
    private SubsamplingScaleImageView floorImage;

    @Override
    public void onStart() {
        super.onStart();
        Model.getInstance().getUpdatesManager().registerUpdateListener(updateListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fr_floor_plan, container, false);
        mLayoutContent = result.findViewById(R.id.layout_content);
        mLayoutPlaceholder = result.findViewById(R.id.layout_placeholder);
        floorSelector = (Spinner) result.findViewById(R.id.spinner);
        floorSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadPlanImage(plans.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        floorImage = (SubsamplingScaleImageView) result.findViewById(R.id.floor_plan_image);

        new LoadPlansTask(loadPlansCallback).execute();

        return result;
    }

    private void loadPlanImage(FloorPlan floorPlan) {
        floorSelector.setEnabled(false);
        floorSelector.setClickable(false);
        new LoadPlanImageTask(loadPlanImageCallback)
                .execute(floorPlan);
    }

    private final LoadPlanImageTask.LoadPlanImageTaskCallback loadPlanImageCallback = new LoadPlanImageTask.LoadPlanImageTaskCallback() {
        @Override
        public void onPlanImageLoaded(Bitmap planImage) {
            floorSelector.setEnabled(true);
            floorSelector.setClickable(true);
            floorImage.setImage(ImageSource.bitmap(planImage));
            floorImage.resetScaleAndCenter();
        }
    };

    private final LoadPlansTask.LoadPlansTaskCallback loadPlansCallback = new LoadPlansTask.LoadPlansTaskCallback() {
        @Override
        public void onPlansLoaded(List<FloorPlan> floorPlans) {
            plans = floorPlans;

            if (floorPlans == null || floorPlans.isEmpty()) {
                mLayoutContent.setVisibility(View.GONE);
                mLayoutPlaceholder.setVisibility(View.VISIBLE);
            } else {
                mLayoutContent.setVisibility(View.VISIBLE);
                mLayoutPlaceholder.setVisibility(View.GONE);

                List<String> names = new ArrayList<>(floorPlans.size());
                for (FloorPlan plan : floorPlans) {
                    names.add(plan.getName());
                }

                FloorSelectorAdapter floorsAdapter = new FloorSelectorAdapter(floorSelector.getContext(), names);
                floorSelector.setAdapter(floorsAdapter);

                floorSelector.setVisibility(floorPlans.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        Model.getInstance().getUpdatesManager().unregisterUpdateListener(updateListener);
    }

    private final UpdatesManager.DataUpdatedListener updateListener = new UpdatesManager.DataUpdatedListener() {
        @Override
        public void onDataUpdated(List<Integer> requestIds) {

            if (requestIds.contains(UpdatesManager.FLOOR_PLANS_REQUEST_ID)) {
                new LoadPlansTask(loadPlansCallback).execute();
            }
        }
    };

    private static class LoadPlansTask extends AsyncTask<Void, Void, List<FloorPlan>> {

        private final LoadPlansTaskCallback callback;

        LoadPlansTask(LoadPlansTaskCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<FloorPlan> doInBackground(Void... params) {
            return Model.getInstance().getFloorPlansManager().getFloorPlans();
        }

        @Override
        protected void onPostExecute(@Nullable List<FloorPlan> floorPlans) {
            if (floorPlans != null) {
                callback.onPlansLoaded(floorPlans);
            }
        }

        interface LoadPlansTaskCallback {

            void onPlansLoaded(List<FloorPlan> floorPlans);
        }
    }

    private static class LoadPlanImageTask extends AsyncTask<FloorPlan, Void, Bitmap> {

        private final LoadPlanImageTaskCallback callback;

        LoadPlanImageTask(LoadPlanImageTaskCallback callback) {
            this.callback = callback;
        }

        @Override
        @Nullable
        protected Bitmap doInBackground(FloorPlan... params) {
            return Model.getInstance()
                    .getFloorPlansManager()
                    .getImageForPlan(
                            params[0],
                            RECOMMENDED_FLOOR_IMAGE_WIDTH,
                            RECOMMENDED_FLOOR_IMAGE_HEIGHT
                    );
        }

        @Override
        protected void onPostExecute(@Nullable Bitmap planImage) {
            if (planImage != null) {
                callback.onPlanImageLoaded(planImage);
            }
        }

        interface LoadPlanImageTaskCallback {

            void onPlanImageLoaded(Bitmap planImage);
        }
    }
}

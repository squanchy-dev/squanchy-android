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

    private View contentView;
    private View placeholderView;
    private Spinner floorSelector;
    private List<FloorPlan> plans;
    private SubsamplingScaleImageView floorImage;
    private LoadPlansTask loadPlansTask;
    private LoadPlanImageTask loadPlanImageTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fr_floor_plan, container, false);
        contentView = result.findViewById(R.id.layout_content);
        placeholderView = result.findViewById(R.id.layout_placeholder);
        floorSelector = (Spinner) result.findViewById(R.id.spinner);
        floorSelector.setOnItemSelectedListener(itemSelectedListener);

        floorImage = (SubsamplingScaleImageView) result.findViewById(R.id.floor_plan_image);

        return result;
    }

    @Override
    public void onStart() {
        super.onStart();
        Model.getInstance().getUpdatesManager().registerUpdateListener(updateListener);
        loadFloorPlans();
    }

    private final AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            loadPlanImage(plans.get(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing
        }
    };

    private void loadPlanImage(FloorPlan floorPlan) {
        if (loadPlanImageTask != null) {
            loadPlanImageTask.cancel(true);
        }

        floorSelector.setEnabled(false);
        floorSelector.setClickable(false);

        loadPlanImageTask = new LoadPlanImageTask(loadPlanImageCallback);
        loadPlanImageTask.execute(floorPlan);
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

    @Override
    public void onStop() {
        super.onStop();
        Model.getInstance().getUpdatesManager().unregisterUpdateListener(updateListener);
    }

    private final UpdatesManager.DataUpdatedListener updateListener = requestIds -> {
        if (requestIds.contains(UpdatesManager.FLOOR_PLANS_REQUEST_ID)) {
            loadFloorPlans();
        }
    };

    private void loadFloorPlans() {
        if (loadPlansTask != null) {
            loadPlansTask.cancel(true);
        }
        loadPlansTask = new LoadPlansTask(loadPlansCallback);
        loadPlansTask.execute();
    }

    private final LoadPlansTask.LoadPlansTaskCallback loadPlansCallback = new LoadPlansTask.LoadPlansTaskCallback() {
        @Override
        public void onPlansLoaded(List<FloorPlan> floorPlans) {
            plans = floorPlans;

            if (floorPlans == null || floorPlans.isEmpty()) {
                contentView.setVisibility(View.GONE);
                placeholderView.setVisibility(View.VISIBLE);
            } else {
                contentView.setVisibility(View.VISIBLE);
                placeholderView.setVisibility(View.GONE);

                display(floorPlans);
            }
        }
    };

    private void display(List<FloorPlan> floorPlans) {
        List<String> names = new ArrayList<>(floorPlans.size());
        for (FloorPlan plan : floorPlans) {
            names.add(plan.getName());
        }

        FloorSelectorAdapter floorsAdapter = new FloorSelectorAdapter(floorSelector.getContext(), names);
        floorSelector.setAdapter(floorsAdapter);

        floorSelector.setVisibility(floorPlans.isEmpty() ? View.INVISIBLE : View.VISIBLE);
    }

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
            if (floorPlans != null && !isCancelled()) {
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
            if (planImage != null && !isCancelled()) {
                callback.onPlanImageLoaded(planImage);
            }
        }

        interface LoadPlanImageTaskCallback {

            void onPlanImageLoaded(Bitmap planImage);
        }
    }
}

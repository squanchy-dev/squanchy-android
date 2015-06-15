package com.ls.drupalconapp.ui.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.Model;
import com.ls.drupalconapp.model.UpdatesManager;
import com.ls.drupalconapp.model.data.POI;
import com.ls.drupalconapp.model.managers.PoisManager;
import com.ls.drupalconapp.ui.adapter.POIsAdapter;

import java.util.List;

public class POIsFragment extends Fragment {

    public static final String TAG = "POIsFragment";
    private POIsAdapter mPoIsAdapter;
    private ProgressBar progressBar;

    private UpdatesManager.DataUpdatedListener updateListener = new UpdatesManager.DataUpdatedListener()
    {
        @Override
        public void onDataUpdated(List<Integer> requestIds) {
            Log.d("UPDATED", "POIsFragment");
            progressBar.setVisibility(View.GONE);
            initView();
        }
    };

    public POIsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        progressBar = (ProgressBar) getActivity().getActionBar().getCustomView().findViewById(R.id.action_bar_indicator);
        return inflater.inflate(R.layout.fr_pois, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        Model.instance().getUpdatesManager().registerUpdateListener(updateListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Model.instance().getUpdatesManager().unregisterUpdateListener(updateListener);
    }

    private void initView() {
        final ListView poisList = (ListView) getView().findViewById(R.id.listPois);

        new AsyncTask<Void, Void, List<POI>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                poisList.setVisibility(View.GONE);
            }

            @Override
            protected List<POI> doInBackground(Void... params) {
                PoisManager poisManager = new PoisManager(Model.instance().getClient());
                return poisManager.getPOIs();
            }

            @Override
            protected void onPostExecute(List<POI> pois) {
                if (isDetached() || getView() == null) {
                    return;
                }

                mPoIsAdapter = new POIsAdapter(getActivity(), pois);

                poisList.setAdapter(mPoIsAdapter);
                poisList.setVisibility(View.VISIBLE);

                getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        }.execute();
    }
}

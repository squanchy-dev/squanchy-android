package com.ls.drupalconapp.ui.fragment;


import android.content.Context;
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
import com.ls.drupalconapp.app.App;
import com.ls.drupalconapp.model.DatabaseManager;
import com.ls.drupalconapp.model.data.POI;
import com.ls.drupalconapp.ui.adapter.POIsAdapter;
import com.ls.drupalconapp.ui.receiver.DataUpdateManager;

import java.util.List;

public class POIsFragment extends Fragment {

    public static final String TAG = "POIsFragment";
    private POIsAdapter mPoIsAdapter;
    private ProgressBar progressBar;

    private DataUpdateManager dataUpdateManager = new DataUpdateManager(new DataUpdateManager.DataUpdatedListener() {
        @Override
        public void onDataUpdated(int[] requestIds) {
            Log.d("UPDATED", "POIsFragment");
            progressBar.setVisibility(View.GONE);
            initView();
        }
    });

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
        dataUpdateManager.register(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataUpdateManager.unregister(getActivity());
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
                Context context = App.getContext();
                DatabaseManager databaseManager = new DatabaseManager(context);
                List<POI> pois = databaseManager.getPOIs();
                return pois;
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

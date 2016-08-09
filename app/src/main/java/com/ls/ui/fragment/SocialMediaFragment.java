package com.ls.ui.fragment;


import com.ls.drupalcon.R;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.PreferencesManager;
import com.ls.drupalcon.model.UpdatesManager;
import com.ls.utils.NetworkUtils;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.SearchTimeline;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocialMediaFragment extends Fragment
{

    public static final String TAG = "SocialMediaFragment";
    private View rootView;

    private UpdatesManager.DataUpdatedListener updateReceiver = new UpdatesManager.DataUpdatedListener() {
        @Override
        public void onDataUpdated(List<Integer> requestIds) {
            updateData(requestIds);
        }
    };

    public SocialMediaFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_social_media, container, false);
    }
    private void updateData(List<Integer> requestIds) {
        for (int id : requestIds) {
            if (UpdatesManager.SETTINGS_REQUEST_ID == id) {
                fillView();
                break;
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;

        Model.instance().getUpdatesManager().registerUpdateListener(updateReceiver);

        fillView();
    }

    private void fillView() {

        if(!NetworkUtils.isOn(getActivity()) ){
            rootView.findViewById(R.id.txtNoConnection).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.list_view).setVisibility(View.GONE);
            rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
        }

        String searchQuery = PreferencesManager.getInstance().getTwitterSearchQuery();

        final SearchTimeline userTimeline = new SearchTimeline.Builder()
                .query(searchQuery)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(rootView.getContext())
                .setTimeline(userTimeline)
                .build();

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        });

        ListView list = (ListView)rootView.findViewById(R.id.list_view);
        list.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        Model.instance().getUpdatesManager().unregisterUpdateListener(updateReceiver);
        super.onDestroyView();
    }
}

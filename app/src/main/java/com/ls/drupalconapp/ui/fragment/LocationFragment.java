package com.ls.drupalconapp.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.DatabaseManager;
import com.ls.drupalconapp.model.data.Location;
import com.ls.drupalconapp.ui.receiver.DataUpdateManager;

import java.util.List;

public class LocationFragment extends Fragment implements CustomMapFragment.OnActivityCreatedListener{
    public static final String TAG = "LocationsFragment";

    private DataUpdateManager dataUpdateManager = new DataUpdateManager(new DataUpdateManager.DataUpdatedListener() {
        @Override
        public void onDataUpdated(int[] requestIds) {
            Log.d("UPDATED", "LocationFragment");
            initView();
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_location, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onActivityCreated(GoogleMap googleMap) {
        if(googleMap != null) {
			DatabaseManager dbManager = DatabaseManager.instance();
			List<Location> locations = dbManager.getLocations();

			for(int i=0; i<locations.size(); i++){
				Location location = locations.get(i);
				LatLng position = new LatLng(location.getLat(), location.getLon());
				googleMap.addMarker(new MarkerOptions().position(position));

				if(i==0) {
					googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(position, 15, 0, 0)));
					fillTextViews(location);
				}
			}

            UiSettings uiSettings = googleMap.getUiSettings();
            uiSettings.setMapToolbarEnabled(false);
            uiSettings.setMyLocationButtonEnabled(false);
            uiSettings.setCompassEnabled(false);
            uiSettings.setRotateGesturesEnabled(false);
        }
        dataUpdateManager.register(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataUpdateManager.unregister(getActivity());
    }

    private void fillTextViews(Location location) {
        TextView txtAmsterdam = (TextView) getView().findViewById(R.id.txtPlace);
        TextView txtAddress = (TextView) getView().findViewById(R.id.txtAddress);

		String locationName = location.getName();
        txtAmsterdam.setText(locationName);

        String address = toTitleCase(location.getAddress());
        txtAddress.setText(address);

    }

    private void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (getActivity() == null){
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(getView() == null) {
                            return;
                        }

                        CustomMapFragment mapFragment = CustomMapFragment.newInstance(LocationFragment.this);
                        LocationFragment.this.getFragmentManager().beginTransaction()
                                .replace(R.id.fragmentHolder, mapFragment)
                                .commitAllowingStateLoss();

                        getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
                    }
                });
            }
        }, 600);
    }

    private String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
}

package net.squanchy.ui.fragment;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class CustomMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    public interface OnActivityCreatedListener {

        void onActivityCreated(GoogleMap googleMap);
    }

    private OnActivityCreatedListener mListener;

    public static CustomMapFragment newInstance(OnActivityCreatedListener listener) {
        CustomMapFragment mapFragment = new CustomMapFragment();
        mapFragment.setOnActivityCreatedListener(listener);

        return mapFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mListener != null) {
            mListener.onActivityCreated(googleMap);
        }
    }

    private void setOnActivityCreatedListener(OnActivityCreatedListener listener) {
        mListener = listener;
    }
}

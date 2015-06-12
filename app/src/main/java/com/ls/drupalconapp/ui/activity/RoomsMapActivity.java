package com.ls.drupalconapp.ui.activity;

import com.ls.drupalconapp.R;
import com.ls.utils.UIUtils;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class RoomsMapActivity extends StateActivity {

    private final String IMAGE_PATH = "images/";
    private final String IMAGE_NAME = "RAI_FLOORPLAN.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_rooms_map);

        initActionBar();
        loadImage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(R.string.rooms_map);
    }

    private void loadImage() {
        final ImageView imgRoomsMap = (ImageView) findViewById(R.id.imgRoomMap);

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return UIUtils.decodeSampledBitmap(getAssets(), IMAGE_PATH + IMAGE_NAME, getResources().getDisplayMetrics().widthPixels,
                            getResources().getDisplayMetrics().heightPixels);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);

                if(result == null) {
                    return;
                }

                imgRoomsMap.setImageBitmap(result);
                imgRoomsMap.setVisibility(View.VISIBLE);
            }
        }.execute();
    }
}

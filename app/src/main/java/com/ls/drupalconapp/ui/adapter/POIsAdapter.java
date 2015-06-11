package com.ls.drupalconapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.data.POI;
import com.ls.util.image.DrupalImageView;

import java.util.List;

public class POIsAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<POI> mPois;

    public POIsAdapter(Context context, List<POI> poiList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mPois = poiList;
    }

    @Override
    public int getCount() {
        return mPois.size();
    }

    @Override
    public Object getItem(int i) {
        return mPois.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mPois.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        final POI poi = mPois.get(position);

        if (convertView == null){
            row = mInflater.inflate(R.layout.item_poi, parent, false);

            holder = new ViewHolder();
            holder.imgPoi = (DrupalImageView) row.findViewById(R.id.imgPoi);
            holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);
            holder.txtDescription = (TextView) row.findViewById(R.id.txtDescription);
            holder.txtLearnMore = (TextView) row.findViewById(R.id.txtLearnMore);
            holder.txtLearnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openLink(poi.getDetailURL());
                }
            });

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.imgPoi.setImageWithURL(poi.getImageURL());
        holder.txtTitle.setText(poi.getName());
        holder.txtDescription.setText(poi.getDescription());

        return row;
    }

    private void openLink(String url){
        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }catch (Exception e1){/*empty*/}
    }

    private class ViewHolder {

        DrupalImageView imgPoi;
        TextView txtTitle;
        TextView txtDescription;
        TextView txtLearnMore;
    }
}

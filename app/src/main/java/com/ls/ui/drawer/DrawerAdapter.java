package com.ls.ui.drawer;

import com.ls.drupalcon.R;
import com.ls.drupalcon.app.App;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class DrawerAdapter extends BaseAdapter {
    private List<DrawerMenuItem> menu;
    private LayoutInflater inflater;
    private int selectedPos = 0;

    public DrawerAdapter(Context theContext, List<DrawerMenuItem> theMenu) {
        inflater = (LayoutInflater) theContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menu = theMenu;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    @Override
    public int getCount() {
        return menu.size(); //+ 1 because header was added to list
    }

    @Override
    public DrawerMenuItem getItem(int position) {
        return menu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return menu.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View result;

        DrawerMenuItem item = menu.get(position);

        if (item.isGroup()) {
            result = inflater.inflate(R.layout.item_drawer_group, null);
        } else {
            result = inflater.inflate(R.layout.item_drawer, null);
        }

        LinearLayout layoutDrawerItem = (LinearLayout) result.findViewById(R.id.layoutDrawerItem);
        TextView txtName = (TextView) result.findViewById(R.id.txtName);
        txtName.setText(item.getName());

        ImageView image = (ImageView) result.findViewById(R.id.image);

        if (position == selectedPos) {
            image.setImageResource(item.getSelIconRes());
            txtName.setTextColor(App.getContext().getResources().getColor(R.color.primary));
        } else {
            image.setImageResource(item.getIconRes());
        }

        if (position == 2 | position == 5) {
            result.findViewById(R.id.divider).setVisibility(View.VISIBLE);
        }

        layoutDrawerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDrawerItemClicked(position);
            }
        });

        return result;
    }

    @Override
    public boolean isEnabled(int position) {
        DrawerMenuItem item = menu.get(position);

        if (item.isGroup()) {
            return false;
        } else {
            return true;
        }
    }

    public OnDrawerItemClickListener mListener;

    public interface OnDrawerItemClickListener {
        public void onDrawerItemClicked(int position);
    }

    public void setDrawerItemClickListener(OnDrawerItemClickListener listener) {
        if (listener != null) {
            mListener = listener;
        }
    }
}

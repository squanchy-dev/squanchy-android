package net.squanchy.ui.drawer;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.squanchy.R;

import java.util.List;

public class DrawerAdapter extends BaseAdapter {

    private final Resources mResources;

    private List<DrawerMenuItem> menuItems;
    private LayoutInflater inflater;
    private int selectedPos = 0;

    public DrawerAdapter(Context context, List<DrawerMenuItem> menuItems) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.menuItems = menuItems;
        this.mResources = context.getResources();
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    @Override
    public int getCount() {
        return menuItems.size(); //+ 1 because header was added to list
    }

    @Override
    public DrawerMenuItem getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return menuItems.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View result;

        DrawerMenuItem item = menuItems.get(position);

        if (item.isGroup()) {
            result = inflater.inflate(R.layout.item_drawer_group, parent, false);
        } else {
            result = inflater.inflate(R.layout.item_drawer, parent, false);
        }

        LinearLayout layoutDrawerItem = (LinearLayout) result.findViewById(R.id.layoutDrawerItem);
        TextView txtName = (TextView) result.findViewById(R.id.txtName);
        txtName.setText(item.getName());

        ImageView image = (ImageView) result.findViewById(R.id.image);

        if (position == selectedPos) {
            image.setImageResource(item.getSelIconRes());
            txtName.setTextColor(mResources.getColor(R.color.item_selection));
        } else {
            image.setImageResource(item.getIconRes());
        }

        if (position == 3 | position == 7) {
            result.findViewById(R.id.divider).setVisibility(View.VISIBLE);
        }

        layoutDrawerItem.setOnClickListener(v -> mListener.onDrawerItemClicked(position));

        return result;
    }

    @Override
    public boolean isEnabled(int position) {
        DrawerMenuItem item = menuItems.get(position);
        return !item.isGroup();
    }

    public OnDrawerItemClickListener mListener;

    public interface OnDrawerItemClickListener {
        void onDrawerItemClicked(int position);
    }

    public void setDrawerItemClickListener(OnDrawerItemClickListener listener) {
        if (listener != null) {
            mListener = listener;
        }
    }
}

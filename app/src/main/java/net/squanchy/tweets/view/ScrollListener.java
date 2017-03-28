package net.squanchy.tweets.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class ScrollListener extends RecyclerView.OnScrollListener {

    private static final int THRESHOLD = 2;

    private final LinearLayoutManager layoutManager;
    protected boolean loading = false;

    protected ScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {

        if (loading) {
            return;
        }

        int totalItemCount = layoutManager.getItemCount();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

        if (lastVisibleItemPosition + THRESHOLD > totalItemCount) {
            loadMore();
        }
    }

    protected abstract void loadMore();
}

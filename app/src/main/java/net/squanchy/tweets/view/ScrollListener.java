package net.squanchy.tweets.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class ScrollListener extends RecyclerView.OnScrollListener {

    private static final int THRESHOLD = 2;
    private int previousTotalItemCount = 0;
    private boolean loading = true;

    private final LinearLayoutManager layoutManager;

    public ScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {

        int totalItemCount = layoutManager.getItemCount();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

        if (totalItemCount < previousTotalItemCount) {
            previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                loading = true;
            }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!loading && (lastVisibleItemPosition + THRESHOLD) > totalItemCount) {
            loadMore();
            loading = true;
        }
    }

    public void reset(){
        previousTotalItemCount = 0;
    }

    protected abstract void loadMore();
}

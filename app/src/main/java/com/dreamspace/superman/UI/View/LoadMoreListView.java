package com.dreamspace.superman.UI.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.dreamspace.superman.R;

public class LoadMoreListView extends ListView implements OnScrollListener {

    private static final String TAG = com.dreamspace.superman.UI.View.LoadMoreListView.class
            .getSimpleName();

    private OnScrollListener mOnScrollListener;
    private LayoutInflater mInflater;

    private View mFooterView;
    private View mLoadMoreStatusView;
    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean mIsLoadingMore = false;
    private int mCurrentScrollState;
    private boolean isIn = false;
    private boolean mIsScrollToUp = true;
    private int previousFirstItem = 0;
    private int mScreenY = 0;
    private OnScrollToTopListener scrollToTopListener;

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {

        mFooterView = (RelativeLayout) mInflater.inflate(R.layout.load_more_footer,
                this, false);
        mLoadMoreStatusView = mFooterView.findViewById(R.id.load_more_progress_bar);
        addFooterView(mFooterView);
        setLoading(false);

        super.setAdapter(adapter);
    }

    public void setIn(boolean in) {
        isIn = in;
    }

    private void init(Context context) {

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        super.setOnScrollListener(this);
    }

    public void setScrollToTopListener(OnScrollToTopListener scrollToTopListener) {
        this.scrollToTopListener = scrollToTopListener;
    }

    public void setLoadMoreStatusView(View v, int statusViewId) {
        removeFooterView(mFooterView);

        mFooterView = v;
        mLoadMoreStatusView = mFooterView.findViewById(statusViewId);
        addFooterView(mFooterView);
    }

    public void setLoadMoreStatusView(View v) {
        removeFooterView(mFooterView);

        mFooterView = v;
        mLoadMoreStatusView = mFooterView.findViewById(R.id.load_more_progress_bar);
        addFooterView(mFooterView);
    }

    /**
     * Set the listener that will receive notifications every time the list
     * scrolls.
     *
     * @param l The scroll listener.
     */
    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }

    /**
     * Register a callback to be invoked when this list reaches the end (last item
     * be visible)
     *
     * @param onLoadMoreListener The callback to run.
     */

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem == 0 && isIn && view.getChildAt(0) != null && view.getChildAt(0).getTop() >= 0 && !mIsScrollToUp) {
            Log.i("isIn", "is In");
            if (scrollToTopListener != null) {
                scrollToTopListener.onScrollToTop();
                mIsScrollToUp = true;
            }
        }

        Log.i("onScroll", "firstVisibleItem: " + firstVisibleItem + ", visibleItemCount: " + visibleItemCount + ",totalItemCount:" + totalItemCount);
        View childAt = view.getChildAt(firstVisibleItem);
//        int[] location = new int[2];
//        childAt.getLocationOnScreen(location);
        if (firstVisibleItem != previousFirstItem) {
            if (firstVisibleItem > previousFirstItem) {
                mIsScrollToUp = true;
            } else {
                mIsScrollToUp = false;
            }

        } else {
//            if (mScreenY > location[1]) {
//                mIsScrollToUp = true;
//            } else if (mScreenY < location[1]) {
//                mIsScrollToUp = false;
//            }
        }
        previousFirstItem = firstVisibleItem;
//        mScreenY = location[1];
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }

        if (visibleItemCount == totalItemCount) {
            if (mLoadMoreStatusView != null) {
                mLoadMoreStatusView.setVisibility(View.GONE);
            }
            return;
        }

        if (mOnLoadMoreListener != null) {

            boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

            if (!mIsLoadingMore && loadMore
                    && mCurrentScrollState != SCROLL_STATE_IDLE) {
                setLoading(true);
                onLoadMore();
            }
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {

        Log.i("ListviewonScroll", "scrollState: " + scrollState + ",mIsScrollToUp: " + mIsScrollToUp);
        mCurrentScrollState = scrollState;
//        if (getFirstVisiblePosition() == 0 && isIn && scrollState == SCROLL_STATE_TOUCH_SCROLL && view.getChildAt(0).getTop() >= 0 && !mIsScrollToUp) {
//            Log.i("isIn", "is In");
//            if (scrollToTopListener != null) {
//                scrollToTopListener.onScrollToTop();
//                mIsScrollToUp = true;
//            }
//        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    public void setLoading(boolean loading) {
        Log.d(TAG, "setLoading: " + loading);

        mIsLoadingMore = loading;
        mLoadMoreStatusView.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    public void onLoadMore() {
        Log.d(TAG, "onLoadMore");
        if (mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onLoadMore();
        }
    }


    public void onLoadMoreComplete() {
        setLoading(false);
    }


    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

    public interface OnScrollToTopListener {
        void onScrollToTop();
    }

}
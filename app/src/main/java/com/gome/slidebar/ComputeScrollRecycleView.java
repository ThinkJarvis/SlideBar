package com.gome.slidebar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by admin on 2018/2/28.
 */

public class ComputeScrollRecycleView extends RecyclerView {

    private OnRecycleViewScrollListener mOnRecycleViewScrollListener;
    private OnScrollLetterListener mOnScrollLetterListener;

    public ComputeScrollRecycleView(Context context) {
        this(context, null);
    }

    public ComputeScrollRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ComputeScrollRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mOnRecycleViewScrollListener = new OnRecycleViewScrollListener();
        addOnScrollListener(mOnRecycleViewScrollListener);
    }

    public void addOnScrollLetterListener(OnScrollLetterListener onScrollLetterListener) {
        mOnScrollLetterListener = onScrollLetterListener;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
//        Log.e("aaa","computeHorizontalScrollOffset() = " + computeHorizontalScrollOffset());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        addOnScrollListener(null);
    }

    private class OnRecycleViewScrollListener extends OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                /**
                 * The RecyclerView is not currently scrolling.
                 * @see #getScrollState()
                 */
                case RecyclerView.SCROLL_STATE_IDLE:
                    Log.e("xly", "SCROLL END");
                    break;

                /**
                 * The RecyclerView is currently being dragged by outside input such as user touch input.
                 * @see #getScrollState()
                 */
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    Log.e("xly", "outside input SCROLL");
                    break;


                /**
                 * The RecyclerView is currently animating to a final position while not under
                 * outside control.
                 * @see #getScrollState()
                 */
                case RecyclerView.SCROLL_STATE_SETTLING:
                    Log.e("xly", "scroll start");
                    break;

                default:
                    break;
            }
        }


        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            View childView = recyclerView.findChildViewUnder(dx, dy);
            //scroll down
            if (dy < 0 && mOnScrollLetterListener != null) {
                mOnScrollLetterListener.onLetterScrollDown();
            }
            //scroll up
            else if (dy > 0 && mOnScrollLetterListener != null) {
                mOnScrollLetterListener.onLetterScrollUp();
            }

            if (mOnScrollLetterListener != null && childView != null) {
                mOnScrollLetterListener.onCurrentLetter((String) childView.getTag());
            }
        }
    }


    public interface OnScrollLetterListener {
        void onCurrentLetter(String tag);
        void onLetterScrollUp();
        void onLetterScrollDown();
    }
}

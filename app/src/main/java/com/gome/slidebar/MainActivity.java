package com.gome.slidebar;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.gome.slidebar.LetterFlagView.VIEW_TAG_FIRST;
import static com.gome.slidebar.LetterFlagView.VIEW_TAG_NEXT;

public class MainActivity extends AppCompatActivity implements Sidebar.OnSelectIndexItemListener, ComputeScrollRecycleView.OnScrollLetterListener {

    private TreeMap<String, List<String>> mContactMap;
    private LinearLayoutManager mLinearLayoutManager;
    private ComputeScrollRecycleView mComputeScrollRecycleView;
    private RecycleAdapter mRecycleAdapter;
    private LetterFlagView mLetterFlagView;
    private Sidebar mSidebar;


    private int mLastFirstVisibleItem = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactMap = Utils.getOrderContactMap();
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.activity_main);
        mLetterFlagView = (LetterFlagView) findViewById(R.id.letter_flag_view);
        mSidebar = (Sidebar) findViewById(R.id.side_bar);
        mSidebar.setMap(mContactMap);
        mSidebar.setOnSelectIndexItemListener(this);
        mComputeScrollRecycleView = (ComputeScrollRecycleView) findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecycleAdapter = new RecycleAdapter(this, mContactMap);
        mComputeScrollRecycleView.addOnScrollLetterListener(this);
        mComputeScrollRecycleView.setLayoutManager(mLinearLayoutManager);
        mComputeScrollRecycleView.setAdapter(mRecycleAdapter);
    }

    @Override
    public void onSelectIndexItem(String index) {
        mLinearLayoutManager.scrollToPositionWithOffset(mRecycleAdapter.getPositionByKey(mContactMap, index), 0);
    }

    @Override
    public void onCurrentLetter(String tag) {
        mSidebar.updateCurrentIndexByScroll(tag);
        TextView letterFlag = mLetterFlagView.findLetterFlag(VIEW_TAG_FIRST);
        if (letterFlag != null) {
            letterFlag.setText(tag);
        }
    }

    @Override
    public void onLetterScrollUp() {
        scrollUp(mLetterFlagView);
    }

    @Override
    public void onLetterScrollDown() {
        scrollDown(mLetterFlagView);
    }

    private void scrollUp(LetterFlagView parent) {
        float translationY = 0f;
        if (mLinearLayoutManager != null) {
            int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
            int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            int nextVisibleItem = 1;
            if (nextVisibleItem > lastVisibleItem) {
                return;
            }

            View view = mLinearLayoutManager.getChildAt(nextVisibleItem);
            if (view == null) {
                return;
            }

            TextView textView = (TextView) view.findViewById(R.id.letter);
            if (view.getTop() <= 40f) {
                float percent = 1 - view.getTop() / 40f;
                Log.e("lion", "scrollUp = " + textView.getText() + " | " + view.getTop() + " | " + percent);
                View nextLetterFlagView = parent.findViewWithTag(VIEW_TAG_NEXT);
                if (nextLetterFlagView == null) {
                    nextLetterFlagView = parent.addLetterFlagItem(VIEW_TAG_NEXT);
                }

                TextView nextLetterFlagTextView = (TextView) nextLetterFlagView.findViewById(R.id.letter_flag);
                nextLetterFlagTextView.setBackgroundColor(Utils.calculateColor(Color.parseColor("#f1f0f5"), Color.parseColor("#ffffff"), percent));
                nextLetterFlagTextView.setTextColor(Utils.calculateColor(Color.parseColor("#000000"), Color.parseColor("#51b038"), percent));
                nextLetterFlagTextView.setText(textView.getText());
                translationY = view.getTop() - 40;
                parent.setTranslationY(translationY);
            }

            if (mLastFirstVisibleItem != firstVisibleItem) {
                parent.setTranslationY(-translationY);
                parent.removeView(parent.findLetterItem(VIEW_TAG_NEXT));
                mLastFirstVisibleItem = firstVisibleItem;
            }
        }
    }

    private void scrollDown(LetterFlagView parent) {
        float translationY = 0f;
        if (mLinearLayoutManager != null) {
            int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
            int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();


            View firstView = mLinearLayoutManager.getChildAt(firstVisibleItem);

            if (firstView == null) {
                return;
            }

//            View view = mComputeScrollRecycleView.findChildViewUnder(firstView.getLeft(), firstView.getBottom() - 40);
//
//            if (view != null) {
//                Log.e("xly","view.getTag() = " + view.getTag());
//            }

            if (1 > lastVisibleItem) {
                return;
            }
            View nextView = mLinearLayoutManager.getChildAt(1);

            if (nextView == null) {
                return;
            }

            TextView textView = (TextView) nextView.findViewById(R.id.letter);
            float scrollY = firstView.getHeight() +  firstView.getTop();
            if (scrollY <= 40f) {
                float percent = scrollY / 40f;
//                Log.e("lion", "scrollDown = " + textView.getText() + " | " + scrollY + " | " + percent);
                View nextLetterFlagView = parent.findViewWithTag(VIEW_TAG_NEXT);
                if (nextLetterFlagView == null) {
                    nextLetterFlagView = parent.addLetterFlagItem(VIEW_TAG_NEXT);
                    parent.setTranslationY(-40f);
                }

                TextView nextLetterFlagTextView = (TextView) nextLetterFlagView.findViewById(R.id.letter_flag);
                nextLetterFlagTextView.setBackgroundColor(Utils.calculateColor(Color.parseColor("#ffffff"), Color.parseColor("#f1f0f5") , percent));
                nextLetterFlagTextView.setTextColor(Utils.calculateColor(Color.parseColor("#51b038"), Color.parseColor("#000000"), percent));
                nextLetterFlagTextView.setText(textView.getText());
                parent.setTranslationY(-40f + scrollY);

                if (scrollY == 40) {
                    parent.removeView(parent.findLetterItem(VIEW_TAG_NEXT));
                    mLastFirstVisibleItem = firstVisibleItem;
                }

            }

//            if (mLastFirstVisibleItem != firstVisibleItem) {
//                parent.removeView(parent.findLetterItem(VIEW_TAG_NEXT));
//                mLastFirstVisibleItem = firstVisibleItem;
//            }
        }
    }

    private class RecycleAdapter extends RecyclerView.Adapter<RecycleViewHolder> {

        private TreeMap<String, List<String>> mContactMap;
        private LayoutInflater mLayoutInflater;

        public RecycleAdapter(Context context, TreeMap<String, List<String>> contactMap) {
            mLayoutInflater = LayoutInflater.from(context);
            mContactMap = contactMap;
        }


        @Override
        public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View convertView = mLayoutInflater.inflate(R.layout.item_view, parent, false);
            return new RecycleViewHolder(convertView);
        }

        @Override
        public void onBindViewHolder(RecycleViewHolder holder, int position) {
            if (mContactMap == null) {
                return;
            }
            String letter = getKey(position);
            holder.itemView.setTag(letter);
            holder.mLetter.setText(letter);
            List<String> list = mContactMap.get(letter);
            bindContainerView(list, holder.mContainer);
        }

        @Override
        public int getItemCount() {
            return mContactMap == null ? -1 : mContactMap.size();
        }

        public int getPositionByKey(Map<String, List<String>> map, String chooseKey) {
            if (map == null || chooseKey == null) {
                return -1;
            }
            int sign = 0;
            for (String key : map.keySet()) {
                if (key.equals(chooseKey)) {
                    return sign;
                }
                sign++;
            }
            return -1;
        }

        private String getKey(int index) {
            if (mContactMap == null) {
                return null;
            }
            int sign = 0;
            for (String key : mContactMap.keySet()) {
                if (sign == index) {
                    return key;
                }
                sign++;
            }
            return null;
        }

        private void bindContainerView(List<String> list, ViewGroup parent) {
            parent.removeAllViews();
            for (int i = 0; i < list.size(); i++) {
                LinearLayout linearLayout = (LinearLayout) mLayoutInflater.
                        inflate(R.layout.container_item_view, parent, false);
                TextView textView = (TextView) linearLayout.findViewById(R.id.container_item_view);
                View baseLine = linearLayout.findViewById(R.id.base_line);
                baseLine.setVisibility(i == list.size() - 1 ? View.INVISIBLE : View.VISIBLE);
                textView.setText(list.get(i));
                parent.addView(linearLayout);
            }
        }
    }


    private class RecycleViewHolder extends RecyclerView.ViewHolder {

        private TextView mLetter;
        private LinearLayout mContainer;

        public RecycleViewHolder(View itemView) {
            super(itemView);
            mLetter = (TextView) itemView.findViewById(R.id.letter);
            mContainer = (LinearLayout) itemView.findViewById(R.id.container);
        }
    }
}

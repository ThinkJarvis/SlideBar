package com.gome.slidebar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by admin on 2018/3/2.
 */

public class LetterFlagView extends LinearLayout{

    public final static String VIEW_TAG_FIRST = "firstLetterFlagView";
    public final static String VIEW_TAG_NEXT = "nextLetterFlagView";

    private LayoutInflater mLayoutInflater;

    public LetterFlagView(Context context) {
        this(context, null);
    }

    public LetterFlagView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LetterFlagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLayoutInflater = LayoutInflater.from(context);
        setOrientation(VERTICAL);
        addLetterFlagItem(VIEW_TAG_FIRST);
    }

    public LinearLayout addLetterFlagItem(String tag) {
        LinearLayout linearLayout = (LinearLayout) mLayoutInflater.inflate(R.layout.letter_flag_item,this,false);
        linearLayout.setTag(tag);
        addView(linearLayout);
        return linearLayout;
    }

    public LinearLayout findLetterItem(String tag) {
        LinearLayout linearLayout = (LinearLayout) findViewWithTag(tag);
        return linearLayout;
    }

    public TextView findLetterFlag(String tag) {
        LinearLayout linearLayout = (LinearLayout) findViewWithTag(tag);
        TextView textView = (TextView) linearLayout.findViewById(R.id.letter_flag);
        return textView;
    }
}

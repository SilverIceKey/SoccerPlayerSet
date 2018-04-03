package com.sk.zhouwj.fieldplayerview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zhouw on 2018/4/2.
 */

public class PlayerView extends LinearLayout {
    private ImageView mPlayerSite;
    private TextView mPlayerName;
    public PlayerView(Context context) {
        this(context,null);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPlayerSite = new ImageView(context);
        mPlayerSite.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mPlayerSite.setAdjustViewBounds(true);
        mPlayerName = new TextView(context);
        mPlayerName.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mPlayerName.setGravity(Gravity.CENTER);
        mPlayerName.setBackgroundResource(R.drawable.name_bg);
        mPlayerName.setTextColor(Color.WHITE);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        addView(mPlayerSite);
        addView(mPlayerName);
    }

    public void setPlayerSiteResource(int resource) {
        if (mPlayerSite!=null){
            mPlayerSite.setImageResource(resource);
        }
    }

    public void setPlayerName(String name) {
        if (mPlayerName!=null){
            mPlayerName.setText(name);
        }
    }

    public int getSiteHeight(){
        if (mPlayerSite!=null){
            return mPlayerSite.getHeight();
        }
        return 0;
    }

    public ImageView getPlayerSite() {
        return mPlayerSite;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }
}
package com.sk.zhouwj.fieldplayerview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mAddPlayer;
    private TextView mPlayerXP;
    private TextView mPlayerYP;
    private TextView mPlayerPosition;
    private RelativeLayout mField;
    private ImageView mFieldBg;
    private FrameLayout mPlayersArea;
    private PlayerView mCurrentView;
    private boolean mIsViewOnTouch = false;
    private float startX = 0, startY = 0;
    private List<PlayerView> mPlayerList;
    private float mPlayerStartX;
    private float mPlayerStartY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAddPlayer = findViewById(R.id.add_player);
        mPlayerXP = findViewById(R.id.playerxp);
        mPlayerYP = findViewById(R.id.playeryp);
        mPlayerPosition = findViewById(R.id.player_position);
        mField = findViewById(R.id.field);
        mPlayersArea = findViewById(R.id.players_area);
        mFieldBg = findViewById(R.id.field_bg);
        mAddPlayer.setOnClickListener(this);
        mPlayerList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_player:
                addPlayer();
                break;
        }
    }

    private void addPlayer() {
        final PlayerView mPlayer = new PlayerView(this);
        mPlayer.setPlayerSiteResource(R.drawable.red_zq);
        mPlayer.setPlayerName("球员" + (mPlayerList.size() + 1));
        mPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mCurrentView = mPlayer;
                        mIsViewOnTouch = true;
                        break;
                }
                return false;
            }
        });
        mPlayerList.add(mPlayer);
        mPlayersArea.addView(mPlayer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsViewOnTouch) {
            FrameLayout.LayoutParams layoutParams;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    layoutParams = (FrameLayout.LayoutParams) mCurrentView.getLayoutParams();
                    mPlayerStartX = layoutParams.leftMargin;
                    mPlayerStartY = layoutParams.topMargin;
                    startX = event.getRawX();
                    startY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    layoutParams = (FrameLayout.LayoutParams) mCurrentView.getLayoutParams();
                    layoutParams.leftMargin += (int) (event.getRawX() - startX);
                    layoutParams.topMargin += (int) (event.getRawY() - startY);
                    mCurrentView.setLayoutParams(layoutParams);
                    mPlayerXP.setText("X:" + ((float)layoutParams.leftMargin + mCurrentView.getWidth()) / mFieldBg.getWidth() * 100 + "%");
                    mPlayerYP.setText("Y:" + ((float)layoutParams.topMargin / mFieldBg.getHeight() * 100) + "%");
                    mPlayerPosition.setText("位置:" + getPosition((int) ((float)layoutParams.topMargin / mFieldBg.getHeight() * 100)));
                    startX = event.getRawX();
                    startY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    layoutParams = (FrameLayout.LayoutParams) mCurrentView.getLayoutParams();
                    layoutParams.topMargin =getYPosition((int) (mCurrentView.getY() / mFieldBg.getHeight() * 100));
                    mCurrentView.setLayoutParams(layoutParams);
                    positionSet();
                    mPlayerXP.setText("X:" + ((float)layoutParams.leftMargin + mCurrentView.getWidth()) / mFieldBg.getWidth() * 100 + "%");
                    mPlayerYP.setText("Y:" + ((float)layoutParams.topMargin / mFieldBg.getHeight() * 100) + "%");
                    mPlayerPosition.setText("位置:" + getPosition((int) ((float)layoutParams.topMargin / mFieldBg.getHeight() * 100)));
                    mCurrentView = null;
                    mIsViewOnTouch = false;
                    startX = 0;
                    startY = 0;
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void positionSet(){
        FrameLayout.LayoutParams layoutParams;
        layoutParams = (FrameLayout.LayoutParams) mCurrentView.getLayoutParams();
        List<PlayerView> mSamePlayers = new ArrayList<>();
        for (int i = 0; i < mPlayerList.size(); i++) {
            FrameLayout.LayoutParams layoutParamsTemp = (FrameLayout.LayoutParams) mPlayerList.get(i).getLayoutParams();
            if (layoutParamsTemp.topMargin==layoutParams.topMargin){
                mSamePlayers.add(mPlayerList.get(i));
            }
        }
        if (mSamePlayers.size()>5){
            layoutParams = (FrameLayout.LayoutParams) mCurrentView.getLayoutParams();
            layoutParams.topMargin = (int) mPlayerStartY;
            layoutParams.leftMargin = (int) mPlayerStartX;
            mCurrentView.setLayoutParams(layoutParams);
            Toast.makeText(this,"每行最多5人，守门员1人",Toast.LENGTH_SHORT).show();
            return;
        }
        Collections.sort(mSamePlayers, new Comparator<PlayerView>() {
            @Override
            public int compare(PlayerView o1, PlayerView o2) {
                FrameLayout.LayoutParams layoutParamsTemp1 = (FrameLayout.LayoutParams) o1.getLayoutParams();
                FrameLayout.LayoutParams layoutParamsTemp2 = (FrameLayout.LayoutParams) o2.getLayoutParams();
                if (layoutParamsTemp1.leftMargin<layoutParamsTemp2.leftMargin){
                    return -1;
                }else if (layoutParamsTemp1.leftMargin>layoutParamsTemp2.leftMargin){
                    return 1;
                }
                return 0;
            }
        });
        for (int i = 0; i < mSamePlayers.size()-1; i++) {
            FrameLayout.LayoutParams layoutParamsTemp1 = (FrameLayout.LayoutParams) mSamePlayers.get(i).getLayoutParams();
            FrameLayout.LayoutParams layoutParamsTemp2 = (FrameLayout.LayoutParams) mSamePlayers.get(i+1).getLayoutParams();
            if (layoutParamsTemp1.leftMargin<layoutParamsTemp2.leftMargin&&layoutParamsTemp1.leftMargin+layoutParams.width>layoutParamsTemp2.leftMargin){
                layoutParamsTemp2.leftMargin = (int) (layoutParamsTemp1.leftMargin+layoutParamsTemp1.width+mFieldBg.getWidth()/15f);
            }
            mSamePlayers.get(i+1).setLayoutParams(layoutParamsTemp2);
        }
    }

    private int getYPosition(int p) {
        if (p < 28 - mCurrentView.getSiteHeight() / 2f / mFieldBg.getHeight() * 100) {
            return (int) (mFieldBg.getHeight() * 0.06);
        } else if (28 - mCurrentView.getSiteHeight() / 2f / mFieldBg.getHeight() * 100 <= p && p < 52 - mCurrentView.getSiteHeight() / 2f / mFieldBg.getHeight() * 100) {
            return (int) (mFieldBg.getHeight() * 0.30);
        } else if (52 - mCurrentView.getSiteHeight() / 2f / mFieldBg.getHeight() * 100 <= p && p < 76 - mCurrentView.getSiteHeight() / 2f / mFieldBg.getHeight() * 100) {
            return (int) (mFieldBg.getHeight() * 0.54);
        } else if (76 - mCurrentView.getSiteHeight() / 2f / mFieldBg.getHeight() * 100 <= p) {
            return (int) (mFieldBg.getHeight() * 0.78);
        }
        return 0;
    }

    private String getPosition(int p) {
        if (0 < p && p < 28) {
            return "前锋";
        } else if (28 <= p && p < 52) {
            return "中锋";
        } else if (52 <= p && p < 76) {
            return "后卫";
        } else if (76 <= p) {
            return "守门员";
        }
        return "未知";
    }
}

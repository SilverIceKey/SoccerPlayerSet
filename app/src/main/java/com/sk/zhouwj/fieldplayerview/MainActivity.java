package com.sk.zhouwj.fieldplayerview;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mAddPlayer;
    private TextView mPlayerXP;
    private TextView mPlayerYP;
    private TextView mPlayerPosition;
    private FrameLayout mField;
    private ImageView mFieldBg;
    private PlayerView mCurrentView;
    private boolean mIsViewOnTouch = false;
    private float startX = 0,startY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAddPlayer = findViewById(R.id.add_player);
        mPlayerXP = findViewById(R.id.playerxp);
        mPlayerYP = findViewById(R.id.playeryp);
        mPlayerPosition = findViewById(R.id.player_position);
        mField = findViewById(R.id.field);
        mFieldBg = findViewById(R.id.field_bg);
        mAddPlayer.setOnClickListener(this);
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
        mPlayer.setPlayerName("球员1");
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
        mField.addView(mPlayer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsViewOnTouch){
            FrameLayout.LayoutParams layoutParams;
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    layoutParams = (FrameLayout.LayoutParams) mCurrentView.getLayoutParams();
                    layoutParams.leftMargin += (int) (event.getRawX() - startX);
                    layoutParams.topMargin += (int) (event.getRawY() - startY);
                    mCurrentView.setLayoutParams(layoutParams);
                    mPlayerXP.setText("X:"+(mCurrentView.getX()+mCurrentView.getWidth())/mFieldBg.getWidth()*100+"%");
                    mPlayerYP.setText("Y:"+(mCurrentView.getY()+mCurrentView.getHeight())/mFieldBg.getHeight()*100+"%");
                    mPlayerPosition.setText("位置:"+getPosition((int) (mCurrentView.getY()/mFieldBg.getHeight()*100)));
                    startX = event.getRawX();
                    startY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    layoutParams = (FrameLayout.LayoutParams) mCurrentView.getLayoutParams();
                    layoutParams.topMargin = getYPosition((int) (mCurrentView.getY()/mFieldBg.getHeight()*100));
                    mCurrentView.setLayoutParams(layoutParams);
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

    private int getYPosition(int p) {
        if (0<p&&p<28-mCurrentView.getSiteHeight()/2f/mFieldBg.getHeight()*100){
            return (int) (mFieldBg.getHeight()*0.06);
        }else if (28-mCurrentView.getSiteHeight()/2f/mFieldBg.getHeight()*100<=p&&p<52-mCurrentView.getSiteHeight()/2f/mFieldBg.getHeight()*100){
            return (int) (mFieldBg.getHeight()*0.30);
        }else if (52-mCurrentView.getSiteHeight()/2f/mFieldBg.getHeight()*100<=p&&p<76-mCurrentView.getSiteHeight()/2f/mFieldBg.getHeight()*100){
            return (int) (mFieldBg.getHeight()*0.54);
        }else if (76-mCurrentView.getSiteHeight()/2f/mFieldBg.getHeight()*100<=p){
            return (int) (mFieldBg.getHeight()*0.78);
        }
        return 0;
    }

    private String getPosition(int p) {
        if (0<p&&p<25){
            return "前锋";
        }else if (25<=p&&p<50){
            return "中锋";
        }else if (50<=p&&p<75){
            return "后卫";
        }else if (75<=p&&p<100){
            return "守门员";
        }
        return "未知";
    }
}

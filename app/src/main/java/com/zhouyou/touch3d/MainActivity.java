package com.zhouyou.touch3d;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.zhouyou.touch3d.utils.Scale;

public class MainActivity extends AppCompatActivity {

    private TouchPanelView touchPanelView;
    private View viewPanelBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();

        viewPanelBackground = findViewById(R.id.view_panel_background);
        touchPanelView = findViewById(R.id.touch_panel_view);

        WindowManager mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = mWindowManager.getDefaultDisplay();
        int displayHeight = display.getHeight();


        int touchPanelHeight = displayHeight - Scale.dip2px(this, 110);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) touchPanelView.getLayoutParams();
        params.height = touchPanelHeight;
        touchPanelView.setLayoutParams(params);
        touchPanelView.setTranslationY(touchPanelHeight);
        touchPanelView.setOnPanelViewDisplayListener(new TouchPanelView.OnPanelViewDisplayListener() {
            @Override
            public void onPanelShow() {
                ObjectAnimator animator = ObjectAnimator.ofFloat(viewPanelBackground, "alpha", 0f, 0.7f);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        viewPanelBackground.setVisibility(View.VISIBLE);
                    }
                });
                animator.setDuration(150);
                animator.start();
            }

            @Override
            public void onPanelDismiss() {
                ObjectAnimator animator = ObjectAnimator.ofFloat(viewPanelBackground, "alpha", 0.7f, 0f);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewPanelBackground.setVisibility(View.GONE);
                    }
                });
                animator.setDuration(150);
                animator.start();
            }
        });
        viewPanelBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (touchPanelView.isShowing()) {
                    touchPanelView.dismissPanel();
                }
            }
        });
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.fl_container, new MyFragment());
        ft.commitAllowingStateLoss();
    }

    public void showTouchPanelView(String item) {
        touchPanelView.showPanel();
    }

    public void dismissTouchPanelView() {
        touchPanelView.dismissPanel();
    }

    public void transferTouchEvent(MotionEvent event) {
        touchPanelView.transferTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (touchPanelView != null && touchPanelView.isShowing()) {
                touchPanelView.dismissPanel();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

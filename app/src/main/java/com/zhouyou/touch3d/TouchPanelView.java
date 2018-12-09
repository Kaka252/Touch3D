package com.zhouyou.touch3d;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TouchPanelView extends FrameLayout {
    private static final String TAG = "TouchPanelView";
    private Context context;

    public TouchPanelView(@NonNull Context context) {
        this(context, null);
    }

    public TouchPanelView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchPanelView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private OnPanelViewDisplayListener onPanelViewDisplayListener;

    public void setOnPanelViewDisplayListener(OnPanelViewDisplayListener onPanelViewDisplayListener) {
        this.onPanelViewDisplayListener = onPanelViewDisplayListener;
    }

    private TextView tvChat, tvViewDetail;
    private int viewHeight;

    private boolean isShowing;

    public boolean isShowing() {
        return isShowing;
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_touch_panel, this);
        RecyclerView rvList = view.findViewById(R.id.rv_list);
        rvList.setAdapter(new MyTouchPanelAdapter(context));

        tvChat = view.findViewById(R.id.tv_chat);
        tvViewDetail = view.findViewById(R.id.tv_view_detail);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = getMeasuredHeight();
    }

    private ValueAnimator valueAnimator;

    public void showPanel() {
        if (valueAnimator != null && valueAnimator.isStarted()) return;
        valueAnimator = ValueAnimator.ofInt(viewHeight, 0);
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                isShowing = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onPanelViewDisplayListener != null) {
                    onPanelViewDisplayListener.onPanelShow();
                }
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                setTranslationY(value);
            }
        });
        valueAnimator.setDuration(250);
        valueAnimator.setStartDelay(16);
        valueAnimator.start();
    }

    public void dismissPanel() {
        if (valueAnimator != null && valueAnimator.isStarted()) return;
        valueAnimator = ValueAnimator.ofInt(0, viewHeight);
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                isShowing = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onPanelViewDisplayListener != null) {
                    onPanelViewDisplayListener.onPanelDismiss();
                }
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                setTranslationY(value);
            }
        });
        valueAnimator.setDuration(250);
        valueAnimator.setStartDelay(16);
        valueAnimator.start();
    }

    public void transferTouchEvent(MotionEvent event) {
        int action = event.getAction();
        boolean isInChatView = isTouchPointInView(tvChat, event.getRawX(), event.getRawY());
        boolean isInDetailView = isTouchPointInView(tvViewDetail, event.getRawX(), event.getRawY());
        if (isInChatView) {
            Log.d(TAG, "打招呼");
        } else if (isInDetailView) {
            Log.d(TAG, "查看");
        }
//        if (action == MotionEvent.ACTION_DOWN) {
//            if (!tvChat.isPressed()) {
//                tvChat.setPressed(true);
//                Toast.makeText(context, "打招呼", Toast.LENGTH_SHORT).show();
//            }
//        } else if (action == MotionEvent.ACTION_MOVE) {
//
//        } else if (action == MotionEvent.ACTION_UP) {
//
//        }
    }

    private boolean isTouchPointInView(View view, float x, float y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "ACTION_DOWN");
        } else if (action == MotionEvent.ACTION_MOVE) {
            Log.d(TAG, "ACTION_MOVE");
        } else if (action == MotionEvent.ACTION_UP) {
            Log.d(TAG, "ACTION_UP");
        }
        return super.onTouchEvent(event);
    }

    public interface OnPanelViewDisplayListener {
        void onPanelShow();

        void onPanelDismiss();
    }
}

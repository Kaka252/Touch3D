package com.zhouyou.touch3d.listener;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class CardTouchUtils {

    private Activity activity;
    private long longPressTimeOut;
    private long actionDownTime;
    private int touchSlop;


    private OnCardInteractListener listener;
    private String item;

    public CardTouchUtils(Activity activity, OnCardInteractListener listener) {
        this.activity = activity;
        this.listener = listener;
        longPressTimeOut = ViewConfiguration.getLongPressTimeout();
        touchSlop = ViewConfiguration.get(activity).getScaledTouchSlop();
    }

    private float startX, startY;

    public boolean dispatchCardTouchEvent(String item, View view, MotionEvent event) {
        this.item = item;
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            startX = event.getX();
            startY = event.getY();
            actionDownTime = event.getDownTime();
            view.getParent().requestDisallowInterceptTouchEvent(true);
            handler.sendEmptyMessageDelayed(EXPAND, longPressTimeOut);
//            handler.sendEmptyMessageDelayed(EXPAND, longPressTimeOut);
        } else if (action == MotionEvent.ACTION_POINTER_DOWN) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            int pointerCount = event.getPointerCount();

            float delX = 0, delY = 0;
            for (int i = 0; i < pointerCount; i++) {
                delX += event.getX(i);
                delY += event.getY(i);
            }
            startX = delX * 1.0f / pointerCount;
            startY = delY * 1.0f / pointerCount;
            handler.sendEmptyMessageDelayed(EXPAND, longPressTimeOut);
        } else if (action == MotionEvent.ACTION_MOVE) {
            long interactTime = System.currentTimeMillis() - actionDownTime;
            if (interactTime < longPressTimeOut) {
                handler.removeMessages(EXPAND);
            } else {
                listener.transferTouchEvent(event);
//                if (bsb.state == BottomSheetBehavior.STATE_EXPANDED) {
//                    dispatchMoveEventToCard(event)
//                }
            }
        } else if (action == MotionEvent.ACTION_UP) {
            view.getParent().requestDisallowInterceptTouchEvent(false);
            float lastX = event.getX();
            float lastY = event.getY();

            long interactTime = System.currentTimeMillis() - actionDownTime;
            if (Math.abs(lastX - startX) > touchSlop || Math.abs(lastY - startY) > touchSlop || interactTime < longPressTimeOut) {
                handler.removeMessages(EXPAND);
            }

//            if (interactTime >= longPressTimeOut) {
//                // 收起
//                handler.sendEmptyMessage(COLLAPSE);
//            } else {
//                if (interactTime < ViewConfiguration.getTapTimeout()) {
//                    view.performClick();
//                }
////                if (interactTime < longPressTimeOut) {
////                    view.removeCallbacks(expandRunnable);
////                }
//            }
        } else if (action == MotionEvent.ACTION_POINTER_UP) {
            view.getParent().requestDisallowInterceptTouchEvent(false);
            int pointerCount = event.getPointerCount();

            float lastX = 0, lastY = 0;
            for (int i = 0; i < pointerCount; i++) {
                lastX += event.getX(i);
                lastY += event.getY(i);
            }
            lastX = lastX * 1.0f / pointerCount;
            lastY = lastY * 1.0f / pointerCount;

            long interactTime = System.currentTimeMillis() - actionDownTime;
            if (Math.abs(lastX - startX) > touchSlop || Math.abs(lastY - startY) > touchSlop || interactTime < longPressTimeOut) {
                handler.removeMessages(EXPAND);
            }

        }else if (action == MotionEvent.ACTION_CANCEL) {
            view.getParent().requestDisallowInterceptTouchEvent(false);
            if (System.currentTimeMillis() - actionDownTime < longPressTimeOut) {
                handler.removeMessages(EXPAND);
            }
        }
        return true;
    }

//    private boolean isLongPressed(float lastX, float lastY, float thisX,
//                                  float thisY, long lastDownTime, long thisEventTime,
//                                  long longPressTime) {
//        float offsetX = Math.abs(thisX - lastX);
//        float offsetY = Math.abs(thisY - lastY);
//        long intervalTime = thisEventTime - lastDownTime;
//        if (offsetX <= touchSlop && offsetY <= touchSlop && intervalTime >= longPressTime) {
//            return true;
//        }
//        return false;
//    }

    private static final int EXPAND = 1;
    private static final int COLLAPSE = 2;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == EXPAND) {
                listener.onPreviewShow(item);
            } else if (msg.what == COLLAPSE) {
                listener.onPreviewDismiss();
            }
            return true;
        }
    });

    public interface OnCardInteractListener {
        void onPreviewShow(String item);

        void transferTouchEvent(MotionEvent event);

        void onPreviewDismiss();
    }
}

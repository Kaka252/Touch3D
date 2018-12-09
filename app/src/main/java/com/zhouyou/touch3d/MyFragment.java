package com.zhouyou.touch3d;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhouyou.touch3d.listener.CardTouchUtils;

public class MyFragment extends Fragment implements MyAdapter.OnItemClickListener {

    private Activity activity;

    private CardTouchUtils cardTouchUtils;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvList = view.findViewById(R.id.rv_list);
        rvList.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL));
        MyAdapter adapter = new MyAdapter(activity, this);
        rvList.setAdapter(adapter);

        cardTouchUtils = new CardTouchUtils(activity, new CardTouchUtils.OnCardInteractListener() {
            @Override
            public void onPreviewShow(String item) {
                ((MainActivity) activity).showTouchPanelView(item);
            }

            @Override
            public void transferTouchEvent(MotionEvent event) {
                ((MainActivity) activity).transferTouchEvent(event);
            }

            @Override
            public void onPreviewDismiss() {
                ((MainActivity) activity).dismissTouchPanelView();
            }
        });
    }

    @Override
    public void onItemClick(String item) {
        Toast.makeText(activity, item, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onItemLongClick(String item) {
//        ((MainActivity) activity).showTouchPanelView(item);
//    }

    @Override
    public boolean onItemTouch(String item, View view, MotionEvent event) {
        return cardTouchUtils.dispatchCardTouchEvent(item, view, event);
    }
}

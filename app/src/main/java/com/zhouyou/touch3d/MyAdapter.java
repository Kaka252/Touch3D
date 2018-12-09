package com.zhouyou.touch3d;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyItemViewHolder> {

    private Activity activity;
    private List<String> data = new ArrayList<>();
    private OnItemClickListener listener;
    private GestureDetector detector;

    public MyAdapter(Activity activity, OnItemClickListener listener) {
        this.activity = activity;
        this.listener = listener;
        this.detector = new GestureDetector(activity, new GestureDetector.SimpleOnGestureListener());
        data.clear();
        for (int i = 1; i <= 100; i++) {
            data.add("item_" + i);
        }
    }

    @NonNull
    @Override
    public MyItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_view, viewGroup, false);
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemViewHolder holder, int position) {
        final String item = getItem(position);
        holder.tvItemText.setText(item);
        holder.tvItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            }
        });
//        holder.tvItemText.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                if (listener != null) {
//                    listener.onItemLongClick(item);
//                }
//                return false;
//            }
//        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return listener != null && listener.onItemTouch(item, view, motionEvent);
            }
        });
    }

    private String getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemText;

        MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemText = itemView.findViewById(R.id.tv_item_text);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String item);

        boolean onItemTouch(String item, View view, MotionEvent motionEvent);
    }
}

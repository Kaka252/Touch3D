package com.zhouyou.touch3d;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyTouchPanelAdapter extends RecyclerView.Adapter<MyTouchPanelAdapter.MyTouchPanelItemViewHolder> {

    private Context context;
    private List<String> data = new ArrayList<>();

    public MyTouchPanelAdapter(Context context) {
        this.context = context;
        data.clear();
        for (int i = 1; i <= 100; i++) {
            data.add("item_"+ i);
        }
    }

    @NonNull
    @Override
    public MyTouchPanelItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_touch_panel_list, viewGroup, false);
        return new MyTouchPanelItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTouchPanelItemViewHolder holder, int position) {
        String item = getItem(position);
        holder.tvText.setText(item);
    }

    private String getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyTouchPanelItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvText;
        MyTouchPanelItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tv_text);
        }
    }
}

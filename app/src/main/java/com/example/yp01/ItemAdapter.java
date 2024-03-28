package com.example.yp01;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> itemList;
    private Context context;
    private List<Item> originalItemList;


    public ItemAdapter(Context context, List<Item> itemList) {
        try {
            this.context = context;
            this.itemList = itemList;
            this.originalItemList = new ArrayList<>(itemList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
            return new ViewHolder(view);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Item item = itemList.get(position);
            holder.titleTextView.setText(item.getTitle());
            holder.priceTextView.setText(item.getPrice());
            holder.imageView.setImageResource(item.getImageResource());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            return itemList.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Item getItem(int position) {
        try {
            if (position >= 0 && position < itemList.size()) {
                return itemList.get(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView priceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                imageView = itemView.findViewById(R.id.imageView);
                titleTextView = itemView.findViewById(R.id.titleTextView);
                priceTextView = itemView.findViewById(R.id.priceTextView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void filter(String text) {
        try {
            itemList.clear();
            if (TextUtils.isEmpty(text)) {
                itemList.addAll(originalItemList);
            } else {
                for (Item item : originalItemList) {
                    if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                        itemList.add(item);
                    }
                }
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
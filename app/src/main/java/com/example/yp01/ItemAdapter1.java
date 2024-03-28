package com.example.yp01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter1 extends RecyclerView.Adapter<ItemAdapter1.ItemViewHolder> {
    private Context context;
    private static List<Item> itemList;
    private OnItemClickListener mListener;
    private Item selectedItem = null;

    public ItemAdapter1(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout1, parent, false);
        return new ItemViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item currentItem = itemList.get(position);
       holder.imageView.setImageResource(currentItem.getImageResource());
        holder.nameTextView.setText(currentItem.getTitle());
        holder.priceTextView.setText(currentItem.getPrice());

        holder.itemView.setOnClickListener(v -> {
            selectedItem = currentItem;
            notifyDataSetChanged();
        });

        if (currentItem.equals(selectedItem)) {

            holder.itemView.setBackgroundResource(R.color.white);
        } else {

            holder.itemView.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setItems(List<Item> newItems) {
        this.itemList = newItems;
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;

        ItemViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imge);
            nameTextView = itemView.findViewById(R.id.Name);
            priceTextView = itemView.findViewById(R.id.price);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(itemList.get(getAdapterPosition()));
                }
            });
        }
    }
}
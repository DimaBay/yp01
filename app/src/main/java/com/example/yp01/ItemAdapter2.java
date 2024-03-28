package com.example.yp01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class ItemAdapter2 extends RecyclerView.Adapter<ItemAdapter2.ItemViewHolder> {
    private List<Item> itemList;
    private Context context;

    public ItemAdapter2(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.oneitemscreen, parent, false);
            return new ItemViewHolder(view);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        try {
            Item currentItem = itemList.get(position);
            holder.imageView.setImageResource(currentItem.getImageResource());
            holder.nameTextView.setText(currentItem.getTitle());
            holder.priceTextView.setText(currentItem.getPrice());

            holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Ваша логика обработки нажатия кнопки "Добавить в корзину"
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameTextView;
        public TextView priceTextView;
        public Button addToCartButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            nameTextView = itemView.findViewById(R.id.Name);
            priceTextView = itemView.findViewById(R.id.Price);
            addToCartButton = itemView.findViewById(R.id.addtocart);
        }
    }
}
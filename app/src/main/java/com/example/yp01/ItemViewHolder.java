package com.example.yp01;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView nameTextView;
    TextView priceTextView;

    LinearLayout addToCartLayout;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imge);
        nameTextView = itemView.findViewById(R.id.Name);
        priceTextView = itemView.findViewById(R.id.price);


    }
}
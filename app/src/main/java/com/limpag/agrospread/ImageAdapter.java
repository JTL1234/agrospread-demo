package com.limpag.agrospread;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final int[] images;
    private final String[] bookTitles;
    private final String[] bookPrices;
    private final Context context;

    private final String CLICK_COUNT_PREF = "click_count_pref";

    public ImageAdapter(Context context, int[] images, String[] bookTitles, String[] bookPrices) {
        this.context = context;
        this.images = images;
        this.bookTitles = bookTitles;
        this.bookPrices = bookPrices;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.imageView.setImageResource(images[position]);
        holder.textViewTitle.setText(bookTitles[position]);  // Set the title for each image
        holder.textViewPrice.setText(bookPrices[position]);  // Set the price for each image

        holder.imageView.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences(CLICK_COUNT_PREF, Context.MODE_PRIVATE);
            int currentCount = sharedPreferences.getInt("cover_" + position, 0); // Get current count
            currentCount++; // Increment click count

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("cover_" + position, currentCount);
            editor.apply();

            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("imageResId", images[position]);
            intent.putExtra("clickCount", currentCount);
            intent.putExtra("bookTitle", bookTitles[position]);  // Pass the book title
            intent.putExtra("bookPrice", bookPrices[position]);  // Pass the price
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle;
        TextView textViewPrice;  // Add a TextView for the price

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);  // Initialize the title TextView
            textViewPrice = itemView.findViewById(R.id.textViewPrice);  // Initialize the price TextView
        }
    }
}
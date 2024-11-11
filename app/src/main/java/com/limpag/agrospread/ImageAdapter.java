package com.limpag.agrospread;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    private List<Integer> imagesList;
    private List<String> titlesList;
    private List<String> pricesList;

    public ImageAdapter(Context context, List<Integer> imagesList, List<String> titlesList, List<String> pricesList) {
        this.context = context;
        this.imagesList = imagesList;
        this.titlesList = titlesList;
        this.pricesList = pricesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productTitle.setText(titlesList.get(position));
        holder.productPrice.setText(pricesList.get(position));
        holder.productImage.setImageResource(imagesList.get(position));
    }

    @Override
    public int getItemCount() {
        return titlesList.size();
    }

    public void updateList(List<Integer> newImages, List<String> newTitles, List<String> newPrices) {
        this.imagesList = newImages;
        this.titlesList = newTitles;
        this.pricesList = newPrices;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productTitle, productPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.product_price);
        }
    }
}

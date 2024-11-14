package com.limpag.agrospread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class Tab1Fragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private ImageAdapter adapter;
    private ImageAdapter adapter2;
    private ViewPager2 viewPager;

    private List<Integer> imagesList = new ArrayList<>();
    private List<String> titlesList = new ArrayList<>();
    private List<String> pricesList = new ArrayList<>();

    private List<Integer> imagesList2 = new ArrayList<>();
    private List<String> titlesList2 = new ArrayList<>();
    private List<String> pricesList2 = new ArrayList<>();

    // Default products data
    private int[] images = {R.drawable.rake, R.drawable.rake, R.drawable.rake};
    private String[] bookTitles = {"Product 1", "Product 2", "Product 3"};
    private String[] bookPrices = {"$10", "$20", "$30"};

    private int[] images2 = {R.drawable.rake, R.drawable.rake, R.drawable.rake};
    private String[] bookTitles2 = {"Featured 1", "Featured 2", "Featured 3"};
    private String[] bookPrices2 = {"$40", "$50", "$60"};

    private List<Integer> sliderImages = new ArrayList<>();

    // Filtered lists for search functionality
    private List<Integer> filteredImages = new ArrayList<>();
    private List<String> filteredTitles = new ArrayList<>();
    private List<String> filteredPrices = new ArrayList<>();

    private List<Integer> filteredImages2 = new ArrayList<>();
    private List<String> filteredTitles2 = new ArrayList<>();
    private List<String> filteredPrices2 = new ArrayList<>();

    private Handler sliderHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        // Setup Image Slider (ViewPager2)
        viewPager = view.findViewById(R.id.viewPager);
        ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(getContext(), sliderImages);
        viewPager.setAdapter(sliderAdapter);

        // Setup RecyclerView for Best Deals
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        loadDefaultBestDeals();
        loadProductsFromDatabase();
        adapter = new ImageAdapter(getActivity(), imagesList, titlesList, pricesList);
        recyclerView.setAdapter(adapter);

        // Setup RecyclerView for Featured Deals
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        loadFeaturedDeals();
        adapter2 = new ImageAdapter(getActivity(), imagesList2, titlesList2, pricesList2);
        recyclerView2.setAdapter(adapter2);

        // Setup Search functionality
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterResults(newText);
                return false;
            }
        });

        // Start automatic sliding of images
        startAutoSlide();

        return view;
    }

    private void loadDefaultBestDeals() {
        // Add default items to Best Deals section
        for (int image : images) {
            imagesList.add(image);
        }

        for (String title : bookTitles) {
            titlesList.add(title);
        }

        for (String price : bookPrices) {
            pricesList.add(price);
        }
    }

    private void loadProductsFromDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        List<Product> products = dbHelper.getAllProducts();

        // Add newly fetched products from the database to the Best Deals section
        for (Product product : products) {
            titlesList.add(product.getName());
            pricesList.add(product.getPrice());
            imagesList.add(R.drawable.rake); // Add placeholder for product image (can replace with actual image)
        }
    }

    private void loadFeaturedDeals() {
        // Load default items for Featured Deals section
        for (int image : images2) {
            imagesList2.add(image);
        }

        for (String title : bookTitles2) {
            titlesList2.add(title);
        }

        for (String price : bookPrices2) {
            pricesList2.add(price);
        }
    }

    private void filterResults(String query) {
        filteredImages.clear();
        filteredTitles.clear();
        filteredPrices.clear();

        filteredImages2.clear();
        filteredTitles2.clear();
        filteredPrices2.clear();

        if (TextUtils.isEmpty(query)) {
            adapter.updateList(imagesList, titlesList, pricesList);
            adapter2.updateList(imagesList2, titlesList2, pricesList2);
        } else {
            for (int i = 0; i < bookTitles.length; i++) {
                if (bookTitles[i].toLowerCase().contains(query.toLowerCase())) {
                    filteredImages.add(images[i]);
                    filteredTitles.add(bookTitles[i]);
                    filteredPrices.add(bookPrices[i]);
                }
            }

            for (int i = 0; i < bookTitles2.length; i++) {
                if (bookTitles2[i].toLowerCase().contains(query.toLowerCase())) {
                    filteredImages2.add(images2[i]);
                    filteredTitles2.add(bookTitles2[i]);
                    filteredPrices2.add(bookPrices2[i]);
                }
            }

            adapter.updateList(filteredImages, filteredTitles, filteredPrices);
            adapter2.updateList(filteredImages2, filteredTitles2, filteredPrices2);
        }
    }

    private void startAutoSlide() {
        sliderHandler.postDelayed(slideRunnable, 3000);  // Slide every 3 seconds
    }

    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            int nextSlide = viewPager.getCurrentItem() + 1;
            if (nextSlide >= sliderImages.size()) {
                nextSlide = 0;
            }
            viewPager.setCurrentItem(nextSlide, true);
            sliderHandler.postDelayed(this, 3000);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sliderHandler.removeCallbacks(slideRunnable);  // Stop slider when fragment is destroyed
    }
}

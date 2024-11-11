package com.limpag.agrospread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

    private final int[] sliderImages = {
            R.drawable.slideimage1,
            R.drawable.slideimage2,
            R.drawable.slideimage3
    };

    private final int[] images = {
            R.drawable.rake,
            R.drawable.rake,
            R.drawable.rake,
            R.drawable.rake,
            R.drawable.rake,
            R.drawable.rake,
            R.drawable.rake,
            R.drawable.rake
    };
    private final int[] images2 = {
            R.drawable.rake,
            R.drawable.rake,
            R.drawable.rake,
            R.drawable.rake,
            R.drawable.rake,
            R.drawable.rake,
            R.drawable.rake,
            R.drawable.rake
    };

    private final String[] bookTitles = {
            "rakebeauty",
            "rakecool",
            "rakefresh",
            "rakeluxe",
            "rakeswift",
            "rakelight",
            "rakemax",
            "rakeclassic"
    };
    private final String[] bookTitles2 = {
            "rakebeauty",
            "rakecool",
            "rakefresh",
            "rakeluxe",
            "rakeswift",
            "rakelight",
            "rakemax",
            "rakeclassic"
    };

    private final String[] bookPrices = {
            "$900",
            "$399",
            "$850",
            "$1200",
            "$750",
            "$650",
            "$500",
            "$300"
    };
    private final String[] bookPrices2 = {
            "$900",
            "$399",
            "$850",
            "$1200",
            "$750",
            "$650",
            "$500",
            "$300"
    };

    private ViewPager2 viewPager;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());

    private RecyclerView recyclerView, recyclerView2;
    private ImageAdapter adapter, adapter2;

    // Declare the lists at class level
    private List<Integer> imagesList = new ArrayList<>();
    private List<String> titlesList = new ArrayList<>();
    private List<String> pricesList = new ArrayList<>();

    private List<Integer> imagesList2 = new ArrayList<>();
    private List<String> titlesList2 = new ArrayList<>();
    private List<String> pricesList2 = new ArrayList<>();

    private List<Integer> filteredImages = new ArrayList<>();
    private List<String> filteredTitles = new ArrayList<>();
    private List<String> filteredPrices = new ArrayList<>();

    private List<Integer> filteredImages2 = new ArrayList<>();
    private List<String> filteredTitles2 = new ArrayList<>();
    private List<String> filteredPrices2 = new ArrayList<>();

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

        // Convert arrays to lists for the first RecyclerView
        for (int image : images) {
            imagesList.add(image);
        }

        for (String title : bookTitles) {
            titlesList.add(title);
        }

        for (String price : bookPrices) {
            pricesList.add(price);
        }

        adapter = new ImageAdapter(getActivity(), imagesList, titlesList, pricesList);
        recyclerView.setAdapter(adapter);

        // Setup RecyclerView for Featured Deals
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // Convert arrays to lists for the second RecyclerView
        for (int image : images2) {
            imagesList2.add(image);
        }

        for (String title : bookTitles2) {
            titlesList2.add(title);
        }

        for (String price : bookPrices2) {
            pricesList2.add(price);
        }

        adapter2 = new ImageAdapter(getActivity(), imagesList2, titlesList2, pricesList2);
        recyclerView2.setAdapter(adapter2);

        // Search functionality
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

        // Start automatic sliding
        startAutoSlide();

        return view;
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
            if (nextSlide >= sliderImages.length) {
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

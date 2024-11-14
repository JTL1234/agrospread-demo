package com.limpag.agrospread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class Tab2Fragment extends Fragment {

    // Arrays for images (Resource IDs for slider and products)
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

    private ViewPager2 viewPager;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());

    private RecyclerView recyclerView;
    private ImageAdapter adapter;

    private List<Integer> imagesList = new ArrayList<>();
    private List<String> titlesList = new ArrayList<>();
    private List<String> pricesList = new ArrayList<>();

    private List<Integer> filteredImages = new ArrayList<>();
    private List<String> filteredTitles = new ArrayList<>();
    private List<String> filteredPrices = new ArrayList<>();

    private TextView countdownTimer;
    private int remainingTimeInSeconds = 86400;  // Start from 24 hours (86400 seconds)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        // Setup SearchView
        SearchView searchView = view.findViewById(R.id.searchView4);
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

        // Convert sliderImages array to List<Integer> for the ImageSliderAdapter
        List<Integer> sliderImageList = new ArrayList<>();
        for (int image : sliderImages) {
            sliderImageList.add(image);
        }

        // Setup Image Slider (ViewPager2)
        viewPager = view.findViewById(R.id.viewPager);
        ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(getContext(), sliderImageList);
        viewPager.setAdapter(sliderAdapter);

        // Setup RecyclerView for Best Deals
        recyclerView = view.findViewById(R.id.recyclerView3);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // Convert arrays to lists for RecyclerView
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

        // Countdown Timer
        countdownTimer = view.findViewById(R.id.countdownTimer);
        updateCountdown(); // Initialize countdown display

        // Start automatic sliding for ViewPager
        startAutoSlide();

        return view;
    }

    private void filterResults(String query) {
        filteredImages.clear();
        filteredTitles.clear();
        filteredPrices.clear();

        if (TextUtils.isEmpty(query)) {
            adapter.updateList(imagesList, titlesList, pricesList);
        } else {
            for (int i = 0; i < bookTitles.length; i++) {
                if (bookTitles[i].toLowerCase().contains(query.toLowerCase())) {
                    filteredImages.add(images[i]);
                    filteredTitles.add(bookTitles[i]);
                    filteredPrices.add(bookPrices[i]);
                }
            }

            adapter.updateList(filteredImages, filteredTitles, filteredPrices);
        }
    }

    private void updateCountdown() {
        int hours = remainingTimeInSeconds / 3600;
        int minutes = (remainingTimeInSeconds % 3600) / 60;
        int seconds = remainingTimeInSeconds % 60;

        String timeString = String.format("Ends In %02d:%02d:%02d", hours, minutes, seconds);
        countdownTimer.setText(timeString);

        if (remainingTimeInSeconds > 0) {
            remainingTimeInSeconds--;
            new Handler(Looper.getMainLooper()).postDelayed(this::updateCountdown, 1000); // Update every second
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

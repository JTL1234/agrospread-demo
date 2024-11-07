package com.limpag.agrospread;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Tab1Fragment extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3)); // 3 columns
        recyclerView.setAdapter(new ImageAdapter(getActivity(), images, bookTitles, bookPrices));
        return view;
    }
}

package com.limpag.agrospread;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AboutUsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aboutus, container, false);

        // Handle the arrow back button click
        ImageView arrowBack = view.findViewById(R.id.arrowback);
        arrowBack.setOnClickListener(v -> {
            // Replace the current fragment with HomeFragment
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.home_fragment_container, new HomeFragment());
            transaction.addToBackStack(null); // Optional: add to back stack for back navigation
            transaction.commit();
        });

        // Handle the More Info button click
        Button moreInfoButton = view.findViewById(R.id.button_more_info);
        moreInfoButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://jethrolimpag.wordpress.com/"));
            startActivity(browserIntent);
        });

        return view;
    }
}

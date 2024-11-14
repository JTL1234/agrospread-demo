package com.limpag.agrospread;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class AddProduct extends Fragment {

    private EditText productName, productPrice, productType, productDesc;
    private Button uploadButton, addProductButton;
    private ImageView productImageView;
    private Uri imageUri;

    private DatabaseHelper dbHelper;

    private static final int PICK_IMAGE_REQUEST = 1;

    public AddProduct() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        // Initialize SQLite database helper
        dbHelper = new DatabaseHelper(getActivity());

        // Find views by ID
        productName = view.findViewById(R.id.productname);
        productPrice = view.findViewById(R.id.productprice);
        productType = view.findViewById(R.id.producttype);
        productDesc = view.findViewById(R.id.productdesc);
        uploadButton = view.findViewById(R.id.uploadimage);
        addProductButton = view.findViewById(R.id.Add_Product);
        productImageView = view.findViewById(R.id.productimage);

        // Set up image upload button
        uploadButton.setOnClickListener(v -> openFileChooser());

        // Set up "Add Product" button
        addProductButton.setOnClickListener(v -> uploadProduct());

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            productImageView.setImageURI(imageUri); // Preview the selected image
        }
    }

    private void uploadProduct() {
        String name = productName.getText().toString();
        String price = productPrice.getText().toString();
        String type = productType.getText().toString();
        String desc = productDesc.getText().toString();

        if (name.isEmpty() || price.isEmpty() || type.isEmpty() || desc.isEmpty() || imageUri == null) {
            Toast.makeText(getActivity(), "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert image URI to string to store in SQLite
        String imageUriString = imageUri.toString();

        // Save the product to SQLite
        dbHelper.addProduct(name, price, type, desc, imageUriString);

        Toast.makeText(getActivity(), "Product added successfully!", Toast.LENGTH_SHORT).show();
        clearFields(); // Clear the input fields after saving
    }

    private void clearFields() {
        productName.setText("");
        productPrice.setText("");
        productType.setText("");
        productDesc.setText("");
        productImageView.setImageResource(0); // Clear the image
    }
}

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddProduct extends Fragment {

    private EditText productName, productPrice, productType, productDesc;
    private Button uploadButton, addProductButton;
    private ImageView productImageView;
    private Uri imageUri;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;

    private static final int PICK_IMAGE_REQUEST = 1;

    public AddProduct() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        // Initialize Firebase
        firebaseStorage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference().child("product_images");

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

        // Upload the image to Firebase Storage
        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveProductToFirestore(name, price, type, desc, imageUrl);
                }))
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveProductToFirestore(String name, String price, String type, String desc, String imageUrl) {
        Map<String, Object> productData = new HashMap<>();
        productData.put("name", name);
        productData.put("price", price);
        productData.put("type", type);
        productData.put("description", desc);
        productData.put("image_url", imageUrl);

        firestore.collection("products")
                .add(productData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(), "Product added successfully!", Toast.LENGTH_SHORT).show();
                    clearFields(); // Clear the input fields after saving
                    // Optionally, you could update a RecyclerView here to show the new product
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void clearFields() {
        productName.setText("");
        productPrice.setText("");
        productType.setText("");
        productDesc.setText("");
        productImageView.setImageResource(0); // Clear the image
    }
}


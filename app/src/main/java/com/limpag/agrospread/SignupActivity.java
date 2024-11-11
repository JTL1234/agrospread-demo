package com.limpag.agrospread;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.limpag.agrospread.databinding.ActivitySignupBinding;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.signupUsername.getText().toString();
                String fullname = binding.signupFullname.getText().toString();
                String email = binding.signupEmail.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword = binding.signupConfirm.getText().toString();
                String address = binding.signupAddress.getText().toString();
                String phone = binding.signupPhone.getText().toString();

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                        username.isEmpty() || fullname.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(confirmPassword)) {
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {

                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        if (user != null) {

                                            String userId = user.getUid();
                                            Map<String, String> userInfo = new HashMap<>();
                                            userInfo.put("username", username);
                                            userInfo.put("fullname", fullname);
                                            userInfo.put("email", email);
                                            userInfo.put("address", address);
                                            userInfo.put("phone", phone);


                                            firestore.collection("userInfo").document(userId)
                                                    .set(userInfo)
                                                    .addOnCompleteListener(databaseTask -> {
                                                        if (databaseTask.isSuccessful()) {
                                                            // Save username and fullname to SharedPreferences
                                                            SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString("username", username);
                                                            editor.putString("fullname", fullname);
                                                            editor.apply();

                                                            Toast.makeText(SignupActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(SignupActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    } else {
                                        // Sign up failure
                                        String errorMessage = "Signup Failed: " + task.getException().getMessage();
                                        Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(SignupActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

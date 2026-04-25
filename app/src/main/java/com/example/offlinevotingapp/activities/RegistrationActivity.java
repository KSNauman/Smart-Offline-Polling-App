package com.example.offlinevotingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinevotingapp.databinding.ActivityRegistrationBinding;
import com.example.offlinevotingapp.managers.UserManager;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userManager = new UserManager(this);

        binding.btnSaveName.setOnClickListener(v -> {
            String name = binding.etUserName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            } else {
                userManager.saveUserName(name);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }
}

package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private Button registerUser, alreadyAccount;
    private TextView banner;
    private EditText editTextFullName, editTextConfirmPassword, editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth=FirebaseAuth.getInstance();
        registerUser=(Button) findViewById(R.id.register);
        editTextFullName=(EditText) findViewById(R.id.fullName) ;
        editTextEmail=(EditText) findViewById((R.id.email));
        editTextPassword=(EditText) findViewById((R.id.password));
        editTextConfirmPassword = (EditText) findViewById((R.id.ConfirmPassword));
        registerUser.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String ConfirmPassword = editTextConfirmPassword.getText().toString().trim();
        if (fullName.isEmpty()) {
            editTextFullName.setError("full name required");
            editTextFullName.requestFocus();
            return;

        }
        if (email.isEmpty()) {
            editTextEmail.setError("Email required");
            editTextEmail.requestFocus();
            return;

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Valid Email Required");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty() && password.length() < 6) {
            editTextPassword.setError("invalid password");
            editTextPassword.requestFocus();
            return;

        }

        if (ConfirmPassword.isEmpty()) {
            editTextConfirmPassword.setError("confirm password");
            editTextConfirmPassword.requestFocus();
            return;

        }

        if (ConfirmPassword.equals(password) == false) {
            editTextConfirmPassword.setError("passwords do not match please try again");
            editTextConfirmPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (ConfirmPassword.equals(password)) {
                            if (task.isSuccessful()) {
                                User user = new User(fullName,password, email);

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegisterUser.this, "user registration succesful", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(RegisterUser.this, "Failed", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                        } else {
                            Toast.makeText(RegisterUser.this, "Passwords Do not Match ! Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void alreadyHaveAccount(View view) {
        switch(view.getId()){
            case R.id.alreadyAccount:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register:
                registerUser();
                break;
        }
    }
}
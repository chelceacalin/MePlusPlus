package com.example.meplusplus.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.example.meplusplus.context.DbContext;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import io.github.muddz.styleabletoast.StyleableToast;


public class RegisterActivity extends AppCompatActivity {
    //Controale
    Button registerActivity_btn_register, registerActivity_button_Login;
    EditText registeractivity_edit_text_username;
    EditText registeractivity_edit_text_name;
    EditText registeractivity_edit_text_email;
    EditText registerActivity_edit_text_password;
    ProgressDialog progressDialog;
    CheckBox showpasswordCheckBox;

    FirebaseAuth auth;
    Map<String, Object> map;

    //Validari
    String username, name, email, password;
    boolean isFieldChecked = false;
    String userID;

    DbContext dbContext = DbContext.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Initializare
        init();

        registerActivity_btn_register.setOnClickListener(view -> {
            username = registeractivity_edit_text_username.getText().toString();
            name = registeractivity_edit_text_name.getText().toString();
            email = registeractivity_edit_text_email.getText().toString();
            password = registerActivity_edit_text_password.getText().toString();
            isFieldChecked = CheckValidations();
            if (isFieldChecked) {
                if (!username.isEmpty() && !name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(username, name, email, password);
                } else {
                    new StyleableToast.Builder(RegisterActivity.this)
                            .text("Empty Credentials")
                            .textColor(Color.RED)
                            .backgroundColor(getResources().getColor(R.color.white))
                            .cornerRadius(25)
                            .iconStart(R.drawable.ic_baseline_error_outline_24)
                            .show();
                }
            }
        });

        registerActivity_button_Login.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_to_left_transition);

        });

        showpasswordCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                registerActivity_edit_text_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                registerActivity_edit_text_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }

    //Initializare controale
    public void init() {
        //Butoane
        registerActivity_btn_register = findViewById(R.id.registerActivity_btn_register);
        registerActivity_button_Login = findViewById(R.id.registerActivity_button_Login);
        showpasswordCheckBox = findViewById(R.id.register_activity_checkBox);

        //EditText-uri
        registeractivity_edit_text_username = findViewById(R.id.registeractivity_edit_text_username);
        registeractivity_edit_text_name = findViewById(R.id.registeractivity_edit_text_name);
        registeractivity_edit_text_email = findViewById(R.id.registeractivity_edit_text_email);
        registerActivity_edit_text_password = findViewById(R.id.registerActivity_edit_text_password);
        //ProgressDialog
        progressDialog = new ProgressDialog(this);

        //Firebase
        auth = FirebaseAuth.getInstance();
        map = new HashMap<>();


    }


    private boolean validatePass(String uPass) {
        if (uPass.isEmpty() || uPass.length() < 4) {
            registerActivity_edit_text_password.setError("Your password must be at least 4 characters in length");
            return false;
        } else
            return true;
    }

    private boolean validateName(String uName) {
        if (uName.length() > 15) {
            registeractivity_edit_text_name.setError("Your name must be shorter than 15 characters");
            return false;
        } else if (uName.length() < 3) {
            registeractivity_edit_text_name.setError("Your name must be longer than 2 characters");
            return false;
        } else
            return true;
    }


    private boolean validateUser(String uUsername) {
        if (uUsername.length() > 15) {
            registeractivity_edit_text_username.setError("Your username must be shorter than 15 characters in length");
            return false;
        } else if (uUsername.length() < 3) {
            registeractivity_edit_text_username.setError("Your username must be longer than 2 characters in length");
            return false;
        } else
            return true;
    }

    private boolean CheckValidations() {
        return validatePass(password) && validateName(name) && validateUser(username);
    }


    //Cand apas butonul de back de la telefon
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_to_left_transition);
    }


    private void registerUser(String username, String name, String email, String password) {
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            if (auth.getCurrentUser() == null) {
                progressDialog.dismiss();
                StyleableToast.makeText(this, "Eroare: Utilizatorul este null!", Toast.LENGTH_SHORT).show();
                return;
            }

            userID = auth.getCurrentUser().getUid();

            map.put("username", username);
            map.put("name", name);
            map.put("email", email);
            map.put("id", userID);
            map.put("bio", "");
            map.put("imageurl", "default");

            dbContext.getReference().child("users").child(userID).setValue(map)
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_to_left_transition);

                            new StyleableToast
                                    .Builder(RegisterActivity.this)
                                    .text("Registration Completed")
                                    .textColor(Color.GREEN)
                                    .backgroundColor(getResources().getColor(R.color.white))
                                    .cornerRadius(25)
                                    .iconStart(R.drawable.ic_baseline_check_circle_24)
                                    .show();
                        } else {
                            new StyleableToast
                                    .Builder(RegisterActivity.this)
                                    .text("Eroare la salvarea în Firebase Database")
                                    .textColor(Color.RED)
                                    .backgroundColor(getResources().getColor(R.color.white))
                                    .cornerRadius(25)
                                    .iconStart(R.drawable.ic_baseline_error_outline_24)
                                    .show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        new StyleableToast
                                .Builder(RegisterActivity.this)
                                .text("Database error: " + e.getMessage())
                                .textColor(Color.RED)
                                .backgroundColor(getResources().getColor(R.color.white))
                                .cornerRadius(25)
                                .iconStart(R.drawable.ic_baseline_error_outline_24)
                                .show();
                    });

        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            new StyleableToast
                    .Builder(RegisterActivity.this)
                    .text("Firebase Auth error: " + e.getMessage())
                    .textColor(Color.RED)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .cornerRadius(25)
                    .iconStart(R.drawable.ic_baseline_error_outline_24)
                    .show();
        });
    }

}
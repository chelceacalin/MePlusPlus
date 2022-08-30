package com.example.meplusplus;

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

import com.example.meplusplus.Utils.CommentDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;

/*
       Status: RFP

       CREATED DATE: 8/18/2022
       UPDATED DATE: 8/18/2022
 */
public class RegisterActivity extends AppCompatActivity {
    //Controale
    Button registerActivity_btn_register, registerActivity_button_Login;
    EditText registeractivity_edit_text_username;
    EditText registeractivity_edit_text_name;
    EditText registeractivity_edit_text_email;
    EditText registerActivity_edit_text_password;
    ProgressDialog progressDialog;
    CheckBox showpasswordCheckBox;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;

    //Validari
    String username, name, email, password;
    boolean isFieldChecked = false;

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
                if (!username.equals("") && !name.equals("") && !email.equals("") && !password.equals("")) {
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


    private boolean validatePass(String uPass) {
        if (uPass.equals("") || uPass.length() < 4) {
            registerActivity_edit_text_password.setError("Your password must be at least 4 characters in length");
            return false;
        } else
            return true;
    }

    private boolean validateName(String uName) {
        if (uName.length() > 12) {
            registeractivity_edit_text_name.setError("Your name must be shorter than 12 characters in length");
            return false;
        } else if (uName.length() < 3) {
            registeractivity_edit_text_name.setError("Your name must be longer than 2 characters in length");
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
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference();

    }

    private void registerUser(String username, String name, String email, String password) {
        progressDialog.setMessage("Loading");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            Map<String, Object> map = new HashMap<>();
            map.put("username", username);
            map.put("name", name);
            map.put("email", email);
            map.put("id", Objects.requireNonNull(auth.getCurrentUser()).getUid());
            map.put("bio", "");
            map.put("imageurl", "default");

            reference.child("users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.slide_right_to_left_transition);
                    Toast.makeText(RegisterActivity.this, "Registration Completed", Toast.LENGTH_SHORT).show();
                }
            });
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
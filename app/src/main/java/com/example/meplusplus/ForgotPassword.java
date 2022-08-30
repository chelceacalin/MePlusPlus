package com.example.meplusplus;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

// RFP
/*
       CREATED DATE: 8/18/2022
       UPDATED DATE: 8/18/2022
 */
public class ForgotPassword extends AppCompatActivity {

    //Controale
     EditText forgotpassowrd_edit_text_email_recover;
     Button forgotpassword_button_send_recovery_email;

    //Firebase
     FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();


        forgotpassword_button_send_recovery_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = forgotpassowrd_edit_text_email_recover.getText().toString().trim();
                if (!emailText.equals("")) {
                    sendRecoveryMail(emailText);
                } else {
                    Toast.makeText(ForgotPassword.this, "You have to introduce and email! ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendRecoveryMail(String emailText) {

        auth.sendPasswordResetEmail(emailText).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "Please check your email!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ForgotPassword.this, "Invalid Mail", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPassword.this, "Email Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Cand apas butonul de back de la telefon
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_to_left_transition);
    }

    //Initializam controalele
    public void init() {
        //Controale
        forgotpassowrd_edit_text_email_recover = findViewById(R.id.forgotpassowrd_edit_text_email_recover);
        forgotpassword_button_send_recovery_email = findViewById(R.id.forgotpassword_button_send_recovery_email);

        //Firebase
        auth = FirebaseAuth.getInstance();

    }
}
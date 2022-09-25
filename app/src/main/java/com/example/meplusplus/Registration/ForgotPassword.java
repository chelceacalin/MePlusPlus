package com.example.meplusplus.Registration;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.R;
import com.google.firebase.auth.FirebaseAuth;

import io.github.muddz.styleabletoast.StyleableToast;

/*

       Status: RFP

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


        forgotpassword_button_send_recovery_email.setOnClickListener(view -> {
            String emailText = forgotpassowrd_edit_text_email_recover.getText().toString().trim();
            if (!emailText.equals("")) {
                sendRecoveryMail(emailText);
            } else {
                new StyleableToast.Builder(ForgotPassword.this)
                        .text("You have to introduce and email! ")
                        .textColor(Color.RED)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .cornerRadius(25)
                        .iconStart(R.drawable.ic_baseline_error_outline_24)
                        .show();
            }
        });
    }

    private void sendRecoveryMail(String emailText) {

        auth.sendPasswordResetEmail(emailText).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                new StyleableToast.Builder(ForgotPassword.this)
                        .text("Please check your email! ")
                        .textColor(Color.GREEN)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .cornerRadius(25)
                        .iconStart(R.drawable.ic_baseline_mark_email_unread_24)
                        .show();
            } else
                new StyleableToast.Builder(ForgotPassword.this)
                        .text("Invalid Mail")
                        .textColor(Color.RED)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .cornerRadius(25)
                        .iconStart(R.drawable.ic_baseline_error_outline_24)
                        .show();
        }).addOnFailureListener(e ->

                new StyleableToast.Builder(ForgotPassword.this)
                        .text("Email Failure")
                        .textColor(Color.RED)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .cornerRadius(25)
                        .iconStart(R.drawable.ic_baseline_error_outline_24)
                        .show()
        );
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
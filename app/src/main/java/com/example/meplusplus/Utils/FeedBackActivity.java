package com.example.meplusplus.Utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.R;

import io.github.muddz.styleabletoast.StyleableToast;

public class FeedBackActivity extends AppCompatActivity {

    EditText feedbackactivity_subject;
    EditText feedbackactivity_message;
    Button feedbackactivity_send_mail;

    String subject;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        init();


        feedbackactivity_send_mail.setOnClickListener(view -> {
            subject = feedbackactivity_subject.getText().toString();
            message = feedbackactivity_message.getText().toString();
            if (!subject.equals("") && !message.equals("")) {
                sendEmail(subject, message);
            } else {
                new StyleableToast.Builder(FeedBackActivity.this)
                        .text("Make sure subject and message fields are not empty")
                        .textColor(Color.RED)
                        .backgroundColor(getResources().getColor(R.color.WhiteSmoke))
                        .cornerRadius(25)
                        .iconStart(R.drawable.ic_baseline_error_outline_24)
                        .show();
            }

        });


    }

    private void init() {
        feedbackactivity_subject = findViewById(R.id.feedbackactivity_subject);
        feedbackactivity_message = findViewById(R.id.feedbackactivity_message);
        feedbackactivity_send_mail = findViewById(R.id.feedbackactivity_send_mail);

    }

    @SuppressLint("IntentReset")
    protected void sendEmail(String subject, String message) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "chelcea.calin@yahoo.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(intent);
        finish();
        new StyleableToast.Builder(FeedBackActivity.this)
                .text("Thank you for your feedback, we will get back to you asap! ")
                .textColor(Color.WHITE)
                .backgroundColor(getResources().getColor(R.color.Black))
                .cornerRadius(25)
                .iconStart(R.drawable.ic_baseline_favorite)
                .show();
    }
}
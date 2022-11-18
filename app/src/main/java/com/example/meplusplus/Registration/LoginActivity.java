package com.example.meplusplus.Registration;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import io.github.muddz.styleabletoast.StyleableToast;

/*

       Status: TC - google to work on all devices, Facebook to work
       CREATED DATE: 8/18/2022
       UPDATED DATE: 8/18/2022
 */
@SuppressWarnings("ALL")
public class LoginActivity extends AppCompatActivity {

    //Google
    private static final int RC_SIGN_IN = 1;
    //Diverse
    String email_text;
    String passowrd_text;
    Map<String, Object> map;
    String userID;
    //Google
    GoogleSignInOptions gso;
    //Controale
    private EditText email, password;
    private TextView forgotpassword;
    private ImageView facebookButton, googleButton, imageViewNextButton;
    private Button signUp;
    private CheckBox showpassCheckBox;
    private GoogleSignInClient mGoogleSignInClient;
    //Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializare
        init();
        signUp.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
        });
        forgotpassword.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            overridePendingTransition(R.anim.fade_out, R.anim.slide_out);
        });
        imageViewNextButton.setOnClickListener(view -> {
            email_text = email.getText().toString();
            passowrd_text = password.getText().toString();
            if (!email_text.equals("") && !passowrd_text.equals("")) {
                loginUser(email_text, passowrd_text);
            } else {
                new StyleableToast.Builder(LoginActivity.this)
                        .text("Empty Credentials")
                        .textColor(Color.RED)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .cornerRadius(25)
                        .iconStart(R.drawable.ic_baseline_error_outline_24)
                        .show();
            }
        });

        //pt show password
        showpassCheckBox.setOnCheckedChangeListener((compoundButton, ischecked) -> {
            if (ischecked) {
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
        googleButton.setOnClickListener(view -> signIn());
    }

    //Initializam controalele
    public void init() {
        //Controale
        email = findViewById(R.id.loginactivity_edit_text_email);
        password = findViewById(R.id.loginActivity_edit_text_password);
        forgotpassword = findViewById(R.id.loginActivity_forgot_password);
        facebookButton = findViewById(R.id.loginActivity_button_facebook_login);
        googleButton = findViewById(R.id.login_activity_btn_google_sign_up);
        imageViewNextButton = findViewById(R.id.login_activity_imageview_next);
        signUp = findViewById(R.id.loginActivity_button_sign_up);
        showpassCheckBox = findViewById(R.id.login_activity_checkBox);

        //Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance("https://applicenta-8582b-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference();
        map = new HashMap<>();

        //Google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("432467919065-e2s07mhf5b56sf3nusdefascua25qbih.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result return from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else
            new StyleableToast.Builder(LoginActivity.this)
                    .text("Empty Credentials")
                    .textColor(Color.RED)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .cornerRadius(25)
                    .iconStart(R.drawable.ic_baseline_error_outline_24)
                    .show();
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult();
            if (completedTask.isSuccessful()) {
                firebaseAuthWithGoogle(account);
            }
        } catch (Exception e) {
            new StyleableToast.Builder(LoginActivity.this)
                    .text("Error")
                    .textColor(Color.RED)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .cornerRadius(25)
                    .iconStart(R.drawable.ic_baseline_error_outline_24)
                    .show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            new StyleableToast
                                    .Builder(LoginActivity.this)
                                    .text("Google Sign In Successful")
                                    .textColor(Color.WHITE)
                                    .backgroundColor(R.color.navy)
                                    .cornerRadius(25)
                                    .iconStart(R.drawable.ic_google)
                                    .show();
                            user = auth.getCurrentUser();
                            String arr[] = user.getDisplayName().split(" ", 2);
                            String username = arr[0];
                            userID = auth.getCurrentUser().getUid();

                            map.put("username", username.toString());
                            map.put("name", user.getDisplayName().toString());
                            map.put("email", user.getEmail().toString());
                            map.put("id", userID);
                            map.put("bio", "");
                            map.put("imageurl", "default");
                            if (user.getPhotoUrl() == null) {
                                map.put("imageurl", "default");
                            } else
                                map.put("imageurl", user.getPhotoUrl() + "");

                            reference.child("users").child(userID).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_to_left_transition);
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    new StyleableToast.Builder(LoginActivity.this)
                                            .text("Database Failed")
                                            .textColor(Color.RED)
                                            .backgroundColor(getResources().getColor(R.color.white))
                                            .cornerRadius(25)
                                            .iconStart(R.drawable.ic_baseline_error_outline_24)
                                            .show();
                                }
                            });
                        } else {
                            new StyleableToast.Builder(LoginActivity.this)
                                    .text("Sign In Failed")
                                    .textColor(Color.RED)
                                    .backgroundColor(getResources().getColor(R.color.white))
                                    .cornerRadius(25)
                                    .iconStart(R.drawable.ic_baseline_error_outline_24)
                                    .show();
                        }
                    }
                });
    }


    //Cand apesi pe back cand esti pe pagina de login
    @Override
    public void onBackPressed() {
        System.exit(0);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
            finish();
        } else {
        }

    }


    //Cand minimizezi aplicatia
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_up);
    }

    //Login
    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new StyleableToast.Builder(LoginActivity.this)
                        .text("Wrong Credentials!")
                        .textColor(Color.RED)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .cornerRadius(25)
                        .iconStart(R.drawable.ic_baseline_error_outline_24)
                        .show();
            }
        });
    }
}
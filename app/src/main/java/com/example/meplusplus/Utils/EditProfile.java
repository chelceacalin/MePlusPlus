package com.example.meplusplus.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.meplusplus.DataSets.User;
import com.example.meplusplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;

/*
        Status: RFP
        CREATED DATE: 8/29/2022
        UPDATED DATE: 8/29/2022

        1.
        UPDATE DATE: 8/29/2022
        Reason: OnActivityResult e deprecated
*/
@SuppressWarnings("ALL")
public class EditProfile extends AppCompatActivity {

    //Controale
    ImageView editprofile_closeImg;
    ImageView editprofile_photo;
    Button editprofile_change_image;
    Button editprofile_button_save;
    EditText editprofile_username;
    EditText editprofile_bio;
    ImageView editprofile_rotate_image;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference reference;

    //Pt imagine
    Uri imageviewuri;
    String imageURL;
    StorageTask uploadtask;

    //Diverse
    int rotationInit;
    ProgressDialog progressDialog;

    //Update
    Map<String, Object> map;
    Uri imgURiDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();

        editprofile_rotate_image.setOnClickListener(view -> {
            rotationInit += 90;
            editprofile_photo.setRotation(rotationInit);
        });


        editprofile_closeImg.setOnClickListener(view -> finish());

        editprofile_button_save.setOnClickListener(view -> {
            fuploadImage();
            finish();
        });

        final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK
                            && result.getData() != null) {
                        imageviewuri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageviewuri);
                            editprofile_photo.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );


        editprofile_change_image.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcher.launch(intent);
        });


        ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                editprofile_username.setText(Objects.requireNonNull(snapshot.getValue(User.class)).getUsername());
                editprofile_bio.setText(Objects.requireNonNull(snapshot.getValue(User.class)).getBio());
                if (Objects.requireNonNull(snapshot.getValue(User.class)).getImageurl().equals("default")) {
                    Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(editprofile_photo);
                } else {
                    Picasso.get().load(Objects.requireNonNull(snapshot.getValue(User.class)).getImageurl()).placeholder(R.drawable.ic_baseline_person).into(editprofile_photo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init() {
        //Controale
        editprofile_closeImg = findViewById(R.id.editprofile_closeImg);
        editprofile_button_save = findViewById(R.id.editprofile_button_save);
        editprofile_photo = findViewById(R.id.editprofile_photo);
        editprofile_change_image = findViewById(R.id.editprofile_change_image);
        editprofile_username = findViewById(R.id.editprofile_username);
        editprofile_bio = findViewById(R.id.editprofile_bio);
        editprofile_rotate_image = findViewById(R.id.editprofile_rotate_image);

        //Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance("https://applicenta-8582b-default-rtdb.europe-west1.firebasedatabase.app");
        ref = database.getReference().child("users");
        progressDialog = new ProgressDialog(this);
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference("uploads");

        //Diverse
        map = new HashMap<>();
        rotationInit = 0;

    }


    private void fuploadImage() {

        if (imageviewuri != null) {
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            reference = reference.child(System.currentTimeMillis() + ".jpeg");
            uploadtask = reference.putFile(imageviewuri);

            uploadtask.continueWithTask(task -> reference.getDownloadUrl()).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        new StyleableToast.Builder(EditProfile.this)
                                .text("Error Uploading")
                                .textColor(Color.RED)
                                .backgroundColor(getResources().getColor(R.color.white))
                                .cornerRadius(25)
                                .iconStart(R.drawable.ic_baseline_error_outline_24)
                                .show();
                    })
                    .addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                        if (!task.isSuccessful()) {
                            new StyleableToast.Builder(EditProfile.this)
                                    .text("Error")
                                    .textColor(Color.RED)
                                    .backgroundColor(getResources().getColor(R.color.white))
                                    .cornerRadius(25)
                                    .iconStart(R.drawable.ic_baseline_error_outline_24)
                                    .show();
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            imgURiDownload = task.getResult();
                            imageURL = imgURiDownload.toString();
                            ref.child(user.getUid()).child("imageurl").setValue(imageURL);
                            new StyleableToast.Builder(EditProfile.this)
                                    .text("Profile Updated")
                                    .textColor(Color.WHITE)
                                    .backgroundColor(getResources().getColor(R.color.blue))
                                    .cornerRadius(25)
                                    .iconStart(R.drawable.ic_baseline_check_circle_24)
                                    .show();
                            map.put("username", editprofile_username.getText().toString());
                            map.put("bio", editprofile_bio.getText().toString());
                            ref.child(user.getUid()).updateChildren(map);
                        }

                    });
        } else {
            map.put("username", editprofile_username.getText().toString());
            map.put("bio", editprofile_bio.getText().toString());
            ref.child(user.getUid()).updateChildren(map);
            new StyleableToast.Builder(EditProfile.this)
                    .text("Profile Updated Without Profile Image ")
                    .textColor(Color.WHITE)
                    .textSize(17)
                    .backgroundColor(getResources().getColor(R.color.navy))
                    .cornerRadius(15)
                    .show();

        }
    }

    @Override
    protected void onDestroy() {
        progressDialog.dismiss();
        super.onDestroy();
    }
}
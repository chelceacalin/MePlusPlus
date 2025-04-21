package com.example.meplusplus.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.meplusplus.R;
import com.example.meplusplus.context.DbContext;
import com.example.meplusplus.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    DbContext dbContext = DbContext.getInstance();


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
        ref = dbContext.getReference().child("users");
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

            Bitmap originalBitmap = ((BitmapDrawable) editprofile_photo.getDrawable()).getBitmap();
            Matrix matrix = new Matrix();
            matrix.postRotate(rotationInit, originalBitmap.getWidth() / 2f,
                    originalBitmap.getHeight() / 2f);
            RectF mappedRect = new RectF(0, 0, originalBitmap.getWidth(), originalBitmap.getHeight());
            matrix.mapRect(mappedRect);

            Bitmap rotatedBitmap = Bitmap.createBitmap((int) mappedRect.width(), (int) mappedRect.height(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(rotatedBitmap);
            canvas.translate(-mappedRect.left, -mappedRect.top);
            canvas.drawBitmap(originalBitmap, matrix, new Paint());

            // Compress the rotated Bitmap to reduce the image size
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] data = baos.toByteArray();


            reference = reference.child(System.currentTimeMillis() + ".jpeg");
            uploadtask = reference.putBytes(data);

            uploadtask.continueWithTask(task -> reference.getDownloadUrl()).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Error uploading picture", Toast.LENGTH_SHORT).show();
                    })
                    .addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(this, "Error updating profile", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            imgURiDownload = task.getResult();
                            imageURL = imgURiDownload.toString();
                            ref.child(user.getUid()).child("imageurl").setValue(imageURL);
                            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            map.put("username", editprofile_username.getText().toString());
                            map.put("bio", editprofile_bio.getText().toString());
                            ref.child(user.getUid()).updateChildren(map);
                        }

                    });
        } else {
            map.put("username", editprofile_username.getText().toString());
            map.put("bio", editprofile_bio.getText().toString());
            ref.child(user.getUid()).updateChildren(map);
            Toast.makeText(this, "Profile updated without profile image", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onDestroy() {
        progressDialog.dismiss();
        super.onDestroy();
    }
}
package com.example.meplusplus.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.Fragments.Social_PageFragment;
import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.example.meplusplus.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.github.muddz.styleabletoast.StyleableToast;


/*

       Status: RFP

       CREATED DATE: 8/21/2022
       UPDATED DATE: 8/21/2022

       1.
        UPDATE DATE: 8/25/2022
        Notes: OnActivityResult e deprecated
         */

@SuppressWarnings("ALL")
public class PostActivity extends AppCompatActivity {
    //Controale
    ImageView postactivity_closeImg;
    ImageView postactivity_imageProfile;
    ImageView postactivity_rotate_image;
    EditText postactivity_description;
    Button postactivity_buttonpost;
    ProgressDialog progressDialog;
    Button selectimage;

    //Firebase
    FirebaseStorage storage;
    StorageReference reference;

    //Firebase - Stocare Postare
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    StorageTask uploadtask;

    //Pt Imagine
    static int REQUEST_CODE;
    Uri imageviewuri;
    int rotationInit;
    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        //Initializare
        init();

        postactivity_rotate_image.setOnClickListener(view -> {
            rotationInit += 90;
            postactivity_imageProfile.setRotation(rotationInit);
        });

        postactivity_buttonpost.setOnClickListener(view -> {
            fuploadImage();

        });
        postactivity_closeImg.setOnClickListener(view -> {
            Intent intent = new Intent(PostActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_to_left_transition);
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
                            postactivity_imageProfile.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        selectimage.setOnClickListener(view -> {
            //fselectimage();
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcher.launch(intent);
        });

        postactivity_imageProfile.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcher.launch(intent);
        });
    }


    private void init() {
        //Initializare
        postactivity_closeImg = findViewById(R.id.postactivity_closeImg);
        postactivity_imageProfile = findViewById(R.id.postactivity_imageProfile);
        postactivity_description = findViewById(R.id.postactivity_description);
        postactivity_rotate_image = findViewById(R.id.postactivity_rotate_image);
        postactivity_buttonpost = findViewById(R.id.postactivity_buttonpost);
        selectimage = findViewById(R.id.postactivity_selectimage);
        progressDialog = new ProgressDialog(this);

        //Firebase
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference("posts");
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = database.getReference("posts");
        auth = FirebaseAuth.getInstance();

        //Divers
        REQUEST_CODE = 69;
        rotationInit = 0;
    }

    private String getFext() {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(imageviewuri));
    }


    private void fuploadImage() {

        if (imageviewuri != null) {
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            reference = reference.child(System.currentTimeMillis() + "." + getFext());
            uploadtask = reference.putFile(imageviewuri);
            uploadtask.continueWithTask(task -> reference.getDownloadUrl()).addOnFailureListener(e -> {
                progressDialog.dismiss();
                new StyleableToast.Builder(PostActivity.this)
                        .text("Error Uploading")
                        .textColor(Color.RED)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .cornerRadius(25)
                        .iconStart(R.drawable.ic_baseline_error_outline_24)
                        .show();
            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                progressDialog.dismiss();
                Uri downloadUri = task.getResult();
                imageURL = downloadUri.toString();

                String postID = databaseReference.push().getKey(); // un id unic
                String description = postactivity_description.getText().toString();
                String IDpublisher = auth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("postid", postID);
                map.put("imageurl", imageURL);
                map.put("description", "   " + description);
                map.put("publisher", IDpublisher);
                assert postID != null;
                databaseReference.child(postID).setValue(map);

                Toast.makeText(PostActivity.this, "Post Uploaded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PostActivity.this, Social_PageFragment.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_to_left_transition);
                finish();
            });
        } else {
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show();
        }
    }


    private void fselectimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image ..."), REQUEST_CODE);
    }
}
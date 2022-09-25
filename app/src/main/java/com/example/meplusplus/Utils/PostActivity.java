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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.example.meplusplus.ZenMode.ZenModeActivity;
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
    //Pt Imagine
    static int REQUEST_CODE;
    //Controale
    ImageView postactivity_closeImg;
    ImageView postactivity_imageProfile;
    ImageView postactivity_rotate_image;
    EditText postactivity_description;
    EditText postactivity_ingredients;
    Button postactivity_buttonpost;
    ProgressDialog progressDialog;
    Button selectimage;
    //Firebase
    FirebaseStorage storage;
    StorageReference reference;
    String UniqueID;
    //Firebase - Stocare Postare
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    StorageTask uploadtask;
    Map<String, Object> map;
    Uri imageviewuri;
    int rotationInit;

    //Diverse
    String imageURL;
    String user;
    String description;
    String ingredients;

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
            startActivity(new Intent(PostActivity.this,MainActivity.class));
            overridePendingTransition(R.anim.slide_left_to_right_transition,R.anim.slide_right_to_left_transition);
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
        postactivity_ingredients=findViewById(R.id.postactivity_ingredients);
        progressDialog = new ProgressDialog(this);

        //Firebase
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference("posts");
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = database.getReference("posts");
        auth = FirebaseAuth.getInstance();

        //Diverse
        REQUEST_CODE = 69;
        rotationInit = 0;
        description="";
        ingredients="";
        map = new HashMap<>();
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
                new StyleableToast.Builder(PostActivity.this)
                        .text("Post Uploaded")
                        .textColor(Color.BLUE)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .cornerRadius(15)
                        .textSize(17)
                        .show();

                Uri downloadUri = task.getResult();
                imageURL = downloadUri.toString();
                UniqueID = databaseReference.push().getKey(); // un id unic
                description = postactivity_description.getText().toString().trim();
                ingredients=postactivity_ingredients.getText().toString().toString();
                user = auth.getCurrentUser().getUid();

                map.put("postid", UniqueID);
                map.put("imageurl", imageURL);
                if(!(description.equals(""))){
                    map.put("description",description);
                }
               else{
                   map.put("description","");
                }
                map.put("publisher", user);
                if(!(ingredients.equals(""))){
                    map.put("ingredients",ingredients);
                }
                else
                    map.put("ingredients","");

                assert UniqueID != null;
                databaseReference.child(UniqueID).setValue(map);


                // Schimbam din activitate in fragment
                Intent intent = new Intent(PostActivity.this, MainActivity.class);
                intent.putExtra("openSocialPageFragment", true);
                overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
                finish();
                startActivity(intent);
            });
        } else {
            new StyleableToast.Builder(PostActivity.this)
                    .text("Please Select an Image")
                    .textColor(Color.RED)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .cornerRadius(25)
                    .iconStart(R.drawable.ic_baseline_error_outline_24)
                    .show();
        }
    }


    private void fselectimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image ..."), REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PostActivity.this,MainActivity.class));
        overridePendingTransition(R.anim.slide_left_to_right_transition,R.anim.slide_right_to_left_transition);
        finish();
        super.onBackPressed();
    }
}
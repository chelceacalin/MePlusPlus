package com.example.meplusplus.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.Fragments.Social_PageFragment;
import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/*
       CREATED DATE: 8/25/2022
       UPDATED DATE: 8/25/2022
 */
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
    static final int REQUEST_CODE = 69;
    Uri imageviewuri;
    int rotationInit = 0;
    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        //Initializare
        init();


        postactivity_rotate_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotationInit += 90;
                postactivity_imageProfile.setRotation(rotationInit);
            }
        });


        postactivity_buttonpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insertIntoFirebase();
                fuploadImage();

            }
        });
        postactivity_closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_to_left_transition);

            }
        });


        /*
        UPDATE DATE: 8/22/2022
        Reason: OnActivityResult e deprecated
         */
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

        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fselectimage();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent);
            }
        });

        postactivity_imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent);
            }
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
    }

    private String getFext(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(imageviewuri));
    }


    private void fuploadImage() {

        if (imageviewuri != null)
        {
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            reference = reference.child(System.currentTimeMillis() + "." + getFext(imageviewuri));
            uploadtask = reference.putFile(imageviewuri);

            uploadtask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            return reference.getDownloadUrl();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PostActivity.this, "Error Uploading", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            progressDialog.dismiss();
                            Uri downloadUri = task.getResult();
                            imageURL = downloadUri.toString();

                            String postID = databaseReference.push().getKey(); // un id unic
                            String description = postactivity_description.getText().toString();
                            String IDpublisher = auth.getCurrentUser().getUid();
                            Map<String, Object> map = new HashMap<>();
                            map.put("postid", postID);
                            map.put("imageurl", imageURL);
                            map.put("description","   "+description);
                            map.put("publisher", IDpublisher);
                            databaseReference.child(postID).setValue(map);

                            Toast.makeText(PostActivity.this, "Post Uploaded", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PostActivity.this, Social_PageFragment.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_to_left_transition);
                            finish();
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show();
        }
    }


    private void fselectimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image ..."), REQUEST_CODE);
    }

    private void insertIntoFirebase() {
        String postID = databaseReference.push().getKey(); // un id unic
        String description = postactivity_description.getText().toString();
        String IDpublisher = auth.getCurrentUser().getUid();
        Map<String, Object> map = new HashMap<>();
        map.put("postid", postID);
        map.put("imageurl", imageURL);
        map.put("description", description);
        map.put("publisher", IDpublisher);
        databaseReference.child(postID).setValue(map);
    }


}
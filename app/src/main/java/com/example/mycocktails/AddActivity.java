package com.example.mycocktails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAddImage;
    Uri mImageUri;

    Cocktail temp;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btnAddImage = findViewById(R.id.btnAddImage);
        btnAddImage.setOnClickListener(this);

        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("1");

        ArrayList<Ingredients> inList = new ArrayList<>();
        inList.add(new Ingredients("Vodka",60));;
        inList.add(new Ingredients("Dry Vermouth",15 ));
        inList.add(new Ingredients("Dash Orange Bitters",1 ));



        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Stir, Strain into Chilled Martini glass.");
        arrayList.add("Garnish with Lemon twist / Olives.");


        temp = new Cocktail();
        temp.name = "Vodka Martini";
        temp.recipes = arrayList;
        temp.uidLikes = stringArrayList;
        temp.ingredients  = inList;
    }

    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
    public void uploadPostToFirebase() {
        final DatabaseReference root = FirebaseDatabase.getInstance().getReference("Cocktails");
        final StorageReference reference = FirebaseStorage.getInstance().getReference();

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
        fileRef.putFile(mImageUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(url -> {
            temp.url = url.toString();
            String modelId = root.push().getKey();
            temp.key = modelId; // like set key
            assert modelId != null;
            root.child(modelId).setValue(temp.toMap());
            Toast.makeText(AddActivity.this, "The post uploaded", Toast.LENGTH_SHORT).show();
        })).addOnProgressListener(snapshot -> {

        }).addOnFailureListener(e -> {
            Toast.makeText(AddActivity.this, "The upload failed", Toast.LENGTH_SHORT).show();
        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void addImage() {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            mImageUri = data.getData();
            uploadPostToFirebase();

        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnAddImage) {
            addImage();
        }
    }
}
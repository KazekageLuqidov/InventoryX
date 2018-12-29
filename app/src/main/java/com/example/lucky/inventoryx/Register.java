package com.example.lucky.inventoryx;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
public class Register extends AppCompatActivity {


        Button b1;
        LocationManager locationmanager;
        EditText et1, et2;
        DatabaseHelper db;
        FirebaseDatabase database;
        private FirebaseAuth mAuth;
        DatabaseReference reference;
        TextView tv;
        private final int RESULT_LOAD_IMAGE = 1;
        ImageView img;
        Uri imageuri, bitmapUri;
        Uri urionline = null;
        private StorageReference mstorage;
        private static final int gallery = 2;
        private ProgressBar progressBar;
        Bitmap bitmap;
        String j;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            mAuth = FirebaseAuth.getInstance();
            et1 = (EditText) findViewById(R.id.editText3);
            et2 = (EditText) findViewById(R.id.editText4);

            mstorage = FirebaseStorage.getInstance().getReference();

            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            b1 = (Button) findViewById(R.id.button4);

            img = (ImageView) findViewById(R.id.imageView2);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
              /*  Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                File image = new File(Environment.getExternalStorageDirectory(),"image1.jpg");
                imageuri = Uri.fromFile(image);

                i.putExtra(MediaStore.EXTRA_OUTPUT,imageuri);
                startActivityForResult(i, RESULT_LOAD_IMAGE);*/

                    Intent inte = new Intent(Intent.ACTION_PICK);
                    inte.setType("image/*");

                    startActivityForResult(inte, gallery);
                }
            });


            database = FirebaseDatabase.getInstance();
            reference = database.getReference();



            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email = et1.getText().toString().trim();
                    String password = et2.getText().toString().trim();

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (password.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);
                    //create user
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //Toast.makeText(Register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful())
                                    { Toast.makeText(Register.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        String N = et1.getText().toString();
                                        String P = et2.getText().toString();

                                        String q = "active";
                                        Calc a = new Calc(N, P, q);
                                        String sp = task.getResult().getUser().getUid();
                                        reference.child(sp).setValue(a);

                                        Intent h = new Intent(Register.this, MainActivity.class);
                                        startActivity(h);
                                        Toast.makeText(Register.this, "You are Successfully Registered", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }
            });
        }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == gallery && resultCode == RESULT_OK) {
                Uri uri = data.getData();
                StorageReference filepath = mstorage.child("photos").child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Register.this, "Upload done", Toast.LENGTH_SHORT).show();

                        Uri download = taskSnapshot.getDownloadUrl();
                        Picasso.with(Register.this).load(download).fit().centerCrop().into(img);

                    }
                });

        /*Uri selectedImage = data.getData();
        Log.e("uRII",imageuri.toString());
        img.setImageURI(null);
        img.setImageURI(imageuri);
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
       // Log.e("String", picturePath);

        //img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    */
            }
        }

    }


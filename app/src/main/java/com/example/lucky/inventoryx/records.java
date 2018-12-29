package com.example.lucky.inventoryx;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.util.ArrayList;

public class records extends AppCompatActivity
{
private RelativeLayout relativeLayout;
    TextView tv;
    ImageView img;

    private GoogleApiClient client;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);



        tv = (TextView) findViewById(R.id.txtside);
        mAuth = FirebaseAuth.getInstance();
        Intent u = getIntent();
      final  String w = u.getStringExtra("scr");


        tv.setText(w);



        img=(ImageView)findViewById(R.id.disp_image);

                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null){
                            Intent inte=new Intent(records.this, Details.class);
                            startActivity(inte);
                            } else {
                                Toast.makeText(records.this, "Login to view", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


            }
    public void Shayo (View view){
        Context context = getApplicationContext();
        LayoutInflater inflater = getLayoutInflater();

        View shayoRoot = inflater.inflate(R.layout.mytoast,null);
        Toast shayoToast = new Toast(context);

        shayoToast.setView(shayoRoot);
        shayoToast.setDuration(Toast.LENGTH_LONG);
        shayoToast.show();
    }





    }
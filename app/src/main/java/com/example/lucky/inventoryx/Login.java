package com.example.lucky.inventoryx;

import android.*;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
/**
 * Created by Lucky on 09/29/2017.
 */

public class Login extends AppCompatActivity
    {
        ImageView img,img1,img2;
        final Activity activity=this;
        EditText et,et1,et2,et7,et8,et9,et10,et11;
        FirebaseDatabase database;
        DatabaseReference reference;
        FirebaseAuth mAuth;
        private ListView mDrawerList;
        private ArrayAdapter<String> mAdapter;
        private DrawerLayout mDrawerLayout;
        private ImageView iv;
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_login);

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},
                    1);

            mAuth=FirebaseAuth.getInstance();

            img1 = (ImageView) findViewById(R.id.imageView3);;
            img2 = (ImageView) findViewById(R.id.imageView4);
            img = (ImageView) findViewById(R.id.imageView2);


            et7=(EditText)findViewById(R.id.editText7);
            et8=(EditText)findViewById(R.id.editText8);
            et9=(EditText)findViewById(R.id.editText9);
            et10=(EditText)findViewById(R.id.editText10);
            et11=(EditText)findViewById(R.id.editText11);

            database=FirebaseDatabase.getInstance();
            reference= database.getReference("All Barcodes");

            et=(EditText)findViewById(R.id.editText7);


            img.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String er=mAuth.getCurrentUser().getUid();
                    reference.child(er).child("status").setValue("inactive");
                    mAuth.getInstance().signOut();
                    Intent r=new Intent(Login.this,MainActivity.class);
                    startActivity(r);
                    finish();
                }
            });

            img2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String B=et7.getText().toString();
                    String PN=et8.getText().toString();
                    String D=et9.getText().toString();
                    String T=et10.getText().toString();
                    String PD=et11.getText().toString();


                    Product PO=new Product(B,PN,PD,T,D);
                    reference.child(B).setValue(PO);
                    Toast.makeText(getApplicationContext(),"Data Successfully Saved",Toast.LENGTH_LONG).show();
                    et7.setText("");
                    et8.setText("");
                    et9.setText("");
                    et10.setText("");
                    et11.setText("");
                }
            });
            img1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    IntentIntegrator in = new IntentIntegrator(activity);
                    in.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    in.setPrompt("scan");
                    in.setCameraId(0);
                    in.setBeepEnabled(true);
                    //in.setBarcodeImageEnabled(false);
                    in.initiateScan();
                }
            });
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if(result!=null)
            {
                if(result.getContents()==null)
                {
                    Log.d("MainActivity","Cancelled scan");
                    Toast.makeText(this,"Cancelled",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Log.d("MainActivity","Scanned");


/*reference.child(result.getContents()).addValueEventListener(new ValueEventListener()
{
    @Override
    public void onDataChange(DataSnapshot dataSnapshot)
    {
   add a= dataSnapshot.getValue(add.class);
Log.e("Name",a.Name);



    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});*/
                    et.setText(result.getContents());
                    Toast.makeText(this, "Scanned", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                super.onActivityResult(requestCode,resultCode,data);
            }
            // super.onActivityResult(requestCode, resultCode, data);
        }



        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case 1: {

                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    } else {


                        Toast.makeText(this, "Permission denied to access Camera", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }


            }
        }



    }

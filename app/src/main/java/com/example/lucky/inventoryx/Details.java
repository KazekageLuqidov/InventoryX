package com.example.lucky.inventoryx;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Details extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button b1, b2,b4;
    EditText ed;
    TextView tv, tv2;
    ImageView img, img1, img2;

    FirebaseDatabase database;

    private DatabaseReference reference;
    FirebaseAuth mAuth;
    String q;
    private DrawerLayout drl;
    private ActionBarDrawerToggle abt;
    String f1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Details");
        setContentView(R.layout.activity_details);


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                1);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        drl = (DrawerLayout) findViewById(R.id.dlayout);
        abt = new ActionBarDrawerToggle(this, drl, R.string.openDrawer, R.string.closeDrawer);
        drl.addDrawerListener(abt);

        abt.syncState();




        img = (ImageView) findViewById(R.id.imageView4);
        img2 = (ImageView) findViewById(R.id.imageView2);
        NavigationView navview = (NavigationView) findViewById(R.id.navv);
        navview.bringToFront();
        navview.setNavigationItemSelectedListener(this);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        f1 = mAuth.getCurrentUser().getEmail();
        tv2 = (TextView) findViewById(R.id.textView14);
        tv = (TextView) findViewById(R.id.textView3);
        tv2.setText(f1);
        reference = database.getReference();

        ed = (EditText) findViewById(R.id.editText12);


        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String er = mAuth.getCurrentUser().getUid();
                reference.child(er).child("status").setValue("inactive");
                mAuth.getInstance().signOut();
                Intent r = new Intent(Details.this, MainActivity.class);
                startActivity(r);
                finish();
            }
        });


        img2 = (ImageView) findViewById(R.id.imageView5);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("All Barcodes");

        final Activity activity = this;
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator in = new IntentIntegrator(activity);
                in.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                in.setPrompt("scan");
                in.setCameraId(0);
                in.setBeepEnabled(false);
                //in.setBarcodeImageEnabled(false);
                in.initiateScan();
            }
        });


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 String B = ed.getText().toString();

                reference = database.getReference("All Barcodes").child(B);


                if (TextUtils.isEmpty(B)) {
                    Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                    return;
                }

                ValueEventListener loadListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get object and use the values to update the UI
                        Product po = dataSnapshot.getValue(Product.class);
                      // String name = po.pname;
                        //String date = po.date;
                        //String desc = po.discription;
                       // String time = po.time;

                       if (po!=null){

                            String finale = tv.getText() + "VERIFIED\n" + po.pname+ "\n"+ po.date + "\n" + po.discription + "\n"+ po.time;
                        //tv.setText(finale);


                        Intent u = new Intent(Details.this, records.class);
                        u.putExtra("scr",finale);
                        startActivity(u);
                        finish();
                    }else {
                            Toast.makeText(Details.this,"Wrong Barcode", Toast.LENGTH_LONG).show();
                        }



                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("", "Error", databaseError.toException());
                        // ...
                    }
                };
                reference.addValueEventListener(loadListener);






            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (abt.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");

                reference.child(result.getContents()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
//                Product p= dataSnapshot.getValue(Product.class);
                        //              Log.e("Name",p.Pname);
                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                ed.setText(result.getContents());
                Toast.makeText(this, "Scanned", Toast.LENGTH_SHORT).show();
                // tv.setText(result.getContents());
                q = ed.getText().toString();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        drl.closeDrawers();

        switch (id) {
            case R.id.nav_q:
                Uri web = Uri.parse("http://www.bitwebdev.com.ng");
                Intent i = new Intent(Intent.ACTION_VIEW, web);
                startActivity(i);
                break;

            case R.id.logout:
                String er = mAuth.getCurrentUser().getUid();
                reference.child(er).child("status").setValue("inactive");
                mAuth.getInstance().signOut();
                Intent r = new Intent(Details.this, MainActivity.class);
                startActivity(r);

                break;
        }

        DrawerLayout d1 = (DrawerLayout) findViewById(R.id.dlayout);
        d1.closeDrawer(GravityCompat.START);
        return true;
    }




    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Details Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://bitwebdev.com.ng/blog"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }



    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

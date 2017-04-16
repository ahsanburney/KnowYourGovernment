package com.example.kulsum.knowyourgovernment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import it.sephiroth.android.library.picasso.Picasso;


public class PhotoDetailActivity extends AppCompatActivity {
    Official official =null;
    private static final String TAG = "PhotoDetailActivity";
    TextView topLocation;
    TextView officialName;
    TextView officialotherName;
    ConstraintLayout officialBackgroundImage;
    ImageView officialImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        topLocation = (TextView) findViewById(R.id.zip_state3);
        officialName = (TextView) findViewById(R.id.photoDetailActivutyPhotoName);
        officialotherName = (TextView) findViewById(R.id.photoDetailActivityName);
        officialImage = (ImageView) findViewById(R.id.photodetailActivityImage);
        officialBackgroundImage = (ConstraintLayout) findViewById(R.id.imageBackground);
        Intent i = getIntent();
        if (i.hasExtra(Official.class.getName())) {
            official = (Official) i.getSerializableExtra(Official.class.getName());
        }
        if(official !=null){
            topLocation.setText(official.getTopLocation());
            officialName.setText(official.getOfficialDetails());
            officialotherName.setText(official.getOfficialName());
            if (official.getSide().contentEquals("Democratic")){
                officialBackgroundImage.setBackgroundResource(R.color.dem);
            }else if (official.getSide().contentEquals("Republican")){
                officialBackgroundImage.setBackgroundResource(R.color.rep);
            }else{
                officialBackgroundImage.setBackgroundResource(R.color.black);
            }
            getImage(official.getImageUrl());
        }
    }
    public int networkCheck(){

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = connectivityManager.getActiveNetworkInfo();
        if (!(network != null && network.isConnectedOrConnecting())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Data cannot be loaded without an internet!");
            builder.setTitle("No Internet Connection");
            AlertDialog dialog = builder.create();
            dialog.show();
            return 0;
        }
        else {
            return 1;
        }
    }
    private void getImage(final String imageURL) {
        if(imageURL.equals(""))
            return;
        if(networkCheck()!=1)
        {
            return;
        }
        Picasso picasso = new Picasso.Builder(this)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        picasso.load(R.drawable.placeholder)
                                .into(officialImage);
                    }
                })
                .build();
        picasso.load(imageURL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(officialImage);
    }
}


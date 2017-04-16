package com.example.kulsum.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import it.sephiroth.android.library.picasso.Picasso;


public class OfficialActivity extends AppCompatActivity {
    private static final String TAG = "PhotoDetailActivity";
    TextView location;
    TextView officeDetails;
    TextView officialName;
    TextView add;
    TextView email;
    TextView side;
    TextView phone;
    TextView url;
    ImageView imageUrl;
    ImageView googleplus;
    ImageView facebook;
    ImageView twitter;
    ImageView youtube;
    Official official = null;
    ConstraintLayout backgroundPhoto;
    ConstraintLayout officeBackground;
    ScrollView scrollView1;
    String Address=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        location = (TextView) findViewById(R.id.zipandstate1);
        officeDetails = (TextView) findViewById(R.id.OfficialActivityName);
        officialName  = (TextView) findViewById(R.id.OfficialActivityOfficial);
        facebook=(ImageView) findViewById(R.id.facebookImage);
        googleplus=(ImageView) findViewById(R.id.googleplusImage);
        twitter=(ImageView) findViewById(R.id.youtubeImage);
        youtube=(ImageView) findViewById(R.id.twitterImage);
        side = (TextView) findViewById(R.id.SideName);
        add = (TextView) findViewById(R.id.addressValue);
        phone  = (TextView) findViewById(R.id.phoneValue);
        email  = (TextView) findViewById(R.id.emailValue);
        url  = (TextView) findViewById(R.id.websiteValue);
        imageUrl =(ImageView) findViewById(R.id.OfficialActivityImage);
        backgroundPhoto = (ConstraintLayout) findViewById(R.id.officialActivityBackground);
        officeBackground = (ConstraintLayout) findViewById(R.id.officeconstBackground);
        scrollView1 = (ScrollView) findViewById(R.id.scrollView);

        Intent i = getIntent();
        if (i.hasExtra(Official.class.getName())) {
            official = (Official) i.getSerializableExtra(Official.class.getName());
        }
        if(official !=null) {
            location.setText(official.getTopLocation());
            officeDetails.setText(official.getOfficialDetails());
            officialName.setText(official.getOfficialName());
            side.setText(official.getSide().equals("")?"":"("+ official.getSide()+")");
            Address = official.getLine1().equals("")?"": official.getLine1()+"\n";
            Address += official.getLine2().equals("")?"": official.getLine2()+"\n";
            Address += official.getLine3().equals("")?"": official.getLine3()+"\n";
            Address += official.getCity().equals("")?"": official.getCity()+",";
            Address += official.getState()+" "+ official.getZip();
            if(!Address.equals(" ")) {
                SpannableString string = new SpannableString(Address);
                string.setSpan(new UnderlineSpan(), 0, string.length(), 0);
                add.setText(string);
            }
            else add.setText("No Data Provided");
            phone.setText(official.getPhone().equals("")?"No Data Provided": official.getPhone());
            email.setText(official.getEmail().equals("")?"No Data Provided": official.getEmail());
            url.setText(official.getUrl().equals("")?"No Data Provided": official.getUrl());
            getImage(official.getImageUrl());

            int design = R.color.black;
            if (official.getSide().contentEquals("Democratic")){
                design = R.color.dem;
            }
            else if (official.getSide().contentEquals("Republican")){
                design = R.color.rep;
            }else{
                design = R.color.black;
            }
            backgroundPhoto.setBackgroundResource(design);
            facebook.setBackgroundColor(getResources().getColor(design));
            googleplus.setBackgroundColor(getResources().getColor(design));
            youtube.setBackgroundColor(getResources().getColor(design));
            twitter.setBackgroundColor(getResources().getColor(design));
            scrollView1.setBackgroundResource(design);
            officeBackground.setBackgroundResource(design);
            location.setBackgroundResource(R.color.purple);
            if (official.getFacebook().equals("")){
                facebook.setVisibility(View.INVISIBLE);
            }
            if (official.getTwitter().equals("")){
                twitter.setVisibility(View.INVISIBLE);
            }
            if (official.getGooglePlus().equals("")){
                googleplus.setVisibility(View.INVISIBLE);
            }
            if (official.getYouTube().equals("")){
                youtube.setVisibility(View.INVISIBLE);
            }
            if(networkCheck()==1) {

                Linkify.addLinks(url, Linkify.WEB_URLS);
                Linkify.addLinks(phone, Linkify.PHONE_NUMBERS);
                Linkify.addLinks(add, Linkify.MAP_ADDRESSES);
                Linkify.addLinks(email, Linkify.EMAIL_ADDRESSES);
                Linkify.addLinks(add, Linkify.ALL);
                add.setLinkTextColor(Color.parseColor("#FDFEFE"));
                phone.setLinkTextColor(Color.parseColor("#FDFEFE"));
                email.setLinkTextColor(Color.parseColor("#FDFEFE"));
                url.setLinkTextColor(Color.parseColor("#FDFEFE"));
            }
        }
    }
    private void getImage(final String imageURL) {
        if(imageURL.equals(""))
            return;
        Picasso picasso = new Picasso.Builder(this)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        picasso.load(R.drawable.placeholder)
                                .into(imageUrl);
                    }
                })
                .build();
        picasso.load(imageURL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(imageUrl);
    }
    public void openPhotoActivity(View v){
        if(official.getImageUrl()==""){
            return;
        }else{
            Intent i = new Intent(this, PhotoDetailActivity.class);
            i.putExtra(Official.class.getName(), official);
            startActivity(i);
        }
    }

    public void facebookClicked(View v) {
        if(official ==null)return;
        if(networkCheck()!=1)
        {
            return;
        }
        String FACEBOOK_URL = "https://www.facebook.com/"+ official.getFacebook();
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + official.getFacebook();
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    public void twitterClicked(View v) {
        if(official ==null)return;
        if(networkCheck()!=1)
        {
            return;
        }

        Intent intent = null;
        String name = official.getTwitter();
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void googlePlusClicked(View v) {
        if(official ==null)return;
        if(networkCheck()!=1)
        {
            return;
        }

        String name = official.getGooglePlus();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void youTubeClicked(View v) {
        if(official ==null)return;
        if(networkCheck()!=1)
        {
            return;
        }

        String name = official.getYouTube();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
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
    public void googleMap(View v)
    {
        if(networkCheck()!=1)
        {
            return;
        }
        if(Address.equals(" "))
            return;
        Uri intent = Uri.parse("geo:0,0?q="+ official.getLine1()+" "+ official.getLine2()+" "+ official.getLine3()+" "+ official.getCity()+" "+ official.getState()+" "+ official.getZip());
        Intent i = new Intent(Intent.ACTION_VIEW, intent);
        i.setPackage("com.google.android.apps.maps");
        startActivity(i);
    }


}



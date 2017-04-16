package com.example.kulsum.knowyourgovernment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {


    private static final String TAG = "MainActivity";
    private List<Official> officialArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AsyncLoaderTask loaderTask;

    private Locator locator;
    int count =0;
    private OfficialAdapter officialAdapter;
    private String saveLocation ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loaderTask = new AsyncLoaderTask(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        officialAdapter = new OfficialAdapter(officialArrayList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        if(networkCheck()==1) {
            locator = new Locator(this);
            count =1;
        }}

    public void receiveData(ArrayList<Official> list) {
        if(list.size()>0)
            saveLocation = list.get(0).getTopLocation();
        officialArrayList.clear();
        officialArrayList.addAll(list);
        officialAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        getMenuInflater().inflate(R.menu.menuitem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(this,AboutActivity.class) );
                return true;

            case R.id.actionPhotoDetailActivity:
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setTitle("Enter a City, State or a Zip Code:");
                final AsyncLoaderTask loaderTask1 = new AsyncLoaderTask(this);
                final EditText value = new EditText(this);
                value.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
                ab.setView(value);
                AlertDialog.Builder builder1 = ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String zipCode = value.getText().toString().trim().toUpperCase().replaceAll(", ", ",");
                        if(networkCheck()==1) {
                            loaderTask1.execute(zipCode);
                        }
                        dialog.dismiss();

                    }


                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                ab.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Official official = officialArrayList.get(pos);
        Intent i = new Intent(MainActivity.this, OfficialActivity.class);
        i.putExtra(Official.class.getName(), official);
        startActivity(i);
    }

    @Override
    public boolean onLongClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        Official official = officialArrayList.get(pos);
        Intent i = new Intent(MainActivity.this, OfficialActivity.class);
        i.putExtra(Official.class.getName(), official);
        startActivity(i);
        return false;
    }

    public int networkCheck(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = connectivityManager.getActiveNetworkInfo();
        if (!(network != null && network.isConnectedOrConnecting())) {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setMessage("Data cannot be loaded");
            b.setTitle("No Network Connection");
            AlertDialog dialog = b.create();
            dialog.show();
            return 0;
        }
        else {return 1;}
    }

    //-----------------------------GeoLocation-------------------------------------------------//

    public void setData(double lat, double lon) {
        Log.d(TAG, "setData: Lat: " + lat + ", Lon: " + lon);
        String address = doAddress(lat, lon);
    }

    private String doAddress(double latitude, double longitude) {

        List<Address> addressList = null;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {
                Log.d(TAG, "doAddress: Getting address now");
                addressList = geocoder.getFromLocation(latitude, longitude, 1);
                StringBuilder stringBuilder = new StringBuilder();
                for (Address ad : addressList) {
                    Log.d(TAG, "doLocation: " + ad);
                    stringBuilder.append("\nAddress\n\n");
                    for (int i = 0; i < ad.getMaxAddressLineIndex(); i++)
                        stringBuilder.append("\t" + ad.getAddressLine(i) + "\n");
                    stringBuilder.append("\t" + ad.getCountryName() + " (" + ad.getCountryCode() + ")\n");
                    ((TextView) findViewById(R.id.zipandstate1)).setText(ad.getAddressLine(1));
                    loaderTask.execute(ad.getPostalCode());
                }
                return stringBuilder.toString();
            } catch (IOException e) {
                Log.d(TAG, "doAddress: " + e.getMessage());
            }
        }
        Toast.makeText(this, "GeoCoder service timed out - please try again", Toast.LENGTH_LONG).show();
        return null;
    }
    public void noLocationAvailable() {
        Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        if(count ==1)
            locator.shutdown();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("Value", saveLocation);
        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);
        saveLocation =savedInstanceState.getString("Value");
        AsyncLoaderTask loaderTask1;
        loaderTask1 = new AsyncLoaderTask(this);
        loaderTask1.execute(saveLocation);
    }
}

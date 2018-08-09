package com.example.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , View.OnLongClickListener {

    private static final String TAG = "MainActivity";
    private List<Official> officialList = new ArrayList<>();
    double longitude;
    double latitude;
    private KygAdapter kygAdapter;
    private RecyclerView recyclerView;
    private String locationToSearch;
    public ArrayList<HashMap<String, String>> officesListForOfficialAvtivity = new ArrayList<>();
    HashMap<String, String> tempHash = new HashMap<>();
    HashMap<String, String> mainlocationData = new HashMap<>();
    String locationToEnter = null;
    private TextView location;
    private Locator locator;
    String firstLoc = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        location = (TextView) findViewById(R.id.loc);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        kygAdapter = new KygAdapter(officialList, this);
        recyclerView.setAdapter(kygAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            locator = new Locator(this);
            locator.determineLocation();

        } else {
            location.setText("No Data For Location");
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("No Network Connection");
            alertDialog.setMessage("Data cannot be accessed/loaded Without A Network Connection");
            alertDialog.show();
        }
    }

    public void doLatLon(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = null;

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    10);
            firstLoc = addresses.get(0).getPostalCode();
            searchLocation(firstLoc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      if(locator!=null)
        locator.shutDown();
        //gps.stopUsingGPS();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 5) {
            Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                        locator.setUpLocationManager();
                        locator.determineLocation();
                        AsyncLoaderTask alt = new AsyncLoaderTask(this);
                        alt.execute(firstLoc);
                    } else {
                        Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Calling super onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.help:
                Intent intent = new Intent(MainActivity.this, About_Activity.class);
                //intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
                startActivity(intent);
                return true;
            case R.id.location:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final EditText text = new EditText(this);
                text.setInputType(InputType.TYPE_CLASS_TEXT);
                text.setGravity(Gravity.CENTER_HORIZONTAL);

                builder.setView(text);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String locationText = text.getText().toString().trim().replaceAll(", ", ",");

                        locationToSearch = locationText;


                        if (locationText.equals("")) {
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Enter Something");
                            alertDialog.setMessage("Cannot be left blank");
                            alertDialog.show();
                        } else {
                            searchLocation(locationText);
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder.setTitle("Enter a City, State or a Zip Code:");

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void searchLocation(String locationText) {
        AsyncLoaderTask alt = new AsyncLoaderTask(this);
        alt.execute(locationText);
    }


    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks

        String tempname = null;
        String name = null;
        int pos = recyclerView.getChildLayoutPosition(v);
        Official c = officialList.get(pos);
        tempname = c.getName();
        for (int i = 0; i < officesListForOfficialAvtivity.size(); i++) {
            name = officesListForOfficialAvtivity.get(i).get("officialName");

            if (name.equals(tempname)) {
                tempHash = new HashMap<>();
                tempHash = officesListForOfficialAvtivity.get(i);
            }
        }
        Intent intent = new Intent(MainActivity.this, Official_Activity.class);
        intent.putExtra("tempHash", tempHash);
        intent.putExtra("locationData", locationToEnter);
        startActivity(intent);
    }


    public void noDataFound() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("No Data Found: ");
        alertDialog.setMessage("There is no data");
        alertDialog.show();
    }


    public void updateData(HashMap<String, String> locationData, ArrayList<HashMap<String, String>> officesList) {
        officesListForOfficialAvtivity = officesList;
        mainlocationData = locationData;
        officialList.removeAll(officialList);

        if (locationData.isEmpty()) {
            Toast.makeText(this, "Not a valid entry", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView location = (TextView) findViewById(R.id.loc);
        String citytoenter = locationData.get("city");
        String statetoenter = locationData.get("state");
        String ziptoenter = locationData.get("zip");


        if (citytoenter.isEmpty()) {
            locationToEnter = statetoenter + " " + ziptoenter;
        }
        if (statetoenter.isEmpty()) {
            locationToEnter = citytoenter + " " + ziptoenter;
        }
        if (ziptoenter.isEmpty() && !citytoenter.isEmpty()) {
            locationToEnter = citytoenter + ", " + statetoenter;
        }
        if (!citytoenter.isEmpty() && !statetoenter.isEmpty() && !ziptoenter.isEmpty()) {
            locationToEnter = citytoenter + ", " + statetoenter + " " + ziptoenter;
        }
        location.setText(locationToEnter);

        for (int i = 0; i < officesList.size(); i++) {
            String designation = officesList.get(i).get("officialName");
            String name = officesList.get(i).get("officesName");
            String party = officesList.get(i).get("party");

            officialList.add(new Official(name, designation, party));
            kygAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onLongClick(View v) {

        String tempname = null;
        String name = null;
        int pos = recyclerView.getChildLayoutPosition(v);
        Official c = officialList.get(pos);
        tempname = c.getName();
        for (int i = 0; i < officesListForOfficialAvtivity.size(); i++) {
            name = officesListForOfficialAvtivity.get(i).get("officialName");

            if (name.equals(tempname)) {
                tempHash = new HashMap<>();
                tempHash = officesListForOfficialAvtivity.get(i);
            }
        }
        Intent intent = new Intent(MainActivity.this, Official_Activity.class);
        intent.putExtra("tempHash", tempHash);
        intent.putExtra("locationData", locationToEnter);
        startActivity(intent);
        return false;
    }


}

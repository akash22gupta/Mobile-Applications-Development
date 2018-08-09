package com.example.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "";
    private ArrayList<Stock> stockList = new ArrayList<>();  // Main content is here
    private  ArrayList<TempStock> tempStockList = new ArrayList<>();
    private RecyclerView recyclerView; // Layout's recyclerview
    private SwipeRefreshLayout swiper; // The SwipeRefreshLayout
    MenuItem newStock;
    private String symboltosearch;
    private String symbolSelectedOnDialog;

    private DatabaseHandler databaseHandler;
    private static String marketwatch = "http://www.marketwatch.com/investing/stock/";
    private StockAdapter stockAdapter;
    private  TempStockAdapter tempStockAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newStock = (MenuItem) findViewById(R.id.add);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        stockAdapter = new StockAdapter(stockList, this);
        tempStockAdapter = new TempStockAdapter(tempStockList, this);

        recyclerView.setAdapter(stockAdapter);
        recyclerView.setAdapter(tempStockAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));




            swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    doRefresh();
                }
            });

            databaseHandler = new DatabaseHandler(this);
            ArrayList<Stock> list = databaseHandler.loadStocks();
            stockList.addAll(list);

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            for (int i = 0; i < stockList.size(); i++) {
                Stock temp = stockList.get(i);
                System.out.println("Inside MainActivity: Loading database:" + temp.getSymbol());
                System.out.println("Inside MainActivity: Loading database:" + temp.getName());
                dataDownload1(temp);
            }
        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("No Network Connection");
            alertDialog.setMessage("Stocks Cannot Be Updated Without A Network Connection");
            alertDialog.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                ConnectivityManager cm =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {


                   // ALL THE NEW ADD STOCK WHEN INTERNET IS THERE GOES HERE
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    final EditText et = new EditText(this);
                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                    et.setGravity(Gravity.CENTER_HORIZONTAL);

                    builder.setView(et);
                    et.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                                // ALL THE NEW ADD STOCK WHEN INTERNET IS THERE GOES HERE
                            String symbol = et.getText().toString().trim().replaceAll(", ", ",");
                            symboltosearch = symbol;
                            if(symbol.equals("")) {
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle("Please Enter a symbol");
                                alertDialog.setMessage("Cannot be left blank");
                                alertDialog.show();
                            }
                           else{
                                searchSymbol(symbol);
                            }
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

                    builder.setMessage("Please enter a Stock Symbol:");
                    builder.setTitle("STOCK SELECTION");

                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("No Network Connection");
                    alertDialog.setMessage("Stocks Cannot Be Updated Without A Network Connection");
                    alertDialog.show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

   private void searchSymbol(String sym){

       AsyncLoaderTask alt = new AsyncLoaderTask(this);
        alt.execute(sym);
   }

    public void noSymbolFound(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("No Symbol Found: " + symboltosearch );
        alertDialog.setMessage("There is no stock with "+ symboltosearch +" symbol");
        alertDialog.show();
    }

    public void noDataFound(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("No Data Found: " + symbolSelectedOnDialog );
        alertDialog.setMessage("There is no data for "+ symbolSelectedOnDialog +" symbol");
        alertDialog.show();
    }


    private void doRefresh() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            // ALL THE refresh WHEN INTERNET IS THERE GOES HERE
            ArrayList<TempStock> copy = (ArrayList<TempStock>) tempStockList.clone();
            tempStockList.clear();
            for(int i=0;i<copy.size();i++) {
                TempStock temp = copy.get(i);
                dataDownload2(temp);
            }
        // Toast.makeText(this, "You ARE Connected to the Internet!", Toast.LENGTH_SHORT).show();

        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("No Network Connection");
            alertDialog.setMessage("Stocks Cannot Be Updated Without A Network Connection");
            alertDialog.show();
        }
        swiper.setRefreshing(false);
    }


    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks

        int pos = recyclerView.getChildLayoutPosition(v);
        TempStock c = tempStockList.get(pos);
        String symbol = c.getSymbol();
        String url = marketwatch + symbol;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);
        TempStock s = tempStockList.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                databaseHandler.deleteStock(tempStockList.get(pos).getSymbol());
                tempStockList.remove(pos);
                tempStockAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.setIcon(R.drawable.ic_action_name);

        builder.setMessage("Want to delete stock: " + tempStockList.get(pos).getSymbol() + "?");
        builder.setTitle("Delete Stock");

        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }


//Adding Data to the array
    public void chooseSymbol(final ArrayList<Stock> sList) {

        // System.out.println("Array list contents : "+sList);

        int j = sList.size();
        Log.d(TAG, String.valueOf(j));
        if (j == 1) {

            //DIRECTLY ADDS TO DATABASE AND LIST ON RECYCLERVIEW
            Stock stock = sList.get(0);
            //databaseHandler.addStock(stock);
            dataDownload(stock);
        } else {
            final CharSequence[] sArray = new CharSequence[j];
            for (int i = 0; i < j; i++) {
                Stock temp = sList.get(i);
                sArray[i] = temp.getSymbol()+"-"+temp.getName();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Make a selection");

            builder.setItems(sArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //tv2.setText(sArray[which]);
                    String dataToDownload = (String) sArray[which];
                    int i;
                    for (i = 0; i < sList.size(); i++) {
                        if(sArray[i] == dataToDownload)
                            break;
                    }
                    Stock temp = sList.get(i);
                   // databaseHandler.addStock(temp);
                    dataDownload(temp);
                    // CHOOSE YOUR SYMBOL AND DO AFTER THAT
                }
            });

            builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog dialog = builder.create();

            dialog.show();
        }
    }



    public void tempchooseSymbol(TempStock sList) {

        // System.out.println("Array list contents : "+sList);

        boolean symbolExistOrNot;
            //System.out.println(sList.getSymbol() +"  "+ sList.getChange()+" " + sList.getName() + "  " + sList.getChangep() +"  "+ sList.getUpdown()+" " + sList.getValue());
            //DIRECTLY ADDS TO DATABASE AND LIST ON RECYCLERVIEW
        Stock stock = new Stock(sList.getSymbol(),sList.getName());

        symbolExistOrNot = databaseHandler.CheckIsDataAlreadyInDBorNot(stock.getSymbol());
        if(symbolExistOrNot == false) {
            databaseHandler.addStock(stock);
            tempStockList.add(sList);

            //Sort the arraylist tempStockList
            Collections.sort(tempStockList, new Comparator<TempStock>() {
                @Override
                public int compare(TempStock lhs, TempStock rhs) {
                    return lhs.getSymbol().compareTo(rhs.getSymbol());
                }
            });


            tempStockAdapter.notifyDataSetChanged();
        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setIcon(R.drawable.alert_icon);

            alertDialog.setTitle("Duplicate Symbol: " + stock.getSymbol() );
            alertDialog.setMessage("Symbol: "+ stock.getSymbol() +" already exits");
            alertDialog.show();
        }

    }


    private void dataDownload (Stock sym){
        String symbol = sym.getSymbol();
        String name = sym.getName();
        symbolSelectedOnDialog = sym.getSymbol();

        AsyncDataLoader alt = new AsyncDataLoader(this);
        alt.execute(symbol ,name);
    }

    private void dataDownload1 (Stock sym){
        //String symbol = sym.getSymbol();
        //String name = sym.getName();


        AsyncDataLoaderForOnStart alt = new AsyncDataLoaderForOnStart(this,sym);
        alt.execute();
    }

    private void dataDownload2 (TempStock sym){
       String symbol = sym.getSymbol();
        String name = sym.getName();
      //  symbolSelectedOnDialog = symbol;
        AsyncDataLoaderForSwipe alt = new AsyncDataLoaderForSwipe(this,new Stock(symbol,name));
        alt.execute();
    }

    public void tempchooseSymbol1(TempStock sList) {

        tempStockList.add(sList);
        Collections.sort(tempStockList, new Comparator<TempStock>() {
            @Override
            public int compare(TempStock lhs, TempStock rhs) {
                return lhs.getSymbol().compareTo(rhs.getSymbol());
            }
        });
            tempStockAdapter.notifyDataSetChanged();

    }
    public void tempchooseSymbol2(TempStock sList) {

        tempStockList.add(sList);
        Collections.sort(tempStockList, new Comparator<TempStock>() {
            @Override
            public int compare(TempStock lhs, TempStock rhs) {
                return lhs.getSymbol().compareTo(rhs.getSymbol());
            }
        });
        tempStockAdapter.notifyDataSetChanged();
    }

}


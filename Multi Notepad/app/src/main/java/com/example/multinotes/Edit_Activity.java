package com.example.multinotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.id;
import static android.R.attr.title;

/**
 * Created by agupt on 2/25/2017.
 */

public class Edit_Activity extends AppCompatActivity {

    private static final String TAG = "Edit_Activity" ;
    EditText title;
    EditText notes;
    Lastnote note;
    String prevnote;
    String prevtitle;
    MenuItem savebutton;

    private static final int edit_REQ = 10;
    List<Lastnote> noteList = new ArrayList<>();
    int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        title = (EditText) findViewById(R.id.title);
        notes = (EditText) findViewById(R.id.main_notes);
        savebutton = (MenuItem) findViewById(R.id.menu_save);

        Intent intent = this.getIntent();
        if (intent != null) {
            if (String.valueOf(intent.getStringExtra("position")) != null)
                if (intent.getParcelableArrayListExtra("list") != null) {
                    pos = intent.getIntExtra("position", pos);
                    noteList = intent.getParcelableArrayListExtra("list");
                    prevnote=noteList.get(pos).getnotes();
                    prevtitle=noteList.get(pos).gettitle();
                    title.setText(noteList.get(pos).gettitle());
                    notes.setText(noteList.get(pos).getnotes());
                }
        }
    }


    public void writeNotes(JsonWriter writer, Lastnote n) throws IOException {
        writer.beginObject();
        writer.name("id").value(n.getId());
        writer.name("title").value(n.gettitle());
        writer.name("notes").value(n.getnotes());
        writer.name("date").value(n.getdatetime());
        writer.endObject();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }


   /* @Override
    protected void onResume() {
        super.onStart();
        Log.d(TAG, "onResume: ");
        note = loadFile();  // Load the JSON containing the product data - if it exists
        if (note != null) { // null means no file was loaded
            title.setText(note.gettitle());
            notes.setText(note.getnotes());
            resumed = title.getText().toString();
            resumed1 = notes.getText().toString();
        }
    }*/

   /* @Override
    protected void onPause() {
        super.onPause();
        if(!(title.equals(""))) {
            paused = title.getText().toString();
            paused1 = notes.getText().toString();
            if (!(title.equals(resumed)) || !(notes.equals(resumed1))) {
                note.settitle(title.getText().toString());
                note.setnotes(notes.getText().toString());
            }
            saveNotes();
        }
    }
*/

   /* private Lastnote loadFile() {
        note = new Lastnote();
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            JsonReader reader = new JsonReader(new InputStreamReader(is, getString(R.string.encoding)));
            reader.beginObject();
            while (reader.hasNext()) {
                String date = reader.nextName();
                if (date.equals("title")) {

                    //  Log.d(TAG, "in load file  "+reader.nextString());
                    note.settitle(reader.nextString());

                }
                else if (date.equals("notes")) {
                    note.setnotes(reader.nextString());
                }
                else {
                    reader.skipValue();
                }
            }
            reader.endObject();

        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.New), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "NOTE VALUE: "+note.toString() );
        return note;
    }*/

    private void saveNotes() {

        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("title").value(note.gettitle());
            writer.name("notes").value(note.getnotes());
            writer.endObject();
            writer.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        title.setText(savedInstanceState.getString("Title"));
        notes.setText(savedInstanceState.getString("Notes"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("Title", title.getText().toString());
        outState.putString("Notes", notes.getText().toString());
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                String temp = title.getText().toString();
                String temp1 = notes.getText().toString();
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


                if(!TextUtils.isEmpty(temp)) {
                    if ((!temp1.equals(prevnote) || !temp.equals(prevtitle))  && prevnote!=null) {
                        Intent data1 = new Intent();
                        data1.putExtra("notesChanged", true);
                        data1.putExtra("NEWNOTE", notes.getText().toString());
                        data1.putExtra("NEWTITLE",title.getText().toString());
                        data1.putExtra("position_new", pos);
                        data1.putExtra("datetime", currentDateTimeString);
                        setResult(RESULT_OK, data1);
                        Toast.makeText(getBaseContext(), "Note Saved", Toast.LENGTH_SHORT).show();
                        Edit_Activity.this.finish();
                    } else
                    if(temp1.equals(prevnote) && temp.equals(prevtitle))
                        Edit_Activity.this.finish();
                    else{
                        Intent data = new Intent();
                        data.putExtra("TITLE", title.getText().toString());
                        data.putExtra("NOTE", notes.getText().toString());
                        setResult(RESULT_OK, data);
                        Edit_Activity.this.finish(); // when click OK button, finish current activity!
                        Toast.makeText(getBaseContext(), "Note Saved", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                if(TextUtils.isEmpty(temp1) && TextUtils.isEmpty(temp)) {

                        Toast.makeText(getBaseContext(), "No Notes to save", Toast.LENGTH_SHORT).show();
                        Edit_Activity.this.finish();
                    }
                else
                {
                    Toast.makeText(getBaseContext(), "Un-titled note cannot be saved", Toast.LENGTH_SHORT).show();
                    Edit_Activity.this.finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { // this is override method
        if(keyCode == KeyEvent.KEYCODE_BACK){
            String temp = title.getText().toString();
            String temp1 = notes.getText().toString();

            if(!TextUtils.isEmpty(temp)) {
                if ((!temp1.equals(prevnote) || !temp.equals(prevtitle)) && prevnote!=null)
                    showExitConfirmDialog1();
                else
                    if(temp1.equals(prevnote) && temp.equals(prevtitle))
                        Edit_Activity.this.finish();
                    else
                    showExitConfirmDialog();

            }
            else
            if(TextUtils.isEmpty(temp1) && TextUtils.isEmpty(temp))
            {
                Toast.makeText(getBaseContext(), "No Notes to save", Toast.LENGTH_SHORT).show();
                Edit_Activity.this.finish();
            }
            else
            {
                Toast.makeText(getBaseContext(), "Un-titled note cannot be saved", Toast.LENGTH_SHORT).show();
                Edit_Activity.this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showExitConfirmDialog(){ // just show an dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Changes Made"); // set title
        dialog.setMessage("Want to save?"); // set message
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            Intent data = new Intent();
                            data.putExtra("TITLE", title.getText().toString());
                            data.putExtra("NOTE", notes.getText().toString());
                            setResult(RESULT_OK, data);
                            Edit_Activity.this.finish(); // when click OK button, finish current activity!
                            Toast.makeText(getBaseContext(), "Note Saved", Toast.LENGTH_SHORT).show();

                    }
                });
        dialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Edit_Activity.this.finish();
                        Toast.makeText(getBaseContext(), "Note Not Saved", Toast.LENGTH_SHORT).show(); // just show a Toast, do nothing else
                    }
                });
        dialog.create().show();
    }


    public void showExitConfirmDialog1(){ // just show an dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Changes Made"); // set title
        dialog.setMessage("Want to save?"); // set message
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String temp = title.getText().toString();
                        String temp1 = notes.getText().toString();
                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                        Intent data1 = new Intent();
                        data1.putExtra("notesChanged", true);
                        data1.putExtra("NEWNOTE", notes.getText().toString());
                        data1.putExtra("NEWTITLE",title.getText().toString());
                        data1.putExtra("position_new", pos);
                        data1.putExtra("datetime", currentDateTimeString);
                        setResult(RESULT_OK, data1);
                        Toast.makeText(getBaseContext(), "Note Saved", Toast.LENGTH_SHORT).show();
                        Edit_Activity.this.finish();

                    }
                });
        dialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Edit_Activity.this.finish();
                        Toast.makeText(getBaseContext(), "Note Not Saved", Toast.LENGTH_SHORT).show(); // just show a Toast, do nothing else
                    }
                });
        dialog.create().show();
    }








    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == edit_REQ) {
            if (resultCode == RESULT_OK) {
                String text = data.getStringExtra("title");
                String text1=data.getStringExtra("notes");

                title.setText(text);
                notes.setText(text1);
            } else {
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }

        } else {
            Log.d(TAG, "onActivityResult: Request Code " + requestCode);
        }
    }

}

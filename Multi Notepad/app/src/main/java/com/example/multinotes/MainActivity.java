package com.example.multinotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private ArrayList<Lastnote> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter mAdapter;
    private static final int newNote_Req = 1;
    private static final int edit_REQ = 10;
    int id=-1;
    private ArrayList<Lastnote> lists = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        mAdapter = new NotesAdapter(noteList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new MyAsyncTask().execute();

    }



    class MyAsyncTask extends AsyncTask<String,String,String>

    { //  <Parameter, Progress, Result>
        boolean fileAvailable=false;
        @Override
        protected String doInBackground(String...params) {
        try {
            if (getApplicationContext().openFileInput(getString(R.string.file_name)).available() != 0) {

                InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
                lists = readJsonStream(is);
                if (noteList.size() == 0) {
                    for (int i = 0; i < lists.size(); i++) {
                        noteList.add(lists.get(i));
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        return null;
    }

        @Override
        protected void onPostExecute(String s) {
        //  super.onPostExecute();
        Context context = getApplicationContext();
       // if(fileAvailable==true)
        //    Toast.makeText(context,"JSON FILE LOADED",Toast.LENGTH_SHORT).show();
    //    else
          //w  Toast.makeText(context,"NO JSON FILE TO LOAD",Toast.LENGTH_SHORT).show();
    }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks

        int pos = recyclerView.getChildLayoutPosition(v);
        Lastnote m = noteList.get(pos);
        Intent a = new Intent(MainActivity.this, Edit_Activity.class);
        a.putExtra("position",pos);
        a.putParcelableArrayListExtra("list",noteList);
        startActivityForResult(a,edit_REQ);
    }


    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks// just show an dialog
        final int pos = recyclerView.getChildLayoutPosition(v);
        Lastnote m = noteList.get(pos);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Want to delete?"); // set title
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noteList.remove(pos); // when click OK button, finish current activity!
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
        dialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "Note Not Deleted", Toast.LENGTH_SHORT).show(); // just show a Toast, do nothing else
                    }
                });
        dialog.create().show();
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_info:
                Intent intent = new Intent(MainActivity.this, About_Activity.class);
                //intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
                startActivity(intent);
                return true;
            case R.id.menu_add:
                Intent intent1 = new Intent(MainActivity.this, Edit_Activity.class);
                startActivityForResult(intent1, newNote_Req);
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





    public ArrayList<Lastnote> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readNotesArray(reader);
        } finally {
            reader.close();
        }
    }

    public ArrayList<Lastnote> readNotesArray(JsonReader reader) throws IOException {
        ArrayList<Lastnote> list = new ArrayList<Lastnote>();

        reader.beginArray();
        while (reader.hasNext()) {
            list.add(readNote(reader));
        }
        reader.endArray();
        return list;
    }

    public Lastnote readNote(JsonReader reader) throws IOException {
        long id = -1;
        String text = null;

        List<Double> geo = null;
        Lastnote n = new Lastnote();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                n.setId(reader.nextInt());
            } else if (name.equals("title")) {
                n.settitle(reader.nextString());
            } else if (name.equals("notes") ) {
                n.setnotes(reader.nextString());
            } else if (name.equals("datetime")) {
                n.setdatetime(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return n;
    }


    @Override
    protected void onPause() {
        super.onPause();

        saveNotes();
    }

    private void saveNotes() {


        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            JSONArray jarray = new JSONArray(noteList);
            writer.setIndent("  ");
            writer.beginArray();
            for(int i=0;i<jarray.length();i++) {
                writer.beginObject();

                writer.name("id").value(noteList.get(i).getId());
                writer.name("title").value(noteList.get(i).gettitle());
                writer.name("notes").value(noteList.get(i).getnotes());
                writer.name("datetime").value(noteList.get(i).getdatetime());

                writer.endObject();

            }

            writer.endArray();
            writer.close();


            for(int i=0;i<jarray.length();i++) {
                StringWriter sw = new StringWriter();
                writer = new JsonWriter(sw);
                writer.setIndent("  ");
                writer.beginObject();
                writer.name("id").value(noteList.get(i).getId());
                writer.name("title").value(noteList.get(i).gettitle());
                writer.name("notes").value(noteList.get(i).getnotes());
                writer.name("datetime").value(noteList.get(i).getdatetime());
                writer.endObject();
                writer.close();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == newNote_Req) {
            if (resultCode == RESULT_OK) {

                String text = data.getStringExtra("TITLE");
                String text1 = data.getStringExtra("NOTE");

                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                if(!TextUtils.isEmpty(text)) {
                    id++;
                    noteList.add(0,new Lastnote(id,text,text1,currentDateTimeString)); //sort according to date
                    mAdapter.notifyDataSetChanged();
                }

            }

        }

        else if(requestCode==edit_REQ) {
            if (data != null) {
                boolean b = data.getBooleanExtra("notesChanged", false);
                if (b == true) {
                    int position = data.getIntExtra("position_new", -1);
                    String newnote = data.getStringExtra("NEWNOTE");
                    String newtitle = data.getStringExtra("NEWTITLE");
                    String date = data.getStringExtra("datetime");
                    String newnote2 = null;
                    if (position != -1) {
                        if (newnote.length() > 80) {
                            newnote2 = newnote.substring(0, 79) + "...";
                            noteList.get(position).setnotes(newnote2);
                            mAdapter.notifyDataSetChanged();
                        }

                        noteList.get(position).setdatetime(date);

                        noteList.add(0, noteList.get(position));
                        noteList.remove(position + 1);
                        mAdapter.notifyDataSetChanged();
                        noteList.get(0).setnotes(newnote);
                        noteList.get(0).settitle(newtitle);
                    }

                }

            }
        }

    }
}

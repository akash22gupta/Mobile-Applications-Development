package com.example.notepad;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Xml;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    SharedPreferences prefs = null;
    private static final String TAG = "MainActivity";
    private TextView datetime;
    private EditText notes;
    private Lastnote lastnote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("com.example.notepad", MODE_PRIVATE);

        Log.d(TAG,"pref set");

        datetime = (TextView) findViewById(R.id.textView2);
        notes = (EditText) findViewById(R.id.editText3);
        //notes.setMovementMethod(new ScrollingMovementMethod());

       // notes.setTextIsSelectable(true);
    }

    @Override
    protected void onResume() {
        super.onStart();
        if (prefs.getBoolean("firstrun", true)) {
            Log.d(TAG,"under pref if");
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            datetime.setText(currentDateTimeString);
            lastnote = loadFile();
            prefs.edit().putBoolean("firstrun", false).commit();
            Log.d(TAG,"pref set to false");
        } else {
            lastnote = loadFile();
            Log.d(TAG,"last note set");
            if (lastnote != null) {
                Log.d(TAG,"last note not null");
             //   if (lastnote.getnotes() == "") {
              //      String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
             //       datetime.setText(currentDateTimeString);
             //       Log.d(TAG,"current date set if no text");
             //   }
             //   else {

                    datetime.setText(lastnote.getdatetime());
                    Log.d(TAG,"last date if there is text");
                    notes.setText(lastnote.getnotes());
                }
            }
        }



    private Lastnote loadFile()
    {
        Log.d(TAG, "loadFile: Loading XML File");
        XmlPullParserFactory xmlFactoryObject;
        lastnote = new Lastnote();
        try
        {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactoryObject.newPullParser();
            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                String name = parser.getName();
                if(name == null || !name.equals("lastnote"))
                {
                    eventType = parser.next();
                    continue;
                }
                eventType = parser.next();
                while (eventType != XmlPullParser.END_TAG)
                {
                    name = parser.getName();
                    if(name == null)
                    {
                        eventType = parser.next();
                        continue;
                    }
                    switch (name) {
                        case "datetime":
                            lastnote.setdatetime(parser.nextText());
                            break;
                        case "notes":
                            lastnote.setnotes(parser.nextText());
                            break;
                        default:
                    }
                    eventType = parser.next();
                }
            }
            return lastnote;
        }
        catch (FileNotFoundException e)
        {

            Toast.makeText(this, getString(R.string.no_notes), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return lastnote;
    }

    @Override
    protected void onPause() {
        super.onPause();

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        lastnote.setdatetime(currentDateTimeString);

        lastnote.setnotes(notes.getText().toString());
        savenote();
    }


    private void savenote() {

        Log.d(TAG, "savenote: Saving XML File");

        try {

            StringWriter writer = new StringWriter();

            XmlSerializer xmlSerializer = Xml.newSerializer();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "lastnotes");
            xmlSerializer.startTag(null, "lastnote");
            if (lastnote != null) {
                xmlSerializer.startTag(null, "datetime");
                xmlSerializer.text("Last Update: " + lastnote.getdatetime());
                xmlSerializer.endTag(null, "datetime");
                xmlSerializer.startTag(null, "notes");
                xmlSerializer.text(lastnote.getnotes());
                xmlSerializer.endTag(null, "notes");
            }
            xmlSerializer.endTag(null, "lastnote");
            xmlSerializer.endTag(null, "lastnotes");
            xmlSerializer.endDocument();
            xmlSerializer.flush();

            FileOutputStream fos =
                    getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            fos.write(writer.toString().getBytes());
            fos.close();

                Toast.makeText(this, getString(R.string.notes_saved), Toast.LENGTH_SHORT).show();

        }
        catch (Exception e)
        {
            e.getStackTrace();
        }

    }

}

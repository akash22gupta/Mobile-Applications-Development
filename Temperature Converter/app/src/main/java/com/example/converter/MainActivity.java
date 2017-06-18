package com.example.converter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText input1;
    private Button button;
    private RadioButton cbutton;
    private Float getf, getc, cconvert, fconvert;
    private RadioButton fbutton;
    private TextView res, history;
    private Toast mtoast;
     


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final View view = this.getCurrentFocus();

        // Bind variables to the screen widgets
        input1 = (EditText) findViewById(R.id.input);
        button = (Button) findViewById(R.id.button);
        cbutton = (RadioButton) findViewById(R.id.f2c);
        fbutton = (RadioButton) findViewById(R.id.c2f);
        res = (TextView) findViewById(R.id.result);
        history = (TextView) findViewById(R.id.History);
        input1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

        button.setOnClickListener( new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                history.setMovementMethod(new ScrollingMovementMethod());
                String s1 = new String("#F to C: ");
                String s2 = new String("#C to F: ");
                String s3 = new String(" -> ");
                String historyText = history.getText().toString();

                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(1);

                if(cbutton.isChecked())
                {
                    String getf1 =  input1.getText().toString();

                    if(!getf1.trim().isEmpty())
                    {
                        getf = Float.valueOf(input1.getText().toString());
                        cconvert = (getf - 32.0f) * 5.0f / 9.0f;
                        float ans = Float.valueOf(cconvert.toString());
                        res.setText(df.format(ans));
                        String res1 = res.getText().toString();
                        history.setText(s1 + getf1 + s3 + res1 + "\n" + historyText);

                        InputMethodManager mgr= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(input1.getWindowToken(),0);
                    }
                    else
                    {
                        if(mtoast != null)
                            mtoast.cancel();
                        mtoast = Toast.makeText(getApplicationContext(), "Enter a Value!", Toast.LENGTH_SHORT);
                        mtoast.show();
                    }
                }
                else if (fbutton.isChecked()) {
                    String getc1 = input1.getText().toString();
                    if (!getc1.trim().isEmpty())
                    {
                        getc = Float.valueOf(input1.getText().toString());
                      fconvert = (getc * 9.0f / 5.0f) + 32.0f;
                      float ans = Float.valueOf(fconvert.toString());
                     res.setText(df.format(ans));
                     String res2 = res.getText().toString();
                     history.setText(s2 + getc1 + s3 + res2 + "\n" + historyText);
                        InputMethodManager mgr= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(input1.getWindowToken(),0);
                }
                    else
                    {
                        if(mtoast != null)
                            mtoast.cancel();
                        mtoast = Toast.makeText(getApplicationContext(), "Enter a Value!", Toast.LENGTH_SHORT);
                        mtoast.show();
                    }
                }

            }


        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("HISTORY", history.getText().toString());
        outState.putString("Result", res.getText().toString());

        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);

        history.setText(savedInstanceState.getString("HISTORY"));
        res.setText(savedInstanceState.getString("Result"));
    }
}

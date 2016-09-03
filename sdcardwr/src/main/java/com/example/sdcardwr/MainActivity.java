package com.example.sdcardwr;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
final  String MyFilename = "test.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button_write = (Button)this.findViewById(R.id.buttonWrite);
        Button button_read = (Button)this.findViewById(R.id.buttonRead);
        button_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutputStream outputStream = null;
                try{
                    if (Environment.getExternalStorageState().
                            equals(Environment.MEDIA_MOUNTED)){
                        File file = Environment.getExternalStorageDirectory();
                        File myfile = new File(file.getCanonicalPath()+"/"+MyFilename);
                        FileOutputStream fileOutputStream = new FileOutputStream(myfile);
                        outputStream = new BufferedOutputStream(fileOutputStream);
                        String content = " sd file write&read";
                        try{
                            outputStream.write(content.getBytes(StandardCharsets.UTF_8));
                        }finally {
                            if(outputStream!=null)
                                outputStream.close();
                        }

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        button_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream inputStream = null;
                try{
                    if (Environment.getExternalStorageState().
                            equals(Environment.MEDIA_MOUNTED)){
                        File file = Environment.getExternalStorageDirectory();
                        File myfile = new File(file.getCanonicalPath()+"/"+MyFilename);
                        FileInputStream fileInputStream = new FileInputStream(myfile);
                        inputStream = new BufferedInputStream(fileInputStream);
                        int c;
                        StringBuilder stringBuilder = new StringBuilder("");
                        try{
                            while((c= inputStream.read())!=-1) {
                                stringBuilder.append((char)c);
                            }
                            Toast.makeText(MainActivity.this,stringBuilder.toString(),
                                    Toast.LENGTH_LONG).show();
                        }finally {
                            if(inputStream!=null)
                                inputStream.close();
                        }

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.example.cboxandrbtn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView txtResult = (TextView) findViewById(R.id.textResult);
        final TextView textView = (TextView)findViewById(R.id.mytext);
        final TextView textView1 = (TextView)findViewById(R.id.textView);
        Button button = (Button)findViewById(R.id.button);
        CheckBox cbMi = (CheckBox) findViewById(R.id.CheckMi);
        CheckBox cbHuawei = (CheckBox) findViewById(R.id.CheckHuawei);
        CheckBox cbMeizu = (CheckBox) findViewById(R.id.CheckMeizu);
        CheckBox cbSmartisan = (CheckBox) findViewById(R.id.CheckSmartisan);
        RadioButton rb = (RadioButton) findViewById(R.id.male);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText("be edited");
            }
        });

        cbHuawei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    txtResult.setText("你选择了华为");
                else
                    txtResult.setText("请选择华为");

            }
        });

        rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    textView.setText("你的性别是男");
                else
                    textView.setText("你的性别是女");
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

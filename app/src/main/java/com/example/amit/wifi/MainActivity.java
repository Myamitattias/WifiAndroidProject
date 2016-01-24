package com.example.amit.wifi;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.net.wifi.*;
import android.widget.CompoundButton;
import android.widget.Switch;
import java.util.List;
import android.view.View.OnContextClickListener;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Button Buttonon = (Button) findViewById(R.id.buttonOn);
        Button Buttonoff = (Button) findViewById(R.id.buttonOff);
        Button buttonScan = (Button) findViewById(R.id.buttonScan);
        final Switch wifi = (Switch) findViewById(R.id.wifibutton);
        final TextView wifiText = (TextView) findViewById(R.id.textView);
        final WifiManager my = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> wireless = my.getScanResults();
        Buttonon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        my.setWifiEnabled(true);
                    }
                }
        );
        Buttonoff.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        my.setWifiEnabled(false);
                    }
                }
        );

        buttonScan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        my.startScan();
                        List<ScanResult> wireless = my.getScanResults();
                    }
                }
        );
        wifi.setChecked(true);
        wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    my.setWifiEnabled(true);
                    wifi.setText("Wifi on");
                }
                else{
                    my.setWifiEnabled(false);
                    wifi.setText("Wifi Off");
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

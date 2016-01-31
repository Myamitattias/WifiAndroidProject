package com.example.amit.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.net.wifi.*;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //region CONSTANTS

    private static final String TAG = "wifi";
    TextView text ;
    Button scan;
    WifiManager wifi;
    TextView strong;
    BroadcastReceiver receiver;
    ListView wifis;
    WifiInfo info;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        scan = (Button) findViewById(R.id.buttonScan);
        text = (TextView) findViewById(R.id.textView);
        strong = (TextView) findViewById(R.id.strong);
        wifis = (ListView) findViewById(R.id.wifis);
        final Switch wifiSwich = (Switch) findViewById(R.id.wifibutton);
        info = wifi.getConnectionInfo();
        text.append("\n\n wifi :" + info.toString());
        scan.setOnClickListener(this);
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        List<WifiConfiguration> configurations = wifi.getConfiguredNetworks();
        for (WifiConfiguration configuration : configurations) {
            text.append("\n\n" + configuration.toString());
        }

        if (receiver == null)
            receiver = new WifiScaner(this);
        registerReceiver(receiver, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        Log.d(TAG, "onCreate");

    //region  WIFI SWITCH
        if(wifi.isWifiEnabled() == true)
            wifiSwich.setChecked(true);
        else
            wifiSwich.setChecked(false);
        wifiSwich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wifi.setWifiEnabled(true);
                    wifiSwich.setText("Wifi on");
                } else {
                    wifi.setWifiEnabled(false);
                    wifiSwich.setText("Wifi Off");
                }

            }
        });
        //endregion
    }

    @Override
    protected void onStop() {
        unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),"Scan Wifi",0).show();
        if (v.getId()== R.id.buttonScan){
            Log.d(TAG,"onCreate() wifi.Startscan");
            wifi.startScan();
        }
    }

}



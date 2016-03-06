package com.example.amit.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    TextView text ;
    Button scan;
    WifiManager wifi;
    TextView strong;
    BroadcastReceiver receiver;
    ListView wifi_listview;
    WifiInfo info;
    Button strongest;
    TextView firstmassege;
    ConnectivityManager isconnected;
    Button current;
    TextView connectText;
    Button settingbutton;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        //region set all the widget
        scan = (Button) findViewById(R.id.buttonScan);
        text = (TextView) findViewById(R.id.textView);
        strong = (TextView) findViewById(R.id.strong);
        strongest = (Button) findViewById(R.id.Strongest);
        wifi_listview = (ListView)findViewById(R.id.Wifi_listview);
        firstmassege = (TextView) findViewById(R.id.firstmassage);
        final Switch wifiSwich = (Switch) findViewById(R.id.wifibutton);
        isconnected = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        current = (Button) findViewById(R.id.currentbutton);
        connectText = (TextView) findViewById(R.id.connectText);
        settingbutton = (Button) findViewById(R.id.settingbutton);
        //endregion

        settingbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.activity_setting);
                    }
                }
        );

        //region set the wifi service , get connection info and configurations
        scan.setOnClickListener(this);
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        info = wifi.getConnectionInfo();
        text.append("\n\n wifi :" + info.toString());
        List<WifiConfiguration> configurations = wifi.getConfiguredNetworks();
        for (WifiConfiguration configuration : configurations) {
            text.append("\n\n" + configuration.toString());
        }
        //endregion

        if(isconnected.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting())
            connectText.setText("Connect!");

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

        //region register reciver and activate WifiScaner
        if (receiver == null)
            receiver = new WifiScaner(this);
        registerReceiver(receiver, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        //endregion



    }

    @Override
    //signout fron reviver when app is off
    protected void onStop() {
        unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    //start scan when clicking
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),"Scan Wifi",0).show();
        if (v.getId()== R.id.buttonScan){
            wifi.startScan();
        }
    }


}



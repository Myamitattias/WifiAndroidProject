package com.example.amit.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.List;

/**
 * Created by amit on 25/01/2016.
 */

public class WifiScaner extends BroadcastReceiver {
    MainActivity main;

    public WifiScaner(MainActivity main) {
        super();
        this.main = main;
    }

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        //region CONSTANTS
        final List<ScanResult> results = main.wifi.getScanResults();
        final ScanResult[] strongest = {null};
        int wifi_list_length = results.size();
        String[] wifi_name = new String[wifi_list_length];
        //endregion

        //region puts all scan result into an array and show on listview
        for (int i = 0; i < results.size(); i++)
            wifi_name[i] = results.get(i).SSID+"\n"+results.get(i).BSSID.toString()+"\n"+results.get(i).level;
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(main , R.layout.wifis_names ,wifi_name);
        main.wifi_listview.setAdapter(adapter);
        //endregion


        //region  BUTTON THAT SHOW THE STRONGEST WIFI
        main.strongest.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (ScanResult result : results){
                            if(strongest[0] == null || WifiManager.calculateSignalLevel(strongest[0].level , result.level)<0)
                                strongest[0] = result;
                        }
                        main.strong.setText(strongest[0].SSID);
                    }
                }
        );
        //endregion
        }


}
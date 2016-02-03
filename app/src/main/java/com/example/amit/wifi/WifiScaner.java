package com.example.amit.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.app.AlertDialog;
import android.widget.EditText;


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


    private String password = "";
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        //region CONSTANTS
        final List<ScanResult> results = main.wifi.getScanResults();
        final ScanResult[] strongest = {null};
        int wifi_list_length = results.size();
        final String[] wifi_names = new String[wifi_list_length];
        final List<WifiConfiguration> configurations = main.wifi.getConfiguredNetworks();
        //endregion


        //region puts all scan result into an array and show on listview
        for (int i = 0; i < results.size(); i++)
            wifi_names[i] = results.get(i).SSID+"\n"+results.get(i).BSSID.toString()+"\n"+results.get(i).level;
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(main , R.layout.wifis_names ,wifi_names);
        main.wifi_listview.setAdapter(adapter);
        //endregion

        main.wifi_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String ssid = results.get(position).SSID;
                AlertDialog.Builder builder = new AlertDialog.Builder(main);
                final EditText input = new EditText(main);
                input.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                builder.setTitle("Connection" + ssid);
                builder.setMessage("To connect the wifi , Enter the password");
                builder.setPositiveButton("CONNECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        password = input.getText().toString();
                        WifiConfiguration configuration = new WifiConfiguration();
                        configuration.SSID = "\""+ssid+"\"";
                        configuration.preSharedKey = "\""+password+"\"";
                        main.wifi.addNetwork(configuration);

                        for (WifiConfiguration configuration1 : configurations) {
                            if (configuration1.SSID != null && configuration1.SSID.equals("\"" + ssid + "\"")) {
                                main.wifi.disconnect();
                                main.wifi.enableNetwork(configuration1.networkId, true);
                                main.wifi.reconnect();
                                break;
                            }
                        }
                    }
                });
                builder.show();
            }

        });




        //region  BUTTON THAT SHOW THE STRONGEST WIFI
        main.strongest.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (ScanResult result : results) {
                            if (strongest[0] == null || WifiManager.calculateSignalLevel(strongest[0].level, result.level) < 0)
                                strongest[0] = result;
                        }
                        main.strong.setText(strongest[0].SSID);
                    }
                }
        );
        //endregion
        }


}
package com.example.amit.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.InputType;
import android.net.ConnectivityManager;
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
        final WifiConfiguration configuration = new WifiConfiguration();
        String currentSSID = null;
        boolean isWifi;
        //endregion

        main.firstmassege.setVisibility(View.INVISIBLE);


        if (main.isconnected.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting())
            isWifi = true;

        else isWifi = false;
        WifiInfo currentWifi = main.wifi.getConnectionInfo();
        if (isWifi == true)
            currentSSID = currentWifi.getSSID();
        //region puts all scan result into an array and show on listview
        for ( int i = 0; i < results.size(); i++) {
            wifi_names[i] = results.get(i).SSID + "\n" + results.get(i).BSSID.toString() + "\n" + results.get(i).level
                    + "\n"+results.get(i).capabilities;
            if (currentSSID != null) {
                if (currentSSID.equals(results.get(i).SSID)) {
                    wifi_names[i] = wifi_names[i] + "\nconnect !";
                } else
                    continue;
            }
            else continue;
        }

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(main , R.layout.wifis_names ,wifi_names);
        main.wifi_listview.setAdapter(adapter);
        //endregion


        //region this method is when clicking some wifi from the list
        main.wifi_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String ssid = results.get(position).SSID;

                //region setup PopUp box for entering the password
                final AlertDialog.Builder builder = new AlertDialog.Builder(main);
                final EditText input = new EditText(main);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                builder.setTitle("Connection" + ssid);
                builder.setMessage("To connect the wifi , Enter the password");
                //endregion

                //region build the connect button
                builder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        password = input.getText().toString();
                        configuration.SSID = "\"" + ssid + "\"";

                        //region check wifi security type
                        String Capabilities = results.get(position).capabilities;
                        if (Capabilities.contains("WPA")) {
                            builder.setMessage("WPA");
                            configuration.preSharedKey = "\"" + password + "\"";
                        }
                        else if (Capabilities.contains("WEP")) {
                            builder.setMessage("WEP");
                            configuration.wepKeys[0] = "\"" + password + "\"";
                            configuration.wepTxKeyIndex = 0;
                            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                        }
                        else {
                            builder.setMessage("NONE ");
                            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                        }
                        //endregion

                        //region connect to the wifi
                        main.wifi.addNetwork(configuration);

                        for (WifiConfiguration configuration1 : configurations) {
                            if (configuration1.SSID != null && configuration1.SSID.equals("\"" + ssid + "\"")) {
                                main.wifi.disconnect();
                                main.wifi.enableNetwork(configuration1.networkId, true);
                                main.wifi.reconnect();
                                break;
                            }
                        }
                        //endregion
                    }
                });
                //endregion

                //region build the cancel button
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                //endregion
                builder.show();
            }

        });
        //endregion

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
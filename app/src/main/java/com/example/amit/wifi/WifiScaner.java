package com.example.amit.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by amit on 25/01/2016.
 */
public class WifiScaner extends BroadcastReceiver {
    private static final String TAG = "WifiScanReciver";
    MainActivity main;
    public WifiScaner(MainActivity main) {
        super();
        this.main = main;

    }
    @Override
    public void onReceive(Context arg0 , Intent arg1){
        List<ScanResult>results = main.wifi.getScanResults();
        ScanResult best = null;
        for (ScanResult result : results){
            if(best == null || WifiManager.calculateSignalLevel(best.level , result.level)<0)
                best = result;
        }
        String message = String.format("%s network found.%s is the strongest" ,results.size(),best.SSID);
        Toast.makeText(main ,message,0).show();
        Log.d(TAG,"onReceive() message : "+message );

    }

}

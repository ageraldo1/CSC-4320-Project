package com.gsu.csc.batteryprophet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.TextView;

public class BatteryReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //TextView statusLabel = ((MainActivity)context).findViewById(R.id.txt_battery_status);
        //TextView percentageLabel = ((MainActivity)context).findViewById(R.id.txt_percentage);

        String action = intent.getAction();

        if (action != null && action.equals(Intent.ACTION_BATTERY_CHANGED)) {

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            String message ="";

            switch (status) {
                case BatteryManager.BATTERY_STATUS_FULL:
                    message = "Full";
                    break;
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    message = "Charging";
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    message = "Discharging";
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    message = "Not charging";
                    break;
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    message = "Unknown";
                    break;
            }

           // statusLabel.setText(message);

            // Percentage
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int percentage = level * 100 / scale;
           // percentageLabel.setText(percentage + "%");

        }


    }
}

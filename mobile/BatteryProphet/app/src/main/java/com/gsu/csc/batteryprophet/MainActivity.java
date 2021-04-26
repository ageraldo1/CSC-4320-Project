package com.gsu.csc.batteryprophet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.IntentFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BatteryProphetActivity";

    private TextView lastPooling;
    private TextView threadStatus;
    private TextView dataPoints;
    private EditText serverAddress;
    private EditText serverPort;
    private Button startButton;
    private Button stopButton;
    private Spinner spinnerInterval;

    private volatile boolean stopThread = false;
    private int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupScreenComponents();

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: startButton");

                stopThread = false;
                points = 0;
                threadStatus.setText("Running");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int sleepTime = Integer.parseInt(spinnerInterval.getSelectedItem().toString().replace("secs", "").trim()) * 1000;

                        while (true) {
                            if (stopThread == true) {
                                return;
                            }

                            try {
                                try {
                                    String msg = getBatterySensorData().toString();

                                    Socket s = new Socket(serverAddress.getText().toString(),
                                            Integer.parseInt(serverPort.getText().toString()));
                                    PrintWriter output = new PrintWriter(s.getOutputStream());
                                    output.write(msg);
                                    output.flush();
                                    output.close();
                                    s.close();


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Thread.sleep(sleepTime);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                                        lastPooling.setText(sdf.format(new Date()));

                                        points++;
                                        dataPoints.setText(points + " points");
                                    }
                                });


                            } catch (InterruptedException e) {
                                threadStatus.setText("Socket Error");
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: stopButton");

                stopThread = true;
                threadStatus.setText("Stopped");

            }
        });

    }

    private void setupScreenComponents() {
        /* TextView */
        lastPooling = findViewById(R.id.txt_last_pool);
        threadStatus = findViewById(R.id.txt_status);
        dataPoints = findViewById(R.id.txt_data_points);

        /* interval spinner */
        spinnerInterval = findViewById(R.id.spinner_interval);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.invervals, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInterval.setAdapter(adapter);

        /* Start/Stop Buttons */
        startButton = findViewById(R.id.cmd_start);
        stopButton = findViewById(R.id.cmd_stop);

        /* Network restrictions */
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        /* EditText */
        serverAddress = findViewById(R.id.txt_server);
        serverPort = findViewById(R.id.txt_port);


    }

    private JSONObject getBatterySensorData() {
        JSONObject payload = new JSONObject();
        JSONObject fields = new JSONObject();
        JSONObject tags = new JSONObject();

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = MainActivity.this.registerReceiver(null, intentFilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int temperature = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        int voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        String batteryString = "No Data";
        if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
            batteryString = "Charging";
        }
        if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
            batteryString = "Discharging";
        }
        if (status == BatteryManager.BATTERY_STATUS_FULL) {
            batteryString = "Full";
        }
        if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
            batteryString = "Not Charging";
        }
        if (status == BatteryManager.BATTERY_STATUS_UNKNOWN) {
            batteryString = "Unknown";
        }

        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        String batterySource = "No Data";
        if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC){batterySource = "AC";}
        if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB){batterySource = "USB";}

        int BHealth = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        String batteryHealth = "No Data";
        if (BHealth == BatteryManager.BATTERY_HEALTH_COLD){batteryHealth = "Cold";}
        if (BHealth == BatteryManager.BATTERY_HEALTH_DEAD){batteryHealth = "Dead";}
        if (BHealth == BatteryManager.BATTERY_HEALTH_GOOD){batteryHealth = "Good";}
        if (BHealth == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){batteryHealth = "Over-Voltage";}
        if (BHealth == BatteryManager.BATTERY_HEALTH_OVERHEAT){batteryHealth = "Overheat";}
        if (BHealth == BatteryManager.BATTERY_HEALTH_UNKNOWN){batteryHealth = "Unknown";}
        if (BHealth == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){batteryHealth = "Unspecified Failure";}

        try {
            tags.put("status", batteryString);
            tags.put("source", batterySource);
            tags.put("health", batteryHealth);


            fields.put("level", level * 100 / (float) scale);
            fields.put("temperature", temperature / 10);
            fields.put("voltage", voltage * 0.001);

            payload.put("measurement", "battery");
            payload.put("tags", tags);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            payload.put("time", sdf.format(new Date()));
            payload.put("fields", fields);

        } catch (JSONException e) {
            Log.d(TAG, e.toString());
        }

        return payload;
    }

}



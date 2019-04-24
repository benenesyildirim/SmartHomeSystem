package com.androidsmile.labbluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final char OPENAIR = '!';
    final char CLOSEAIR = '#';
    final char OPENGARAGE = '$';
    final char CLOSEGARAGE = '%';
    final char OPENDOOR = '&';
    final char CLOSEDOOR = '*';
    final char OPENWINDOW = '+';
    final char CLOSEWINDOW = '-';
    final char OPENCURTAIN = '/';
    final char CLOSECURTAIN = '?';
    final char LEDON = '<';
    final char LEDOFF = '>';

    private boolean lampBoolean = false, garageBoolean = false, doorBoolean = false, curtainBoolean = false, windowBoolean = false, airBoolean = false, bluetoothBoolean = false;

    private BluetoothSPP bluetooth;

    private ImageButton lampButton, doorButton, curtainButton, garageButton, windowButton, airButton, bluetoothButton;
    private TextView temperatureText;

    private SensorManager sensorManager;
    private StringBuilder recDataString = new StringBuilder();
    private final int handlerState = 0;
    private Handler bluetoothIn;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getApplicationContext();
        //Sensor defaultTEMP = sensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        initView();
        temperatureText.setText("aaaa");
//        temp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int byteCount = inputStream.available();
//                if(byteCount > 0)
//                {
//                    byte[] rawBytes = new byte[byteCount];
//                    inputStream.read(rawBytes);
//                    final String string=new String(rawBytes,"UTF-8");
//                    handler.post(new Runnable() {
//                        public void run()
//                        {
//                            textView.append(string);
//                        }
//                    });
//                }
//            }
//        });


        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                        //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);                                    //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        temperatureText.setText("Data Received = " + dataInPrint);
                        int dataLength = dataInPrint.length();                            //get length of data received
                        temperatureText.setText("String Length = " + String.valueOf(dataLength));

                        if (recDataString.charAt(0) == '#')                                //if it starts with # we know it is what we are looking for
                        {
                            String sensor0 = recDataString.substring(1, 5);             //get sensor value from string between indices 1-5
                            String sensor1 = recDataString.substring(6, 10);            //same again...
                            String sensor2 = recDataString.substring(11, 15);
                            String sensor3 = recDataString.substring(16, 20);

                            temperatureText.setText(" Sensor 0 Voltage = " + sensor0 + sensor1 + sensor2 + sensor3 + "V");    //update the textviews with sensor values
                        }
                        recDataString.delete(0, recDataString.length());                    //clear all string data
                        // strIncom =" ";
                        dataInPrint = " ";
                    }
                }
            }
        };
    }

    private void initView() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        bluetooth = new BluetoothSPP(this);

        lampButton = (ImageButton) findViewById(R.id.lamp_button);
        doorButton = (ImageButton) findViewById(R.id.door_button);
        garageButton = (ImageButton) findViewById(R.id.garage_door_button);
        windowButton = (ImageButton) findViewById(R.id.window_button);
        curtainButton = (ImageButton) findViewById(R.id.curtain_button);
        airButton = (ImageButton) findViewById(R.id.air_button);
        bluetoothButton = (ImageButton) findViewById(R.id.bluetooth_button);

        temperatureText = (TextView) findViewById(R.id.temperature_text);

        lampButton.setOnClickListener(this);
        doorButton.setOnClickListener(this);
        garageButton.setOnClickListener(this);
        windowButton.setOnClickListener(this);
        curtainButton.setOnClickListener(this);
        airButton.setOnClickListener(this);
        bluetoothButton.setOnClickListener(this);

        if (!bluetooth.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onStart() {
        super.onStart();
        if (!bluetooth.isBluetoothEnabled()) {
            bluetooth.startService(BluetoothState.DEVICE_OTHER);
            bluetooth.enable();
        } else {
            if (!bluetooth.isServiceAvailable()) {
                bluetooth.setupService();
                bluetooth.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        bluetooth.stopService();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bluetooth.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetooth.setupService();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    //TODO burası temp deneme
    @SuppressLint("HandlerLeak")
    public void getTemp() {
//        Toast.makeText(this, "eedsfs", Toast.LENGTH_SHORT).show();
//        bluetoothIn = new Handler() {
//            public void handleMessage(android.os.Message msg) {
//                if (msg.what == handlerState) {                                        //if message is what we want
//                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
//                    recDataString.append(readMessage);                                    //keep appending to string until ~
//                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
//                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
//                        String dataInPrint = recDataString.substring(0, endOfLineIndex);// extract string
//
//                        temperatureText.setText(dataInPrint);
//                        Toast.makeText(MainActivity.this, dataInPrint, Toast.LENGTH_SHORT).show();
//
//                        recDataString.delete(0, recDataString.length());                    //clear all string data
//                        // strIncom =" ";
//                        dataInPrint = " ";
//                    }
//                }
//            }
//        };


//        bluetooth.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
//            public void onDataReceived(byte[] data, String message) {
//                Toast.makeText(MainActivity.this, message + String.valueOf(data), Toast.LENGTH_SHORT).show();
//            }
//        });


        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                        //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);                                    //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        temperatureText.setText("Data Received = " + dataInPrint);
                        int dataLength = dataInPrint.length();                            //get length of data received
                        temperatureText.setText("String Length = " + String.valueOf(dataLength));

                        if (recDataString.charAt(0) == '#')                                //if it starts with # we know it is what we are looking for
                        {
                            String sensor0 = recDataString.substring(1, 5);             //get sensor value from string between indices 1-5
                            String sensor1 = recDataString.substring(6, 10);            //same again...
                            String sensor2 = recDataString.substring(11, 15);
                            String sensor3 = recDataString.substring(16, 20);

                            temperatureText.setText(" Sensor 0 Voltage = " + sensor0 + sensor1 + sensor2 + sensor3 + "V");    //update the textviews with sensor values
                        }
                        recDataString.delete(0, recDataString.length());                    //clear all string data
                        // strIncom =" ";
                        dataInPrint = " ";
                    }
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == lampButton.getId()) {
            if (!lampBoolean) {
                bluetooth.send(String.valueOf(LEDON), true);
                Toast.makeText(this, "Opened", Toast.LENGTH_SHORT).show();
                lampButton.setImageResource(R.mipmap.lamp_on);
                lampBoolean = true;
            } else if (lampBoolean) {
                bluetooth.send(String.valueOf(LEDOFF), true);
                Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show();
                lampButton.setImageResource(R.mipmap.lamp_off);
                lampBoolean = false;
            }
        }
        if (view.getId() == garageButton.getId()) {
            if (!garageBoolean) {
                bluetooth.send(String.valueOf(OPENGARAGE), true);
                Toast.makeText(this, "Opened", Toast.LENGTH_SHORT).show();
                garageButton.setImageResource(R.mipmap.garage_open);
                garageBoolean = true;
            } else if (garageBoolean) {
                bluetooth.send(String.valueOf(CLOSEGARAGE), true);
                Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show();
                garageButton.setImageResource(R.mipmap.garage_close);
                garageBoolean = false;
            }
        }
        if (view.getId() == doorButton.getId()) {
            if (!doorBoolean) {
                bluetooth.send(String.valueOf(OPENDOOR), true);
                Toast.makeText(this, "Opened", Toast.LENGTH_SHORT).show();
                doorButton.setImageResource(R.mipmap.door_open);
                doorBoolean = true;
            } else if (doorBoolean) {
                bluetooth.send(String.valueOf(CLOSEDOOR), true);
                Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show();
                doorButton.setImageResource(R.mipmap.door_close);
                doorBoolean = false;
            }
        }
        if (view.getId() == windowButton.getId()) {
            if (!windowBoolean) {
                bluetooth.send(String.valueOf(OPENWINDOW), true);
                Toast.makeText(this, "Opened", Toast.LENGTH_SHORT).show();
                windowButton.setImageResource(R.mipmap.window_open);
                windowBoolean = true;
                //sendData("+");
            } else if (windowBoolean) {
                bluetooth.send(String.valueOf(CLOSEWINDOW), true);
                Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show();
                windowButton.setImageResource(R.mipmap.window_close);
                windowBoolean = false;
                //sendData("-");
            }
        }
        if (view.getId() == curtainButton.getId()) {
            if (!curtainBoolean) {
                bluetooth.send(String.valueOf(OPENCURTAIN), true);
                bluetooth.send(String.valueOf(OPENCURTAIN), true);
                Toast.makeText(this, "Opened", Toast.LENGTH_SHORT).show();
                curtainButton.setImageResource(R.mipmap.curtain_open);
                curtainBoolean = true;
                //sendData("/");
            } else if (curtainBoolean) {
                bluetooth.send(String.valueOf(CLOSECURTAIN), true);
                bluetooth.send(String.valueOf(CLOSECURTAIN), true);
                Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show();
                curtainButton.setImageResource(R.mipmap.curtains_close);
                curtainBoolean = false;
                //sendData("?");
            }
        }
        if (view.getId() == airButton.getId()) {
            if (!airBoolean) {
                bluetooth.send(String.valueOf(OPENAIR), true);
                Toast.makeText(this, "Opened", Toast.LENGTH_SHORT).show();
                airButton.setImageResource(R.mipmap.air_conditioner_open);
                airBoolean = true;
                //sendData("!");
            } else if (airBoolean) {
                bluetooth.send(String.valueOf(CLOSEAIR), true);
                Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show();
                airButton.setImageResource(R.mipmap.air_conditioner_close);
                airBoolean = false;
                //sendData("#");
            }
        }
        if (view.getId() == bluetoothButton.getId()) {
            if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
                bluetooth.disconnect();
            } else {
                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
            }

            bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
                public void onDeviceConnected(String name, String address) {
                    bluetoothButton.setImageResource(R.mipmap.bluetooth_paired);
                }

                public void onDeviceDisconnected() {
                    bluetoothButton.setImageResource(R.mipmap.bluetooth_unpaired);
                }

                public void onDeviceConnectionFailed() {
                    bluetoothButton.setImageResource(R.mipmap.bluetooth_unpaired);
                }
            });
        }
    }
}
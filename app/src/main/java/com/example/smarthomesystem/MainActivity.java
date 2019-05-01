package com.example.smarthomesystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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


    private boolean lampBoolean = false, garageBoolean = false, doorBoolean = false, curtainBoolean = false, windowBoolean = false, coolerBoolean = false, bluetoothBoolean = false;

    private BluetoothSPP bluetooth;

    private ImageButton lampButton, doorButton, curtainButton, garageButton, windowButton, coolerButton, bluetoothButton, setCoolerButton;
    private TextView otoTempText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getApplicationContext();
        initView();
    }

    private void initView() {
        bluetooth = new BluetoothSPP(this);


        lampButton = findViewById(R.id.lamp_button);
        doorButton = findViewById(R.id.door_button);
        garageButton = findViewById(R.id.garage_door_button);
        windowButton = findViewById(R.id.window_button);
        curtainButton = findViewById(R.id.curtain_button);
        coolerButton = findViewById(R.id.cooler_button);
        bluetoothButton = findViewById(R.id.bluetooth_button);
        setCoolerButton = findViewById(R.id.set_cooler_button);

        otoTempText = findViewById(R.id.oto_temp_text);

        lampButton.setOnClickListener(this);
        doorButton.setOnClickListener(this);
        garageButton.setOnClickListener(this);
        windowButton.setOnClickListener(this);
        curtainButton.setOnClickListener(this);
        coolerButton.setOnClickListener(this);
        bluetoothButton.setOnClickListener(this);
        setCoolerButton.setOnClickListener(this);


    }

    public void onStart() {
        super.onStart();
        if (!bluetooth.isBluetoothEnabled()) {
            bluetooth.startService(BluetoothState.DEVICE_OTHER);
            bluetooth.enable();
            finish();
            startActivity(getIntent());
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

            } else if (windowBoolean) {
                bluetooth.send(String.valueOf(CLOSEWINDOW), true);
                Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show();
                windowButton.setImageResource(R.mipmap.window_close);
                windowBoolean = false;

            }
        }
        if (view.getId() == curtainButton.getId()) {
            if (!curtainBoolean) {
                bluetooth.send(String.valueOf(OPENCURTAIN), true);

                Toast.makeText(this, "Opened", Toast.LENGTH_SHORT).show();
                curtainButton.setImageResource(R.mipmap.curtain_open);
                curtainBoolean = true;

            } else if (curtainBoolean) {
                bluetooth.send(String.valueOf(CLOSECURTAIN), true);

                Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show();
                curtainButton.setImageResource(R.mipmap.curtains_close);
                curtainBoolean = false;

            }
        }
        if (view.getId() == coolerButton.getId()) {
            if (!coolerBoolean) {
                bluetooth.send(String.valueOf(OPENAIR), true);
                Toast.makeText(this, "Opened", Toast.LENGTH_SHORT).show();
                coolerButton.setImageResource(R.mipmap.cooler_open);
                coolerBoolean = true;

            } else if (coolerBoolean) {
                bluetooth.send(String.valueOf(CLOSEAIR), true);
                Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show();
                coolerButton.setImageResource(R.mipmap.cooler_close);
                coolerBoolean = false;

            }
        }
        if (view.getId() == setCoolerButton.getId()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Set Cooler ");
            final String[] types = {"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
            builder.setItems(types, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    switch (which) {
                        case 0:

                            bluetooth.send(String.valueOf('A'), true);
                            coolerBoolean = true;
                            coolerButton.setImageResource(R.mipmap.cooler_open);
                            otoTempText.setText("Oto Cooler Temperature: " + types[0]);
                            break;
                        case 1:

                            bluetooth.send(String.valueOf('B'), true);
                            coolerBoolean = true;
                            coolerButton.setImageResource(R.mipmap.cooler_open);
                            otoTempText.setText("Oto Cooler Temperature: " + types[1]);
                            break;
                        case 2:

                            bluetooth.send(String.valueOf('C'), true);
                            coolerBoolean = true;
                            coolerButton.setImageResource(R.mipmap.cooler_open);
                            otoTempText.setText("Oto Cooler Temperature: " + types[2]);
                            break;
                        case 3:

                            bluetooth.send(String.valueOf('D'), true);
                            coolerBoolean = true;
                            coolerButton.setImageResource(R.mipmap.cooler_open);
                            otoTempText.setText("Oto Cooler Temperature: " + types[3]);
                            break;
                        case 4:

                            bluetooth.send(String.valueOf('E'), true);
                            coolerBoolean = true;
                            coolerButton.setImageResource(R.mipmap.cooler_open);
                            otoTempText.setText("Oto Cooler Temperature: " + types[4]);
                            break;
                        case 5:

                            bluetooth.send(String.valueOf('F'), true);
                            coolerBoolean = true;
                            coolerButton.setImageResource(R.mipmap.cooler_open);
                            otoTempText.setText("Oto Cooler Temperature: " + types[5]);
                            break;
                        case 6:

                            bluetooth.send(String.valueOf('G'), true);
                            coolerBoolean = true;
                            coolerButton.setImageResource(R.mipmap.cooler_open);
                            otoTempText.setText("Oto Cooler Temperature: " + types[6]);
                            break;
                        case 7:

                            bluetooth.send(String.valueOf('H'), true);
                            coolerBoolean = true;
                            coolerButton.setImageResource(R.mipmap.cooler_open);
                            otoTempText.setText("Oto Cooler Temperature: " + types[7]);
                            break;
                        case 8:

                            bluetooth.send(String.valueOf('I'), true);
                            coolerBoolean = true;
                            coolerButton.setImageResource(R.mipmap.cooler_open);
                            otoTempText.setText("Oto Cooler Temperature: " + types[8]);
                            break;
                        case 9:

                            bluetooth.send(String.valueOf('J'), true);
                            coolerBoolean = true;
                            coolerButton.setImageResource(R.mipmap.cooler_open);
                            otoTempText.setText("Oto Cooler Temperature: " + types[9]);
                            break;
                        case 10:

                            bluetooth.send(String.valueOf('K'), true);
                            coolerBoolean = true;
                            coolerButton.setImageResource(R.mipmap.cooler_open);
                            otoTempText.setText("Oto Cooler Temperature: " + types[10]);
                            break;
                    }
                }

            });
            builder.show();
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
                    Toast.makeText(MainActivity.this, bluetooth.getConnectedDeviceName() + " Connected !", Toast.LENGTH_SHORT).show();
                    bluetoothButton.setImageResource(R.mipmap.bluetooth_paired);
                }

                public void onDeviceDisconnected() {
                    Toast.makeText(MainActivity.this, bluetooth.getConnectedDeviceName() + " Disconnected !", Toast.LENGTH_SHORT).show();
                    bluetoothButton.setImageResource(R.mipmap.bluetooth_unpaired);
                }

                public void onDeviceConnectionFailed() {
                    Toast.makeText(MainActivity.this, "Connection Failed !", Toast.LENGTH_SHORT).show();
                    bluetoothButton.setImageResource(R.mipmap.bluetooth_unpaired);
                }
            });
        }
    }
}

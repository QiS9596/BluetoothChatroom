package com.example.rolo.bluetoothchatroom;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //local bluetooth adapter
    private BluetoothAdapter myBluetoothAdapter;
    //intent request codes
    private static final int REQUEST_ENABLE_BT = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!testBluetoothAvailable())
            finish();
        activateBluetooth();
        setDiscoverable();
    }
    //ask for default bluetooth adapter, if failed return false which result in the termination of app
    private boolean testBluetoothAvailable(){
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(myBluetoothAdapter == null){
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //set the current device to discoverable
    private void activateBluetooth(){
        if(!myBluetoothAdapter.isEnabled()){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }
    //set current device to discoverable
    private void setDiscoverable(){
        if(myBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent setDiscoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            setDiscoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(setDiscoverableIntent);
        }
    }
    //start device discover with the BluetoothAdapter
    private void scanBluetoothDevice(){

    }
}

package com.example.rolo.bluetoothchatroom;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    //local bluetooth adapter
    private BluetoothAdapter myBluetoothAdapter;
    //intent request codes
    private static final int REQUEST_ENABLE_BT = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFullTextViewTest();
        if(!testBluetoothAvailable())
            finish();
        activateBluetooth();
        setDiscoverable();
    }

    private void initFullTextViewTest(){
        ListView messageTextView = (ListView)findViewById(R.id.chatView);
        ArrayList<String> ListViewDataSource = new ArrayList<>();
        for(int index = 0; index < 30; index++){
            ListViewDataSource.add("data source : " + index);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.defaultlistviewlayout,ListViewDataSource);
        messageTextView.setAdapter(adapter);
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
//        Set<BluetoothDevice>;
    }
}

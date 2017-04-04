package com.example.rolo.bluetoothchatroom;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

public class bluetoothListShow extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private final String TAG = "MainActivity";
    private static final int REQUEST_ENABLE_SCAN = 3;

    private BluetoothAdapter bluetoothAdapter;
    private Button rescanButton;
    private ListView discoveredBluetoothDevice;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> strings = new ArrayList<>();
    private ArrayList<BluetoothDevice> devices = new ArrayList<>();
    private BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list_show);

//        BluetoothAdapter
        //attach reference to UserInterface components
        rescanButton = (Button) findViewById(R.id.ScanButton);
        discoveredBluetoothDevice = (ListView)findViewById(R.id.bluetoothDevices);
        discoveredBluetoothDevice.setOnItemClickListener(this);
        rescanButton.setOnClickListener(this);
        adapter = new ArrayAdapter<String>(this,
                R.layout.defaultlistviewlayout,
                strings);
        discoveredBluetoothDevice.setAdapter(adapter);
        //open and find bluetooth device
        OpenAndFindBluetoothDevice();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        initializeReceiver();
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onStop(){
        unregisterReceiver(receiver);
        super.onStop();
    }

    private void initializeReceiver(){
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(BluetoothDevice.ACTION_FOUND.equals(action)){
                    //if we find some device
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_NAME);
                    //if it's a device that we already discovered
                    if(devices.contains(device))
                        return;
                    //else it's a new device
                    strings.add(device.getName() + "\n" + device.getAddress());
                    devices.add(device);
                }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                    //setProgressBarIndeterminateVisibility(false);
                }
            }
        };
    }

    private void OpenAndFindBluetoothDevice(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null)
            return;
        if(!bluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,REQUEST_ENABLE_SCAN);
        }else{

            findBluetoothDevices();
        }
    }
    private void findBluetoothDevices(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if(pairedDevices.size()>0){
            for(BluetoothDevice device:pairedDevices){
                strings.add(device.getName() + "\n" + device.getAddress());
                devices.add(device);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_ENABLE_SCAN:
                if(resultCode == RESULT_OK)
                    findBluetoothDevices();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent selectedDeviceData = new Intent();
        selectedDeviceData.putExtra("DEVICE",devices.get(position));
        //when the user select one of the devices from the list, return the activity to main activity
        setResult(RESULT_OK,selectedDeviceData);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        strings.clear();
        devices.clear();
        OpenAndFindBluetoothDevice();
    }
}

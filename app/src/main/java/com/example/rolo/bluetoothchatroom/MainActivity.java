package com.example.rolo.bluetoothchatroom;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //local bluetooth adapter
    private BluetoothAdapter myBluetoothAdapter;
    //intent request codes
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;
    //listview that displays chat infomation
    private ListView messageTextView;
    private ArrayList<String> chatHistoryData;//arraylist that holds the chat history data
    private EditText inputField;//edittext for user input as a message
    private Button sendButton;//button to trigger send message
    private Button askScanMenuButton;//button that invoke the menu to scan bluetooth devices
    private BaseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initFullTextViewTest();
//        if(!testBluetoothAvailable())
//            finish();
//        activateBluetooth();
//        setDiscoverable();

        //allocate objects
        chatHistoryData = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.defaultlistviewlayout,chatHistoryData);
        //attach references to xml
        messageTextView = (ListView)findViewById(R.id.chatView);
        inputField = (EditText)findViewById(R.id.messageText);
        sendButton = (Button)findViewById(R.id.sendButton);
        askScanMenuButton = (Button)findViewById(R.id.ButtonBluetooth);

        //initialize messageTextView with adapter
        messageTextView.setAdapter(adapter);

        //open bluetooth device
        //try to get bluetooth adapter
        if(!getBluetoothAdapter())
        {
            //if failed send the information to user and terminate the application
            Toast.makeText(this,"Bluetooth adapter attach failed",Toast.LENGTH_LONG).show();
            finish();
        }
        activateBluetooth();

    }



    private void initFullTextViewTest(){

        ArrayList<String> ListViewDataSource = new ArrayList<>();
        for(int index = 0; index < 30; index++){
            ListViewDataSource.add("data source : " + index);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.defaultlistviewlayout,ListViewDataSource);
        messageTextView.setAdapter(adapter);
    }

    //ask for default bluetooth adapter, if failed return false which result in the termination of app
    private boolean getBluetoothAdapter(){
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
            //use implicit intent to enable bluetooth adapter
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }else{
            //the bluetooth adapter is already enabled
            //then we can straightly start multi-thread bluetooth service
            //TO-DO
            //startServiceAsServer();
        }
    }
    private void startServiceAsServer(){
        //TaskService.start(this, handler);

        //give a new task to background, the taskservice will pop out tasks from task array and accomplish them
        //TaskService.newTask(new Task(handler,Task.TASK_START_ACCEPT,null));
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

    //the current method is called after the termination of the startActivityForResult
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        switch(requestCode){
            case REQUEST_ENABLE_BLUETOOTH:
                if(resultCode == RESULT_OK)
                {
                    //TO-DO
                    //start multi-thread bluetooth service
                    //startServiceAsServer
                }
                break;
            default:
                break;
        }
    }
}

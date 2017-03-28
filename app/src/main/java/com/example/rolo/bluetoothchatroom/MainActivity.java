package com.example.rolo.bluetoothchatroom;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //local bluetooth adapter
    private BluetoothAdapter myBluetoothAdapter;
    //intent request codes
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;
    //scan bluetooth device code
    private static final int REQUEST_SCAN_BT_DEVICE = 1;
    //listview that displays chat infomation
    private ListView messageTextView;
    private ArrayList<String> chatHistoryData;//arraylist that holds the chat history data
    private EditText inputField;//edittext for user input as a message
    private Button sendButton;//button to trigger send message
    private Button askScanMenuButton;//button that invoke the menu to scan bluetooth devices
    private BaseAdapter adapter;
    private Handler handler;
    private BluetoothDevice pairedDevice;
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
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case Task.SEND_MSG:
                        Toast.makeText(MainActivity.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
                        break;
                    case Task.RECEIVE_MSG:
                        chatHistoryData.add(msg.obj.toString());
                        adapter.notifyDataSetChanged();
                        break;
                    case Task.GET_REMOTE_STATE:
                        setTitle(msg.obj.toString());
                        //Toast.makeText(MainActivity.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
        //open bluetooth device
        //try to get bluetooth adapter
        if(!getBluetoothAdapter())
        {
            //if failed send the information to user and terminate the application
            Toast.makeText(this,"Bluetooth adapter attach failed",Toast.LENGTH_LONG).show();
            finish();
        }
        activateBluetooth();

        //attach send button action
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentMessage = inputField.getText().toString().trim();
                if(currentMessage.length() <= 0)
                    return;
                chatHistoryData.add(myBluetoothAdapter.getName() + " : " + currentMessage);
                //update data locally
                adapter.notifyDataSetChanged();
                //send message to background service to complete remote message deliver

                TaskService.newTask(new Task(handler,Task.SEND_MSG,new Object[]{currentMessage}));
                inputField.setText("");
            }
        });

    }

    //onclick method that start scan bluetooth device menu
    public void askScanMenuButtonOnClick(View v){
        Toast.makeText(this, "Hello,world", Toast.LENGTH_SHORT).show();
        startActivityForResult(new Intent(this, bluetoothListShow.class),REQUEST_SCAN_BT_DEVICE);
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

            startServiceAsServer();
        }
    }
    private void startServiceAsServer(){
        TaskService.start(this, handler);

        //give a new task to background, the taskservice will pop out tasks from task array and accomplish them
        TaskService.newTask(new Task(handler,Task.START_ACCEPT,null));
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
                    startServiceAsServer();
                }
                break;
            case REQUEST_SCAN_BT_DEVICE:
                if(resultCode == RESULT_OK){
                    pairedDevice = data.getParcelableExtra("DEVICE");
                    if(pairedDevice == null)
                        return;
                    //commit start connect device task, as a clint
                    TaskService.newTask(new Task(handler,Task.CONNECT_THREAD, new Object[]{}));
                }
                break;
            default:
                break;
        }
    }
}

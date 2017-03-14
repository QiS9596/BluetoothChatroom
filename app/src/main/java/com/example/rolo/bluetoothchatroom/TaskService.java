package com.example.rolo.bluetoothchatroom;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.io.IOError;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.UUID;

public class TaskService extends Service {
    @Override
    public void onCreate() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            return;
        }
        taskThread = new TaskThread();
        taskThread.start();
        super.onCreate();
    }

    public TaskService() {
    }
    private boolean serving = false;
    private static ArrayList<Task> taskQueue = new ArrayList<>();
    private TaskThread taskThread;
    private BluetoothAdapter bluetoothAdapter;
    private AcceptThread acceptThread;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    private static Handler myHandler;

    /**
     * called when start service from outer component
     * @param c environment variable
     * @param handler handler to update User Interface
     */
    public static void start(Context c, Handler handler){
        myHandler = handler;
        //start service explicitly
        Intent intent = new Intent(c,TaskService.class);
        c.startService(intent);
    }

    //close service
    public static void stop(Context c){
        Intent intent = new Intent(c, TaskService.class);
        c.stopService(intent);
    }

    //commit task to taskservice
    public static void newTask(Task task){
        taskQueue.add(task);
    }

    private class TaskThread extends Thread{
        private Boolean isRun = true;
        private int count = 0;

        //Terminates the current Thread
        public void cancel(){
            isRun = false;
        }

        @Override
        public void run() {
            Task task;
            while (isRun){
                if(taskQueue.size() > 0){
                    synchronized (taskQueue){
                        task = taskQueue.get(0);
                        accomplishTask(task);
                    }
                }else{
                    try{
                        Thread.sleep(200);
                        count++;
                    }catch(InterruptedException e){}
                    if(count >= 50){
                        count = 0;
                        Message handlerMsg = myHandler.obtainMessage();
                        handlerMsg.what = Task.GET_REMOTE_STATE;
                        myHandler.sendMessage(handlerMsg);
                    }
                }
            }
        }

        private void accomplishTask(Task task){
            switch(task.getCurrentTaskID()){
                case Task.START_ACCEPT:
                    //accept client as a server
                    acceptThread = new AcceptThread();
                    acceptThread.start();
                    serving = true;
                    break;
                case Task.CONNECT_THREAD:
                    if(task.parameters == null)
                        break;
                    BluetoothDevice pair = (BluetoothDevice)task.parameters[0];
                    //TODO connectThread = new ConnectThread();
                    //TODO connectThread.start();
                    serving = false;
                    break;
            }
        }
    }

    private class AcceptThread extends Thread{
        private final BluetoothServerSocket serverSocket;
        private boolean isRun = true;

        public AcceptThread() {
            BluetoothServerSocket bluetoothServerSocket = null;
            try{
                bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("BluetoothChatRoomR",
                        UUID.fromString("BluetoothChatRoomR"));//UUID? question
            }catch(IOException e){}
            serverSocket = bluetoothServerSocket;
        }

        @Override
        public void run() {
            BluetoothSocket bluetoothSocket = null;
            while(isRun){
                try{
                    bluetoothSocket = serverSocket.accept();
                }catch(IOException e){
                    if(isRun){
                        try{
                            serverSocket.close();
                        }catch (IOException e1){
                        }
                        acceptThread = new AcceptThread();
                        acceptThread.start();
                        serving = true;
                    }
                    break;
                }
                if(bluetoothSocket != null){
                    //TODO manageConnectedSocket(socket);
                    try{
                        bluetoothSocket.close();
                    }catch(IOException e){
                    }
                    acceptThread = null;
                    isRun = false;
                }
            }
        }
    }
}

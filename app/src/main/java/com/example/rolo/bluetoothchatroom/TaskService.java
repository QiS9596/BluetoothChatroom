package com.example.rolo.bluetoothchatroom;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.ArrayList;

public class TaskService extends Service {
    public TaskService() {
    }
    private static ArrayList<Task> taskQueue = new ArrayList<>();
//  TODO  private TaskThread taskThread;
    private BluetoothAdapter bluetoothAdapter;
//  TODO  private AcceptThread acceptThread;
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
}

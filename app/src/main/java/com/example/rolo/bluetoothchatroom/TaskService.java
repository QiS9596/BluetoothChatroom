package com.example.rolo.bluetoothchatroom;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class TaskService extends Service {
    public TaskService() {
    }

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

//    public static void
}

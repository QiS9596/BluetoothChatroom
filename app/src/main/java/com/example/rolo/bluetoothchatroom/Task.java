package com.example.rolo.bluetoothchatroom;


import android.os.Handler;

/**
 * used to pass tasks between UserInterface and background service thread
 * Created by rolo on 2017/3/14.
 */

public class Task {
    public static final int START_ACCEPT = 1;
    public static final int CONNECT_THREAD = 2;
    public static final int SEND_MSG = 3;
    public static final int GET_REMOTE_STATE = 4;
    public static final int RECEIVE_MSG = 5;

    private int currentTaskID;
    private Handler handler;
    public Object[] parameters;
    public Task(Handler handler, int currentTaskID, Object[] parameters){
        this.handler = handler;
        this.currentTaskID = currentTaskID;
        this.parameters = parameters;
    }

    public int getCurrentTaskID(){
        return this.currentTaskID;
    }
}

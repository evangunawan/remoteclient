package me.vincevan.myremoteapp;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


//ResponseListener Thread read all response from the server.
//The server can send response to android client. For example: close the activity after shutdown or error.

public class ResponseListener extends Thread {

    private Activity activity; //In this case, Dashboard Activity
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ResponseListener(Activity activity){
        this.activity = activity;
    }

    public void run(){
        ois = SocketHandler.getOis();
        oos = SocketHandler.getOos();

        while(true){
            try{
                String response = (String)ois.readObject();
                Log.i("ServerResponse", response);

                translateResponse(response);

            }catch (IOException | ClassNotFoundException ex){
                Log.i("SocketException", ex.getMessage());
                break;
            }
        }
    }

    private void translateResponse(String response) {
        switch(response){
            case "finish_activity":
                SocketHandler.closeConnection();
                activity.finish();
                break;
        }
    }
}

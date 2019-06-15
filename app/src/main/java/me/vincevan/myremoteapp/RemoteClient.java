package me.vincevan.myremoteapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//RemoteClient task is:
//1. Create a connection to the host/server and store the objects into SocketHandler class.
//2. Open a new Activity (DashboardActivity) when this client is successfully connected to the host.
//3. Send model details automatically on connected to the host.

//Please use static class SocketHandler to get OOS/OIS/Socket.

public class RemoteClient extends AsyncTask<Void,Void,Boolean> {
    private Activity activity = null;
    private Socket socket;
    private String host;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public RemoteClient(Socket s, String host){
        this.socket = s;
        this.host = host;
    }

    public RemoteClient(Socket s,String host, Activity activity){
        this.activity = activity;
        this.host = host;
        this.socket = s;
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected Boolean doInBackground(Void... arg0) {
        Log.i("Socket","Connecting to the remote server: " + host);
        try{
            socket = new Socket(host,8025);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            SocketHandler.setSocket(socket);
            SocketHandler.setOos(oos);
            SocketHandler.setOis(ois);

            //On Connected to the server

            //Send Device Details to Server.
            oos.writeObject((String)Build.MODEL);
            oos.flush();

            return true;
        }catch(IOException ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onProgressUpdate(Void... arg0){

    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(result == true){
            //Start Dashboard Activity
            activity.startActivity(new Intent(activity,DashboardActivity.class));
        }else{
            Toast.makeText(activity.getApplicationContext(),"Failed to connect" ,Toast.LENGTH_SHORT).show();
        }

        //Make sure the button is secured when launching the activity. To avoid bug or double clicking and open 2 activities we need to delay the button enable.
        if(activity instanceof MainActivity){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) activity).enableInterfaceObjects();
                }
            }, 1000);
        }
        if(activity instanceof SavedHostActivity){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((SavedHostActivity) activity).setListViewEnabled(true);
                }
            }, 1000);
        }

    }

}

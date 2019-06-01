package me.vincevan.myremoteapp;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class SocketHandler {

    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private static Socket socket;

    public static synchronized ObjectOutputStream getOos() {
        return oos;
    }

    public static synchronized void setOos(ObjectOutputStream oos) {
        SocketHandler.oos = oos;
    }

    public static synchronized ObjectInputStream getOis() {
        return ois;
    }

    public static synchronized void setOis(ObjectInputStream ois) {
        SocketHandler.ois = ois;
    }

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket s){
        SocketHandler.socket = s;
    }

    public static void closeConnection(){
        try {
            oos.writeObject(1);
            socket.close();
            oos.close();
            ois.close();
        } catch (IOException e) {
            Log.e("Socket",e.getMessage());
        }

    }

    public static boolean sendCommand(int command){
        try{
            oos.writeObject(command);
            return true;
        }catch (IOException ex){
            ex.printStackTrace();
            return false;
        }
    }



}

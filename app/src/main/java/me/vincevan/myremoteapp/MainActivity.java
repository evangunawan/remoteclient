package me.vincevan.myremoteapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Button btnConnect;
    RemoteClient remoteClient;
    EditText editHost;
    Socket socket;
    ImageButton savedHostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        editHost = (EditText) findViewById(R.id.editHostIp);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        savedHostButton = (ImageButton)findViewById(R.id.savedHostBtn);

        btnConnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (isIpAddress(editHost.getText().toString())) {
                    editHost.setEnabled(false);
                    btnConnect.setEnabled(false);
                    Toast.makeText(getApplicationContext(),"Connecting to host..", Toast.LENGTH_SHORT).show();
                    remoteClient = new RemoteClient(socket,editHost.getText().toString(),MainActivity.this);
                    remoteClient.execute();
                }else{
                    Toast.makeText(getApplicationContext(),"Please input an IPv4 Address", Toast.LENGTH_SHORT).show();
                }

            }
        });

        savedHostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent savedHosts = new Intent(getApplicationContext(),SavedHostActivity.class);
                startActivity(savedHosts);
            }
        });

    }

    protected void startDashboard(){
        Intent dashboard = new Intent(this,DashboardActivity.class);
//        dashboard.putExtra("RemoteConnection",remoteConnection);
        startActivity(dashboard);
    }

    protected void enableInterfaceObjects(){

        this.editHost.setEnabled(true);
        this.btnConnect.setEnabled(true);
    }

    private Boolean isIpAddress(String ip){
        try{
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;

        } catch (NumberFormatException ex){
            return false;
        }
    }
}

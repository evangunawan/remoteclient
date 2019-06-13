package me.vincevan.myremoteapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.net.Socket;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private Integer commandToSend = 0;

    private TextView txtView;
    private Socket socket;
    private ListView listView;
    private ArrayAdapter adapter;

    private ArrayList<Command> commandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        txtView = findViewById(R.id.textView);

        //Toolbar Initialization
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocketHandler.closeConnection();
                finish();
            }
        });

        //Initialize Socket and Data
        socket = SocketHandler.getSocket();
        populateData();

        adapter = new ArrayAdapter(DashboardActivity.this, R.layout.list_dashboard, commandList);
        listView = (ListView) findViewById(R.id.controls_listView);
        listView.setAdapter(adapter);
        listView.setClickable(true);

        //Create ResponseListener Thread
        //This will create the thread immediately after this activity is created.
        new ResponseListener(DashboardActivity.this).start();

        //Status at the bottom of the screen.
        txtView.setText("Connected to: " + socket.getInetAddress() + ":" + socket.getPort() + "");

        //On Item click in ListView, sending command to the server/host.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Command item = (Command) parent.getItemAtPosition(position);
                //Verify the command first and send it, check if this client needs prompt or anything.
                verifyCommand(item);

            }
        });
    }

    @Override
    public void onBackPressed() {
        SocketHandler.closeConnection();
        finish();
    }

    //This method verify a command whether the command needs something (e.g. prompts/dialogs) and send the command.
    private void verifyCommand(Command command) {
        this.commandToSend = command.getId();
        if (command.isNeedPrompt()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(command.getPromptMessage());

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if(commandToSend == 1){
                        SocketHandler.closeConnection();
                        finish();
                        return;
                    }
                    SocketHandler.sendCommand(commandToSend);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            AlertDialog prompt = builder.create();
            prompt.show();

        } else {
            //Add more verification here if necessary

            //The command needs no prompt dialog, proceed to send command.
            SocketHandler.sendCommand(commandToSend);
        }

        //Add more verification here (if necessary)

        //Debugging, Logging
        Log.i("ListViewClicked", "" + command.getId());
    }

    private void populateData() {
        //Populate Data
        commandList = new ArrayList<>();

        Command cmd_toggleDisplay = new Command(20, "Toggle Host Display", false);
        commandList.add(cmd_toggleDisplay);

        Command cmd_restart = new Command(11, "Restart Host", true);
        cmd_restart.setPromptMessage("Are you sure to restart host?");
        commandList.add(cmd_restart);

        Command cmd_shutdown = new Command(10, "Shutdown Host", true);
        cmd_shutdown.setPromptMessage("Are you sure to shutdown host?");
        commandList.add(cmd_shutdown);

        Command cmd_closeConn = new Command(1, "Close Connection", true);
        cmd_closeConn.setPromptMessage("Close Connection to Host?");
        commandList.add(cmd_closeConn);

    }
}

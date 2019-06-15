package me.vincevan.myremoteapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.vincevan.myremoteapp.model.SavedHostItem;

public class SavedHostActivity extends AppCompatActivity {

    private EditText txtDialogName;
    private EditText txtDialogIp;
    private ListView hostListView;
    private ArrayAdapter adapter;
    private ArrayList<SavedHostItem> hostList;
    private RemoteClient remoteClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_host);

        hostListView = findViewById(R.id.listHosts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.saved_host_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Populate data from file
        hostList = new ArrayList<SavedHostItem>();
        adapter = new HostListAdapter(this,hostList);
        loadListItems();
        adapter.notifyDataSetChanged();

        hostListView.setAdapter(adapter);
        hostListView.setClickable(true);
        hostListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //TODO: REMOVE SELECTED ITEM'S LINE FROM THE FRICKIN FILE.
                //Create new method for that.

                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Item Removed", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        hostListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Socket socket = new Socket();
                hostListView.setEnabled(false);
                Toast.makeText(getApplicationContext(),"Connecting to host..", Toast.LENGTH_SHORT).show();
                remoteClient = new RemoteClient(socket,hostList.get(position).getHostAddress(),SavedHostActivity.this);
                remoteClient.execute();

            }
        });

        if(hostList.isEmpty()){
            findViewById(R.id.txtEmptyPrompt).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_saved_host,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_add:
                addNewHost();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void addNewHost(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_host, null);

        txtDialogName = (EditText)dialogView.findViewById(R.id.txtDialogName);
        txtDialogIp = (EditText)dialogView.findViewById(R.id.txtDialogIp);

        builder.setView(dialogView);
        builder.setTitle("Add New Host");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(txtDialogName.getText().toString()!=null || !txtDialogName.getText().toString().isEmpty() || isIpAddress(txtDialogIp.getText().toString())){
                    writeHostToFile(txtDialogName.getText().toString(),txtDialogIp.getText().toString(),getApplicationContext());
                    Toast.makeText(getApplicationContext(),"Host Added",Toast.LENGTH_SHORT).show();
                    loadListItems();
                    adapter.notifyDataSetChanged();
                    findViewById(R.id.txtEmptyPrompt).setVisibility(View.GONE);

                }else{
                    Toast.makeText(getApplicationContext(),"Wrong Input",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void writeHostToFile(String name, String ip, Context context){
        try{
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput("hosts.dat",Context.MODE_APPEND));
            osw.write(name + ":" + ip + "\n");
            osw.close();
        }catch(IOException ex){
            Log.e("Exception", ex.getMessage());
        }

    }

    //Read Hosts from File
    private ArrayList<SavedHostItem> readHostList(Context context){
        ArrayList<SavedHostItem> result = new ArrayList<>();
        try{
            InputStream inputStream = context.openFileInput("hosts.dat");
            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receivedLine = "";
                String[] lineValues;

                while((receivedLine = bufferedReader.readLine()) != null){
                    lineValues = receivedLine.split(":");
                    result.add(new SavedHostItem(lineValues[0],lineValues[1]));
                }
            }
        } catch (FileNotFoundException ex){
            Log.e("Exception",ex.getMessage());
        } catch (IOException ex){
            Log.e("Exception", ex.getMessage());
        }
        return result;
    }

    private void loadListItems(){
        hostList.clear();
        for(SavedHostItem item : readHostList(this)){
            hostList.add(item);
        }
        adapter.notifyDataSetChanged();

    }

    public void setListViewEnabled(Boolean b){
        if(b==true){
            this.hostListView.setEnabled(true);
        }else{
            this.hostListView.setEnabled(false);
        }
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

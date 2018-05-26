package edu.csusb.cse.employeemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.Snackbar;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.csusb.cse.employeemanager.adapters.RecyclerAdapter;
import edu.csusb.cse.employeemanager.httprequests.GetURLContentTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DEBUG";
    private static final String SERVER = "https://3abf132c.ngrok.io";
    private static String dialogText;

    final Context context = this;

    private RecyclerView recyclerView;
    private List<String> dataList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);

        updateList(getEmployees(httpGet()));
        initRecyclerView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrompt();
                updateList(getEmployees(httpGet()));
            }
        });
    }

    public void initRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerAdapter adapter =  new RecyclerAdapter(MainActivity.this, dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    public void showPrompt(){
        //get prompt.xml
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptView);

        final EditText userInput = promptView.findViewById(R.id.editTextDialogUserInput);

        //set dialog message
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialogText = userInput.getText().toString();
                                dataList.add(dialogText);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

        //create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        //show alert dialog
        alertDialog.show();
    }

    public String httpGet(){
        String employees = null;
        GetURLContentTask getURLContentTask = new GetURLContentTask();
        getURLContentTask.execute(SERVER);
        try {
            employees = getURLContentTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public List<String> getEmployees(String json){
        List<String> items = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()){
                String key = iterator.next();
                items.add(jsonObject.get(key).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void updateList(List<String> newList){
        for (String s: newList) {
            if (!dataList.contains(s)){
                dataList.add(s);
            }
        }
    }
}

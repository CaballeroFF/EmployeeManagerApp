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
import edu.csusb.cse.employeemanager.httprequests.PostURLContentTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DEBUG";
    private static final String SERVER = "https://6379b90c.ngrok.io";
    private static String dialogText;

    final Context context = this;

    private RecyclerView recyclerView;
    RecyclerAdapter adapter;
    private List<String> dataList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);

        updateList(getEmployeesFromJSON(httpGetRequest()));
        initRecyclerView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrompt();
            }
        });
    }

    public void initRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        adapter =  new RecyclerAdapter(MainActivity.this, dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    public void showPrompt(){
        //get prompt.xml
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.prompt, null);

        final EditText userInputName = promptView.findViewById(R.id.editTextDialogName);
        final EditText userInputID = promptView.findViewById(R.id.editTextDialogID);
        final EditText userInputDepartment = promptView.findViewById(R.id.editTextDialogDep);
        final EditText userInputTitle = promptView.findViewById(R.id.editTextDialogTitle);

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(promptView)
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (userInputName.getText().toString().length() == 0){
                            userInputName.setError("name required");
                        }
                        else if(userInputID.getText().toString().length() == 0){
                            userInputID.setError("ID required");
                        }
                        else if(userInputDepartment.getText().toString().length() == 0){
                            userInputDepartment.setError("Department required");
                        }
                        else if(userInputTitle.getText().toString().length() == 0){
                            userInputTitle.setError("Title required");
                        }
                        else {

                            dialogText = userInputName.getText().toString() + "," +
                                    userInputID.getText().toString() + "," +
                                    userInputDepartment.getText().toString() + "," +
                                    userInputTitle.getText().toString();

                            String returned = httpPOSTRequest(employeeToJSON(dialogText));
                            Log.d(TAG, "onClick: " + returned);
                            //refresh
                            updateList(getEmployeesFromJSON(httpGetRequest()));
                            adapter.notifyDataSetChanged();

                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        //show alert dialog
        dialog.show();
    }

    public String employeeToJSON(String employee){

        String[] employeeArray = employee.split(",");

        JSONObject jsonEmployee = new JSONObject();
        try {
            jsonEmployee.put("name",employeeArray[0]);
            jsonEmployee.put("id",employeeArray[1]);
            jsonEmployee.put("department",employeeArray[2]);
            jsonEmployee.put("title",employeeArray[3]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonEmployee.toString();
    }

    public String httpGetRequest(){
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

    public String httpPOSTRequest(String json){
        Log.d(TAG, "httpPOSTRequest: " + json);

        String returnMessage = null;
        PostURLContentTask postURLContentTask = new PostURLContentTask();
        postURLContentTask.execute(SERVER, json);
        try {
            returnMessage = postURLContentTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return returnMessage;
    }

    public List<String> getEmployeesFromJSON(String json){
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

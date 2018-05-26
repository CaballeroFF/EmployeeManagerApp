package edu.csusb.cse.employeemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import edu.csusb.cse.employeemanager.helpers.StringParser;
import edu.csusb.cse.employeemanager.httprequests.DeleteURLContentTask;
import edu.csusb.cse.employeemanager.httprequests.GetURLContentTask;
import edu.csusb.cse.employeemanager.httprequests.HttpRequests;
import edu.csusb.cse.employeemanager.httprequests.PostURLContentTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DEBUG";
    private static final String SERVER = "http://10.0.2.2:5000";
    private static String dialogText;

    StringParser stringParser = new StringParser();

    final Context context = this;

    private RecyclerView recyclerView;
    RecyclerAdapter adapter;
    private List<String> dataList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);

        stringParser.updateList(dataList, stringParser
                .getEmployeesFromJSON(new HttpRequests()
                        .httpGetRequest(SERVER)));

        initRecyclerView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrompt();
            }
        });
    }

    //side menu for the application
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View aboutView = layoutInflater.inflate(R.layout.about_us, null);

                final AlertDialog dialog = new AlertDialog.Builder(context)
                        .setView(aboutView)
                        .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                        .create();
                dialog.show();
                return true;
            case R.id.action_search:
                // About option clicked.
                return true;
            case R.id.action_exit:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                // Settings option clicked.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        adapter =  new RecyclerAdapter(MainActivity.this, dataList, SERVER);
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

                            String returned = new HttpRequests()
                                    .httpPostRequest(stringParser
                                    .employeeToJSON(dialogText), SERVER);
                            Log.d(TAG, "onClick: line 120 " + returned);
                            //refresh
                            stringParser.updateList(dataList, stringParser
                                    .getEmployeesFromJSON(new HttpRequests()
                                    .httpGetRequest(SERVER)));
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

}

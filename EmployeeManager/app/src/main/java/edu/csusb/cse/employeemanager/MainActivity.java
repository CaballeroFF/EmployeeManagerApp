package edu.csusb.cse.employeemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.Snackbar;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.csusb.cse.employeemanager.adapters.RecyclerAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DEBUG";
    private static String dialogText;

    final Context context = this;

    private RecyclerView recyclerView;
    private List<String> dataList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);

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
}

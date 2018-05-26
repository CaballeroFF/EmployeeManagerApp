package edu.csusb.cse.employeemanager.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.csusb.cse.employeemanager.MainActivity;
import edu.csusb.cse.employeemanager.R;
import edu.csusb.cse.employeemanager.helpers.StringParser;
import edu.csusb.cse.employeemanager.httprequests.DeleteURLContentTask;
import edu.csusb.cse.employeemanager.httprequests.HttpRequests;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG = "DEBUG";
    private List<String> dataList;
    private Context context;
    private String SERVER;
    private String dialogText;

    StringParser stringParser = new StringParser();

    private static View view;


    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageButton trash;
        ImageButton edit;
        RelativeLayout relativeLayout;


        ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            textView = itemView.findViewById(R.id.recycler_text);
            trash = itemView.findViewById(R.id.recycler_trash);
            edit = itemView.findViewById(R.id.edit_recycler);
            relativeLayout = itemView.findViewById(R.id.recycler_parent_layout);
        }
    }

    public RecyclerAdapter(Context context, List<String> dataList, String server) {
        this.dataList = dataList;
        this.context = context;
        this.SERVER = server;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView.setText(dataList.get(position));
        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteResponse = new HttpRequests()
                        .httpDeleteRequest(stringParser
                        .employeeToJSON(stringParser
                        .formattedEmployee(dataList.get(position))), SERVER);

                Log.d(TAG, "TESTING DELETE: line 77 " + deleteResponse);
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPrompt(position);
                Snackbar.make(view, "clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void showPrompt(int position){
        //get prompt.xml
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.prompt, null);

        final EditText userInputName = promptView.findViewById(R.id.editTextDialogName);
        final EditText userInputID = promptView.findViewById(R.id.editTextDialogID);
        final EditText userInputDepartment = promptView.findViewById(R.id.editTextDialogDep);
        final EditText userInputTitle = promptView.findViewById(R.id.editTextDialogTitle);

        String[] employee = stringParser.formattedEmployee(dataList.get(position)).split(",");

        userInputName.setText(employee[0]);
        userInputID.setText(employee[1]);
        userInputDepartment.setText(employee[2]);
        userInputTitle.setText(employee[3]);

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
                                    .httpPutRequest(stringParser
                                            .employeeToJSON(dialogText), SERVER);
                            Log.d(TAG, "onClick: line 120 " + returned);
                            //refresh
                            stringParser.updateList(dataList, stringParser
                                    .getEmployeesFromJSON(new HttpRequests()
                                    .httpGetRequest(SERVER)));
                            notifyDataSetChanged();

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

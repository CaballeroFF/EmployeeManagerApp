package edu.csusb.cse.employeemanager.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.csusb.cse.employeemanager.MainActivity;
import edu.csusb.cse.employeemanager.R;
import edu.csusb.cse.employeemanager.helpers.StringParser;
import edu.csusb.cse.employeemanager.httprequests.DeleteURLContentTask;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG = "DEBUG";
    private List<String> dataList;
    private Context context;
    private String SERVER;

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
                String test = httpDeleteRequest(new StringParser()
                        .employeeToJSON(new StringParser()
                        .formattedEmployee(dataList.get(position))));
                Log.d(TAG, "TESTING DELETE: " + test);
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder stringBuilder = new StringBuilder();
                String[] list = dataList.get(position).split("\n");
                Log.d(TAG, "onClick: " + dataList.get(position));

                for (String s: list) {
                    String[] lines = s.split(": ");
                    stringBuilder.append(lines[1]).append(",");
                    Log.d(TAG, "individual:" + s + "----");
                }
                Log.d(TAG, "finally: " + stringBuilder.deleteCharAt(stringBuilder.length() - 1));
                Snackbar.make(view, "clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public String httpDeleteRequest(String json){
        Log.d(TAG, "httpDeleteRequest: line 111 " + json);

        String returnMessage = null;
        DeleteURLContentTask deleteURLContentTask = new DeleteURLContentTask();
        deleteURLContentTask.execute(SERVER, json);
        try {
            returnMessage = deleteURLContentTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return returnMessage;
    }
}

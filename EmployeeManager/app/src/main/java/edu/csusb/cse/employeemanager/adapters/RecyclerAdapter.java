package edu.csusb.cse.employeemanager.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import edu.csusb.cse.employeemanager.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG = "DEBUG";
    private List<String> dataList;
    private Context context;

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

    public RecyclerAdapter(Context context, List<String> dataList) {
        this.dataList = dataList;
        this.context = context;
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
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

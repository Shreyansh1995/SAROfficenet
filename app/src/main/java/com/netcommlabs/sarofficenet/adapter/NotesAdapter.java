package com.netcommlabs.sarofficenet.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.model.NotesModel;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {

    private ArrayList<NotesModel> notesModelArrayList;
    private Context context;
    private Activity activity;
    private RemoveNotes removeNotes;
    AlertDialog.Builder builder;

    public NotesAdapter(ArrayList<NotesModel> notesModelArrayList, Context context, Activity activity, RemoveNotes removeNotes) {
        this.notesModelArrayList = notesModelArrayList;
        this.context = context;
        this.activity = activity;
        this.removeNotes = removeNotes;
    }


    @Override
    public NotesAdapter.NotesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_row, parent, false);
        return new NotesAdapter.NotesHolder(itemView, context, notesModelArrayList);
    }

    @Override
    public void onBindViewHolder(NotesAdapter.NotesHolder holder, final int position) {
        holder.tvdate.setText(notesModelArrayList.get(position).getDate());
        holder.tvnotes.setText(notesModelArrayList.get(position).getShortNotes() + notesModelArrayList.get(position).getFullNotes());
        holder.ivdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(position);

            }
        });
    }

    private void showDialog(final int position) {
        builder = new AlertDialog.Builder(activity);

        builder.setTitle("Alert").setMessage("Do you want to delete this note?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (removeNotes != null) {
                            removeNotes.onDeleteClick(notesModelArrayList.get(position).getNotesID());
                        }
                        removeAt(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public int getItemCount() {
        if (notesModelArrayList == null)
            return 0;
        return notesModelArrayList.size();
    }

    public class NotesHolder extends RecyclerView.ViewHolder {
        TextView tvnotes, tvdate;
        ImageView ivdelete;

        public NotesHolder(View itemView, Context context, ArrayList<NotesModel> notesModelArrayList) {
            super(itemView);
            tvnotes = itemView.findViewById(R.id.tv_notes);
            tvdate = itemView.findViewById(R.id.tv_date);
            ivdelete = itemView.findViewById(R.id.iv_dlt);
        }
    }

    public interface RemoveNotes {
        void onDeleteClick(String NotesID);
    }

    void removeAt(int position) {
        notesModelArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, notesModelArrayList.size());
    }
}

package com.example.mynotes.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotes.R;
import com.example.mynotes.Update_Note;
import com.example.mynotes.model.NotesModel;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.Myviewholder> implements Filterable {

    Context context;
    Activity activity;
    List<NotesModel> notesList;

    List<NotesModel> notesListAll;   // Search Note

    public NotesAdapter(Context context, Activity activity, List<NotesModel> notesList) {
        this.context = context;
        this.activity = activity;
        this.notesList = notesList;

        this.notesListAll = new ArrayList<>(notesList);
    }

    public class Myviewholder extends RecyclerView.ViewHolder{
        TextView title , description;
        RelativeLayout note_layout;
        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            description = (TextView)itemView.findViewById(R.id.description);
            note_layout = (RelativeLayout) itemView.findViewById(R.id.note_layout);
        }
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_note , parent , false);
        return new Myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.Myviewholder holder, int position) {
        NotesModel nm = notesList.get(position);
        holder.title.setText(nm.getTitle());
        holder.description.setText(nm.getDescription());

        holder.note_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context , Update_Note.class);
                intent.putExtra("id" , nm.getId());
                intent.putExtra("title" , nm.getTitle());
                intent.putExtra("description" , nm.getDescription());
                activity.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }


    // Seach Notes
    @Override
    public Filter getFilter() {
        return noteFilter;
    }

    private Filter noteFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<NotesModel> searchlist = new ArrayList<>();

            if (constraint == null || constraint.length()==0){
                searchlist.addAll(notesListAll);
            }else {

                for (NotesModel item : notesListAll){
                    if (item.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())){
                        searchlist.add(item);
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = searchlist;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notesList.clear();
            notesList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    // Delete Single Note
    public List<NotesModel> getList(){
        return notesList;
    }
    public void removeItem(int position){
        notesList.remove(position);
        notifyItemRemoved(position);
    }
    public void restoreItem(NotesModel note , int position){
        notesList.add(position , note);
        notifyItemInserted(position);
    }

}

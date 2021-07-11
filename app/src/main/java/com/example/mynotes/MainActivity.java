package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mynotes.adapter.NotesAdapter;
import com.example.mynotes.data.MyDbHandler;
import com.example.mynotes.model.NotesModel;
import com.example.mynotes.params.Params;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView note_rv;
    NotesAdapter nadapter;
    List<NotesModel> notesList = new ArrayList<>();
    MyDbHandler db;

    CoordinatorLayout main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_layout = (CoordinatorLayout)findViewById(R.id.main_layout);
        note_rv = (RecyclerView)findViewById(R.id.note_rv);

        db = new MyDbHandler(this);

        fetchAllNotes();
        note_rv.setLayoutManager(new LinearLayoutManager(this));
        nadapter = new NotesAdapter(this , MainActivity.this , notesList);
        note_rv.setAdapter(nadapter);
        Toast.makeText(this, db.getCount() + " Notes", Toast.LENGTH_SHORT).show();

        // delete for remove right
        ItemTouchHelper helper = new ItemTouchHelper(simpleCallback);
        helper.attachToRecyclerView(note_rv);

    } //============================================================================================

    // Show Notes
    private void fetchAllNotes() {

        List<NotesModel> notes = db.showAllData();
        for (NotesModel note : notes){
            notesList.add(new NotesModel(note.getId(),note.getTitle(),note.getDescription()));
        }

    }

    public void Add_Click(View view) {
        Intent intent = new Intent(MainActivity.this , AddNotes.class);
        startActivity(intent);
    }


    // Search Notes
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu , menu);

        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search Notes ");

        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                nadapter.getFilter().filter(newText);
                return true;
            }
        };

        searchView.setOnQueryTextListener(listener);

        return super.onCreateOptionsMenu(menu);
    }


    // Delete All Notes
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.delete_all_notes){
            db.deleteAllNotes();
            recreate();
        }

        return super.onOptionsItemSelected(item);
    }


    // Delete Single Note
    ItemTouchHelper.SimpleCallback simpleCallback =new ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            NotesModel note = nadapter.getList().get(position);

            nadapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(main_layout , "Note Deleted" , Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nadapter.restoreItem(note , position);
                        }
                    })
                    .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);

                            if (!(event == DISMISS_EVENT_ACTION)){
                                db.deleteSingleNotes(note.getId());
                            }

                        }
                    });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    };

}
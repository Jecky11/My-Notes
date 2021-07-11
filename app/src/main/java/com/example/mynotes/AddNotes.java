package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mynotes.data.MyDbHandler;
import com.example.mynotes.model.NotesModel;

public class AddNotes extends AppCompatActivity {

    EditText add_title , add_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        add_title = (EditText) findViewById(R.id.add_title);
        add_desc = (EditText)findViewById(R.id.add_desc);




    }

    public void Addnote_Click(View view) {

        String title_txt = add_title.getText().toString();
        String desc_txt = add_desc.getText().toString();

        MyDbHandler db = new MyDbHandler(AddNotes.this);
        NotesModel notes = new NotesModel();
        notes.setTitle(title_txt);
        notes.setDescription(desc_txt);

        db.addnotes(notes);

        Intent intent = new Intent(AddNotes.this , MainActivity.class);
        startActivity(intent);

    }
}

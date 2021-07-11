package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mynotes.data.MyDbHandler;
import com.example.mynotes.model.NotesModel;

public class Update_Note extends AppCompatActivity {

    EditText title , description;
    String id;
    NotesModel notesModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        title = (EditText)findViewById(R.id.title);
        description = (EditText)findViewById(R.id.description);


        Intent in = getIntent();
        id = in.getStringExtra("id" );
        title.setText(in.getStringExtra("title"));
        description.setText(in.getStringExtra("description"));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString())){

            MyDbHandler db = new MyDbHandler(Update_Note.this);

            notesModel = new NotesModel();
            notesModel.setId(id);
            notesModel.setTitle(title.getText().toString());
            notesModel.setDescription(description.getText().toString());

            db.updateNote(notesModel);

            Intent intent = new Intent(Update_Note.this , MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

//        }

    }

}
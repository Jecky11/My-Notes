package com.example.mynotes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mynotes.model.NotesModel;
import com.example.mynotes.params.Params;

import java.util.ArrayList;
import java.util.List;


public class MyDbHandler extends SQLiteOpenHelper {


    public MyDbHandler(Context context) {
        super(context, Params.DB_NAME , null , Params.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String create = "CREATE TABLE " + Params.TABLE_NAME + "("
                + Params.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Params.KEY_TITLE
                + " TEXT, " + Params.KEY_DESCRIPTION + " TEXT" + ");";

        db.execSQL(create);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    // Add notes [insert]
    public void addnotes(NotesModel notes){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Params.KEY_TITLE , notes.getTitle());
        values.put(Params.KEY_DESCRIPTION , notes.getDescription());

        db.insert(Params.TABLE_NAME , null , values);
        Log.d("db" , "Successfully inserted");
        db.close();

    }


    // Show all notes
    public List<NotesModel> showAllData(){

        List<NotesModel> notesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + Params.TABLE_NAME;
        Cursor cursor = db.rawQuery(select , null);

        if (cursor.moveToFirst()){
            do {
                NotesModel notes = new NotesModel();
                notes.setId(cursor.getString(0));
                notes.setTitle(cursor.getString(1));
                notes.setDescription(cursor.getString(2));
                notesList.add(notes);
            }while (cursor.moveToNext());
        }
        return notesList;

    }


    // Update Note
    public int updateNote(NotesModel notesModel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Params.KEY_TITLE , notesModel.getTitle());
        values.put(Params.KEY_DESCRIPTION , notesModel.getDescription());

        Log.d("db" , "Successfully updated");
        // Lets update now
        return db.update(Params.TABLE_NAME , values , Params.KEY_ID + "=?" ,
                new String[]{String.valueOf(notesModel.getId())});

    }


    // Delete All Notes
    public void deleteAllNotes(){
        SQLiteDatabase db = this.getWritableDatabase();

        String query ="DELETE FROM " +Params.TABLE_NAME;
        db.execSQL(query);
    }

    // Delete Single Notes
    public void deleteSingleNotes(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Params.TABLE_NAME , Params.KEY_ID +"=?" , new String[]{id});
    }


    public int getCount(){
        String query = "SELECT * FROM "+ Params.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query , null);

        return cursor.getCount();
    }


}

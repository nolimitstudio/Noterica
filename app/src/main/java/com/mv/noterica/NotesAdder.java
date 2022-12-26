package com.mv.noterica;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mv.noterica.Modules.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesAdder extends AppCompatActivity {

    EditText Etitle,Enote;
    ImageView savebtn;
    Notes notes;
    boolean isOldNote=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_adder);

        savebtn = findViewById(R.id.image_save);
        Etitle= findViewById(R.id.edit_Title);
        Enote = findViewById(R.id.edit_Note);

        notes=new Notes();
        try {
        notes = (Notes) getIntent().getSerializableExtra("old_note");
        Etitle.setText(notes.getTitle());
        Enote.setText(notes.getNotes());
        isOldNote=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = Etitle.getText().toString();
                String note = Enote.getText().toString();
                if (note.isEmpty()){
                    Toast.makeText(NotesAdder.this,"Please Inter Your Notes",Toast.LENGTH_LONG);
                    return;
                }
                SimpleDateFormat format=new SimpleDateFormat("EEE, d MMM yyy HH:MM a");
                Date date = new Date();

                if (!isOldNote){
                    notes = new Notes();
                }

                notes.setTitle(title);
                notes.setNotes(note);
                notes.setDate(format.format(date));

                Intent intent = new Intent();
                intent.putExtra("note", notes);
                setResult(Activity.RESULT_OK,intent);
                finish();

            }
        });

    }
}
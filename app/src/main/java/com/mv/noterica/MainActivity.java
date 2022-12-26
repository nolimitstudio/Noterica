package com.mv.noterica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mv.noterica.Adapters.NotesListAdapter;
import com.mv.noterica.Modules.Notes;
import com.mv.noterica.database.RoomDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    NotesListAdapter adapter;
    List<Notes> notes = new ArrayList<>();
    RoomDB roomDB;
    FloatingActionButton actionButton;
    SearchView SearchText;
    Notes SNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        actionButton = findViewById(R.id.fab_add);
        SearchText = findViewById(R.id.SearchText);

        roomDB = RoomDB.getInstance(this);
        notes= roomDB.mainDBAccess().getAll();

        updateRecycler(notes);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NotesAdder.class);
                startActivityForResult(intent,101);
            }
        });
        SearchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {

        List<Notes> filterdList = new ArrayList<>();
        for (Notes singleNote :notes) {
            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
            || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filterdList.add(singleNote);
            }
        }
        adapter.filterList(filterdList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101){
            if (resultCode== Activity.RESULT_OK){
                Notes NewNotes= (Notes) data.getSerializableExtra("note");
                roomDB.mainDBAccess().insert(NewNotes);
                notes.clear();
                notes.addAll(roomDB.mainDBAccess().getAll());
                adapter.notifyDataSetChanged();
            }
        }else if (requestCode==102){
            if (resultCode== Activity.RESULT_OK){
                Notes NewNotes= (Notes) data.getSerializableExtra("note");
                roomDB.mainDBAccess().update(NewNotes.getId(),NewNotes.getTitle(),NewNotes.getNotes());
                notes.clear();
                notes.addAll(roomDB.mainDBAccess().getAll());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Notes> notes) {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        adapter = new NotesListAdapter(MainActivity.this,notes,notesClickListner);
        recyclerView.setAdapter(adapter);


    }

    private final NotesClickListner notesClickListner = new NotesClickListner() {
        @Override
        public void onClik(Notes notes) {
            Intent intent = new Intent(MainActivity.this,NotesAdder.class);
            intent.putExtra("old_note",notes);
            startActivityForResult(intent,102);

        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            SNote = new Notes();
            SNote=notes;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {

        PopupMenu popupMenu = new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.pinItem:
                if (SNote.isPinned()){
                    roomDB.mainDBAccess().pin(SNote.getId(),false);
                }else{
                roomDB.mainDBAccess().pin(SNote.getId(),true);
            }
                notes.clear();
                notes.addAll(roomDB.mainDBAccess().getAll());
                adapter.notifyDataSetChanged();
                return true;
            case R.id.delItem:
                roomDB.mainDBAccess().delete(SNote);
                notes.remove(SNote);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return false;
        }

    }
}


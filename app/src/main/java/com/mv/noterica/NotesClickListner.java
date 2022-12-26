package com.mv.noterica;

import androidx.cardview.widget.CardView;

import com.mv.noterica.Modules.Notes;

public interface NotesClickListner {

    void onClik(Notes notes);
    void  onLongClick(Notes notes, CardView cardView);

}

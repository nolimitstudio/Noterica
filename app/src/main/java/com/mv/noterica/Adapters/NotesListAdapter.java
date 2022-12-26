package com.mv.noterica.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mv.noterica.Modules.Notes;
import com.mv.noterica.NotesClickListner;
import com.mv.noterica.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder>{

    Context context;
    List<Notes> notes;
    NotesClickListner notesClickListner;

    public NotesListAdapter(Context context, List<Notes> notes, NotesClickListner notesClickListner) {
        this.context = context;
        this.notes = notes;
        this.notesClickListner = notesClickListner;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.textTitle.setText(notes.get(position).getTitle());
        holder.textTitle.setSelected(true);

        holder.text_Notes.setText(notes.get(position).getNotes());

        holder.text_date.setText(notes.get(position).getDate());
        holder.text_date.setSelected(true);

        if (notes.get(position).isPinned())
        {
            holder.image_Pin.setImageResource(R.drawable.ic_baseline_push_pin_24);
        }
        else {
            holder.image_Pin.setImageResource(0);
        }

        int color=getRandomColor();
        holder.notes_containor.setCardBackgroundColor(holder.itemView.getResources().getColor(color,null));

        holder.notes_containor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesClickListner.onClik(notes.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_containor.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                notesClickListner.onLongClick(notes.get(holder.getAdapterPosition()),holder.notes_containor);
                return true;
            }
        });

    }

    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.blue);
        colorCode.add(R.color.red);
        colorCode.add(R.color.green);
        colorCode.add(R.color.cayan);
        colorCode.add(R.color.purple);
        colorCode.add(R.color.yellow);

        Random random = new Random();
        int random_color = random.nextInt(colorCode.size());
        return colorCode.get(random_color);
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void filterList(List<Notes> filterdList){
        notes = filterdList;
        notifyDataSetChanged();
    }

}

class NotesViewHolder extends RecyclerView.ViewHolder{

    CardView notes_containor;
    TextView textTitle,text_Notes,text_date;
    ImageView image_Pin;
    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        notes_containor = itemView.findViewById(R.id.notes_containor);
        textTitle =  itemView.findViewById(R.id.textTitle);
        text_Notes = itemView.findViewById(R.id.text_Notes);
        text_date = itemView.findViewById(R.id.text_date);
        image_Pin = itemView.findViewById(R.id.image_Pin);

    }
}

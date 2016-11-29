package book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.view.recycler;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import book.retrofit.com.vutka.gettingstartedwithmvp.R;

/**
 * Created by vutka bilai on 11/29/16.
 * mail : la4508@gmail.com
 */

public class NotesViewHolder extends RecyclerView.ViewHolder{

    public RelativeLayout container;
    public TextView noteText , noteDate;
    public ImageButton deletenoteBtn;

    public NotesViewHolder(View itemView) {
        super(itemView);

        container = (RelativeLayout) itemView.findViewById(R.id.holder_container);
        noteText = (TextView) itemView.findViewById(R.id.note_text);
        noteDate = (TextView) itemView.findViewById(R.id.note_date);
        deletenoteBtn = (ImageButton) itemView.findViewById(R.id.btn_delete);
    }
}

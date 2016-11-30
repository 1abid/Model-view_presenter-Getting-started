package book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.model.Note;
import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.view.recycler.NotesViewHolder;

/**
 * Created by vutka bilai on 11/29/16.
 * mail : la4508@gmail.com
 */

public interface MainMVP {


    /**
     * Required View methods available to Presenter.
     * A passive layer, responsible to show data
     * and receive user interactions
     * (presenter -> view )
     */

    interface RequiredViewOps{
        Context getApplicationContext();
        Context getActivityContext();

        void notifyItemInserted(int layoutPosition);
        void notifyitemRemoved(int adapterPos);
        void notifyItemRangeChanged(int positionStat , int itemCount);
        void notifyDataSetChanged();

        void clearEditText();
        void showProgress();
        void hideProgress();
        void showAlert(AlertDialog alertDialog);
        void showToast(Toast toast);
    }


    /**
     * Operations offered to View to communicate with Presenter.
     * Process user interaction, sends data requests to Model, etc.
     * (view -> presenter)
     */

    interface ProvidedPresenterOps{
        void onDestroy(boolean inChnagingConfigurations);
        void setView(RequiredViewOps view);
        NotesViewHolder createViewHolder(ViewGroup parent , int viewType);
        void bindViewHolder(NotesViewHolder holder , int position);
        int getItemCount();
        void clickNewNote(EditText editText);
        void clickDeleteNote(Note note , int adapterPos , int layoutPos);

    }

    /**
     * required Presenter operation available
     * to model (model -> presenter)
     */

    interface RequiredPresenterOPS{
        Context getAppContext();
        Context getActivityContext();
    }


    /**
     * Operations offered to model to communicate with presenter
     * Handles all data business logic
     * (presenter -> model )
     */


    interface ProvidedModelOps{
        void onDestroy(boolean isChangingConfigurations);
        int insertNote(Note note);
        boolean loadData();
        Note getNote(int position);
        boolean deleteNote(Note note , int adapterPos);
        int getNotesCount();
    }

}

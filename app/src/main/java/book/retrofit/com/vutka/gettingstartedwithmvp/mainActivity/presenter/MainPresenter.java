package book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.presenter;


import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import book.retrofit.com.vutka.gettingstartedwithmvp.R;
import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.MainMVP;
import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.model.Note;
import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.view.recycler.NotesViewHolder;

/**
 * Created by vutka bilai on 11/30/16.
 * mail : la4508@gmail.com
 * <p>
 * Presenter Layer for view(mainActivity)
 */

public class MainPresenter implements MainMVP.ProvidedPresenterOps, MainMVP.RequiredPresenterOPS {

    // View reference. We use as a WeakReference
    // because the Activity could be destroyed at any time
    // and we don't want to create a memory leak
    private WeakReference<MainMVP.RequiredViewOps> mView;

    //model reference
    private MainMVP.ProvidedModelOps mModel;


    public MainPresenter(MainMVP.RequiredViewOps mView) {
        this.mView = new WeakReference<MainMVP.RequiredViewOps>(mView);
    }

    /**
     * Called by View every time it is destroyed.
     *
     * @param isChnagingConfigurations true: is changing configuration
     *                                 and will be recreated
     */
    @Override
    public void onDestroy(boolean isChnagingConfigurations) {
        //view should be null every time onDestroy is called
        mView = null;
        //infor model about the event
        mModel.onDestroy(isChnagingConfigurations);

        //activity destroyed
        if (!isChnagingConfigurations) {
            mModel = null;
        }
    }

    /**
     * Return the View reference.
     * Could throw an exception if the View is unavailable.
     *
     * @return {@link MainMVP.RequiredViewOps}
     * @throws NullPointerException when View is unavailable
     */

    private MainMVP.RequiredViewOps getView() throws NullPointerException {
        if (mView != null)
            return mView.get();
        else {
            throw new NullPointerException("view is unavailable");
        }
    }


    /**
     * Called by View during the reconstruction events
     *
     * @param view Activity instance
     */

    @Override
    public void setView(MainMVP.RequiredViewOps view) {
        mView = new WeakReference<MainMVP.RequiredViewOps>(view);
    }


    /**
     * Called by Activity during MVP setup. Only called once.
     *
     * @param model Model instance
     */
    public void setModel(MainMVP.ProvidedModelOps model) {

        mModel = model;

        //start loading data
        loadData();
    }

    /**
     * Load data from Model in a AsyncTask
     */
    private void loadData() {

        try {


            getView().showProgress();
            new AsyncTask<Void, Void, Boolean>() {

                @Override
                protected Boolean doInBackground(Void... voids) {

                    //load data from model
                    return mModel.loadData();
                }

                @Override
                protected void onPostExecute(Boolean result) {

                    try {
                        getView().hideProgress();
                        if(!result)
                            getView().showToast(makeToast("error loading data !"));
                        else
                            getView().notifyDataSetChanged();
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            }.execute();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the RecyclerView holder and setup its view
     * @param parent    Recycler viewgroup
     * @param viewType  Holder type
     * @return          Recycler ViewHolder
     */

    @Override
    public NotesViewHolder createViewHolder(ViewGroup parent, int viewType) {

        NotesViewHolder viewHolder ;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewTaskRow = inflater.inflate(R.layout.holder_notes,parent,false);
        viewHolder = new NotesViewHolder(viewTaskRow);
        return viewHolder;
    }

    /**
     * Binds ViewHolder with RecyclerView
     * @param holder    Holder to bind
     * @param position  Position on Recycler adapter
     */

    @Override
    public void bindViewHolder(final NotesViewHolder holder, int position) {
        final Note note = mModel.getNote(position);
        holder.noteText.setText(note.getText());
        holder.noteDate.setText(note.getDate());

        holder.deletenoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDeleteNote(note , holder.getAdapterPosition() , holder.getLayoutPosition());
            }
        });
    }


    /**
     * Retrieve total Notes count from Model
     * @return  Notes size
     */
    @Override
    public int getItemCount() {
        return mModel.getNotesCount();
    }

    @Override
    public void clickNewNote(EditText editText) {
        getView().showProgress();
        final String noteText = editText.getText().toString();

        if(!noteText.isEmpty()){

            new AsyncTask<Void , Void , Integer>(){

                @Override
                protected Integer doInBackground(Void... voids) {
                    return mModel.insertNote(makeNote(noteText));
                }

                @Override
                protected void onPostExecute(Integer adapterPos) {
                    try{
                        if(adapterPos > -1){
                            getView().clearEditText();
                            getView().notifyItemInserted(adapterPos +1);
                            getView().notifyItemRangeChanged(adapterPos , mModel.getNotesCount());
                            getView().hideProgress();
                        }else {
                            getView().hideProgress();
                            getView().showToast(makeToast("Error creating note ("+noteText+")"));
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            }.execute();
        }else {
            try {
                getView().showToast(makeToast("cannot add a blank note !"));
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void clickDeleteNote(Note note, int adapterPos, int layoutPos) {
        openDeleteAlert(note , adapterPos , layoutPos);
    }

    @Override
    public Context getAppContext() {
        try{
            return getView().getApplicationContext();
        }catch (NullPointerException e){
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public Context getActivityContext() {
        try{
            return getView().getActivityContext();
        }catch (NullPointerException e){
            e.printStackTrace();

            return null;
        }
    }


    /**
     * Creat a Toast object with given message
     * @param msg   Toast message
     * @return      A Toast object
     */
    private Toast makeToast(String msg) {
        return Toast.makeText(getView().getApplicationContext(), msg, Toast.LENGTH_SHORT);
    }


    /**
     * Get current Date as a String
     * @return  The current date
     */
    private String getDate() {
        return new SimpleDateFormat("HH:mm:ss - MM/dd/yyyy", Locale.getDefault()).format(new Date());
    }


    /**
     * Create an AlertBox to confirm a delete action
     * @param note          Note to be deleted
     * @param adapterPos    Adapter postion
     * @param layoutPos     Recycler layout position
     */

    private void openDeleteAlert(final Note note , final int adapterPos , final int layoutPos){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivityContext());
        alertBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete note if action is confirmed
                deleteNote(note, adapterPos, layoutPos);
            }
        });
        alertBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.setTitle("Delete Note");
        alertBuilder.setMessage("Delete " + note.getText() + " ?");

        AlertDialog alertDialog = alertBuilder.create();
        try {
            getView().showAlert(alertDialog);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    /**
     * Create a asyncTask to delete the object in Model
     * @param note          Note to delete
     * @param adapterPos    Adapter position
     * @param layoutPos     Recycler layout position
     */
    private void deleteNote(final Note note, final int adapterPos, final int layoutPos) {
        getView().showProgress();
        new AsyncTask<Void ,Void ,Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids) {
                return mModel.deleteNote(note , adapterPos);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                try{
                    getView().hideProgress();
                    if(!result){
                        getView().showToast(makeToast("Error deleting note ("+note.getId()+")"));
                    }else {
                        getView().notifyitemRemoved(layoutPos);
                        getView().showToast(makeToast("Note Deleted"));
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }.execute();
    }


    /**
     * Create a Note object with giver text
     * @param noteText  String with Note text
     * @return  A Note object
     */
    public Note makeNote(String noteText) {
        Note note = new Note();
        note.setText( noteText );
        note.setDate(getDate());
        return note;

    }

}

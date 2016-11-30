package book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.model;


import java.util.ArrayList;

import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.MainMVP;
import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.data.DAO;

/**
 * Created by vutka bilai on 11/30/16.
 * mail : la4508@gmail.com
 */

public class MainModel implements MainMVP.ProvidedModelOps {

    //presenter reference
    private MainMVP.RequiredPresenterOPS mPresenter;
    private DAO mDao;

    private ArrayList<Note> mNotes;




    /**
     * Main constructor, called by Activity during MVP setup
     * @param presenter Presenter instance
     */
    public MainModel(MainMVP.RequiredPresenterOPS presenter) {
        this.mPresenter = presenter;

        mDao = new DAO(mPresenter.getAppContext());
    }




    /**
     * Called by Presenter when View is destroyed
     * @param isChangingConfiguration   true configuration is changing
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        if (!isChangingConfiguration) {
            mPresenter = null;
            mDao = null;
            mNotes = null;
        }
    }

    @Override
    public int insertNote(Note note) {

        Note insertedNote = mDao.insertNote(note);

        if(insertedNote !=null){
            loadData();
            return getNotePosition(insertedNote);
        }

        return -1;
    }


    /**
     * Loads all Data, getting notes from DB
     * @return  true with success
     */
    @Override
    public boolean loadData() {
        mNotes = mDao.getAllNotes();
        return mNotes!=null;
    }


    /**
     * Get a specific note from notes list using its array postion
     * @param position    Array position
     * @return            Note from list
     */
    @Override
    public Note getNote(int position) {
        return mNotes.get(position);
    }


    /**
     * Get Note's positon on ArrayList
     * @param note  Note to check
     * @return      Positon on ArrayList
     */
    public int getNotePosition(Note note){
        for(int i=0; i<mNotes.size();i++){
            if(note.getId() == mNotes.get(i).getId())
                return i;
        }

        return -1;
    }


    /**
     * Delete a given Note form DB and ArrayList
     * @param note          Note to be deleted
     * @param adapterPos    Position on array
     * @return              true when success
     */
    @Override
    public boolean deleteNote(Note note, int adapterPos) {
        long res = mDao.deleteNote(note);

        if(res > 0){
            mNotes.remove(adapterPos);
            return true;
        }

        return false;
    }

    @Override
    public int getNotesCount() {
        return mNotes!=null ? mNotes.size() : 0 ;
    }
}

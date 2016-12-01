package book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.data;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.model.Note;

/**
 * Created by vutka bilai on 11/30/16.
 * mail : la4508@gmail.com
 */

public class DAO {

    private DBschema dbHelper;
    private Context mContext;

    //SELECTION
    private static final String SELECT_ID_BASED = DBschema.TB_NOTES.ID + " =? ";
    private static final String PROJECTION_ALL = " * ";
    private static final String SORT_ORDER_DEFAULT = DBschema.TB_NOTES.ID + " DESC";


    public DAO(Context mContext) {
        this.mContext = mContext;
        dbHelper = new DBschema(mContext);
    }

    private SQLiteDatabase getReadDb(){
        return dbHelper.getReadableDatabase();
    }

    private SQLiteDatabase getWritDb(){
        return dbHelper.getWritableDatabase();
    }


    public Note insertNote(Note note){

        SQLiteDatabase db = getWritDb();

        long id = db.insert(DBschema.TABLE_NOTES , null , note.getValues());

        Log.d(DAO.class.getSimpleName() , "inserted note ..."+id);

        Note insertedNote = getNote((int) id);
        db.close();

        return insertedNote;
    }


    public long deleteNote(Note note) {
        SQLiteDatabase db = getWritDb();
        long res = db.delete(
                DBschema.TABLE_NOTES,
                SELECT_ID_BASED,
                new String[]{Integer.toString(note.getId())}

        );
        db.close();
        return res;
    }



    public Note getNote(int id){
        SQLiteDatabase db = getReadDb();
        Cursor c = db.query(DBschema.TABLE_NOTES,
                null,
                SELECT_ID_BASED,
                new String[]{Integer.toString(id)},
                null,
                null,
                null);
        if(c != null){

            c.moveToFirst();
            Note note = new Note();
            note.setId(c.getInt(c.getColumnIndexOrThrow(DBschema.TB_NOTES.ID)));
            note.setText(c.getString(c.getColumnIndexOrThrow(DBschema.TB_NOTES.NOTE)));
            note.setDate(c.getString(c.getColumnIndexOrThrow(DBschema.TB_NOTES.DATE)));

            c.close();
            db.close();

            return note;
        }else {
            return null ;
        }

    }

    public ArrayList<Note> getAllNotes(){
        SQLiteDatabase db = getReadDb();

        ArrayList<Note> allNotes = new ArrayList<>();

        Cursor c = db.query(DBschema.TABLE_NOTES,
                null,
                null,
                null,
                null,
                null,
                null);

        if(c != null){
            c.moveToFirst();

            while (!c.isAfterLast()){
                Note note = new Note();

                note.setId(c.getInt(c.getColumnIndexOrThrow(DBschema.TB_NOTES.ID)));
                note.setText(c.getString(c.getColumnIndexOrThrow(DBschema.TB_NOTES.NOTE)));
                note.setDate(c.getString(c.getColumnIndexOrThrow(DBschema.TB_NOTES.DATE)));

                allNotes.add(note);
                c.moveToNext();
            }

            c.close();
            db.close();

            return allNotes;
        }else {
            return  null;
        }


    }
}

package book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by vutka bilai on 11/30/16.
 * mail : la4508@gmail.com
 */

public class DBschema extends SQLiteOpenHelper {

    private static final int    DB_VERSION  = 1;
    private static final String DB_NAME     = "mvp_sample.db";

    public DBschema(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }



    //Tables
    public static final String TABLE_NOTES  = "notes";

    private static final String COMMA_SPACE     = ", ";
    private static final String CREATE_TABLE    = "CREATE TABLE ";
    private static final String PRIMARY_KEY     = "PRIMARY KEY ";
    private static final String UNIQUE          = "UNIQUE ";
    private static final String TYPE_TEXT       = " TEXT ";
    private static final String TYPE_DATE       = " DATETIME ";
    private static final String TYPE_INT        = " INTEGER ";
    private static final String DEFAULT         = "DEFAULT ";
    private static final String AUTOINCREMENT   = "AUTOINCREMENT ";
    private static final String NOT_NULL        = "NOT NULL ";
    private static final String DROP_TABLE      = "DROP TABLE IF EXISTS ";

    public static final class TB_NOTES {


        public static final String ID = "_id";
        public static final String NOTE = "note";
        public static final String DATE = "date";


    }

    private static final String CREATE_TABLE_NOTES =
            CREATE_TABLE + TABLE_NOTES + " ( " +
                    TB_NOTES.ID + TYPE_INT + NOT_NULL + PRIMARY_KEY + AUTOINCREMENT + COMMA_SPACE +
                    TB_NOTES.NOTE + TYPE_DATE + NOT_NULL + COMMA_SPACE +
                    TB_NOTES.DATE + TYPE_TEXT + NOT_NULL +
                    ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_TABLE);
    }
}

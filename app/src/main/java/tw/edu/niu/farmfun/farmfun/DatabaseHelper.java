package tw.edu.niu.farmfun.farmfun;

import static tw.edu.niu.farmfun.farmfun.DbConstants.TABLE_NAME;
import static tw.edu.niu.farmfun.farmfun.DbConstants._ID;
import static tw.edu.niu.farmfun.farmfun.DbConstants.MEMBER;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TITLE;
import static tw.edu.niu.farmfun.farmfun.DbConstants.IDENTIFY;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Waileong910910 on 2016/3/3.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "demo.db";
    private final static int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String INIT_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MEMBER + " CHAR, " +
                TITLE + " CHAR, " +
                IDENTIFY + " CHAR);";
        db.execSQL(INIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

}

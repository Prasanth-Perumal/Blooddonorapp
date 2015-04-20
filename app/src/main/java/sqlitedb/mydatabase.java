package sqlitedb;

/**
 * Created by Prasanth on 02/04/15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class mydatabase extends SQLiteOpenHelper {

    public static final String TABLE_COMMENTS = "donor";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_MOBILE="mobile";
    public static final String COLUMN_BLOODGROUP="bloodgroup";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE="longitude";
    public static final String COLUMN_CITY="city";

    private static final String DATABASE_NAME = "donors.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_COMMENTS + "("
            +COLUMN_ID +" integer primary key autoincrement,"
            +COLUMN_NAME+ " text not null,"
            +COLUMN_MOBILE+" text,"
            +COLUMN_LONGITUDE+" text,"
            +COLUMN_LATITUDE+" text,"
            +COLUMN_CITY+" text,"
            +COLUMN_BLOODGROUP+" text"+");";


    //private static final String DATABASE_CREATE="create table donor(_id integer,Name text,mobile integer,bloodgroup text,latitude text,longitude text,city text);";


    public mydatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(mydatabase.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        onCreate(db);
    }

}

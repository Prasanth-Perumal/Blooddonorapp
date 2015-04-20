package DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Model.User;
import sqlitedb.mydatabase;

/**
 * Created by Prasanth on 02/04/15.
 */
public class Donordatasource {
    private SQLiteDatabase database;
    private mydatabase dbHelper;
    private String[] allColumns = {
            mydatabase.COLUMN_ID,
            mydatabase.COLUMN_NAME,
            mydatabase.COLUMN_MOBILE,
            mydatabase.COLUMN_LONGITUDE,
            mydatabase.COLUMN_LATITUDE,
            mydatabase.COLUMN_BLOODGROUP,
            mydatabase.COLUMN_CITY
    };

//    private static final String DATABASE_CREATE = "create table "
//            + TABLE_COMMENTS + "(" + COLUMN_ID
//            + " integer primary key autoincrement, " + COLUMN_NAME+
//            " text not null,"+COLUMN_MOBILE+
//            " text," +COLUMN_LONGITUDE+
//            " text,"+COLUMN_LATITUDE+
//            " text,"+COLUMN_CITY+
//            " text,"+COLUMN_BLOODGROUP
//            +" text"+");";

    public Donordatasource(Context context)
    {
        dbHelper=new mydatabase(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }


    public User createDonor(String name,String mobile,Double longitude,Double latitude,String city,String bloodtype) {
        ContentValues values = new ContentValues();
        values.put(mydatabase.COLUMN_NAME,name);
        values.put(mydatabase.COLUMN_MOBILE,mobile);
        values.put(mydatabase.COLUMN_LONGITUDE,longitude);
        values.put(mydatabase.COLUMN_LATITUDE,latitude);
        values.put(mydatabase.COLUMN_CITY,city);
        values.put(mydatabase.COLUMN_BLOODGROUP,bloodtype);

        long insertId = database.insert(mydatabase.TABLE_COMMENTS,null,values);
        //long insertId=database.insert()


        Cursor cursor = database.query(mydatabase.TABLE_COMMENTS,
                allColumns, mydatabase.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        User newComment = cursorToComment(cursor);
        cursor.close();
        Log.e("db","city"+newComment.getCity()+"lat"+String.valueOf(newComment.getLatitude())+"bt"+newComment.getBloodtype()+"mobile"+newComment.getMobile());
        return newComment;
    }

    public void deleteDonor(User comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(mydatabase.TABLE_COMMENTS, mydatabase.COLUMN_ID
                + " = " + id, null);
    }

    public List<User> getAllDonors(String Bloodtype) {
        List<User> donors = new ArrayList<User>();

        Cursor cursor = database.query(mydatabase.TABLE_COMMENTS,
                allColumns,mydatabase.COLUMN_BLOODGROUP + " = " +"'" +Bloodtype+"'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User comment = cursorToComment(cursor);
            donors.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return donors;
    }

    private User cursorToComment(Cursor cursor) {
        User comment = new User();
        comment.setId(cursor.getLong(0));
        comment.setName(cursor.getString(1));
        comment.setMobile(cursor.getString(2));
        comment.setLongitude(cursor.getDouble(3));
        comment.setLatitude(cursor.getDouble(4));
        comment.setCity(cursor.getString(5));
        comment.setBloodtype(cursor.getString(6));



        return comment;
    }
}

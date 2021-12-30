package ast.bstu.oopproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbEvent {


    public static long add(SQLiteDatabase db, EventModel ev) {

        ContentValues values = new ContentValues();
        values.put("title", ev.title());
        values.put("imguri", ev.imguri());
        values.put("category", ev.category());
        values.put("address", ev.address());
        values.put("latitude", ev.latitude());
        values.put("longitude", ev.longitude());
        values.put("price", ev.price());
        values.put("date", ev.date());
        values.put("note", ev.note());
        values.put("priority", ev.priority());

        return db.insert(DbHelper.Events_table, null, values);
    }

    public static Cursor getAll(SQLiteDatabase db) {
        return db.rawQuery("select * from " + DbHelper.Events_table + ";", null);
    }

    public static Cursor getAllByPriority(SQLiteDatabase db) {
        return db.rawQuery("select * from " + DbHelper.Events_table + " order by priority asc", null);
    }

    public static Cursor getAllByTitle(SQLiteDatabase db) {
        return db.rawQuery("select * from " + DbHelper.Events_table + " order by title desc", null);
    }

    public static void deleteById(SQLiteDatabase db, int id) {
        //db.rawQuery(" delete from " + DbHelper.Events_table + " where id_event = ? ", new String[]{String.valueOf(id)});
        db.delete(DbHelper.Events_table, "id_event = ?" , new String[]{String.valueOf(id)});
        return;
    }
    public static void editNote(SQLiteDatabase db, int id, String new_note, String new_priority) {

       // db.rawQuery(" update " + DbHelper.Events_table + " set note = ?, priority = ? where id_event = ? ", new String[]{ new_note ,new_priority, String.valueOf(id) });
        ContentValues cv = new ContentValues();

        cv.put("note",new_note);
        cv.put("priority",new_priority);
        db.update(DbHelper.Events_table,cv,"id_event = ?",new String[]{String.valueOf(id)});

        return ;
    }
    public static Cursor findbyid(SQLiteDatabase db, int id) {
        return db.rawQuery("select * from " + DbHelper.Events_table + " where id_event = ?", new String[]{String.valueOf(id)});
    }
}

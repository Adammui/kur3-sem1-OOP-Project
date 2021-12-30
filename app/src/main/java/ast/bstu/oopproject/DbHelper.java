package ast.bstu.oopproject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DbHelper extends SQLiteOpenHelper {

    private static final int SCHEMA = 1;
    private static final String Database_name = "EventsDB";
    public static final String Events_table = "Events";
    private static final String Locations_table = "Locs";

    private static DbHelper instance = null;

    public DbHelper(Context context) {
        super(context, Database_name, null, SCHEMA);
    }


    public static DbHelper getInstance(Context context) {
        if(instance == null) instance = new DbHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Events_table + " (                    "
                + "id_event integer primary key autoincrement not null,"
                + "title text not null,                               "
                + "picture text , "
                + "category text not null, "
                + "address text not null, "
                + "latitude double not null, "
                + "longitude double not null, "
                + "price text not null, "
                + "date date not null, "
                + "note text, "
                + "priority text not null);"
        );

        db.execSQL("INSERT INTO " + Events_table +
                " ( title , category, address, latitude, longitude, price, date, note, priority) " +
                " VALUES ('Поход к врачу','здоровье', 'Минск, Больница 14',52.097622, 23.734051, 'бесценно', '2021/12/11', 'бобо в коленке', 1 );");
        db.execSQL("INSERT INTO " + Events_table +
                " ( title , category, address, latitude, longitude, price, date, note, priority) " +
                " VALUES ('Сдача проекта по java','учеба', 'Минск, БГТУ',53.891427, 27.5597439, 'мой сон', '2021/12/30', 'сдать до нг', 5 );");

        /*db.execSQL("create table " + LOCATIONS_table + " (                           "
                + "idtalon integer primary key autoincrement not null,       "
                + "prof_name text not null,                                   "
                + "iddoc integer not null,                                         "
                + "town text not null,                                     "
                + "time text not null,                                      "
                + "analysis text not null,                                    "
                // + "foreign key(prof_name) references " + PROFILE_TABLE + "(prof_name) "
                // + " on delete cascade on update cascade,
                + "foreign key(iddoc) references " + EVENTS_table + "(iddoc) "
                + " on delete cascade on update cascade );"
        );*/
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + Events_table);
        db.execSQL("drop table if exists " + Locations_table);
        onCreate(db);
    }
}


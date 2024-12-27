package Package.WC;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    // БАЗА ДАННЫХ
    public static final String DATABASE_NAME = "TestDrive16";

    // ТАБЛИЦЫ
    public static final String TABLE_OTCHET = "TestOtchet";
    public static final String WORK_DAYS = "Work_days";

    // ПЕРЕМЕННЫЕ ТАБЛИЦЫ TestOtchet
    public static final String KEY_ID = "_id";
    public static final String KEY_ZAGOLOVOK = "Zagolovok";
    public static final String KEY_START_TIME = "StartVremya";
    public static final String KEY_END_TIME = "EndVremya";
    public static final String KEY_TIME = "TimeWork";
    public static final String KEY_STAVKA = "Stavka";


    // ПЕРЕМЕННЫЕ ТАБЛИЦЫ Work_days
    public static final String WORK_ID = "_id";
    public static final String WORK_ZAGL = "zagl";
    public static final String WORK_DATA = "Data_work_day";
    public static final String WORK_TIME = "Time_work";
    public static final String WORK_ZP = "workZP";
    public static final String WORK_CHAI = "Dop_zp_work_day";

    public DBHelper(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // СОЗДАНИЕ ТАБЛИЦЫ TestOtchet
        db.execSQL("create table " + TABLE_OTCHET + " (" + KEY_ID
        + " integer primary key," + KEY_ZAGOLOVOK + " text not null," + KEY_START_TIME + " text not null," + KEY_END_TIME + " text not null,"
                + KEY_TIME + " real not null," + KEY_STAVKA + " integer not null" + ")");

        // СОЗДАНИЕ ТАБЛИЦЫ Work_days
        db.execSQL("create table " + WORK_DAYS + " (" + WORK_ID
                + " integer primary key," + WORK_ZAGL + " text," + WORK_DATA + " text,"
                + WORK_TIME + " real," + WORK_ZP + " integer,"
                + WORK_CHAI + " integer" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_OTCHET);
        onCreate(db);
    }
}

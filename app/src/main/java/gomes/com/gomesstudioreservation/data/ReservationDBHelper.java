package gomes.com.gomesstudioreservation.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import gomes.com.gomesstudioreservation.data.ReservationContract.CityEntry;
import gomes.com.gomesstudioreservation.data.ReservationContract.StudioEntry;
import gomes.com.gomesstudioreservation.data.ReservationContract.UserEntry;

import static gomes.com.gomesstudioreservation.data.ReservationContract.UserEntry.KEY_USER_NAME;

public class ReservationDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reservation.db";
    private static final int DATABASE_VERSION = 2;

    public ReservationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqlCreateUserTable(sqLiteDatabase);
        sqlCreateStudioTable(sqLiteDatabase);
        sqlCreateCityTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + UserEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + StudioEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + CityEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    private void sqlCreateStudioTable(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_STUDIO_TABLE =
                "CREATE TABLE " + StudioEntry.TABLE_NAME + " (" +
                StudioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StudioEntry.COLUMN_STUDIO_ID + " INTEGER NOT NULL, " +
                StudioEntry.COLUMN_STUDIO_NAMA + " TEXT NOT NULL, " +
                StudioEntry.COLUMN_STUDIO_ALAMAT + " TEXT NOT NULL, " +
                StudioEntry.COLUMN_STUDIO_HARGA + " TEXT NOT NULL, " +
                StudioEntry.COLUMN_STUDIO_TELEPON + " TEXT NOT NULL " +
                " ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_STUDIO_TABLE);
    }

    private void sqlCreateCityTable(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_CITY_TABLE =
                "CREATE TABLE " + CityEntry.TABLE_NAME + " (" +
                CityEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CityEntry.COLUMN_CITY_ID + " INTEGER NOT NULL, " +
                CityEntry.COLUMN_CITY_NAMA + " TEXT NOT NULL, UNIQUE (" +
                CityEntry.COLUMN_CITY_ID + ", " +
                CityEntry.COLUMN_CITY_NAMA + ") ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_CITY_TABLE);
    }

    private void sqlCreateUserTable(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_USER_TABLE =
                "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_USER_NAME + " TEXT NOT NULL, " +
                UserEntry.KEY_USER_EMAIL + " TEXT NOT NULL, " +
                UserEntry.KEY_TIPE_USER + " INTEGER NOT NULL, " +
                UserEntry.KEY_USER_NO_HP + " TEXT NOT NULL, " +
                UserEntry.KEY_USER_PASSWORD + " TEXT NOT NULL, UNIQUE (" +
                KEY_USER_NAME + ", " +
                UserEntry.KEY_USER_EMAIL + ") ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
    }
}

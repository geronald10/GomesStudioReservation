package gomes.com.gomesstudioreservation.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import gomes.com.gomesstudioreservation.data.ReservationContract.StudioEntry;

public class ReservatioDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reservation.db";
    private static final int DATABASE_VERSION = 1;

    public ReservatioDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqlCreateStudioTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + StudioEntry.TABLE_NAME);
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
                StudioEntry.COLUMN_STUDIO_TELEPON + " TEXT NOT NULL, " +
                StudioEntry.COLUMN_STUDIO_OPEN_HOUR + " INTEGER NOT NULL, " +
                StudioEntry.COLUMN_STUDIO_CLOSE_HOUR + " INTEGER NOT NULL " +
                " ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_STUDIO_TABLE);
    }
}

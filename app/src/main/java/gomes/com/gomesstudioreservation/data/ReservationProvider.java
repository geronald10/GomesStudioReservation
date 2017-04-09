package gomes.com.gomesstudioreservation.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

public class ReservationProvider extends ContentProvider {

    public static final int CODE_CITY = 100;
    public static final int CODE_CITY_WITH_ID = 110;
    public static final int CODE_STUDIO = 200;
    public static final int CODE_STUDIO_WITH_ID = 210;
    public static final int CODE_USER = 300;
    public static final int CODE_USER_WITH_ID = 310;
    public static final int CODE_JADWAL = 400;
    public static final int CODE_JADWAL_WITH_ID = 410;

    private static final UriMatcher cUriMatcher = buildUriMatcher();
    private ReservationDBHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ReservationContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ReservationContract.PATH_CITY, CODE_CITY);
        matcher.addURI(authority, ReservationContract.PATH_CITY + "/#", CODE_CITY_WITH_ID);
        matcher.addURI(authority, ReservationContract.PATH_USER, CODE_USER);
        matcher.addURI(authority, ReservationContract.PATH_USER + "/#", CODE_USER_WITH_ID);
        matcher.addURI(authority, ReservationContract.PATH_STUDIO, CODE_STUDIO);
        matcher.addURI(authority, ReservationContract.PATH_STUDIO + "/#", CODE_STUDIO_WITH_ID);
        matcher.addURI(authority, ReservationContract.PATH_JADWAL, CODE_JADWAL);
        matcher.addURI(authority, ReservationContract.PATH_JADWAL + "/#", CODE_JADWAL_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ReservationDBHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@Nullable Uri uri, @Nullable ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsInserted;
        Log.d("masuk sini bulk insert", String.valueOf(uri));

        switch (cUriMatcher.match(uri)) {
            case CODE_CITY:
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ReservationContract.CityEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    db.close();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;

            case CODE_STUDIO:
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ReservationContract.StudioEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    db.close();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;

            case CODE_USER:
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ReservationContract.UserEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    db.close();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;

            case CODE_JADWAL:
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ReservationContract.JadwalEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    db.close();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        Log.d("uri masuk", String.valueOf(uri));

        switch (cUriMatcher.match(uri)) {
            case CODE_CITY: {
                queryBuilder.setTables(ReservationContract.CityEntry.TABLE_NAME);
                break;
            }

            case CODE_STUDIO: {
                queryBuilder.setTables(ReservationContract.StudioEntry.TABLE_NAME);
                break;
            }

            case CODE_USER: {
                queryBuilder.setTables(ReservationContract.UserEntry.TABLE_NAME);
                break;
            }

            case CODE_USER_WITH_ID: {
                queryBuilder.setTables(ReservationContract.UserEntry.TABLE_NAME);
                queryBuilder.appendWhere(ReservationContract.UserEntry._ID + "="
                        + uri.getLastPathSegment());
                break;
            }

            case CODE_JADWAL: {
                queryBuilder.setTables(ReservationContract.JadwalEntry.TABLE_NAME);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        Cursor cursor = queryBuilder.query(
                mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        Log.d("From Provider", "query total " + cursor.getCount());
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int delete_count = 0;

        switch (cUriMatcher.match(uri)) {
            case CODE_USER:
                Log.d("From Provider", "masuk delete method");
                delete_count = db.delete(ReservationContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case CODE_CITY:
                Log.d("From Provider", "masuk delete method");
                delete_count = db.delete(ReservationContract.CityEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case CODE_STUDIO:
                Log.d("From Provider", "masuk delete method");
                delete_count = db.delete(ReservationContract.StudioEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case CODE_JADWAL:
                Log.d("From Provider", "masuk delete method");
                delete_count = db.delete(ReservationContract.JadwalEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (selection == null || delete_count != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return delete_count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}

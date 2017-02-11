package gomes.com.gomesstudioreservation.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class ReservationContract {

    public static final String CONTENT_AUTHORITY = "gomes.com.gomesstudioreservation";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_STUDIO = "studio";
    public static final String PATH_CITY = "city";
    public static final String PATH_USER = "user";

    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_USER)
                .build();

        public static final String TABLE_NAME = "user";
        public static final String KEY_USER_NAME = "user_name";
        public static final String KEY_USER_EMAIL = "user_email";
        public static final String KEY_TIPE_USER = "tipe_user";
        public static final String KEY_USER_NO_HP = "user_hp";
        public static final String KEY_USER_PASSWORD = "user_password";
    }

    public static final class StudioEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_STUDIO)
                .build();

        public static final String TABLE_NAME = "studio";
        public static final String COLUMN_STUDIO_ID = "studio_id";
        public static final String COLUMN_STUDIO_NAMA = "studio_nama";
        public static final String COLUMN_STUDIO_ALAMAT = "studio_alamat";
        public static final String COLUMN_STUDIO_TELEPON = "studio_telepon";
        public static final String COLUMN_STUDIO_HARGA = "studio_harga";
    }

    public static final class CityEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CITY)
                .build();

        public static final String TABLE_NAME = "city";

        public static final String COLUMN_CITY_ID = "city_id";
        public static final String COLUMN_CITY_NAMA = "city_nama";
    }
}

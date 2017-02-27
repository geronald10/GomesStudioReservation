package gomes.com.gomesstudioreservation.utilities;

/*
    these utilities will be used to communicate with the servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    // Registration API
    public static final String REGISTER_CHAMBER_URL =
            "http://128.199.139.178/reservasistudio/public/index.php/u/register";
    // Login API
    public static final String LOGIN_CHAMBER_URL =
            "http://128.199.139.178/reservasistudio/public/index.php/u/login";
    // List Kota + Studio API
    public static final String KOTA_STUDIO_CHAMBER_URL =
            "http://128.199.139.178/reservasistudio/public/index.php/j/kotastudio";
    // Search Jadwal
    public static final String MANAGE_BOOKING_CHAMBER_URL =
            "http://128.199.139.178/reservasistudio/public/index.php/j/jadwal";
    // Store Reservasi
    public static final String STORE_RESERVATION_CHAMBER_URL =
            "http://128.199.139.178/reservasistudio/public/index.php/r/store";
    // History Reservasi
    public static final String HISTORY_RESERVATION_CHAMBER_URL =
            "http://128.199.139.178/reservasistudio/public/index.php/r/history";
    // Confirm Reservasi
    public static final String CONFIRM_RESERVATION_CHAMBER_URL =
            "http://128.199.139.178/reservasistudio/public/index.php/r/issue";
    // Cancel Reservasi
    public static final String CANCEL_RESERVATION_CHAMBER_URL =
            "http://128.199.139.178/reservasistudio/public/index.php/r/cancel";
    // Get Profile Data
    public static final String GET_USER_PROFILE_CHAMBER_URL =
            "http://128.199.139.178/reservasistudio/public/index.php/u/view";
    // Edit Profile Data
    public static final String EDIT_USER_PROFILE_CHAMBER_URL =
            "http://128.199.139.178/reservasistudio/public/index.php/u/edit";
    // Kontak Studio for Payment
    public static final String CONTACT_PAYMENT_STUDIO =
            "http://128.199.139.178/reservasistudio/public/index.php/r/kontak";
    // Get New Jadwal
    public static final String GET_USER_RESERVATION_DATA_DETAIL =
            "http://128.199.139.178/reservasistudio/public/index.php/j/newjadwal";
    // Send Edit Jadwal Request
    public static final String EDIT_JADWAL_CHAMBER_URL =
            "http://128.199.139.178/reservasistudio/public/index.php/r/edit";
}

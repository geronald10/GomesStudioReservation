package gomes.com.gomesstudioreservation.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

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

    // format query parameter to API
    private static final String USERNAME_PARAM = "user_name";
    private static final String USER_EMAIL_PARAM = "user_email";
    private static final String USER_HP_PARAM = "user_hp";
    private static final String USER_PASSWORD_PARAM = "user_password";

    // Build the URL used to register new user.
    private static URL buildUrlRegisterNewUser(String username, String email, String userHP, String userPassword) {
        Uri reservationQueryUri = Uri.parse(REGISTER_CHAMBER_URL).buildUpon()
                .appendQueryParameter(USERNAME_PARAM, username)
                .appendQueryParameter(USER_EMAIL_PARAM, email)
                .appendQueryParameter(USER_HP_PARAM, userHP)
                .appendQueryParameter(USER_PASSWORD_PARAM, userPassword)
                .build();

        try {
            URL reservationQueryUrl = new URL(reservationQueryUri.toString());
            Log.v(TAG, "URL: " + reservationQueryUrl);
            return reservationQueryUrl;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}

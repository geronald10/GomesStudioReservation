package gomes.com.gomesstudioreservation;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import gomes.com.gomesstudioreservation.data.ReservationContract;
import gomes.com.gomesstudioreservation.data.ReservationSessionManager;
import gomes.com.gomesstudioreservation.utilities.AppController;
import gomes.com.gomesstudioreservation.utilities.NetworkUtils;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private ReservationSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        inputEmail = (EditText) findViewById(R.id.edt_email_login);
        inputPassword = (EditText) findViewById(R.id.edt_password_login);
        Button btnLogin = (Button) findViewById(R.id.btnSignIn);
        Button btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress Dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new ReservationSessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if(session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, BookingActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(operation);
        btnLinkToRegister.setOnClickListener(operation);
    }

    View.OnClickListener operation = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.btnSignIn:
                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();

                    if(!email.isEmpty() && !password.isEmpty()) {
                        checkLogin(email, password);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.btnLinkToRegisterScreen:
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Sign in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                NetworkUtils.LOGIN_CHAMBER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in. Create Login session
                        session.setLogin(true);

                        JSONObject user = jsonObject.getJSONObject("user");
                        String name = user.getString(ReservationContract.UserEntry.KEY_USER_NAME);
                        String email = user.getString(ReservationContract.UserEntry.KEY_USER_EMAIL);
                        String tipeUser = user.getString(ReservationContract.UserEntry.KEY_TIPE_USER);
                        String noHp = user.getString(ReservationContract.UserEntry.KEY_USER_NO_HP);
                        String password = user.getString(ReservationContract.UserEntry.KEY_USER_PASSWORD);

                        // Inserting row in users table
                        addUser(name, email, tipeUser, noHp, password);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                BookingActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put(ReservationContract.UserEntry.KEY_USER_EMAIL, email);
                params.put(ReservationContract.UserEntry.KEY_USER_PASSWORD, password);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void addUser(String username, String email, String tipeUser, String noHP, String password) {
        Uri mNewUri = ReservationContract.UserEntry.CONTENT_URI;
        ContentValues values = new ContentValues();

        values.put(ReservationContract.UserEntry.KEY_USER_NAME, username);
        values.put(ReservationContract.UserEntry.KEY_USER_EMAIL, email);
        values.put(ReservationContract.UserEntry.KEY_TIPE_USER, tipeUser);
        values.put(ReservationContract.UserEntry.KEY_USER_NO_HP, noHP);
        values.put(ReservationContract.UserEntry.KEY_USER_PASSWORD, password);

        getApplicationContext().getContentResolver().insert(mNewUri, values);
    }
}


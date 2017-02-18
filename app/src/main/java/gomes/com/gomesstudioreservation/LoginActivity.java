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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gomes.com.gomesstudioreservation.data.ReservationContract;
import gomes.com.gomesstudioreservation.data.ReservationSessionManager;
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
        StringRequest strReq;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        pDialog.setMessage("Sign in ...");
        showDialog();

        strReq = new StringRequest(Request.Method.POST,
                NetworkUtils.LOGIN_CHAMBER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    // Check for error node in json
                    if (code.equals("1")) {
                        // user successfully logged in. Create Login session
                        session.setLogin(true);

                        JSONObject user = jsonObject.getJSONObject("user");
                        String userId = user.getString(ReservationContract.UserEntry.KEY_USER_ID);
                        String name = user.getString(ReservationContract.UserEntry.KEY_USER_NAME);
                        String email = user.getString(ReservationContract.UserEntry.KEY_USER_EMAIL);
                        String noHp = user.getString(ReservationContract.UserEntry.KEY_USER_NO_HP);

                        // Inserting row in users table
                        addUser(userId, name, email, noHp);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                BookingActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        // Error in login. Get the error message
                        Toast.makeText(getApplicationContext(),
                                status, Toast.LENGTH_LONG).show();
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
        strReq.setTag(tag_string_req);
        requestQueue.add(strReq);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void addUser(String userId, String username, String email, String noHP) {
        //        ReservationDBHelper db = new ReservationDBHelper(BookingActivity.this);
        Uri mNewUri = ReservationContract.UserEntry.CONTENT_URI;
        final ContentValues values = new ContentValues();
//
//        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {
//        };
        values.put(ReservationContract.UserEntry.KEY_USER_ID, userId);
        values.put(ReservationContract.UserEntry.KEY_USER_NAME, username);
        values.put(ReservationContract.UserEntry.KEY_USER_EMAIL, email);
        values.put(ReservationContract.UserEntry.KEY_USER_NO_HP, noHP);
//
//        queryHandler.startInsert(-1, null, mNewUri, values);
//        Log.d("Insertion from login ", values.toString());

        List<ContentValues> userValues = new ArrayList<ContentValues>();
        userValues.add(values);

        this.getContentResolver().bulkInsert(
                mNewUri,
                userValues.toArray(new ContentValues[1]));
        Log.d("jumlah data dikirim", String.valueOf(userValues.size()));

//        Uri mNewUri = ReservationContract.UserEntry.CONTENT_URI;
//        final ContentValues values = new ContentValues();
//
//        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {
//        };
//        values.put(ReservationContract.UserEntry.KEY_USER_NAME, username);
//        values.put(ReservationContract.UserEntry.KEY_USER_EMAIL, email);
//        values.put(ReservationContract.UserEntry.KEY_USER_NO_HP, noHP);
//
//        queryHandler.startInsert(-1, null, mNewUri, values);
//        Log.d("Insertion from login ", values.toString());
    }
}


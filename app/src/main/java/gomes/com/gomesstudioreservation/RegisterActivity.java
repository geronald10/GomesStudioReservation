package gomes.com.gomesstudioreservation;

import android.app.ProgressDialog;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

import gomes.com.gomesstudioreservation.data.ReservationContract;
import gomes.com.gomesstudioreservation.data.ReservationSessionManager;
import gomes.com.gomesstudioreservation.utilities.NetworkUtils;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText edtUsername;
    private EditText edtUserEmail;
    private EditText edtUserPhone;
    private EditText edtUserPassword;
    private EditText edtUserConfirmPassword;
    private Button btnRegister;
    private Button btnLinkToLogin;
    private ProgressDialog pDialog;
    private ReservationSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtUserEmail = (EditText) findViewById(R.id.edt_user_email);
        edtUserPhone = (EditText) findViewById(R.id.edt_user_phone_number);
        edtUserPassword = (EditText) findViewById(R.id.edt_password);
        edtUserConfirmPassword = (EditText) findViewById(R.id.edt_confirm_password);

        btnRegister = (Button) findViewById(R.id.register_button);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new ReservationSessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    BookingActivity.class);
            startActivity(intent);
            finish();
        }

        btnRegister.setOnClickListener(operation);
        btnLinkToLogin.setOnClickListener(operation);
    }

    View.OnClickListener operation = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.register_button:
                    String name = edtUsername.getText().toString().trim();
                    String email = edtUserEmail.getText().toString().trim();
                    String noHp = edtUserPhone.getText().toString().trim();
                    String password = edtUserPassword.getText().toString().trim();
                    String confirmPassword = edtUserConfirmPassword.getText().toString().trim();

                    if (!name.isEmpty() && !email.isEmpty() && !noHp.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
                        if (password.equals(confirmPassword))
                            registerUser(name, email, String.valueOf(1), noHp, password);
                        else
                            Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter your details!", Toast.LENGTH_LONG)
                                .show();
                    }
                    break;
                case R.id.btnLinkToLoginScreen:
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    /**
     * Function to store user in MySQL database will post params(name,
     * email, noHp, password) to register url
     */
    private void registerUser(final String name, final String email, final String tipeUser, final String noHp, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        StringRequest strReq;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        pDialog.setMessage("Sign up ...");
        showDialog();

        strReq = new StringRequest(Request.Method.POST,
                NetworkUtils.REGISTER_CHAMBER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        // User successfully stored in server.
                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "masuk sini");

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error occurred in registration. Get the error message
                        Toast.makeText(getApplicationContext(),
                                status, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put(ReservationContract.UserEntry.KEY_USER_NAME, name);
                params.put(ReservationContract.UserEntry.KEY_USER_EMAIL, email);
                params.put(ReservationContract.UserEntry.KEY_TIPE_USER, tipeUser);
                params.put(ReservationContract.UserEntry.KEY_USER_NO_HP, noHp);
                params.put(ReservationContract.UserEntry.KEY_USER_PASSWORD, password);
                return params;
            }
        };
        // Adding request to requestqueue
        strReq.setTag(tag_string_req);
        requestQueue.add(strReq);
        Log.d("stringRequest:", String.valueOf(strReq));
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

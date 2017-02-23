package gomes.com.gomesstudioreservation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
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

public class ProfileActivity extends BaseActivity {

    private final String TAG = ProfileActivity.class.getSimpleName();

    private Context mContext;
    private ProgressDialog progressDialog;

    private EditText edtNama;
    private EditText edtEmail;
    private EditText edtNoHp;
    private EditText edtOldPassword;
    private EditText edtNewPassword;
    private EditText edtConfirmNewPassword;
    private EditText edtPasswordConfirmation;
    private Button btnSaveProfile;
    private Button btnChangePassword;
    private Button btnCancelEdit;
    private Button btnEditProfile;
    private Button btnSaveNewPassword;

    private String userId;
    private String nameFetched;
    private String emailFetched;
    private String noHpFetched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mContext = this;

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        HashMap<String, String> user = session.getUserDetails();
        Log.d("user Id from profile", user.get(ReservationSessionManager.KEY_USER_ID));
        userId = user.get(ReservationSessionManager.KEY_USER_ID);
        getUserProfileData(userId);

        edtNama = (EditText)findViewById(R.id.edtNamaUser);
        edtEmail = (EditText)findViewById(R.id.edtUserEmail);
        edtNoHp = (EditText)findViewById(R.id.phoneNumber);
        edtPasswordConfirmation = (EditText)findViewById(R.id.edtPasswordForChanges);
        edtOldPassword = (EditText)findViewById(R.id.edtCurrentPassword);
        edtNewPassword = (EditText)findViewById(R.id.edtNewPassword);
        edtConfirmNewPassword = (EditText)findViewById(R.id.edtConfirmNewPassword);
        btnEditProfile = (Button)findViewById(R.id.btnEditUserInfo);
        btnSaveProfile = (Button)findViewById(R.id.btnSaveEditUserInfo);
        btnChangePassword = (Button)findViewById(R.id.btnChangePassword);
        btnSaveNewPassword = (Button)findViewById(R.id.btnSaveNewPassword);
        btnCancelEdit = (Button)findViewById(R.id.btnCancelEditUserInfo);

        edtNama.setEnabled(false);
        edtEmail.setEnabled(false);
        edtNoHp.setEnabled(false);

        btnEditProfile.setOnClickListener(operate);
        btnSaveProfile.setOnClickListener(operate);
        btnChangePassword.setOnClickListener(operate);
        btnSaveNewPassword.setOnClickListener(operate);
        btnCancelEdit.setOnClickListener(operate);
    }

    View.OnClickListener operate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.btnEditUserInfo:
                    edtNama.setEnabled(true);
                    edtEmail.setEnabled(true);
                    edtNoHp.setEnabled(true);
                    btnEditProfile.setVisibility(View.INVISIBLE);
                    btnCancelEdit.setVisibility(View.VISIBLE);
                    btnSaveProfile.setVisibility(View.VISIBLE);
                    edtPasswordConfirmation.setVisibility(View.VISIBLE);
                    break;
                case R.id.btnSaveEditUserInfo:
                    String newUserName = edtNama.getText().toString();
                    String newEmail = edtEmail.getText().toString();
                    String newNoHp = edtNoHp.getText().toString();
                    String passwordConfirmation = edtPasswordConfirmation.getText().toString();
                    String password = "";
                    if(newUserName.length() > 0 && newEmail.length() > 0 && newNoHp.length() > 0 && passwordConfirmation.length() > 0) {
                        updateUserProfileData(userId, newUserName, newEmail, newNoHp, passwordConfirmation, password);
                        btnEditProfile.setVisibility(View.VISIBLE);
                        btnSaveProfile.setVisibility(View.INVISIBLE);
                        btnCancelEdit.setVisibility(View.INVISIBLE);
                        edtPasswordConfirmation.getText().clear();
                        edtPasswordConfirmation.setVisibility(View.INVISIBLE);
                        edtNama.setEnabled(false);
                        edtEmail.setEnabled(false);
                        edtNoHp.setEnabled(false);
                        getUserProfileData(userId);
                    } else {
                        Toast.makeText(mContext, "Semua data harus terisi", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnChangePassword:
                    edtOldPassword.getText().clear();
                    edtNewPassword.getText().clear();
                    edtConfirmNewPassword.getText().clear();
                    edtOldPassword.setEnabled(true);
                    edtNewPassword.setEnabled(true);
                    edtConfirmNewPassword.setEnabled(true);
                    edtOldPassword.setVisibility(edtOldPassword.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
                    edtNewPassword.setVisibility(edtNewPassword.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
                    edtConfirmNewPassword.setVisibility(edtConfirmNewPassword.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
                    btnSaveNewPassword.setVisibility(btnSaveNewPassword.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
                    break;
                case R.id.btnSaveNewPassword:
                    String oldPassword = edtOldPassword.getText().toString();
                    String newPassword = edtNewPassword.getText().toString();
                    String confirmNewPassword = edtConfirmNewPassword.getText().toString();

                    if (oldPassword.equals(newPassword)) {
                        Toast.makeText(mContext, "Password lama dan baru tidak boleh sama", Toast.LENGTH_SHORT).show();
                    } else if (!newPassword.equals(confirmNewPassword)) {
                        Toast.makeText(mContext, "Konfirmasi Password baru tidak cocok", Toast.LENGTH_SHORT).show();
                    } else {
                        updateUserProfileData(userId, nameFetched, emailFetched, noHpFetched, oldPassword, newPassword);
                        edtNewPassword.setVisibility(View.INVISIBLE);
                        edtOldPassword.setVisibility(View.INVISIBLE);
                        edtConfirmNewPassword.setVisibility(View.INVISIBLE);
                        btnSaveNewPassword.setVisibility(View.INVISIBLE);
                        edtOldPassword.setEnabled(false);
                        edtNewPassword.setEnabled(false);
                        edtConfirmNewPassword.setEnabled(false);
                    }
                    break;
                case R.id.btnCancelEditUserInfo:
                    getUserProfileData(userId);
                    edtNama.setEnabled(false);
                    edtEmail.setEnabled(false);
                    edtNoHp.setEnabled(false);
                    edtPasswordConfirmation.setVisibility(View.INVISIBLE);
                    btnSaveProfile.setVisibility(View.INVISIBLE);
                    btnCancelEdit.setVisibility(View.INVISIBLE);
                    btnEditProfile.setVisibility(View.VISIBLE);
            }
        }
    };

    public void loadUserDetailData(String name, String email, String noHp) {
        edtNama.setText(name);
        edtEmail.setText(email);
        edtNoHp.setText(noHp);
    }

    public void getUserProfileData(final String userId) {
        // Tag used to cancel the request
        String tag_string_req = "req_user_detail_data";
        StringRequest stringRequest;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        progressDialog.setMessage("get user detail data..");
        showDialog();

        stringRequest = new StringRequest(Request.Method.POST,
                NetworkUtils.GET_USER_PROFILE_CHAMBER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "User Detail Info Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    // Check for error node in json
                    if (code.equals("1")) {
                        // user successfully logged in. Create Login session
                        JSONObject user = jsonObject.getJSONObject("user");
                        nameFetched = user.getString(ReservationContract.UserEntry.KEY_USER_NAME);
                        emailFetched = user.getString(ReservationContract.UserEntry.KEY_USER_EMAIL);
                        noHpFetched = user.getString(ReservationContract.UserEntry.KEY_USER_NO_HP);

                        Toast.makeText(mContext, status, Toast.LENGTH_SHORT).show();

                        loadUserDetailData(nameFetched, emailFetched, noHpFetched);
                    }
                    hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Fetch Data Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put(ReservationContract.UserEntry.KEY_USER_ID, userId);

                return params;
            }
        };

        // Adding request to request queue
        stringRequest.setTag(tag_string_req);
        requestQueue.add(stringRequest);
    }

    public void updateUserProfileData(final String userId, final String nama, final String email,
                                      final String noHp, final String userPassword,
                                      final String userNewPassword) {
        // Tag used to cancel the request
        String tag_string_req = "req_user_update_data";
        StringRequest stringRequest;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        progressDialog.setMessage("update user detail data..");
        showDialog();

        stringRequest = new StringRequest(Request.Method.POST,
                NetworkUtils.EDIT_USER_PROFILE_CHAMBER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "User Detail Info Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    // Check for error node in json
                    hideDialog();
                    if (code.equals("1")) {
                        Toast.makeText(mContext, status, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Sending Data Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put(ReservationContract.UserEntry.KEY_USER_ID, userId);
                params.put(ReservationContract.UserEntry.KEY_USER_NAME, nama);
                params.put(ReservationContract.UserEntry.KEY_USER_EMAIL, email);
                params.put(ReservationContract.UserEntry.KEY_USER_NO_HP, noHp);
                params.put("user_password", userPassword);
                params.put("user_new_password", userNewPassword);

                return params;
            }
        };

        // Adding request to request queue
        stringRequest.setTag(tag_string_req);
        requestQueue.add(stringRequest);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}

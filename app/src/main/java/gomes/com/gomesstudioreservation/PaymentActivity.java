package gomes.com.gomesstudioreservation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
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

import gomes.com.gomesstudioreservation.utilities.NetworkUtils;

public class PaymentActivity extends AppCompatActivity {

    private final String TAG = PaymentActivity.class.getSimpleName();

    private TextView tvBankAccountNumber;
    private TextView tvContactPerson;
    private TextView tvLimitPaymentTime;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        tvBankAccountNumber = (TextView)findViewById(R.id.tv_bank_account_number);
        tvContactPerson = (TextView)findViewById(R.id.tv_contact_person);
        tvLimitPaymentTime = (TextView)findViewById(R.id.tv_limit_payment_time);

        Intent intent = getIntent();
        int reservasiId = intent.getIntExtra("reservasi_id", 0);
        String reservasiBatas = intent.getStringExtra("reservasi_batas");

        getPaymentInfo(String.valueOf(reservasiId));
        tvLimitPaymentTime.setText(reservasiBatas);

        // Set up action bar.
        setupToolbar();
    }

    private void getPaymentInfo(final String reservasiId) {
        // Tag used to cancel the request
        String tag_string_req = "req_payment_info";
        StringRequest stringRequest;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        progressDialog.setMessage("Get Payment info ...");
        showDialog();

        stringRequest = new StringRequest(Request.Method.POST,
                NetworkUtils.CONTACT_PAYMENT_STUDIO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Booking Review Response: " + response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    // Check for error node in json
                    if (code.equals("1")) {
                        Log.d("status respon", status);

                        JSONObject contact = jsonObject.getJSONObject("studio");
                        String namaContact = contact.getString("studio_nama");
                        String alamatContact = contact.getString("studio_alamat");
                        String teleponContact = contact.getString("studio_telepon");
                        String rekeningContact = contact.getString("studio_rekening");

                        String contactDetails = teleponContact + " (" + namaContact + ")\n" + alamatContact;

                        tvBankAccountNumber.setText(rekeningContact);
                        tvContactPerson.setText(contactDetails);
                    } else {
                        // Error in sending data. Get the error message
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
                Log.e(TAG, "get ContactPerson Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("reservasi_id", reservasiId);

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

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TextView textToolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textToolbarTitle.setText(R.string.payment_info);
    }
}

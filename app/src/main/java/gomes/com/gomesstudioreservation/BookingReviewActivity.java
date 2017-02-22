package gomes.com.gomesstudioreservation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import gomes.com.gomesstudioreservation.data.ReservationContract;
import gomes.com.gomesstudioreservation.data.ReservationSessionManager;
import gomes.com.gomesstudioreservation.utilities.NetworkUtils;
import gomes.com.gomesstudioreservation.utilities.RupiahCurrencyFormat;

public class BookingReviewActivity extends AppCompatActivity {

    private final String TAG = BookingReviewActivity.class.getSimpleName();

    private Context mContext;
    private ProgressDialog progressDialog;
    private ReservationSessionManager session;

    String userId, bandName, studioName, date, hour, roomId, stringRoom, jadwalId;
    int tagihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_review);
        mContext = this;
        session = new ReservationSessionManager(getApplicationContext());
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        userId = user.get(ReservationSessionManager.KEY_USER_ID);
        jadwalId = intent.getStringExtra("jadwal_id");
        bandName = intent.getStringExtra("bandName");
        studioName = intent.getStringExtra("studioName");
        date = intent.getStringExtra("selected_date");
        hour = intent.getStringExtra("selected_hour");
        roomId = intent.getStringExtra("selected_room");
        tagihan = intent.getIntExtra("tagihan", 0);

        RupiahCurrencyFormat toRupiah = new RupiahCurrencyFormat();
        String totalTagihan = toRupiah.toRupiahFormat(String.valueOf(tagihan));
        stringRoom =  getResources().getString(R.string.nama_room) + " " + roomId;

        TextView tvBandName = (TextView) findViewById(R.id.tv_band_name);
        TextView tvStudioName = (TextView) findViewById(R.id.tv_studio_name);
        TextView tvDate = (TextView) findViewById(R.id.tv_date);
        TextView tvHour = (TextView) findViewById(R.id.tv_hour);
        TextView tvRoom = (TextView) findViewById(R.id.tv_room);
        TextView tvTotalPayment = (TextView) findViewById(R.id.tv_total_payment);
        Button btnContinue = (Button) findViewById(R.id.btn_continue_to_payment);

        tvBandName.setText(bandName);
        tvStudioName.setText(studioName);
        tvDate.setText(date);
        tvHour.setText(hour);
        tvRoom.setText(stringRoom);
        tvTotalPayment.setText(totalTagihan);

        btnContinue.setOnClickListener(operasi);
    }

    View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_continue_to_payment:
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(R.string.dialog_message)
                            .setTitle(R.string.dialog_title);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            checkBookingReview(userId, roomId, bandName, date, jadwalId);
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
            }
        }
    };

    private void checkBookingReview(final String userId, final String roomId, final String namaBand, final String tanggal, final String jadwalId) {
        // Tag used to cancel the request]
        Log.d("userId", userId);
        Log.d("roomId", roomId);
        Log.d("namaBand", namaBand);
        Log.d("tanggal", tanggal);
        Log.d("jadwalId", jadwalId);

        String tag_string_req = "req_booking_review";
        StringRequest stringRequest;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        progressDialog.setMessage("Get Booking Review Details ...");
        showDialog();

        stringRequest = new StringRequest(Request.Method.POST,
                NetworkUtils.STORE_RESERVATION_CHAMBER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Booking Review Response: " + response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");

                    if (code.equals("1")) {
                        Log.d("status respon", status);

                        JSONObject reservasi = jsonObject.getJSONObject("reservasi");
                        int reservasiId = reservasi.getInt("id");

                        // Launch Booking Review Activity;
                        Intent intentToPayment = new Intent(BookingReviewActivity.this, PaymentActivity.class);
                        intentToPayment.putExtra("reservasi_id", reservasiId);
                        startActivity(intentToPayment);
                        finish();

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
                Log.e(TAG, "get review details error: " + error.getMessage());
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
                params.put(ReservationContract.JadwalEntry.COLUMN_ROOM_ID, roomId);
                params.put("nama_band", namaBand);
                params.put(ReservationContract.JadwalEntry.COLUMN_TANGGAL, tanggal);
                params.put(ReservationContract.JadwalEntry.COLUMN_JADWAL_ID, jadwalId);

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

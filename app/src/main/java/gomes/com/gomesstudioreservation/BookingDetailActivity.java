package gomes.com.gomesstudioreservation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import gomes.com.gomesstudioreservation.utilities.RupiahCurrencyFormat;

public class BookingDetailActivity extends AppCompatActivity {

    private final String TAG = BookingDetailActivity.class.getSimpleName();

    private Context mContext;
    private ProgressDialog progressDialog;

    private CardView cardViewRefund;

    private Button btnContinueToPayment;
    private Button btnContinueToEditReservation;
    private Button btnRequestCancelBooking;

    private ImageView ivStatusReservasi;
    private TextView tvStatusReservasi;
    private TextView tvNomorBooking;
    private TextView tvNamaBand;
    private TextView tvTagihan;
    private TextView tvWaktuBooking;
    private TextView tvTanggal;
    private TextView tvRefundStatus;
    private TextView tvRefundAt;
    private TextView tvTotalRefund;
    private TextView tvRoomNama;
    private TextView tvStudioNama;
    private TextView tvBatasPembayaran;
    private TextView tvJadwalReservasi;

    private int reservationId;
    private String reserveNomorBooking;
    private String reserveNamaBand;
    private String reserveTagihan;
    private String reserveStatus;
    private String reserveWaktuBooking;
    private String reserveTanggal;
    private String reserveBatas;
    private String reserveRefund;
    private String reserveRefundAt;
    private String refundStatus;
    private String reserveRoomNama;
    private String reserveStudioNama;
    private String reserveJadwal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        mContext = this;

        // Set up action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Booking Detail");

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        reservationId = intent.getIntExtra("reservasi_id", 0);
        reserveNomorBooking = intent.getStringExtra("reservasi_nomor_booking");
        reserveNamaBand = intent.getStringExtra("reservasi_nama_band");
        reserveTagihan = intent.getStringExtra("reservasi_tagihan");
        reserveStatus = intent.getStringExtra("reservasi_status");
        reserveWaktuBooking = intent.getStringExtra("reservasi_waktu_booking");
        reserveTanggal = intent.getStringExtra("reservasi_tanggal");
        reserveBatas = intent.getStringExtra("reservasi_batas");
        reserveRefund = intent.getStringExtra("reservasi_refund");
        reserveRefundAt = intent.getStringExtra("refunded_at");
        refundStatus = intent.getStringExtra("refund_status");
        reserveRoomNama = intent.getStringExtra("room_nama");
        reserveStudioNama = intent.getStringExtra("studio_nama");
        reserveBatas = intent.getStringExtra("reservasi_batas");
        reserveJadwal = intent.getStringExtra("jadwal");

        tvNomorBooking = (TextView)findViewById(R.id.tvKodeBooking);
        tvNamaBand = (TextView)findViewById(R.id.tvNamaBand);
        tvTagihan = (TextView)findViewById(R.id.tvTagihan);
        tvWaktuBooking = (TextView)findViewById(R.id.tvWaktuBooking);
        tvTanggal = (TextView)findViewById(R.id.tvTanggalBooking);
        tvTotalRefund = (TextView)findViewById(R.id.tvTotalRefund);
        tvRefundStatus = (TextView)findViewById(R.id.tvRefundStatus);
        tvRefundAt = (TextView)findViewById(R.id.tvRefundAt);
        tvRoomNama = (TextView)findViewById(R.id.tvRoomName);
        tvStudioNama = (TextView)findViewById(R.id.tvStudioName);
        tvBatasPembayaran = (TextView)findViewById(R.id.tvBatasBayar);
        tvJadwalReservasi = (TextView)findViewById(R.id.tvJadwalReservasi);

        ivStatusReservasi = (ImageView)findViewById(R.id.iv_status);
        tvStatusReservasi = (TextView)findViewById(R.id.tv_status);

        cardViewRefund = (CardView)findViewById(R.id.cardViewBookingRefund);

        btnContinueToPayment = (Button)findViewById(R.id.btn_continue_to_payment);
        btnContinueToEditReservation = (Button)findViewById(R.id.btn_continue_to_edit_booking);
        btnRequestCancelBooking = (Button)findViewById(R.id.btn_request_cancel_booking);

        btnContinueToPayment.setOnClickListener(operate);
        btnContinueToEditReservation.setOnClickListener(operate);
        btnRequestCancelBooking.setOnClickListener(operate);

        loadBookingDetailEntry(reserveNomorBooking, reserveNamaBand, reserveTagihan,
                reserveStatus, reserveWaktuBooking, reserveTanggal, reserveRefund,
                reserveRefundAt, refundStatus, reserveRoomNama, reserveStudioNama,
                reserveJadwal, reserveBatas);
    }

    View.OnClickListener operate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_continue_to_payment:
                    Intent intentToPayment = new Intent(mContext, PaymentActivity.class);
                    intentToPayment.putExtra("reservasi_id", reservationId);
                    intentToPayment.putExtra("reservasi_batas", reserveBatas);
                    startActivity(intentToPayment);
                    break;
                case R.id.btn_request_cancel_booking:
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(R.string.dialog_cancel_booking_message)
                            .setTitle(R.string.dialog_cancel_booking_title);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            issuedCancelBookingRequest(reservationId);
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
                    break;
                case R.id.btn_continue_to_edit_booking:
                    Intent intentToEditBooking = new Intent(mContext, EditBookingActivity.class);
                    intentToEditBooking.putExtra("reservation_id", reservationId);
                    startActivity(intentToEditBooking);
                    break;
            }
        }
    };

    private void loadBookingDetailEntry(String nomorBooking, String namaBand, String tagihan,
                                        String status, String waktuBooking, String tanggal, String totalRefund,
                                        String refundAt, String refundStatus, String roomNama, String studioNama,
                                        String jadwalReservasi, String batasPembayaran) {

        btnContinueToPayment.setVisibility(View.INVISIBLE);
        btnContinueToEditReservation.setVisibility(View.INVISIBLE);
        btnRequestCancelBooking.setVisibility(View.INVISIBLE);
        cardViewRefund.setVisibility(View.INVISIBLE);

        RupiahCurrencyFormat covertedRupiah = new RupiahCurrencyFormat();
        tvNomorBooking.setText(nomorBooking);
        tvNamaBand.setText(namaBand);
        tvTagihan.setText(covertedRupiah.toRupiahFormat(tagihan));
        tvWaktuBooking.setText(waktuBooking);
        tvTanggal.setText(tanggal);
        tvRoomNama.setText(roomNama);
        tvStudioNama.setText(studioNama);
        tvJadwalReservasi.setText(jadwalReservasi);
        tvBatasPembayaran.setText(batasPembayaran);
        if(refundStatus == null) {
            tvRefundStatus.setText("-");
        }
        if(refundAt == null) {
            tvRefundAt.setText("-");
        }
        tvTotalRefund.setText(totalRefund);
        switch (status) {
            case "0":
                ivStatusReservasi.setBackgroundColor(getResources().getColor(R.color.colorStatusBooked));
                tvStatusReservasi.setText(getResources().getString(R.string.booked_status));
                btnContinueToPayment.setVisibility(View.VISIBLE);
                break;
            case "1":
                ivStatusReservasi.setBackgroundColor(getResources().getColor(R.color.colorStatusConfirmed));
                tvStatusReservasi.setText(getResources().getString(R.string.confirmed_status));
                btnContinueToEditReservation.setVisibility(View.VISIBLE);
                btnRequestCancelBooking.setVisibility(View.VISIBLE);
                break;
            case "2":
                ivStatusReservasi.setBackgroundColor(getResources().getColor(R.color.colorStatusCanceled));
                tvStatusReservasi.setText(getResources().getString(R.string.canceled_status));
                cardViewRefund.setVisibility(View.VISIBLE);
                break;
            case "3":
                ivStatusReservasi.setBackgroundColor(getResources().getColor(R.color.colorStatusFailed));
                tvStatusReservasi.setText(getResources().getString(R.string.failed_status));
                break;
        }
    }

    private void issuedCancelBookingRequest(final int reservationId) {
        // Tag used to cancel the request
        String tag_string_req = "req_cancel_booking_request";
        StringRequest stringRequest;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        progressDialog.setMessage("Cancel Booking Reservation...");
        showDialog();

        stringRequest = new StringRequest(Request.Method.POST,
                NetworkUtils.CANCEL_RESERVATION_CHAMBER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "History Booking List Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String status = jsonObject.getString("status");
                    if (Integer.parseInt(code) > 0) {
                        Toast.makeText(mContext, status, Toast.LENGTH_SHORT).show();
                    }
                    hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("reservasi_id", String.valueOf(reservationId));

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

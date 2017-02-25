package gomes.com.gomesstudioreservation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import gomes.com.gomesstudioreservation.utilities.NetworkUtils;
import gomes.com.gomesstudioreservation.utilities.RupiahCurrencyFormat;

public class BookingDetailActivity extends AppCompatActivity {

    private final String TAG = BookingDetailActivity.class.getSimpleName();

    private Context mContext;
    private ProgressDialog progressDialog;

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

    private String reserveNomorBooking;
    private String reserveNamaBand;
    private String reserveTagihan;
    private String reserveStatus;
    private String reserveWaktuBooking;
    private String reserveTanggal;
    private String reserveRefund;
    private String reserveRefundPlace;
    private String refundStatus;
    private String reserveRoomNama;
    private String reserveStudioNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        mContext = this;

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        int reservationId = intent.getIntExtra("reservasi_id", 0);

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

        ivStatusReservasi = (ImageView)findViewById(R.id.iv_status);
        tvStatusReservasi = (TextView)findViewById(R.id.tv_status);

        getBookingDetail(reservationId);
    }

    private void loadBookingDetailEntry(String nomorBooking, String namaBand, String tagihan,
                                        String status, String waktuBooking, String tanggal, String totalRefund,
                                        String refundAt, String refundStatus, String roomNama, String studioNama) {

        RupiahCurrencyFormat covertedRupiah = new RupiahCurrencyFormat();
        tvNomorBooking.setText(nomorBooking);
        tvNamaBand.setText(namaBand);
        tvTagihan.setText(covertedRupiah.toRupiahFormat(tagihan));
        tvWaktuBooking.setText(waktuBooking);
        tvTanggal.setText(tanggal);
        tvRoomNama.setText(roomNama);
        tvStudioNama.setText(studioNama);
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
                break;
            case "1":
                ivStatusReservasi.setBackgroundColor(getResources().getColor(R.color.colorStatusConfirmed));
                tvStatusReservasi.setText(getResources().getString(R.string.confirmed_status));
                break;
            case "2":
                ivStatusReservasi.setBackgroundColor(getResources().getColor(R.color.colorStatusCanceled));
                tvStatusReservasi.setText(getResources().getString(R.string.canceled_status));
                break;
            case "3":
                ivStatusReservasi.setBackgroundColor(getResources().getColor(R.color.colorStatusFailed));
                tvStatusReservasi.setText(getResources().getString(R.string.failed_status));
                break;
        }
    }

    private void getBookingDetail(final int reservationId) {
        // Tag used to cancel the request
        String tag_string_req = "req_booking_history_list";
        StringRequest stringRequest;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        progressDialog.setMessage("Fetch Booking Data Detail ...");
        showDialog();

        stringRequest = new StringRequest(Request.Method.POST,
                NetworkUtils.GET_USER_RESERVATION_DATA_DETAIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "History Booking List Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    JSONObject reservasi = jsonObject.getJSONObject("reservasi");
                    if (Integer.parseInt(code) > 0) {
                        reserveNomorBooking = reservasi.getString("reservasi_nomor_booking");
                        reserveNamaBand = reservasi.getString("reservasi_nama_band");
                        reserveTagihan = reservasi.getString("reservasi_tagihan");
                        reserveStatus = reservasi.getString("reservasi_status");
                        reserveWaktuBooking = reservasi.getString("reservasi_waktu_booking");
                        reserveTanggal = reservasi.getString("reservasi_tanggal");
                        reserveRefund = reservasi.getString("reservasi_refund");
                        reserveRefundPlace = reservasi.getString("refunded_at");
                        refundStatus = reservasi.getString("refund_status");
                        reserveRoomNama = reservasi.getString("room_nama");
                        reserveStudioNama = reservasi.getString("studio_nama");
                        Toast.makeText(mContext, "load detail jadwal success", Toast.LENGTH_SHORT).show();
                        loadBookingDetailEntry(reserveNomorBooking, reserveNamaBand, reserveTagihan,
                                reserveStatus, reserveWaktuBooking, reserveTanggal, reserveRefund,
                                reserveRefundPlace, refundStatus, reserveRoomNama, reserveStudioNama);
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

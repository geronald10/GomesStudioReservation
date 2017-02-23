package gomes.com.gomesstudioreservation;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gomes.com.gomesstudioreservation.data.ReservationContract;
import gomes.com.gomesstudioreservation.data.ReservationSessionManager;
import gomes.com.gomesstudioreservation.models.HistoryBooking;
import gomes.com.gomesstudioreservation.utilities.DividerItemDecoration;
import gomes.com.gomesstudioreservation.utilities.NetworkUtils;
import gomes.com.gomesstudioreservation.utilities.SearchJadwalJsonUtils;

public class BookingHistoryActivity extends BaseActivity {

    private final String TAG = BookingHistoryActivity.class.getSimpleName();

    private Context mContext;
    private RecyclerView mRecyclerView;
    private BookingHistoryAdapter mAdapter;
    private List<HistoryBooking> historyBookingList;

    private ProgressDialog progressDialog;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        mContext = this;

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        HashMap<String, String> user = session.getUserDetails();
        userId = user.get(ReservationSessionManager.KEY_USER_ID);

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        getBookingHistoryList(userId);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvBookingHistory);
        historyBookingList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.hasFixedSize();

        mAdapter = new BookingHistoryAdapter(this, historyBookingList);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getBookingHistoryList(final String userId) {
        // Tag used to cancel the request
        String tag_string_req = "req_booking_history_list";
        StringRequest stringRequest;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        progressDialog.setMessage("Fetch Hisotry Booking Data ...");
        showDialog();

        stringRequest = new StringRequest(Request.Method.POST,
                NetworkUtils.HISTORY_RESERVATION_CHAMBER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "History Booking List Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    String status = jsonObject.getString("status");
                    if (jsonObject.length() > 0 && code > 0) {
                        JSONArray listReservasi = jsonObject.getJSONArray("reservasi");
                        Log.d("jumlah histori", String.valueOf(listReservasi.length()));
                        for (int i = 0; i < listReservasi.length(); i++) {
                            JSONObject history = listReservasi.getJSONObject(i);
                            Log.d("cek status ke-" + i, history.getString("reservasi_status"));
                            HistoryBooking data =
                                    new HistoryBooking(history.getInt("reservasi_id"),
                                            history.getInt("studio_id"),
                                            history.getInt("room_id"),
                                            history.getString("reservasi_nomor_booking"),
                                            history.getString("reservasi_nama_band"),
                                            history.getString("reservasi_tagihan"),
                                            history.getString("reservasi_status"),
                                            history.getString("reservasi_waktu_booking"),
                                            history.getString("reservasi_tanggal"),
                                            history.getString("reservasi_refund"),
                                            history.getString("room_nama"),
                                            history.getString("studio_nama"),
                                            history.getString("jadwal"));
                            historyBookingList.add(data);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(mContext, status, Toast.LENGTH_SHORT).show();
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
                params.put(ReservationContract.UserEntry.KEY_USER_ID, userId);

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

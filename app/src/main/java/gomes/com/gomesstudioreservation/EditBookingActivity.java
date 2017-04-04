package gomes.com.gomesstudioreservation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gomes.com.gomesstudioreservation.models.Schedule;
import gomes.com.gomesstudioreservation.utilities.DividerItemDecoration;
import gomes.com.gomesstudioreservation.utilities.NetworkUtils;

public class EditBookingActivity extends AppCompatActivity {

    private final String TAG = BookingHistoryActivity.class.getSimpleName();

    private Context mContext;
    private RecyclerView mRecyclerView;
    private EditScheduleAdapter mAdapter;
    private List<Schedule> scheduleList;
    private List<Schedule> oldScheduleList;
    private List<Integer> newCheckedList = new ArrayList<>();

    private ProgressDialog progressDialog;

    private Button btnSimpan;
    private EditText bandName;
    private TextView studioName;
    private TextView roomName;
    private TextView tanggalBooking;
    private TextView jadwalLama;

    private int limitEditJadwal;
    private int reservationId;
    private String bandNama;
    private String studioNama;
    private String roomNama;
    private String tglBooking;
    private String oldJadwal = "";
    private String newJadwalId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking);
        mContext = this;

        // Set up action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Booking Detail");

        bandName = (EditText) findViewById(R.id.edt_editted_band_name);
        studioName = (TextView) findViewById(R.id.tv_studio_name);
        roomName = (TextView) findViewById(R.id.tv_room);
        tanggalBooking = (TextView) findViewById(R.id.tv_tanggal_booking);
        jadwalLama = (TextView) findViewById(R.id.tv_jadwal);
        btnSimpan = (Button) findViewById(R.id.btn_booking_button);

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        reservationId = intent.getIntExtra("reservation_id", 0);
        Log.d("cek ID from edit", String.valueOf(reservationId));

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list_jadwal);
        scheduleList = new ArrayList<>();
        oldScheduleList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.hasFixedSize();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        getJadwalAvailable(reservationId);
        btnSimpan.setOnClickListener(operate);
    }

    View.OnClickListener operate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_booking_button:
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(R.string.dialog_message)
                            .setTitle(R.string.dialog_title);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            String newBandName = bandName.getText().toString();
                            Log.d("jumlah list tercheck", String.valueOf(newCheckedList.size()));

                            for (int i = 0; i < newCheckedList.size(); i++) {
                                Log.d("cek status checked", String.valueOf(newCheckedList));
                                newJadwalId += newCheckedList.get(i).toString();
                                if (i != newCheckedList.size() - 1) {
                                    newJadwalId += ",";
                                }
                            }
                            Log.d("yg dikirim", reservationId + " " + newBandName + " " + newJadwalId);
                            submitDataChange(reservationId, newBandName, newJadwalId);
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

    private void submitDataChange(final int reservationId, final String newBandName, final String newJadwalId) {
        final String tag_string_req = "req_updating_jadwal";
        StringRequest stringRequest;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        progressDialog.setMessage("Updating booking data ...");
        showDialog();

        stringRequest = new StringRequest(Request.Method.POST,
                NetworkUtils.EDIT_JADWAL_CHAMBER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Booking List Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    String message = jsonObject.getString("message");
                    if (jsonObject.length() > 0 && code > 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
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
                params.put("reservasi_id", String.valueOf(reservationId).trim());
                params.put("nama_band", newBandName);
                params.put("jadwal", String.valueOf(newJadwalId).trim());

                return params;
            }
        };
        // Adding request to request queue
        stringRequest.setTag(tag_string_req);
        requestQueue.add(stringRequest);
    }

    private void getJadwalAvailable(final int reservationId) {
        // Tag used to cancel the request
        final String tag_string_req = "req_available_jadwal_list";
        StringRequest stringRequest;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        progressDialog.setMessage("Fetch Available Schedule ...");
        showDialog();

        stringRequest = new StringRequest(Request.Method.POST,
                NetworkUtils.GET_USER_RESERVATION_DATA_DETAIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "History Booking List Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    String message = jsonObject.getString("message");
                    limitEditJadwal = jsonObject.getInt("jumlah");
                    if (jsonObject.length() > 0 && code > 0) {
                        JSONArray listJadwalLama = jsonObject.getJSONArray("jadwal_lama");
                        JSONArray listJadwal = jsonObject.getJSONArray("jadwal");
                        JSONObject reservasi = jsonObject.getJSONObject("reservasi");

                        Log.d("jumlah jadwal lama", String.valueOf(listJadwalLama.length()));
                        Log.d("jumlah jadwal tersedia", String.valueOf(listJadwal.length()));

                        for (int j = 0; j < listJadwalLama.length(); j++) {
                            JSONObject jadwalLama = listJadwalLama.getJSONObject(j);
                            Schedule dataJadwalLama = new Schedule(jadwalLama.getInt("jadwal_id"),
                                    jadwalLama.getString("jadwal_start"),
                                    jadwalLama.getString("jadwal_end"),
                                    true);
                            oldScheduleList.add(dataJadwalLama);
                            oldJadwal += j + 1 + ". " + oldScheduleList.get(j).getJadwalStart() + " - " + oldScheduleList.get(j).getJadwalEnd();
                            if (j != oldJadwal.length() - 1) {
                                oldJadwal += "\n";
                            }
                        }
                        for (int i = 0; i < listJadwal.length(); i++) {
                            boolean checked = false;
                            JSONObject jadwal = listJadwal.getJSONObject(i);
                            for (int j = 0; j < listJadwalLama.length(); j++) {
                                if (oldScheduleList.get(j).getJadwal() == jadwal.getInt("jadwal_id")) {
                                    Log.d("masuk sini", "sama jadwal id");
                                    checked = true;
                                }
                            }
                            Schedule data =
                                    new Schedule(jadwal.getInt("jadwal_id"),
                                            jadwal.getString("jadwal_start"),
                                            jadwal.getString("jadwal_end"),
                                            checked);
                            scheduleList.add(data);
                        }
                        studioNama = reservasi.getString("studio_nama");
                        roomNama = reservasi.getString("room_nama");
                        tglBooking = reservasi.getString("reservasi_tanggal");
                        bandNama = reservasi.getString("reservasi_nama_band");
                        loadDetailBooking();
                    }
                    mAdapter = new EditScheduleAdapter(mContext, scheduleList, limitEditJadwal);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

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

    private void loadDetailBooking() {
        bandName.setText(bandNama);
        studioName.setText(studioNama);
        roomName.setText(roomNama);
        tanggalBooking.setText(tglBooking);
        jadwalLama.setText(oldJadwal);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void saveToList(int jadwalId) {
        newCheckedList.add(jadwalId);
        for (int i = 0; i < newCheckedList.size(); i++) {
            Log.d("list setelah add", String.valueOf(newCheckedList.size()));
        }
    }

    public void removeFromList(int jadwalId) {
        newCheckedList.remove(Integer.valueOf(jadwalId));
        for (int i = 0; i < newCheckedList.size(); i++) {
            Log.d("list setelah remove", String.valueOf(newCheckedList.size() ));
        }
    }
}

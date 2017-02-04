package gomes.com.gomesstudioreservation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import gomes.com.gomesstudioreservation.utilities.BasicCalendar;

public class BookingActivity extends AppCompatActivity {

    private EditText namaBand;
    private Spinner namaKota;
    private Spinner namaStudio;
    private EditText tanggalBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        tanggalBooking = (EditText)findViewById(R.id.edt_select_date);
        tanggalBooking.setOnClickListener(operation);
    }

    View.OnClickListener operation = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.edt_select_date:
                    Intent intent = new Intent(getApplicationContext(), BasicCalendar.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
}

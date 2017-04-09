package gomes.com.gomesstudioreservation.utilities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import gomes.com.gomesstudioreservation.R;

public class BasicCalendar extends AppCompatActivity implements OnDateSelectedListener {

    private static final DateFormat FORMATTER = new SimpleDateFormat("EEEE, dd-MM-yyyy", Locale.US);
    private static final DateFormat FORMATTER_TO_SERVER = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private TextView selectedDate;
    private MaterialCalendarView calendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Set up action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_event_note);
        actionBar.setTitle("  Select Date");

        selectedDate = (TextView)findViewById(R.id.tv_selected_date);
        calendar =(MaterialCalendarView)findViewById(R.id.calendarView);
        Button btnSelectDate = (Button) findViewById(R.id.btn_select_date);

        calendar.setOnDateChangedListener(this);
        btnSelectDate.setOnClickListener(operation);

        Intent intent = getIntent();
        CalendarDay today = CalendarDay.today();

        //Setup initial text
        selectedDate.setText(getSelectedDateString());

        if (intent.hasExtra("dateToShow")) {
            String stringDate = intent.getStringExtra("dateToShow");
            Date date = null;
            try {
                date = FORMATTER.parse(stringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.setSelectedDate(date);
            selectedDate.setText(stringDate);
        } else {
            calendar.setSelectedDate(today.getDate());
            selectedDate.setText(FORMATTER.format(today.getDate()));
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(Activity.RESULT_CANCELED);
                finish();
        }
        return true;
    }

    View.OnClickListener operation = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.btn_select_date:
                    getSelectedDateExtra();
            }
        }
    };

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        if(!isPastDay(date)) {
            selectedDate.setText(getSelectedDateString());
        } else {
            calendar.setSelectedDate(CalendarDay.today());
            selectedDate.setText("No Selection");
            Toast.makeText(this, "Can't select the past day", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSelectedDateString() {
        CalendarDay date = calendar.getSelectedDate();
        if(date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }

    private String getSelectedDateServerFormat() {
        CalendarDay date = calendar.getSelectedDate();
        if(date == null) {
            return "No Selection";
        }
        return FORMATTER_TO_SERVER.format(date.getDate());
    }

    private void getSelectedDateExtra() {
        Intent data = new Intent();
        data.putExtra("dateToShow", getSelectedDateString());
        data.putExtra("dateToSend", getSelectedDateServerFormat());
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    private boolean isPastDay(CalendarDay date) {
        Calendar c = Calendar.getInstance();

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        // and get that as a Date
        CalendarDay today = CalendarDay.from(c.getTime());

        // test your condition, if Date specified is before today
        if (date.isBefore(today)) {
            return true;
        }
        return false;
    }
}

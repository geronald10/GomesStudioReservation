package gomes.com.gomesstudioreservation.utilities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import gomes.com.gomesstudioreservation.R;

public class BasicCalendar extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    private TextView selectedDate;
    private MaterialCalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        selectedDate = (TextView)findViewById(R.id.tv_selected_date);
        calendar =(MaterialCalendarView)findViewById(R.id.calendarView);

        calendar.setOnDateChangedListener(this);
        calendar.setOnMonthChangedListener(this);

        //Setup initial text
        selectedDate.setText(getSelectedDateString());
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        selectedDate.setText(getSelectedDateString());
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        getSupportActionBar().setTitle(FORMATTER.format(date.getDate()));
    }

    private String getSelectedDateString() {
        CalendarDay date = calendar.getSelectedDate();
        if(date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }
}

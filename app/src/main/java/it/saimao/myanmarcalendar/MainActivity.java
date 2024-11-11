package it.saimao.myanmarcalendar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import it.saimao.myanmarcalendar.databinding.ActivityMainBinding;
import mmcalendar.CalendarType;
import mmcalendar.Config;
import mmcalendar.Language;
import mmcalendar.MyanmarDate;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};


    private LocalDate today;
    private int monthValue;
    private static final String TAG = "LUFFY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
        initListener();
    }

    private void initListener() {
        binding.tvHeading.setOnClickListener(v -> prevMonth());
        binding.tvTail.setOnClickListener(v -> nextMonth());
    }

    private void nextMonth() {
        today = today.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        updateCalendar();

    }

    private void prevMonth() {

        today = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        updateCalendar();

    }

    private void initUi() {


        Config.initDefault(
                new Config.Builder()
                        .setCalendarType(CalendarType.ENGLISH)
                        .setLanguage(Language.MYANMAR)
                        .build());

        today = LocalDate.now();

        updateCalendar();


    }

    private void updateCalendar() {
        updateCalendarHeader();
        updateCalendarBody();
    }

    private void updateCalendarBody() {

        LocalDate startDate = today.with(TemporalAdjusters.firstDayOfMonth());
        int startWeekDay = startDate.getDayOfWeek().getValue() == 7 ? 0 : startDate.getDayOfWeek().getValue();

        int dayIndex;
        for (dayIndex = 0; dayIndex < startWeekDay; dayIndex++) {
            binding.glCalendar.getChildAt(dayIndex).setVisibility(View.INVISIBLE);
        }

        while (startDate.getMonthValue() == monthValue) {
            int index = dayIndex;
            if (index >= 35) {
                index = dayIndex - 35;
            }

            TextView day = (TextView) binding.glCalendar.getChildAt(index);
            if (day.getVisibility() == View.INVISIBLE) day.setVisibility(View.VISIBLE);
            day.setText(String.valueOf(startDate.getDayOfMonth()));

            if (startDate.isEqual(LocalDate.now())) {
                day.setBackgroundResource(R.drawable.bg_text_today);
            } else if (startDate.isEqual(today)) {
                day.setBackgroundResource(R.drawable.bg_text_selected);
            } else {
                day.setBackgroundResource(R.drawable.bg_text_default);
            }

            day.setTag(startDate);
            startDate = startDate.plusDays(1);
            dayIndex++;

        }

        while (dayIndex < 35) {
            binding.glCalendar.getChildAt(dayIndex).setVisibility(View.INVISIBLE);
            dayIndex++;
        }


    }

    private void updateCalendarHeader() {

        monthValue = today.getMonthValue();

        binding.tvTail.setText(MONTHS[monthValue - 1]);
        binding.tvHeading.setText(String.valueOf(today.getDayOfMonth()));
        binding.tvFullDate.setText(today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        MyanmarDate myanmarDate = MyanmarDate.of(today);
        binding.tvShortDay.setText(myanmarDate.getWeekDay());
        binding.tvDescription.setText(myanmarDate.format("S s k, B y k, M p f r En"));
    }

    public void onDateClicked(View view) {
        TextView tv = (TextView) view;
        today = (LocalDate) tv.getTag();
        updateCalendar();
    }
}
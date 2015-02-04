package com.github.jjobes.slidedaytimepicker.sample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.github.jjobes.slidedaytimepicker.SlideDayTimeListener;
import com.github.jjobes.slidedaytimepicker.SlideDayTimePicker;

/**
 * Sample test class for SlideDayTimePicker.
 *
 * @author jjobes
 *
 */
public class SampleActivity extends FragmentActivity
{
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sample);

        mButton = (Button) findViewById(R.id.button);

        final SlideDayTimeListener listener = new SlideDayTimeListener() {

            @Override
            public void onDayTimeSet(int day, int hour, int minute)
            {
                Toast.makeText(
                        SampleActivity.this,
                        "day = " + day + "\nhour = " + hour + "\nminute = " + minute,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDayTimeCancel()
            {
                Toast.makeText(SampleActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        };

        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                new SlideDayTimePicker.Builder(getSupportFragmentManager())
                    .setListener(listener)
                    .setInitialDay(1)
                    .setInitialHour(13)
                    .setInitialMinute(30)
                    //.setIs24HourTime(false)
                    //.setCustomDaysArray(getResources().getStringArray(R.array.days_of_week))
                    //.setTheme(SlideDayTimePicker.HOLO_DARK)
                    //.setIndicatorColor(Color.parseColor("#990000"))
                    .build()
                    .show();
            }
        });
    }
}

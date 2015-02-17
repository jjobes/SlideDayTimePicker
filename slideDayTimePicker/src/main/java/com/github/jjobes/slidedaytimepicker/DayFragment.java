package com.github.jjobes.slidedaytimepicker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

/**
 * The fragment for the first page in the ViewPager that holds
 * the {@link CustomNumberPicker} for displaying the days.
 *
 * @author jjobes
 *
 */
public class DayFragment extends Fragment
{
    /**
     * Used to communicate back to the parent fragment as the user
     * is changing the day spinner so we can dynamically update
     * the tab text.
     */
    public interface DayChangedListener
    {
        void onDayChanged(int day);
    }

    private DayChangedListener mCallback;

    public DayFragment()
    {
        // Required empty public constructor for fragment.
    }

    /**
     * Cast the reference to {@link SlideDayTimeDialogFragment} to a
     * {@link DayChangedListener}.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            mCallback = (DayChangedListener) getTargetFragment();
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException("Calling fragment must implement " +
                "DayFragment.DayChangedListener interface");
        }
    }

    /**
     * Return an instance of DayFragment with its bundle filled with the
     * constructor arguments. The values in the bundle are retrieved in
     * {@link #onCreateView()} below to properly initialize the DatePicker.
     *
     * @param theme
     * @param initialDay
     * @param isCustomDaysArraySpecified
     * @param customDaysArray
     * @return an instance of DayFragment
     */
    public static final DayFragment newInstance(
            int theme, int initialDay, boolean isCustomDaysArraySpecified,
            String[] customDaysArray)
    {
        DayFragment f = new DayFragment();

        Bundle b = new Bundle();
        b.putInt("theme", theme);
        b.putInt("initialDay", initialDay);
        b.putBoolean("isCustomDaysArraySpecified", isCustomDaysArraySpecified);
        b.putStringArray("customDaysArray", customDaysArray);
        f.setArguments(b);

        return f;
    }

    /**
     * Create and return the user interface view for this fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        int initialDay = getArguments().getInt("initialDay");
        boolean isCustomDaysArraySpecified = getArguments().getBoolean("isCustomDaysArraySpecified");

        String[] daysArray;
        if (isCustomDaysArraySpecified)
        {
            daysArray = getArguments().getStringArray("customDaysArray");
        }
        else
        {
            daysArray = getResources().getStringArray(R.array.days_array);
        }

        // Unless we inflate using a cloned inflater with a Holo theme,
        // on Lollipop devices the TimePicker will be the new-style
        // radial TimePicker, which is not what we want. So we will
        // clone the inflater that we're given but with our specified
        // theme, then inflate the layout with this new inflater.
        int theme = getArguments().getInt("theme");

        Context contextThemeWrapper = new ContextThemeWrapper(
                getActivity(),
                theme == SlideDayTimePicker.HOLO_DARK ?
                        android.R.style.Theme_Holo : android.R.style.Theme_Holo_Light);

        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View v = localInflater.inflate(R.layout.fragment_day, container, false);

        CustomNumberPicker dayPicker = (CustomNumberPicker) v.findViewById(R.id.dayPicker);
        // remove blinking cursor from NumberPicker
        enableNumberPickerEditing(dayPicker, false);
        // block keyboard popping up on touch
        dayPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        dayPicker.setMinValue(0);
        dayPicker.setMaxValue(daysArray.length - 1);
        dayPicker.setDisplayedValues(daysArray);
        dayPicker.setValue(initialDay);
        dayPicker.setOnValueChangedListener(new OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                mCallback.onDayChanged(newVal);
            }
        });

        return v;
    }

    /**
     * Enable or disable NumberPicker editing. We use this to turn
     * off editing and this has the effect of removing the blinking
     * cursor that is shown by default.
     */
    private void enableNumberPickerEditing(NumberPicker numberPicker, boolean enable)
    {
        int childCount = numberPicker.getChildCount();

        for (int i = 0; i < childCount; i++)
        {
            View childView = numberPicker.getChildAt(i);

            if (childView instanceof EditText)
            {
                EditText editText = (EditText) childView;
                editText.setFocusable(enable);
                return;
            }
        }
    }
}

package com.github.jjobes.slidedaytimepicker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Interface for the library client to create a new {@code SlideDayTimePicker}.
 *
 * @author jjobes
 *
 */
public class SlideDayTimePicker
{
    public static final int HOLO_DARK = 1;
    public static final int HOLO_LIGHT = 2;

    private FragmentManager mFragmentManager;
    private SlideDayTimeListener mListener;
    private int mInitialDay;
    private int mInitialHour;
    private int mInitialMinute;
    private boolean mIsClientSpecified24HourTime;
    private boolean mIs24HourTime;
    private boolean mIsCustomDaysArraySpecified;
    private String[] mCustomDaysArray;
    private int mTheme;
    private int mIndicatorColor;

    /**
     * Creates a new instance of {@code SlideDayTimePicker}.
     *
     * @param fm  The {@code FragmentManager} from the calling activity that is used
     *            internally to show the {@code DialogFragment}.
     */
    public SlideDayTimePicker(FragmentManager fm)
    {
        // See if there are any DialogFragments from the FragmentManager
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(SlideDayTimeDialogFragment.TAG_SLIDE_DAY_TIME_DIALOG_FRAGMENT);

        // Remove if found
        if (prev != null)
        {
            ft.remove(prev);
            ft.commit();
        }

        mFragmentManager = fm;
    }

    /**
     * <p>Sets the listener that is used to inform the client when
     * the user selects a new date and time.</p>
     *
     * <p>This must be called before {@link #show()}.</p>
     *
     * @param listener
     */
    public void setListener(SlideDayTimeListener listener)
    {
        mListener = listener;
    }

    /**
     * <p>Specify the initial day to display when the dialog appears.</p>
     *
     * <p>If you do not specify a custom days array (with {@code setCustomDaysArray()}),
     * the {@code initialDay} that you pass in should be the {@link java.util.Calendar}
     * day of week constant to initially display.</p>
     *
     * <p>For example, if you want to initially display Sunday as selected, then you
     * should pass in the integer <tt>1</tt> (or {@code Calendar.SUNDAY}).</p>
     *
     * <p>However, if you specify a custom days array (with {@code setCustomDaysArray()}),
     * then you should pass in the raw 0-based index of your custom array to display as the
     * initial day.</p>
     *
     * @param initialDay
     */
    public void setInitialDay(int initialDay)
    {
        mInitialDay = initialDay;
    }

    /**
     * Specifies the initial hour to display (<tt>0-23</tt>).
     *
     * @param initialHour  the initial hour to display. valid values
     *                     are <tt>0-23</tt>
     */
    public void setInitialHour(int initialHour)
    {
        if (initialHour < 0 || initialHour > 23)
        {
            throw new IllegalArgumentException(
                    "Initial hour specified as " + initialHour + ". " +
                    "Initial hour must be >= 0 and <= 23");
        }

        mInitialHour = initialHour;
    }

    /**
     * Specifies the initial minute to display (<tt>0-59</tt>)
     *
     * @param initialMinute  the initial minutes to display. valid values
     *                       are <tt>0-59</tt>
     */
    public void setInitialMinute(int initialMinute)
    {
        if (initialMinute < 0 || initialMinute > 59)
        {
            throw new IllegalArgumentException(
                    "Initial minute specified as " + initialMinute + ". " +
                    "Initial minute must be >= 0 and <= 59");
        }

        mInitialMinute = initialMinute;
    }

    private void setIsClientSpecified24HourTime(boolean isClientSpecified24HourTime)
    {
        mIsClientSpecified24HourTime = isClientSpecified24HourTime;
    }

    /**
     * <p>Sets whether the TimePicker displays the time in 12-hour (AM/PM) or 24-hour
     * format. If this method is not called, the device's default time format is used.
     * This also affects the time format displayed in the tab.</p>
     *
     * <p>Must be called before {@link #show()}.</p>
     *
     * @param is24HourTime <tt>true</tt> to force 24-hour time format,
     *        <tt>false</tt> to force 12-hour (AM/PM) time format.
     */
    public void setIs24HourTime(boolean is24HourTime)
    {
        setIsClientSpecified24HourTime(true);
        mIs24HourTime = is24HourTime;
    }

    private void setIsCustomDaysArraySpecified(boolean isCustomDaysArraySpecified)
    {
        mIsCustomDaysArraySpecified = isCustomDaysArraySpecified;
    }

    /**
     * <p>Specify a custom {@code String} array of items to display in the picker.
     * When the user presses "OK", {@code onDayTimeSet()} will
     * return the raw 0-based index of the selected item from your
     * custom array.</p>
     *
     * <p>Must be called before {@link #show()}.</p>
     *
     * @param customDaysArray
     */
    public void setCustomDaysArray(String[] customDaysArray)
    {
        if (customDaysArray != null)
            setIsCustomDaysArraySpecified(true);

        mCustomDaysArray = customDaysArray;
    }

    /**
     * Sets the theme of the dialog. If no theme is specified, it
     * defaults to holo light.
     *
     * @param theme  {@code SlideDateTimePicker.HOLO_DARK} for a dark theme, or
     *               {@code SlideDateTimePicker.HOLO_LIGHT} for a light theme
     */
    public void setTheme(int theme)
    {
        mTheme = theme;
    }

    /**
     * Sets the color of the underline for the currently selected tab.
     *
     * @param indicatorColor  the color of the selected tab's underline
     */
    public void setIndicatorColor(int indicatorColor)
    {
        mIndicatorColor = indicatorColor;
    }

    /**
     * Show the dialog to the user. Make sure to set the listener before calling this.
     */
    public void show()
    {
        if (mListener == null)
        {
            throw new NullPointerException(
                    "Attempting to bind null listener to SlideDayTimePicker");
        }

        SlideDayTimeDialogFragment dialogFragment =
                SlideDayTimeDialogFragment.newInstance(
                        mListener,
                        mIsCustomDaysArraySpecified,
                        mCustomDaysArray,
                        mInitialDay,
                        mInitialHour,
                        mInitialMinute,
                        mIsClientSpecified24HourTime,
                        mIs24HourTime,
                        mTheme,
                        mIndicatorColor);

        dialogFragment.show(mFragmentManager,
                SlideDayTimeDialogFragment.TAG_SLIDE_DAY_TIME_DIALOG_FRAGMENT);
    }

    /*
     * The following implements the builder API to simplify
     * creation and display of the dialog.
     */
    public static class Builder
    {
        // Required
        private FragmentManager fm;
        private SlideDayTimeListener listener;

        // Optional
        private String[] customDaysArray;
        private int initialDay;
        private int initialHour;
        private int initialMinute;
        private boolean isClientSpecified24HourTime;
        private boolean is24HourTime;
        private int theme;
        private int indicatorColor;

        public Builder(FragmentManager fm)
        {
            this.fm = fm;
        }

        /**
         * @see SlideDayTimePicker#setListener(SlideDayTimeListener)
         */
        public Builder setListener(SlideDayTimeListener listener)
        {
            this.listener = listener;
            return this;
        }

        /**
         * @see SlideDayTimePicker#setCustomDaysArray(String[])
         */
        public Builder setCustomDaysArray(String[] customDaysArray)
        {
            this.customDaysArray = customDaysArray;
            return this;
        }

        /**
         * @see SlideDayTimePicker#setInitialDay(int)
         */
        public Builder setInitialDay(int initialDay)
        {
            this.initialDay = initialDay;
            return this;
        }

        /**
         * @see SlideDayTimePicker#setInitialHour(int)
         */
        public Builder setInitialHour(int initialHour)
        {
            this.initialHour = initialHour;
            return this;
        }

        /**
         * @see SlideDayTimePicker#setInitialMinute(int)
         */
        public Builder setInitialMinute(int initialMinute)
        {
            this.initialMinute = initialMinute;
            return this;
        }

        /**
         * @see SlideDayTimePicker#setIs24HourTime(boolean)
         */
        public Builder setIs24HourTime(boolean is24HourTime)
        {
            this.isClientSpecified24HourTime = true;
            this.is24HourTime = is24HourTime;
            return this;
        }

        /**
         * @see SlideDayTimePicker#setTheme(int)
         */
        public Builder setTheme(int theme)
        {
            this.theme = theme;
            return this;
        }

        /**
         * @see SlideDayTimePicker#setIndicatorColor(int)
         */
        public Builder setIndicatorColor(int indicatorColor)
        {
            this.indicatorColor = indicatorColor;
            return this;
        }

        /**
         * <p>Build and return a {@code SlideDayTimePicker} object based on the previously
         * supplied parameters.</p>
         *
         * <p>You should call {@link #show()} immediately after this.</p>
         *
         * @return
         */
        public SlideDayTimePicker build()
        {
            SlideDayTimePicker picker = new SlideDayTimePicker(fm);
            picker.setListener(listener);
            picker.setCustomDaysArray(customDaysArray);
            picker.setInitialDay(initialDay);
            picker.setInitialHour(initialHour);
            picker.setInitialMinute(initialMinute);
            picker.setIsClientSpecified24HourTime(isClientSpecified24HourTime);
            picker.setIs24HourTime(is24HourTime);
            picker.setTheme(theme);
            picker.setIndicatorColor(indicatorColor);

            return picker;
        }
    }
}

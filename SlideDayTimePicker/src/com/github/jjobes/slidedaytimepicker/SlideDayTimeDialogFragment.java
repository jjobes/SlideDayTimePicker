package com.github.jjobes.slidedaytimepicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * <p>The {@code DialogFragment} that contains the {@link SlidingTabLayout} and
 * {@link CustomViewPager}.</p>
 *
 * <p>The {@code CustomViewPager} contains the {@link DayFragment} and {@link TimeFragment}.</p>
 *
 * <p>This {@code DialogFragment} is managed by {@link SlideDayTimePicker}.</p>
 *
 * @author jjobes
 *
 */
public class SlideDayTimeDialogFragment extends DialogFragment implements DayFragment.DayChangedListener,
                                                                          TimeFragment.TimeChangedListener
{
    public static final String TAG_SLIDE_DAY_TIME_DIALOG_FRAGMENT = "tagSlideDayTimeDialogFragment";

    private static SlideDayTimeListener mListener;

    private Context mContext;
    private CustomViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private SlidingTabLayout mSlidingTabLayout;
    private View mButtonHorizontalDivider;
    private View mButtonVerticalDivider;
    private Button mOkButton;
    private Button mCancelButton;
    private boolean mIsCustomDaysArraySpecified;
    private String[] mCustomDaysArray;
    private String[] mDaysArray;
    private int mDayIndex;
    private int mHour;
    private int mMinute;
    private int mTheme;
    private int mIndicatorColor;
    private boolean mIsClientSpecified24HourTime;
    private boolean mIs24HourTime;
    private Calendar mCalendar;

    public SlideDayTimeDialogFragment()
    {
        // Required empty public constructor
    }

    /**
     * <p>Return a new instance of {@code SlideDayTimeDialogFragment} with its bundle
     * filled with the incoming arguments.</p>
     *
     * <p>Called by {@link SlideDayTimePicker#show()}.</p>
     *
     * @param listener
     * @param isCustomDaysArraySpecified
     * @param initialDay
     * @param initialHour
     * @param initialMinute
     * @param isClientSpecified24HourTime
     * @param is24HourTime
     * @param theme
     * @param indicatorColor
     * @return
     */
    public static SlideDayTimeDialogFragment newInstance(SlideDayTimeListener listener,
            boolean isCustomDaysArraySpecified, String[] customDaysArray, int initialDay,
            int initialHour, int initialMinute, boolean isClientSpecified24HourTime,
            boolean is24HourTime, int theme, int indicatorColor)
    {
        // Set listener to a static member variable in order to avoid the
        // NotSerializableException on rotation.
        mListener = listener;

        // Create a new instance of SlideDayTimeDialogFragment
        SlideDayTimeDialogFragment dialogFragment = new SlideDayTimeDialogFragment();

        // Store the arguments and attach the bundle to the fragment
        Bundle bundle = new Bundle();
        bundle.putBoolean("isCustomDaysArraySpecified", isCustomDaysArraySpecified);
        bundle.putStringArray("customDaysArray", customDaysArray);
        bundle.putInt("initialDay", initialDay);
        bundle.putInt("initialHour", initialHour);
        bundle.putInt("initialMinute", initialMinute);
        bundle.putBoolean("isClientSpecified24HourTime", isClientSpecified24HourTime);
        bundle.putBoolean("is24HourTime", is24HourTime);
        bundle.putInt("theme", theme);
        bundle.putInt("indicatorColor", indicatorColor);
        dialogFragment.setArguments(bundle);

        // Return the fragment with its bundle
        return dialogFragment;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mDaysArray = getResources().getStringArray(R.array.days_array);

        unpackBundle();

        if (!mIsCustomDaysArraySpecified)
            mDayIndex--;

        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);

        switch (mTheme)
        {
        case SlideDayTimePicker.HOLO_DARK:
            setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_NoActionBar);
            break;
        case SlideDayTimePicker.HOLO_LIGHT:
            setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
            break;
        default:  // if no theme was specified, default to holo light
            setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.slide_day_time_picker, container);

        setupViews(view);
        customizeViews();
        initViewPager();
        initTabs();
        initButtons();

        return view;
    }

    @Override
    public void onDestroyView()
    {
        // Workaround for a bug in the compatibility library where calling
        // setRetainInstance(true) does not retain the instance across
        // orientation changes.
        if (getDialog() != null && getRetainInstance())
        {
            getDialog().setDismissMessage(null);
        }

        super.onDestroyView();
    }

    private void unpackBundle()
    {
        Bundle args = getArguments();

        mIsCustomDaysArraySpecified = args.getBoolean("isCustomDaysArraySpecified");
        mCustomDaysArray = args.getStringArray("customDaysArray");
        mDayIndex = args.getInt("initialDay");
        mHour = args.getInt("initialHour");
        mMinute = args.getInt("initialMinute");
        mIsClientSpecified24HourTime = args.getBoolean("isClientSpecified24HourTime");
        mIs24HourTime = args.getBoolean("is24HourTime");
        mTheme = args.getInt("theme");
        mIndicatorColor = args.getInt("indicatorColor");
    }

    private void setupViews(View v)
    {
        mViewPager = (CustomViewPager) v.findViewById(R.id.viewPager);
        mSlidingTabLayout = (SlidingTabLayout) v.findViewById(R.id.slidingTabLayout);
        mButtonHorizontalDivider = v.findViewById(R.id.buttonHorizontalDivider);
        mButtonVerticalDivider = v.findViewById(R.id.buttonVerticalDivider);
        mOkButton = (Button) v.findViewById(R.id.okButton);
        mCancelButton = (Button) v.findViewById(R.id.cancelButton);
    }

    private void customizeViews()
    {
        int lineColor = mTheme == SlideDayTimePicker.HOLO_DARK ?
                getResources().getColor(R.color.gray_holo_dark) :
                    getResources().getColor(R.color.gray_holo_light);

        // Set the colors of the horizontal and vertical lines for the
        // bottom buttons depending on the theme.
        switch (mTheme)
        {
        case SlideDayTimePicker.HOLO_LIGHT:
        case SlideDayTimePicker.HOLO_DARK:
            mButtonHorizontalDivider.setBackgroundColor(lineColor);
            mButtonVerticalDivider.setBackgroundColor(lineColor);
            break;

        default:  // if no theme was specified, default to holo light
            mButtonHorizontalDivider.setBackgroundColor(getResources().getColor(R.color.gray_holo_light));
            mButtonVerticalDivider.setBackgroundColor(getResources().getColor(R.color.gray_holo_light));
        }

        // Set the color of the selected tab underline if one was specified.
        if (mIndicatorColor != 0)
            mSlidingTabLayout.setSelectedIndicatorColors(mIndicatorColor);
    }

    private void initViewPager()
    {
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        // Setting this custom layout for each tab ensures that the tabs will
        // fill all available horizontal space.
        mSlidingTabLayout.setCustomTabView(R.layout.custom_tab, R.id.tabText);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    private void initTabs()
    {
        // Set intial date on date tab
        updateDayTab();

        // Set initial time on time tab
        updateTimeTab();
    }

    private void initButtons()
    {
        mOkButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                if (mListener == null)
                {
                    throw new NullPointerException(
                            "Listener no longer exists for mOkButton");
                }

                // mDayIndex currenly holds the index of the day in days_array,
                // but we want to return a day integer that is compliant with
                // the java.util.Calendar API, so we increment it by 1 if they're
                // using the built-in days array.  If the user specifies their
                // own custom array, return the raw index.
                int returnDay;

                if (!mIsCustomDaysArraySpecified)
                {
                    returnDay = mDayIndex + 1;
                }
                else
                {
                    returnDay = mDayIndex;
                }

                mListener.onDayTimeSet(returnDay, mHour, mMinute);

                dismiss();
            }
        });

        mCancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                if (mListener == null)
                {
                    throw new NullPointerException(
                            "Listener no longer exists for mCancelButton");
                }

                mListener.onDayTimeCancel();

                dismiss();
            }
        });
    }

    /**
     * <p>The callback used by the day picker to update {@code mCalendar} as
     * the user changes the date. Each time this is called, we update the
     * day tab to reflect the day the user has currenly selected.</p>
     *
     * <p>Implements the {@link DayFragment.DayChangedListener} interface.</p>
     */
    @Override
    public void onDayChanged(int day)
    {
        mDayIndex = day;  // Sunday 0, Monday 1, ... Saturday 6

        updateDayTab();
    }

    /**
     * <p>The callback used by the time picker to update {@code mCalendar}
     * as the user changes the time. Each time this is called, we update
     * the time tab to reflect the time the user has currenly selected.</p>
     *
     * <p>Implements the {@link TimeFragment.TimeChangedListener} interface.</p>
     */
    @Override
    public void onTimeChanged(int hour, int minute)
    {
        mHour = hour;
        mMinute = minute;

        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);

        updateTimeTab();
    }

    private void updateDayTab()
    {
        if (mIsCustomDaysArraySpecified)
            mSlidingTabLayout.setTabText(0, mCustomDaysArray[mDayIndex]);
        else
            mSlidingTabLayout.setTabText(0, mDaysArray[mDayIndex]);
    }

    @SuppressLint("SimpleDateFormat")
    private void updateTimeTab()
    {
        if (mIsClientSpecified24HourTime)
        {
            SimpleDateFormat formatter;

            if (mIs24HourTime)
            {
                formatter = new SimpleDateFormat("HH:mm");
                mSlidingTabLayout.setTabText(1, formatter.format(mCalendar.getTime()));
            }
            else
            {
                formatter = new SimpleDateFormat("h:mm aa");
                mSlidingTabLayout.setTabText(1, formatter.format(mCalendar.getTime()));
            }
        }
        else  // display time using the device's default 12/24 hour format preference
        {
            mSlidingTabLayout.setTabText(1, DateFormat.getTimeFormat(
                    mContext).format(mCalendar.getTimeInMillis()));
        }
    }

    /**
     * <p>Called when the user clicks outside the dialog or presses the device's
     * <b>Back</b> button.</p>
     *
     * <p><b>Note:</b> Actual Cancel button clicks are handled by {@code mCancelButton}'s
     * event handler.</p>
     */
    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);

        if (mListener == null)
        {
            throw new NullPointerException(
                    "Listener no longer exists in onCancel()");
        }

        mListener.onDayTimeCancel();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter
    {
        public ViewPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
            case 0:
                Fragment dayFragment = DayFragment.newInstance(
                        mTheme,
                        mDayIndex,
                        mIsCustomDaysArraySpecified,
                        mCustomDaysArray);
                dayFragment.setTargetFragment(SlideDayTimeDialogFragment.this, 100);
                return dayFragment;
            case 1:
                Fragment timeFragment = TimeFragment.newInstance(
                        mTheme,
                        mCalendar.get(Calendar.HOUR_OF_DAY),
                        mCalendar.get(Calendar.MINUTE),
                        mIsClientSpecified24HourTime,
                        mIs24HourTime);
                timeFragment.setTargetFragment(SlideDayTimeDialogFragment.this, 200);
                return timeFragment;
            }

            return null;
        }

        @Override
        public int getCount()
        {
            return 2;
        }
    }
}

package com.github.jjobes.slidedaytimepicker;

/**
 * <p>This listener class informs the library client when the user sets
 * a day and time by pressing "OK".</p>
 *
 * <p>Overriding {@code onDayTimeCancel()} is optional. The client
 * can override this to listen for when the user cancels the dialog.
 * This is called when the user presses the Cancel button, touches
 * outside the dialog or presses the device's <b>Back</b> button.</p>
 *
 * @author jjobes
 *
 */
public abstract class SlideDayTimeListener
{
    /**
     * Informs the client when the user presses "OK"
     * and selects a day and time.
     *
     * @param day  the day the user has selected
     * @param hour  the hour the user has selected
     * @param minute  the minutes the user has selected
     */
    public abstract void onDayTimeSet(int day, int hour, int minute);

    /**
     * Informs the client when the user cancels the
     * dialog by pressing Cancel, touching outside
     * the dialog or pressing the Back button.
     * This override is optional.
     */
    public void onDayTimeCancel()
    {

    }
}

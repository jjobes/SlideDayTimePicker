package com.github.jjobes.slidedaytimepicker;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.NumberPicker;

/**
 * A subclass of {@link android.widget.NumberPicker} that uses
 * reflection to allow for customization of the default blue
 * dividers.
 *
 * @author jjobes
 *
 */
public class CustomNumberPicker extends NumberPicker
{
    private static final String TAG = "CustomNumberPicker";

    public CustomNumberPicker(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        Class<?> numberPickerClass = null;
        Field selectionDividerField = null;

        try
        {
            // Create an instance of the NumberPicker class
            numberPickerClass = Class.forName("android.widget.NumberPicker");

            // Set the value of the mSelectionDivider field in this NumberPicker
            // to refer to our custom drawable
            selectionDividerField = numberPickerClass.getDeclaredField("mSelectionDivider");
            selectionDividerField.setAccessible(true);
            selectionDividerField.set(this, getResources().getDrawable(R.drawable.selection_divider));
        }
        catch (ClassNotFoundException e)
        {
            Log.e(TAG, "ClassNotFoundException in CustomNumberPicker", e);
        }
        catch (NoSuchFieldException e)
        {
            Log.e(TAG, "NoSuchFieldException in CustomNumberPicker", e);
        }
        catch (IllegalAccessException e)
        {
            Log.e(TAG, "IllegalAccessException in CustomNumberPicker", e);
        }
    }
}

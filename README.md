SlideDayTimePicker
===================

SlideDayTimePicker is an Android library that displays a single DialogFragment in which the user can select a day of week and a time. The user can swipe between the day picker and time picker, and the tab underline will gradually animate as the user swipes. The colors of the tab indicator and divider lines are customizable to fit your project's theme. Tested on Android 4.0+.

<img src="https://raw.github.com/jjobes/SlideDayTimePicker/master/screenshots/1.png" width="270" style="margin-right:10px;">
<img src="https://raw.github.com/jjobes/SlideDayTimePicker/master/screenshots/2.png" width="270">

Setup
=====

**Eclipse/ADT**:
From your main project, simply reference the SlideDayTimePicker library:

Right click on your project name and select Properties.

Select Android from the left column.

Click Add.

Select SlideDayTimePicker.

Click Apply and then OK.

**Android Studio**:
Coming soon.

How to Use
==========
(See [SampleActivity](https://github.com/jjobes/SlideDayTimePicker/blob/master/SlideDayTimePickerSample/src/com/github/jjobes/slidedaytimepicker/sample/SampleActivity.java) for a more complete example)

First create a listener object:

``` java
final SlideDayTimeListener listener = new SlideDayTimeListener() {

    @Override
    public void onDayTimeSet(int day, int hour, int minute)
    {
        // Do something with the day, hour and minute
        // the user has selected.
    }

    @Override
    public void onDayTimeCancel()
    {
        // The user has canceled the dialog.
        // This override is optional.
    }
};
```

Then pass the listener into the builder and show the dialog:

``` java
new SlideDayTimePicker.Builder(getSupportFragmentManager())
    .setListener(listener)
    .setInitialDay(1)
    .setInitialHour(13)
    .setInitialMinute(30)
    .build()
    .show();
```

**To set the initial day of week to display**

``` java
.setInitialDay(int)
```

The integer that you pass in to `setInitialDay()` should correspond to a `java.util.Calendar` day of week constant. So either `1` or `Calendar.SUNDAY` would be acceptable arguments to `setInitialDay()`. (However, if you specify a custom days array with `setCustomDaysArray()`, pass in the raw index instead.)

**To set the initial hour to display:**

``` java
.setInitialHour(int)
```

Acceptable range of values: 0-23

**To set the initial minute to display:**
``` java
.setInitialMinute(int)
```

Acceptable range of values: 0-59

The default time format is the current device's default, but you can force a 24-hour or 12-hour time format:

**To force 24-hour time:**

``` java
.setIs24HourTime(true)
```

**To force 12-hour time:**
``` java
.setIs24HourTime(false)
```

**To display a custom days array:**
By default, the dialog will display the normal seven days of the week. However, if you want to customize the days to display, you can pass in your own String array.
``` java
.setCustomDaysArray(String[])
```

**The default theme is Holo Light, but you can specify either Holo Light or Dark explicitly:**
``` java
.setTheme(SlideDayTimePicker.HOLO_LIGHT)
```
or
``` java
.setTheme(SlideDayTimePicker.HOLO_DARK)
```

**To specify the color for the sliding tab underline (indicator):**
``` java
.setIndicatorColor(Color.parseColor("#FF0000"))
```

**To specify the color of the horizontal divider lines in the day picker and TimePicker:**
You can also set a custom color for the horizontal divider lines in the day picker and TimePicker, but for this you have to paste your own version of selection_divider.9.png into the the library's drawable-xxxx folders that has your desired color. To do this, open selection_divider.9.png in a graphics editor, change the color, then paste your new files into the drawable-xxxx folders.

Contributing
============
Contributions are welcome! Please open up an issue in GitHub or submit a PR.

Translations are especially needed in `strings.xml`

Changelog
=========

### v1.0.0

* First release

License
=======
Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

Please see [LICENSE](https://github.com/jjobes/SlideDayTimePicker/blob/master/LICENSE)

Acknowledgements
================
Thanks to Arman Pagilagan's [blog post](http://armanpagilagan.blogspot.com/2014/05/creating-custom-date-and-time-picker-in.html) for the initial idea.

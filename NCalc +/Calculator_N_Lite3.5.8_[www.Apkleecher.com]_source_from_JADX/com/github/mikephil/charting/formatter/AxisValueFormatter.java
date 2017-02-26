package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;

public interface AxisValueFormatter {
    int getDecimalDigits();

    String getFormattedValue(float f, AxisBase axisBase);
}

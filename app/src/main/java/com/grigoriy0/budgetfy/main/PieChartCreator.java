package com.grigoriy0.budgetfy.main;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.grigoriy0.budgetfy.R;
import com.grigoriy0.budgetfy.accountdetails.Category;

import java.util.ArrayList;
import java.util.List;

public class PieChartCreator {
    private PieChart pieChart;
    private List<String> categories;
    private List<Float> percents;
    private boolean isLoss;

    PieChartCreator(float[] percents, boolean isLoss, View pieChart) {
        this.pieChart = (PieChart) pieChart;
        this.isLoss = isLoss;
        this.percents = new ArrayList<>();
        this.categories = new ArrayList<>();
        for (int i = 0; i < percents.length; ++i) {
            this.percents.add(percents[i]);
        }
        for (Category cat :
                Category.values()) {
            if (cat.isLoss() == isLoss)
                categories.add(cat.toString());
        }

        this.pieChart.setCenterText(isLoss ? "Loss" : "Increase");
        this.pieChart.setHoleRadius(20.0f);
        this.pieChart.setTransparentCircleAlpha(0);
        this.pieChart.setDrawEntryLabels(true);

        addDataSet(this.pieChart);
    }

    private void addDataSet(PieChart pieChart) {
        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();

        for (int i = 0; i < percents.size(); ++i) {
            yEntries.add(new PieEntry(percents.get(i), i));
        }

        xEntries.addAll(categories);
        PieDataSet dataSet = new PieDataSet(yEntries, "Categories values");
        dataSet.setSliceSpace(2);
        dataSet.setValueTextSize(15);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.BLUE);
        colors.add(Color.MAGENTA);
        colors.add(Color.CYAN);
        colors.add(Color.DKGRAY);
        colors.add(Color.LTGRAY);
        dataSet.setColors(colors);

        Legend legend = new Legend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }


}

package com.grigoriy0.budgetfy.main;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.grigoriy0.budgetfy.accountdetails.Category;
import com.grigoriy0.budgetfy.accountdetails.Transaction;

import java.util.ArrayList;
import java.util.List;

public class PieChartManager {
    private final PieChart pieChart;
    private final boolean isLoss;
    private DataParser parser;

    private static class DataParser {
        public final List<String> categories;
        public final List<Integer> allColors;
        public List<Integer> colors;
        private List<PieEntry> pieEntries;

        DataParser(boolean isLoss) {
            categories = new ArrayList<>();
            allColors = new ArrayList<>();
            colors = new ArrayList<>();
            for (Category cat :
                    Category.values()) {
                if (cat.isLoss() == isLoss) {
                    categories.add(cat.toString());
                    allColors.add(cat.getColor());
                }
            }
        }

        public List<PieEntry> parse(List<Transaction> transactions) {
            if (transactions == null) {
                // return last saved data;
                return pieEntries;
            }
            pieEntries = new ArrayList<>();
            List<Float> percents = new ArrayList<>();
            for (int i = 0; i < categories.size(); i++) {
                float value = 0f;
                for (Transaction trans : transactions) {
                    if (trans.category.toString().equals(categories.get(i)))
                        value += (float) trans.sum / 100;
                }

                percents.add(value);
            }

            colors.clear();
            for (int i = 0; i < percents.size(); ++i) {
                if (percents.get(i) != 0) {
                    PieEntry entry = new PieEntry(percents.get(i), categories.get(i));
                    pieEntries.add(entry);
                    colors.add(allColors.get(i));
                }
            }
            return pieEntries;
        }
    }

    PieChartManager(boolean isLoss, View pieChartView) {
        pieChart = (PieChart) pieChartView;
        this.isLoss = isLoss;

        pieChart.setRotationEnabled(false);
        pieChart.setHoleRadius(40.0f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText(isLoss ? "Loss" : "Increase");
        pieChart.setCenterTextSize(18);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelColor(Color.BLACK);
    }

    public void update(List<Transaction> transactions) {
        if (parser == null)
            parser = new DataParser(isLoss);

        PieDataSet dataSet = new PieDataSet(parser.parse(transactions), "");
        dataSet.setValueTextSize(18);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setColors(parser.colors);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextColor(Color.BLACK);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}

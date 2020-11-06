package com.grigoriy0.budgetfy.main;

import android.app.Application;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.grigoriy0.budgetfy.accountdetails.Category;
import com.grigoriy0.budgetfy.accountdetails.Transaction;
import com.grigoriy0.budgetfy.accountdetails.TransactionRepository;
import com.grigoriy0.budgetfy.accountdetails.TransactionViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PieChartManager {
    private final PieChart pieChart;
    private final boolean isLoss;
    private DataParser parser;

    private static class DataParser {
        public final List<String> categories;
        private List<PieEntry> pieEntries;

        DataParser(boolean isLoss) {
            categories = new ArrayList<>();
            for (Category cat :
                    Category.values()) {
                if (cat.isLoss() == isLoss)
                    categories.add(cat.toString());
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
                for (Transaction trans : transactions){
                    if (trans.category.toString().equals(categories.get(i)))
                        value += (float) trans.sum / 100;
                }
                percents.add(value);
            }

            for (int i = 0; i < percents.size(); ++i) {
                pieEntries.add(new PieEntry(percents.get(i), i));
            }
            return pieEntries;
        }
    }

    PieChartManager(boolean isLoss, View pieChartView) {
        pieChart = (PieChart) pieChartView;
        this.isLoss = isLoss;

        pieChart.setCenterText(isLoss ? "Loss" : "Increase");
        pieChart.setHoleRadius(40.0f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setDrawEntryLabels(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setRotationEnabled(false);
    }

    public void update(List<Transaction> transactions) {
        if (parser == null)
            parser = new DataParser(isLoss);
        List<String> categories = new ArrayList<>();
        for (Category cat :
                Category.values()) {
            if (cat.isLoss() == isLoss)
                categories.add(cat.toString());
        }


        PieDataSet dataSet = new PieDataSet(parser.parse(transactions), "Categories usage");
        dataSet.setSliceSpace(2);
        dataSet.setValueTextSize(15);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.animate();
    }
}

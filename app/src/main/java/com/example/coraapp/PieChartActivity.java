package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PieChartActivity extends AppCompatActivity
{

    PieChart pie_chart;


    private FirebaseDatabase OccRef;
    private DatabaseReference OccDBRef;

    private int AssaultCount = 0;
    private int TheftCount = 0;
    private int BurglaryCount = 0;
    private int SuspiciousCount = 0;
    private int MurderCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        pie_chart = findViewById(R.id.pie_chart);

        OccDBRef = OccRef.getInstance().getReference("Occurrence");


        pie_chart.setDrawHoleEnabled(true);
        pie_chart.setUsePercentValues(true);
        pie_chart.setEntryLabelTextSize(11);
        pie_chart.setEntryLabelColor(Color.BLACK);
        pie_chart.setCenterText("Crime Categories");
        pie_chart.setCenterTextSize(24);
        pie_chart.getDescription().setEnabled(false);
        Legend legend = pie_chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(true);



        OccDBRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                //scan through firebase and increment variables accordingly
                for(DataSnapshot data : snapshot.getChildren())
                {
                    CrimeLocations type = data.getValue(CrimeLocations.class);

                    String OccurrenceType = type.category;

                    if(OccurrenceType.equals("Assault"))
                    {
                        AssaultCount += 1;
                    }
                    if(OccurrenceType.equals("Theft"))
                    {
                        TheftCount += 1;
                    }
                    if(OccurrenceType.equals("Burglary"))
                    {
                        BurglaryCount += 1;
                    }
                    if(OccurrenceType.equals("Other"))
                    {
                        SuspiciousCount += 1;
                    }
                    if(OccurrenceType.equals("Murder"))
                    {
                        MurderCount += 1;
                    }
                }


                ArrayList<PieEntry> entries = new ArrayList<>();

                entries.add(new PieEntry(AssaultCount, "Assault"));
                entries.add(new PieEntry(TheftCount, "Theft"));
                entries.add(new PieEntry(BurglaryCount, "Burglary"));
                entries.add(new PieEntry(MurderCount, "Murder"));
                entries.add(new PieEntry(SuspiciousCount, "Other"));



                ArrayList<Integer> colors = new ArrayList<>();
                for(int color: ColorTemplate.MATERIAL_COLORS)
                {
                    colors.add(color);
                }

                for(int color: ColorTemplate.VORDIPLOM_COLORS)
                {
                    colors.add(color);
                }


                PieDataSet dataSet = new PieDataSet(entries, "Crime Categories");
                dataSet.setColors(colors);


                PieData data = new PieData(dataSet);
                data.setDrawValues(true);
                data.setValueFormatter(new PercentFormatter(pie_chart));
                data.setValueTextSize(11f);
                data.setValueTextColor(Color.BLACK);

                pie_chart.setData(data);
                pie_chart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });



    }


}
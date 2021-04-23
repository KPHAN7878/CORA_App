package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity
{

    private BarChart barchart;

    private FirebaseDatabase OccRef;
    private DatabaseReference OccDBRef;

    private int AssaultCount = 0;
    private int TheftCount = 0;
    private int BurglaryCount = 0;
    private int SuspiciousCount = 0;
    private int MurderCount = 0;

    ArrayList<BarEntry> BARENTRY;

    ArrayList<String> BarEntryLabels;

    BarDataSet Bardataset;

    BarData BARDATA;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        barchart = (BarChart) findViewById(R.id.barchart_id);

        /** crime cateogry array list */
        ArrayList CrimeType = new ArrayList();
        //ArrayList


        OccDBRef = OccRef.getInstance().getReference("Occurrence");


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


                /** initialize charts */
                BARENTRY = new ArrayList<>();

                BarEntryLabels = new ArrayList<String>();

                /**
                BARENTRY.add(new BarEntry(AssaultCount, 0));
                BARENTRY.add(new BarEntry(TheftCount, 1));
                BARENTRY.add(new BarEntry(BurglaryCount, 2));
                BARENTRY.add(new BarEntry(SuspiciousCount, 3));
                BARENTRY.add(new BarEntry(MurderCount, 4));
                */

                BARENTRY.add(new BarEntry(0, AssaultCount));
                BARENTRY.add(new BarEntry(1, TheftCount));
                BARENTRY.add(new BarEntry(2, BurglaryCount));
                BARENTRY.add(new BarEntry(3, SuspiciousCount));
                BARENTRY.add(new BarEntry(4, MurderCount));

                final String[] lb = new String[] {"Assault", "Theft", "Burglar", "Other", "Murder"};
                XAxis xAxis = barchart.getXAxis();
                xAxis.setLabelCount(5);

                xAxis.setValueFormatter(new IndexAxisValueFormatter(lb));
                //xAxis.setGranularity(0.6f);
                //xAxis.setGranularityEnabled(true);


                BarEntryLabels.add("Assault");
                BarEntryLabels.add("Theft");
                BarEntryLabels.add("Burglary");
                BarEntryLabels.add("Other");
                BarEntryLabels.add("Murder");

                Bardataset = new BarDataSet(BARENTRY, "Projects");
                BARDATA = new BarData(Bardataset);
                BARDATA.setBarWidth(0.6f);

                Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

                barchart.setData(BARDATA);
                barchart.getAxisLeft().setDrawGridLines(false);
                barchart.getXAxis().setDrawGridLines(false);
                barchart.getAxisRight().setDrawGridLines(false);

                barchart.animateY(3000);
            }




            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}
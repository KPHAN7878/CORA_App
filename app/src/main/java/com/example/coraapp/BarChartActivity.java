package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

    private Button bar_zipcode_button, bar_return_home;
    private EditText filter_bar_edittext;
    private ImageView back_button2;

    private FirebaseDatabase OccRef;
    private DatabaseReference OccDBRef;

    private int AssaultCount = 0;
    private int TheftCount = 0;
    private int BurglaryCount = 0;
    private int SuspiciousCount = 0;
    private int MurderCount = 0;

    ArrayList<BarEntry> entry;

    ArrayList<String> BarEntryLabels;

    BarDataSet Bardataset;

    BarData data;

    //filter variables
    private String zip_filter = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        try
        {
            zip_filter = getIntent().getExtras().get("filter_data").toString();
        }
        catch(Exception e)
        {

        }

        barchart = (BarChart) findViewById(R.id.barchart_id);

        bar_zipcode_button = findViewById(R.id.bar_zipcode_button);
        bar_return_home = findViewById(R.id.bar_return_home);
        filter_bar_edittext = findViewById(R.id.filter_bar_edittext);
        back_button2 = findViewById(R.id.back_button2);

        back_button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent Home = new Intent(BarChartActivity.this, MainActivity.class);
                startActivity(Home);
            }
        });


        bar_zipcode_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                zip_filter = filter_bar_edittext.getText().toString();
                Intent ReloadFilter = new Intent(BarChartActivity.this, BarChartActivity.class);
                ReloadFilter.putExtra("filter_data", zip_filter);
                startActivity(ReloadFilter);
            }
        });


        bar_return_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent GoHome = new Intent(BarChartActivity.this, MainActivity.class);
                startActivity(GoHome);
            }
        });




        /** crime cateogry array list */
        ArrayList CrimeType = new ArrayList();
        //ArrayList


        OccDBRef = OccRef.getInstance().getReference("Occurrence");


        if(zip_filter.isEmpty())
        {
            AssaultCount = 0;
            TheftCount = 0;
            BurglaryCount = 0;
            SuspiciousCount = 0;
            MurderCount = 0;

            OccDBRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    //scan through firebase and increment variables accordingly
                    for (DataSnapshot data : snapshot.getChildren())
                    {
                        CrimeLocations type = data.getValue(CrimeLocations.class);

                        String OccurrenceType = type.category;
                        String Zipcode = type.zipcode;

                        if (OccurrenceType.equals("Assault"))
                        {
                            AssaultCount += 1;
                        }
                        if (OccurrenceType.equals("Theft"))
                        {
                            TheftCount += 1;
                        }
                        if (OccurrenceType.equals("Burglary"))
                        {
                            BurglaryCount += 1;
                        }
                        if (OccurrenceType.equals("Other"))
                        {
                            SuspiciousCount += 1;
                        }
                        if (OccurrenceType.equals("Murder"))
                        {
                            MurderCount += 1;
                        }
                    }


                    /** initialize charts */
                    entry = new ArrayList<>();

                    BarEntryLabels = new ArrayList<String>();

                    /**
                     BARENTRY.add(new BarEntry(AssaultCount, 0));
                     BARENTRY.add(new BarEntry(TheftCount, 1));
                     BARENTRY.add(new BarEntry(BurglaryCount, 2));
                     BARENTRY.add(new BarEntry(SuspiciousCount, 3));
                     BARENTRY.add(new BarEntry(MurderCount, 4));
                     */

                    entry.add(new BarEntry(0, AssaultCount));
                    entry.add(new BarEntry(1, TheftCount));
                    entry.add(new BarEntry(2, BurglaryCount));
                    entry.add(new BarEntry(3, SuspiciousCount));
                    entry.add(new BarEntry(4, MurderCount));

                    final String[] lb = new String[]{"Assault", "Theft", "Burglar", "Other", "Murder"};
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

                    Bardataset = new BarDataSet(entry, "Projects");
                    data = new BarData(Bardataset);
                    data.setBarWidth(0.6f);

                    Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

                    barchart.setData(data);
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
        else
        {
            AssaultCount = 0;
            TheftCount = 0;
            BurglaryCount = 0;
            SuspiciousCount = 0;
            MurderCount = 0;

            OccDBRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    //scan through firebase and increment variables accordingly
                    for (DataSnapshot data : snapshot.getChildren())
                    {
                        CrimeLocations type = data.getValue(CrimeLocations.class);

                        String OccurrenceType = type.category;
                        String Zipcode = type.zipcode;

                        if (OccurrenceType.equals("Assault") && Zipcode.equals(zip_filter))
                        {
                            AssaultCount += 1;
                        }
                        if (OccurrenceType.equals("Theft") && Zipcode.equals(zip_filter))
                        {
                            TheftCount += 1;
                        }
                        if (OccurrenceType.equals("Burglary") && Zipcode.equals(zip_filter))
                        {
                            BurglaryCount += 1;
                        }
                        if (OccurrenceType.equals("Other") && Zipcode.equals(zip_filter))
                        {
                            SuspiciousCount += 1;
                        }
                        if (OccurrenceType.equals("Murder") && Zipcode.equals(zip_filter))
                        {
                            MurderCount += 1;
                        }
                    }


                    /** initialize charts */
                    entry = new ArrayList<>();

                    BarEntryLabels = new ArrayList<String>();

                    /**
                     BARENTRY.add(new BarEntry(AssaultCount, 0));
                     BARENTRY.add(new BarEntry(TheftCount, 1));
                     BARENTRY.add(new BarEntry(BurglaryCount, 2));
                     BARENTRY.add(new BarEntry(SuspiciousCount, 3));
                     BARENTRY.add(new BarEntry(MurderCount, 4));
                     */

                    entry.add(new BarEntry(0, AssaultCount));
                    entry.add(new BarEntry(1, TheftCount));
                    entry.add(new BarEntry(2, BurglaryCount));
                    entry.add(new BarEntry(3, SuspiciousCount));
                    entry.add(new BarEntry(4, MurderCount));

                    final String[] lb = new String[]{"Assault", "Theft", "Burglar", "Other", "Murder"};
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

                    Bardataset = new BarDataSet(entry, "Projects");
                    data = new BarData(Bardataset);
                    data.setBarWidth(0.6f);

                    Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

                    barchart.setData(data);
                    barchart.getAxisLeft().setDrawGridLines(false);
                    barchart.getXAxis().setDrawGridLines(false);
                    barchart.getAxisRight().setDrawGridLines(false);

                    barchart.animateY(1000);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
        }
    }
}
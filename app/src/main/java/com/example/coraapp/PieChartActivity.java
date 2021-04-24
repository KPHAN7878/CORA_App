package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.concurrent.ExecutionException;

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


    //filter variables
    private String zip_filter = "";

    private Button zipcode_filter, pie_return_home;
    private EditText zipcode_edittext;
    private ImageView back_button2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            zip_filter = getIntent().getExtras().get("filter_data").toString();
        }
        catch(Exception e)
        {

        }

        setContentView(R.layout.activity_pie_chart);

        pie_chart = findViewById(R.id.pie_chart);
        zipcode_filter = findViewById(R.id.zipcode_filter);
        zipcode_edittext = findViewById(R.id.zipcode_edittext);
        pie_return_home = findViewById(R.id.pie_return_home);

        back_button2 = findViewById(R.id.back_button2);

        back_button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent Home = new Intent(PieChartActivity.this, MainActivity.class);
                startActivity(Home);
            }
        });


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


        zipcode_filter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                zip_filter = zipcode_edittext.getText().toString();
                Intent ReloadFilter = new Intent(PieChartActivity.this, PieChartActivity.class);
                ReloadFilter.putExtra("filter_data", zip_filter);
                startActivity(ReloadFilter);

            }
        });


        pie_return_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent GoHome = new Intent(PieChartActivity.this, MainActivity.class);
                startActivity(GoHome);
            }
        });


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


                    ArrayList<PieEntry> entries = new ArrayList<>();

                    if(AssaultCount > 0)
                    {
                        entries.add(new PieEntry(AssaultCount, "Assault"));
                    }
                    if(TheftCount > 0)
                    {
                        entries.add(new PieEntry(TheftCount, "Theft"));
                    }
                    if(BurglaryCount > 0)
                    {
                        entries.add(new PieEntry(BurglaryCount, "Burglary"));
                    }
                    if(MurderCount > 0)
                    {
                        entries.add(new PieEntry(MurderCount, "Murder"));
                    }
                    if(SuspiciousCount > 0)
                    {
                        entries.add(new PieEntry(SuspiciousCount, "Other"));
                    }


                    ArrayList<Integer> colors = new ArrayList<>();
                    for (int color : ColorTemplate.MATERIAL_COLORS)
                    {
                        colors.add(color);
                    }

                    for (int color : ColorTemplate.VORDIPLOM_COLORS)
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
        else
        {
            AssaultCount = 0;
            TheftCount = 0;
            BurglaryCount = 0;
            SuspiciousCount = 0;
            MurderCount = 0;
            Toast.makeText(this, zip_filter, Toast.LENGTH_SHORT).show();

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


                    ArrayList<PieEntry> entries = new ArrayList<>();

                    if(AssaultCount > 0)
                    {
                        entries.add(new PieEntry(AssaultCount, "Assault"));
                    }
                    if(TheftCount > 0)
                    {
                        entries.add(new PieEntry(TheftCount, "Theft"));
                    }
                    if(BurglaryCount > 0)
                    {
                        entries.add(new PieEntry(BurglaryCount, "Burglary"));
                    }
                    if(MurderCount > 0)
                    {
                        entries.add(new PieEntry(MurderCount, "Murder"));
                    }
                    if(SuspiciousCount > 0)
                    {
                        entries.add(new PieEntry(SuspiciousCount, "Other"));
                    }


                    ArrayList<Integer> colors = new ArrayList<>();
                    for (int color : ColorTemplate.MATERIAL_COLORS)
                    {
                        colors.add(color);
                    }

                    for (int color : ColorTemplate.VORDIPLOM_COLORS)
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




}
package com.example.coraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseChart extends AppCompatActivity
{

    Button choose_barchart, choose_piechart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_chart);

        choose_barchart = findViewById(R.id.choose_barchart);
        choose_piechart = findViewById(R.id.choose_piechart);



        choose_barchart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent GoToBar = new Intent(ChooseChart.this, BarChartActivity.class);
                startActivity(GoToBar);
            }
        });


        choose_piechart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent GoToPie = new Intent(ChooseChart.this, PieChartActivity.class);
                startActivity(GoToPie);
            }
        });
    }
}
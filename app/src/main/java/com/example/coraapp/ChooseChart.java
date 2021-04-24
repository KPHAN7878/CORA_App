package com.example.coraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ChooseChart extends AppCompatActivity
{

    private Button choose_barchart, choose_piechart;
    private ImageView back_button2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_chart);

        choose_barchart = findViewById(R.id.choose_barchart);
        choose_piechart = findViewById(R.id.choose_piechart);

        back_button2 = findViewById(R.id.back_button2);

        back_button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent Home = new Intent(ChooseChart.this, MainActivity.class);
                startActivity(Home);
            }
        });



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
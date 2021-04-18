package com.example.coraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;

public class MapFilter extends AppCompatActivity
{

    private Switch theft_switch, burglar_switch, assault_switch, murder_switch, other_switch;

    private Button filter_submit;

    private ArrayList<String> filters = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_filter);

        theft_switch = findViewById(R.id.theft_switch);
        burglar_switch = findViewById(R.id.burglar_switch);
        assault_switch = findViewById(R.id.assault_switch);
        murder_switch = findViewById(R.id.murder_switch);
        other_switch = findViewById(R.id.other_switch);

        filter_submit = findViewById(R.id.filter_submit);

        /** get array list from MapPlot activity */
        Bundle extra = getIntent().getBundleExtra("extra");
        filters = (ArrayList<String>) extra.getSerializable("crimefilters");

        //theft onclick listener
        theft_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    filters.add("Theft");
                }
                else
                {
                    if(filters.contains("Theft"))
                    {
                        filters.remove("Theft");
                    }
                }
            }
        });

        //theft onclick listener
        burglar_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    filters.add("Burglary");
                }
                else
                {
                    if(filters.contains("Burglary"))
                    {
                        filters.remove("Burglary");
                    }
                }
            }
        });

        //theft onclick listener
        assault_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    filters.add("Assault");
                }
                else
                {
                    if(filters.contains("Assault"))
                    {
                        filters.remove("Assault");
                    }
                }
            }
        });

        //theft onclick listener
        murder_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    filters.add("Murder");
                }
                else
                {
                    if(filters.contains("Murder"))
                    {
                        filters.remove("Murder");
                    }
                }
            }
        });

        //theft onclick listener
        other_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    filters.add("Suspicious Activity");
                }
                else
                {
                    if(filters.contains("Suspicious Activity"))
                    {
                        filters.remove("Suspicious Activity");
                    }
                }
            }
        });


        /** filter submit on click listener */
        filter_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle filteredextra = new Bundle();
                filteredextra.putSerializable("filterSetted", filters);
                Intent GoToMapPlot = new Intent(MapFilter.this, MapPlot.class);
                GoToMapPlot.putExtra("extra", filteredextra);
                startActivity(GoToMapPlot);
            }
        });
    }
}
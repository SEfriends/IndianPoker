package com.example.samsung.indianpoker1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void ranking_click(View v){
        Intent intent_ranking = new Intent(getApplicationContext(), Ranking.class);
        startActivity(intent_ranking);
    }
    public void help_click(View v){
        Intent intent_help = new Intent(getApplicationContext(), Help1.class);
        startActivity(intent_help);
    }
}

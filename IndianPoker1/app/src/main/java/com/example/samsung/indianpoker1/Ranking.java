package com.example.samsung.indianpoker1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Ranking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
    }
    public void OK_click(View v){
        Intent intent_ok = new Intent(getApplicationContext(), Login.class);
        startActivity(intent_ok);
    }
}

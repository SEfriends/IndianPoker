package com.example.samsung.indianpoker1;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Help1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help1);
    }
    public void bet_click(View v){
        Intent intent_bet = new Intent(getApplicationContext(), Help2.class);
        startActivity(intent_bet);
    }

    public void main_click(View v){
        Intent intent_main = new Intent(getApplicationContext(), Main.class);
        startActivity(intent_main);
    }
}

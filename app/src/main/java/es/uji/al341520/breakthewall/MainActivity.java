package es.uji.al341520.breakthewall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import es.uji.al341520.breakthewall.testCharacter.TestCharacter;
import es.uji.al341520.breakthewall.testParallax.TestParallax;
import es.uji.al341520.breakthewall.testFramework.TestFramework;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void testFramework(View view){
        Intent intent = new Intent(this, TestFramework.class);
        startActivity(intent);
    }

    public void testParallax(View view){
        Intent intent = new Intent(this, TestParallax.class);
        startActivity(intent);
    }

    public void testCharacter(View view){
        Intent intent = new Intent(this, TestCharacter.class);
        startActivity(intent);
    }
    /*
    public void testBricks(View view){
        Intent intent = new Intent(this, TestBricks.class);
        startActivity(intent);
    }
    public void testLevels(View view){
        Intent intent = new Intent(this, TestLevels.class);
        startActivity(intent);
    }
    public void testLives(View view){
        Intent intent = new Intent(this, TestLives.class);
        startActivity(intent);
    }
    public void playGame(View view){
        Intent intent = new Intent(this, PlayGame.class);
        startActivity(intent);
    }*/



}

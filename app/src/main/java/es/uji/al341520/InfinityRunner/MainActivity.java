package es.uji.al341520.InfinityRunner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import es.uji.al341520.InfinityRunner.InfinityRunner.InfinityRunner;
import es.uji.al341520.InfinityRunner.testCharacter.TestCharacter;
import es.uji.al341520.InfinityRunner.testLevelsHUD.TestLevelsHud;
import es.uji.al341520.InfinityRunner.testObstacles.TestObstacles;
import es.uji.al341520.InfinityRunner.testParallax.TestParallax;
import es.uji.al341520.InfinityRunner.testFramework.TestFramework;

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

    public void testObstacles(View view){
        Intent intent = new Intent(this, TestObstacles.class);
        startActivity(intent);
    }
    public void testLevelsHud(View view){
        Intent intent = new Intent(this, TestLevelsHud.class);
        startActivity(intent);
    }
    public void ujiRunner(View view){
        Intent intent = new Intent(this, InfinityRunner.class);
        startActivity(intent);
    }



}

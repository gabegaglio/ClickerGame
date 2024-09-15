package com.example.clickgame;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button counterBtn1, counterBtn2, startBtn;
    TextView timer, counter, highScore;

    Integer timeLeft=20, count=0, firstScore=-1, newScore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        counterBtn1=findViewById(R.id.counterBtn1);
        counterBtn2=findViewById(R.id.counterBtn2);
        startBtn=findViewById(R.id.startBtn);
        timer=findViewById(R.id.timer);
        counter=findViewById(R.id.counter);
        highScore=findViewById(R.id.highScore);

        counterBtn1.setEnabled(false);
        counterBtn2.setEnabled(false);

        CountDownTimer time = new CountDownTimer(20000,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long l) {
                timeLeft--;
                timer.setText("Time: "+timeLeft+"s");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                counterBtn1.setEnabled(false);
                counterBtn2.setEnabled(false);
                startBtn.setEnabled(true);

                //done to seperate the string from count value int
                String counterText = counter.getText().toString();
                String[] parts = counterText.split("Clicks: ");
                newScore = Integer.parseInt(parts[1].trim());

                if (firstScore == -1 || newScore > firstScore) {
                    firstScore = newScore;
                    highScore.setText("High Score: " + firstScore);
                }
            }
        };

        counterBtn1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                boolean overlap;
                int randomX, randomY;

                // Get the screen dimensions
                View root = counterBtn1.getRootView();
                int screenWidth = root.getWidth();
                int screenHeight = root.getHeight();

                // Get button size
                int buttonWidth = counterBtn1.getWidth();
                int buttonHeight = counterBtn1.getHeight();

                do {
                    // Calculate the maximum random values for X and Y with a 30px buffer
                    int maxX = screenWidth - buttonWidth - 50;
                    int maxY = screenHeight - buttonHeight - 50;

                    // Generate random X and Y positions within screen bounds, considering the buffer zone
                    randomX = (int) (Math.random() * maxX) - (maxX / 2);
                    randomY = (int) (Math.random() * maxY) - (maxY / 2);

                    // Ensure the position is at least 30px away from the edge
                    if (randomX < 50) randomX = 50;
                    if (randomY < 50) randomY = 50;

                    // Get the current location of the second button
                    int btn2X = (int) counterBtn2.getX();
                    int btn2Y = (int) counterBtn2.getY();

                    // Check for overlap with counterBtn2
                    overlap = (btn2X == randomX && btn2Y == randomY);

                    if (overlap) {
                        Log.d("OverlapCheck", "Overlap detected!");
                    }
                } while (overlap); // Keep generating new random positions until there's no overlap

                // Move the button to the new random position
                counterBtn1.animate()
                        .translationX(randomX)
                        .translationY(randomY)
                        .setDuration(250)
                        .start();

                // Randomly enable or disable buttons
                int randInt = (int) (Math.random() * 2 + 1);

                if (randInt == 1) {
                    counterBtn1.setEnabled(false);
                    counterBtn2.setEnabled(true);
                } else {
                    counterBtn1.setEnabled(true);
                    counterBtn2.setEnabled(false);
                }

                count++;
                counter.setText("Clicks: " + count);
            }
        });


        counterBtn2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                boolean overlap;
                int randomX, randomY;

                // Get the screen dimensions
                View root = counterBtn2.getRootView();
                int screenWidth = root.getWidth();
                int screenHeight = root.getHeight();

                // Get button size
                int buttonWidth = counterBtn2.getWidth();
                int buttonHeight = counterBtn2.getHeight();

                do {
                    // Calculate the maximum random values for X and Y with a 30px buffer
                    int maxX = screenWidth - buttonWidth - 50;
                    int maxY = screenHeight - buttonHeight - 50;

                    // Generate random X and Y positions within screen bounds, considering the buffer zone
                    randomX = (int) (Math.random() * maxX) - (maxX / 2);
                    randomY = (int) (Math.random() * maxY) - (maxY / 2);

                    // Ensure the position is at least 30px away from the edge
                    if (randomX < 50) randomX = 50;
                    if (randomY < 50) randomY = 50;

                    // Get the current location of the first button
                    int btn1X = (int) counterBtn1.getX();
                    int btn1Y = (int) counterBtn1.getY();

                    // Check for overlap with counterBtn1
                    overlap = (btn1X == randomX && btn1Y == randomY);

                    if (overlap) {
                        Log.d("OverlapCheck", "Overlap detected!");
                    }
                } while (overlap); // Keep generating new random positions until there's no overlap

                // Move the button to the new random position
                counterBtn2.animate()
                        .translationX(randomX)
                        .translationY(randomY)
                        .setDuration(250)
                        .start();

                // Randomly enable or disable buttons
                int randInt = (int) (Math.random() * 2 + 1);

                if (randInt == 1) {
                    counterBtn1.setEnabled(false);
                    counterBtn2.setEnabled(true);
                } else {
                    counterBtn1.setEnabled(true);
                    counterBtn2.setEnabled(false);
                }

                count++;
                counter.setText("Clicks: " + count);
            }
        });




        startBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                time.start();
                startBtn.setEnabled(false);
                counterBtn1.setEnabled(true);
                timeLeft=20;
                count=0;
                timer.setText("Time: "+timeLeft);
                counter.setText("Number of clicks: "+count);
                counterBtn1.setTranslationX(0); //reset buttons back to original
                counterBtn1.setTranslationY(0);
                counterBtn2.setTranslationX(0);
                counterBtn2.setTranslationY(0);
            }
        });

    }
}
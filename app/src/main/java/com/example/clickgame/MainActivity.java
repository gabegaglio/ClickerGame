package com.example.clickgame;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button counterBtn1, counterBtn2, startBtn;
    TextView timer, counter, highScoreTV;
    Integer timeLeft = 20, count = 0, score = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counterBtn1 = findViewById(R.id.counterBtn1);
        counterBtn2 = findViewById(R.id.counterBtn2);
        startBtn = findViewById(R.id.startBtn);
        timer = findViewById(R.id.timer);
        counter = findViewById(R.id.counter);
        highScoreTV = findViewById(R.id.highScore);
        FrameLayout frame = findViewById(R.id.frame);

        counterBtn1.setEnabled(false);
        counterBtn2.setEnabled(false);
        counter.setVisibility(View.INVISIBLE);

        // get high score from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        int highScore = sharedPreferences.getInt("highScore", 0);
        highScoreTV.setText("High Score: " + highScore);

        // create button handler class
        ButtonHandler buttonHandler = new ButtonHandler(frame, counterBtn1, counterBtn2);

        CountDownTimer time = new CountDownTimer(20000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long l) {
                timeLeft--;
                timer.setText(timeLeft.toString() + "s");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                counterBtn1.setEnabled(false);
                counterBtn2.setEnabled(false);
                startBtn.setEnabled(true);
                startBtn.setVisibility(View.VISIBLE);
                timer.setText("20s");

                int newScore = Integer.parseInt(counter.getText().toString());

                if (score == -1 || newScore > score) {
                    score = newScore;
                    highScoreTV.setText("High Score: " + newScore);

                    // set score to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("highScore", score);
                    editor.apply();
                    Toast.makeText(MainActivity.this, "New High Score: "+ score, Toast.LENGTH_LONG).show();
                }
            }
        };

        counterBtn1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                buttonHandler.moveBtn(counterBtn1);
                buttonHandler.toggleBtn(); //doesnt need to be passed in since btn1 always starts off enabled
                count++;
                counter.setText(count.toString());
            }
        });

        counterBtn2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                buttonHandler.moveBtn(counterBtn2);
                buttonHandler.toggleBtn();
                count++;
                counter.setText(count.toString());
            }
        });

        // Set click listener for startBtn
        startBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                time.start();
                startBtn.setEnabled(false);
                startBtn.setVisibility(View.INVISIBLE);
                counter.setVisibility(View.VISIBLE);
                counterBtn1.setEnabled(true);
                timeLeft = 20;
                count = 0;
                timer.setText(timeLeft + "s");
                counter.setText(count.toString());
                counterBtn1.setTranslationX(0);
                counterBtn1.setTranslationY(0);
                counterBtn2.setTranslationX(0);
                counterBtn2.setTranslationY(0);
            }
        });
    }

    // define button handler class
    public class ButtonHandler {
        private View root;
        private Button button1, button2;
        private int screenWidth, screenHeight;

        public ButtonHandler(View root, Button button1, Button button2) {
            this.root = root;
            this.button1 = button1;
            this.button2 = button2;

            root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // Remove the listener to prevent multiple calls
                    root.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    // Now you can get the width and height
                    screenWidth = root.getWidth();
                    screenHeight = root.getHeight();

                    // log the screen dimensions to check
                    Log.d("ButtonHandler", "Screen Width: " + screenWidth + ", Screen Height: " + screenHeight);
                }
            });
        }

        public void moveBtn(Button button) {
            boolean overlap;
            int randomX, randomY;

            int buttonWidth = button.getWidth();
            int buttonHeight = button.getHeight();

            int xLocation = (int) button.getX();
            int yLocation = (int) button.getY();

            // Get the position of the other button
            int otherBtnX = (int) (button == button1 ? button2.getX() : button1.getX());
            int otherBtnY = (int) (button == button1 ? button2.getY() : button1.getY());
            Log.d("ButtonHandler", "Other button position: " + otherBtnX + ", " + otherBtnY);

            do {
                overlap = false;
                // Generate random X and Y positions within screen bounds
                randomX = (int) (Math.random() * (screenWidth - buttonWidth - xLocation));
                randomY = (int) (Math.random() * (screenHeight - buttonHeight - yLocation));

                // Ensure the button stays within screen bounds
                if (randomX + buttonWidth > screenWidth) {
                    randomX = screenWidth - buttonWidth;
                    Log.d("ButtonHandler", "X out of bound");
                }
                if (randomY + buttonHeight > screenHeight) {
                    randomY = screenHeight - buttonHeight;
                    Log.d("ButtonHandler", "Y out of bound");
                }
                // Check for overlap
                if (isOverlapping(randomX, randomY, buttonWidth, buttonHeight, otherBtnX, otherBtnY, buttonWidth, buttonHeight)) {
                    overlap = true;
                    Log.d("ButtonHandler", "Overlap detected, recalculating...");
                }

            } while (overlap); // Repeat until no overlap is found

            // Animate the button to the new random position
            button.animate()
                    .translationX(randomX)
                    .translationY(randomY)
                    .rotationBy(360)
                    .setDuration(100)
                    .start();
            Log.d("ButtonHandler", "New position: " + button.getX() + ", " + button.getY());
        }
        // Function to check if two buttons overlap
        private boolean isOverlapping(int x1, int y1, int width1, int height1, int x2, int y2, int width2, int height2) {
            return !(x1 + width1 < x2 || // Completely to the left
                    x1 > x2 + width2 || // Completely to the right
                    y1 + height1 < y2 || // Completely above
                    y1 > y2 + height2);  // Completely below
        }

        public void toggleBtn() {
            int randInt = (int) (Math.random() * 2 + 1); // Generates either 1 or 2

            if (randInt == 1) {

                button1.setEnabled(false);
                button1.setClickable(false);
                button1.setFocusable(false);

                button2.setEnabled(true);
                button2.setClickable(true);
                button2.setFocusable(true);
            } else {
                button2.setEnabled(false);
                button2.setClickable(false);
                button2.setFocusable(false);

                button1.setEnabled(true);
                button1.setClickable(true);
                button1.setFocusable(true);
            }
        }
    }
}




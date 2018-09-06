package com.ni3bobade.khelkabaddi;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SplashActivity extends AppCompatActivity {

    Button letsPlayButton;
    EditText matchTimeEditText, raidTimeEditText;
    ConstraintLayout splashActivityConstraintLayout;

    public static final String MATCH_TIME_IN_MINUTES = "com.ni3bobade.khelkabaddi.MATCH_TIME_IN_MINUTES";
    public static final String RAID_TIME_IN_SECONDS = "com.ni3bobade.khelkabaddi.RAID_TIME_IN_SECONDS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        letsPlayButton = findViewById(R.id.lets_play_button);
        matchTimeEditText = findViewById(R.id.match_time_editText);
        raidTimeEditText = findViewById(R.id.raid_time_editText);
        splashActivityConstraintLayout = findViewById(R.id.splash_activity_constraintLayout);

        // restrict the user to enter valid match and raid timings
        letsPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String matchTimeInput = matchTimeEditText.getText().toString();
                String raidTimeInput = raidTimeEditText.getText().toString();

                if (matchTimeInput.length() == 0) {
                    Snackbar.make(splashActivityConstraintLayout, R.string.match_time_cant_be_empty, Snackbar.LENGTH_SHORT).show();
                    return;
                } else if (raidTimeInput.length() == 0) {
                    Snackbar.make(splashActivityConstraintLayout, R.string.raid_time_cant_be_empty, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // divide the match duration in two halves
                long matchTimeMillisInput = Long.parseLong(matchTimeInput) / 2;

                long raidTimeMillisInput = Long.parseLong(raidTimeInput);

                if (matchTimeMillisInput < 2) {
                    Snackbar.make(splashActivityConstraintLayout, R.string.match_time_cant_be_less, Snackbar.LENGTH_SHORT).show();
                    return;
                } else if (raidTimeMillisInput < 10) {
                    Snackbar.make(splashActivityConstraintLayout, R.string.raid_time_cant_be_less, Snackbar.LENGTH_SHORT).show();
                    return;
                } else if (matchTimeMillisInput > 20) {
                    Snackbar.make(splashActivityConstraintLayout, R.string.match_time_cant_be_greater, Snackbar.LENGTH_SHORT).show();
                    return;
                } else if (raidTimeMillisInput > 30) {
                    Snackbar.make(splashActivityConstraintLayout, R.string.raid_time_cant_be_greater, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // send the match and raid timings to game play screen
                Intent letsPlayIntent = new Intent(SplashActivity.this, MainActivity.class);

                matchTimeMillisInput = matchTimeMillisInput * 60000;
                raidTimeMillisInput = raidTimeMillisInput * 1000;

                letsPlayIntent.putExtra(MATCH_TIME_IN_MINUTES, matchTimeMillisInput);
                letsPlayIntent.putExtra(RAID_TIME_IN_SECONDS, raidTimeMillisInput);

                letsPlayIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                startActivity(letsPlayIntent);

                finish();
            }
        });
    }
}

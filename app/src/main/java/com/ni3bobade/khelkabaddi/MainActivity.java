package com.ni3bobade.khelkabaddi;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private long matchTimeInMillis;
    private long raidTimeInMillis;

    public static final String FIRST_TEAM_RESULT = "com.ni3bobade.khelkabaddi.FIRST_TEAM_RESULT";
    public static final String SECOND_TEAM_RESULT = "com.ni3bobade.khelkabaddi.SECOND_TEAM_RESULT";

    // declaring and initializing each team score variables
    private int firstTeamScore = 0;
    private int secondTeamScore = 0;

    // initializing views
    ConstraintLayout mainActivityConstraintLayout;

    // TextViews
    TextView firstTeamScoreTextView;
    TextView secondTeamScoreTextView;
    TextView firstOrSecondHalfLabelTextView;
    TextView gamePlayCountdownTimerTextView;
    TextView firstTeamPlayersInCourtCountTextView;
    TextView secondTeamPlayersInCourtCountTextView;
    TextView raidCountdownTimerTextView;

    // Buttons
    Button firstTeamRaidButton;
    Button secondTeamRaidButton;
    Button firstTeamTouchPointButton;
    Button secondTeamTouchPointButton;
    Button firstTeamTackleButton;
    Button secondTeamTackleButton;
    Button startSecondHalfButton;

    // ProgressBar
    ProgressBar raidProgressBar;

    // countdown timer
    private CountDownTimer raidCountDownTimer;
    private CountDownTimer gamePlayCountDownTimer;

    // players in court
    private int firstTeamPlayersInCourtCount = 7;
    private int secondTeamPlayersInCourtCount = 7;

    private long raidTimeLeftInMillis;
    private long matchTimeLeftInMillis;

    private long backPressedTime;
    private Snackbar backPressedSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent matchAndRaidInputIntent = getIntent();

        long matchTimeInMinutes = matchAndRaidInputIntent.getLongExtra(SplashActivity.MATCH_TIME_IN_MINUTES, 0);
        long raidTimeInSeconds = matchAndRaidInputIntent.getLongExtra(SplashActivity.RAID_TIME_IN_SECONDS, 0);

        int raidProgress = (int) (raidTimeInSeconds / 1000);

        Toast.makeText(getApplicationContext(), R.string.first_half_started, Toast.LENGTH_LONG).show();

        matchTimeInMillis = matchTimeInMinutes;
        raidTimeInMillis = raidTimeInSeconds;

        matchTimeLeftInMillis = matchTimeInMillis;
        raidTimeLeftInMillis = raidTimeInMillis;

        // start the first half gamePlay countdown timer as the activity starts
        startFirstHalfGamePlayCountDownTimer();

        // required for snackbar
        mainActivityConstraintLayout = findViewById(R.id.main_activity_constraint_layout);

        // TextViews
        firstTeamScoreTextView = findViewById(R.id.first_team_score_textView);
        secondTeamScoreTextView = findViewById(R.id.second_team_score_textView);
        firstOrSecondHalfLabelTextView = findViewById(R.id.first_or_second_half_label_textView);
        gamePlayCountdownTimerTextView = findViewById(R.id.game_play_countdown_timer_textView);
        firstTeamPlayersInCourtCountTextView = findViewById(R.id.first_team_players_in_court_count_textView);
        secondTeamPlayersInCourtCountTextView = findViewById(R.id.second_team_players_in_court_count_textView);
        raidCountdownTimerTextView = findViewById(R.id.raid_countdown_timer_textView);

        // Buttons
        firstTeamRaidButton = findViewById(R.id.first_team_raid_button);
        secondTeamRaidButton = findViewById(R.id.second_team_raid_button);
        firstTeamTouchPointButton = findViewById(R.id.first_team_touch_point_button);
        secondTeamTouchPointButton = findViewById(R.id.second_team_touch_point_button);
        firstTeamTackleButton = findViewById(R.id.first_team_tackle_button);
        secondTeamTackleButton = findViewById(R.id.second_team_tackle_button);

        // second half button
        startSecondHalfButton = findViewById(R.id.start_second_half_button);

        // ProgressBar
        raidProgressBar = findViewById(R.id.raid_progressBar);
        raidProgressBar.setMax(raidProgress);

        // disable the buttons that are not required when match starts
        enableDisableButtons();

        startSecondHalfButton.setVisibility(View.INVISIBLE);

        // firstTeamRaidButton onClick listener
        firstTeamRaidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRaidTimer();

                firstTeamRaidButton.setEnabled(false);
                secondTeamRaidButton.setEnabled(false);
                firstTeamTouchPointButton.setEnabled(true);
                secondTeamTouchPointButton.setEnabled(false);
                firstTeamTackleButton.setEnabled(false);
                secondTeamTackleButton.setEnabled(true);
            }
        });

        // secondTeamRaidButton onClick listener
        secondTeamRaidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRaidTimer();

                firstTeamRaidButton.setEnabled(false);
                secondTeamRaidButton.setEnabled(false);
                firstTeamTouchPointButton.setEnabled(false);
                secondTeamTouchPointButton.setEnabled(true);
                firstTeamTackleButton.setEnabled(true);
                secondTeamTackleButton.setEnabled(false);
            }
        });

        firstTeamTouchPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstTeamTouchAndTacklePoint();

                Snackbar.make(mainActivityConstraintLayout, R.string.touch_point_mumba, Snackbar.LENGTH_SHORT).show();
            }
        });

        secondTeamTouchPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                secondTeamTouchAndTacklePoint();

                Snackbar.make(mainActivityConstraintLayout, R.string.touch_point_paltan, Snackbar.LENGTH_SHORT).show();
            }
        });

        firstTeamTackleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstTeamTouchAndTacklePoint();

                Snackbar.make(mainActivityConstraintLayout, R.string.tackle_point_mumba, Snackbar.LENGTH_SHORT).show();
            }
        });

        secondTeamTackleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondTeamTouchAndTacklePoint();
                Snackbar.make(mainActivityConstraintLayout, R.string.tackle_point_paltan, Snackbar.LENGTH_SHORT).show();
            }
        });

        // second half button
        startSecondHalfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.second_half_started, Toast.LENGTH_SHORT).show();

                firstOrSecondHalfLabelTextView.setText(R.string.second_half);

                startSecondHalfGamePlayCountDownTimer();

                enableDisableButtons();

                startSecondHalfButton.setVisibility(View.INVISIBLE);
            }
        });

        updateRaidCountDownText();
    }

    // handle the unexpected back button press
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            backPressedSnackbar.dismiss();
            return;
        } else {
            backPressedSnackbar = Snackbar.make(mainActivityConstraintLayout, R.string.press_again_to_exit, Snackbar.LENGTH_SHORT);
            backPressedSnackbar.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    // first team touch and tackle point
    private void firstTeamTouchAndTacklePoint() {
        resetRaidTimer();

        if (firstTeamScore < 9) {
            firstTeamScoreTextView.setText("0" + ++firstTeamScore);
        } else {
            firstTeamScoreTextView.setText("" + ++firstTeamScore);
        }

        secondTeamPlayersInCourtCountTextView.setText(MessageFormat.format("{0}", --secondTeamPlayersInCourtCount));

        if (firstTeamPlayersInCourtCount == 7) {
            firstTeamPlayersInCourtCountTextView.setText(MessageFormat.format("{0}", firstTeamPlayersInCourtCount));
        } else {
            firstTeamPlayersInCourtCountTextView.setText(MessageFormat.format("{0}", ++firstTeamPlayersInCourtCount));
        }

        enableDisableButtons();
    }

    // second team touch and tackle point
    private void secondTeamTouchAndTacklePoint() {
        resetRaidTimer();

        if (secondTeamScore < 9) {
            secondTeamScoreTextView.setText("0" + ++secondTeamScore);
        } else {
            secondTeamScoreTextView.setText("" + ++secondTeamScore);
        }

        firstTeamPlayersInCourtCountTextView.setText(MessageFormat.format("{0}", --firstTeamPlayersInCourtCount));

        if (secondTeamPlayersInCourtCount == 7) {
            secondTeamPlayersInCourtCountTextView.setText(MessageFormat.format("{0}", secondTeamPlayersInCourtCount));
        } else {
            secondTeamPlayersInCourtCountTextView.setText(MessageFormat.format("{0}", ++secondTeamPlayersInCourtCount));
        }

        enableDisableButtons();
    }

    // enable or disable the buttons
    private void enableDisableButtons() {
        firstTeamRaidButton.setEnabled(true);
        secondTeamRaidButton.setEnabled(true);
        firstTeamTouchPointButton.setEnabled(false);
        secondTeamTouchPointButton.setEnabled(false);
        firstTeamTackleButton.setEnabled(false);
        secondTeamTackleButton.setEnabled(false);
    }

    // first half gamePlay countdown timer
    private void startFirstHalfGamePlayCountDownTimer() {
        gamePlayCountDownTimer = new CountDownTimer(matchTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                matchTimeLeftInMillis = millisUntilFinished;
                updateFirstHalfGamePlayCountDownText();
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), R.string.first_half_over, Toast.LENGTH_SHORT).show();
                resetFirstHalfGamePlayTimer();
                startSecondHalfButton.setVisibility(View.VISIBLE);
                resetRaidTimer();
                enableDisableButtons();

                firstTeamRaidButton.setEnabled(false);
                secondTeamRaidButton.setEnabled(false);
                gamePlayCountdownTimerTextView.setText(R.string.game_play_countdown_timer_text);
            }
        }.start();
    }

    private void updateFirstHalfGamePlayCountDownText() {
        int minutes = (int) ((matchTimeLeftInMillis / 1000) / 60);
        int seconds = (int) ((matchTimeLeftInMillis / 1000) % 60);

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        gamePlayCountdownTimerTextView.setText(timeLeftFormatted);
    }

    private void resetFirstHalfGamePlayTimer() {
        gamePlayCountDownTimer.cancel();
        matchTimeLeftInMillis = matchTimeInMillis;
        updateFirstHalfGamePlayCountDownText();
    }

    // second half gamePlay countdown timer
    private void startSecondHalfGamePlayCountDownTimer() {
        gamePlayCountDownTimer = new CountDownTimer(matchTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                matchTimeLeftInMillis = millisUntilFinished;
                updateSecondHalfGamePlayCountDownText();
            }

            @Override
            public void onFinish() {
                resetSecondHalfGamePlayTimer();
                startSecondHalfButton.setVisibility(View.INVISIBLE);
                resetRaidTimer();
                enableDisableButtons();

                firstTeamRaidButton.setEnabled(false);
                secondTeamRaidButton.setEnabled(false);
                gamePlayCountdownTimerTextView.setText(R.string.game_play_countdown_timer_text);

                declareWinner();
            }
        }.start();
    }

    private void updateSecondHalfGamePlayCountDownText() {
        int minutes = (int) ((matchTimeLeftInMillis / 1000) / 60);
        int seconds = (int) ((matchTimeLeftInMillis / 1000) % 60);

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        gamePlayCountdownTimerTextView.setText(timeLeftFormatted);
    }

    private void resetSecondHalfGamePlayTimer() {
        gamePlayCountDownTimer.cancel();
        matchTimeLeftInMillis = matchTimeInMillis;
        updateSecondHalfGamePlayCountDownText();
    }

    // raid countdown timer
    private void startRaidTimer() {
        raidCountDownTimer = new CountDownTimer(raidTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                raidTimeLeftInMillis = millisUntilFinished;
                updateRaidCountDownText();
                checkForAllOut();
            }

            @Override
            public void onFinish() {
                Snackbar.make(mainActivityConstraintLayout, R.string.empty_raid, Snackbar.LENGTH_SHORT).show();
                resetRaidTimer();
                enableDisableButtons();
            }
        }.start();
    }

    private void updateRaidCountDownText() {
        int seconds = (int) ((raidTimeLeftInMillis / 1000) % 60);
        raidProgressBar.setProgress(seconds);
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
        raidCountdownTimerTextView.setText(timeLeftFormatted);
    }

    private void resetRaidTimer() {
        raidCountDownTimer.cancel();
        raidTimeLeftInMillis = raidTimeInMillis;
        updateRaidCountDownText();
    }

    // check for all out
    private void checkForAllOut() {
        if (firstTeamPlayersInCourtCount < 1) {
            Snackbar.make(mainActivityConstraintLayout, R.string.mumba_all_out, Snackbar.LENGTH_LONG).show();

            secondTeamScore += 2;
            secondTeamScoreTextView.setText(MessageFormat.format("{0}", secondTeamScore));

            enableDisableButtons();
            placePlayersBackInGame();
            resetRaidTimer();
        } else if (secondTeamPlayersInCourtCount < 1) {
            Snackbar.make(mainActivityConstraintLayout, R.string.paltan_all_out, Snackbar.LENGTH_LONG).show();

            firstTeamScore += 2;
            firstTeamScoreTextView.setText(MessageFormat.format("{0}", firstTeamScore));

            enableDisableButtons();
            placePlayersBackInGame();
            resetRaidTimer();
        }
    }

    // place the players back in court
    private void placePlayersBackInGame() {
        firstTeamPlayersInCourtCount = 7;
        firstTeamPlayersInCourtCountTextView.setText(MessageFormat.format("{0}", firstTeamPlayersInCourtCount));
        secondTeamPlayersInCourtCount = 7;
        secondTeamPlayersInCourtCountTextView.setText(MessageFormat.format("{0}", secondTeamPlayersInCourtCount));
    }

    // declare the winner
    private void declareWinner() {

        // start match summary activity
        Intent matchSummaryIntent = new Intent(MainActivity.this, MatchSummaryActivity.class);

        int firstTeamResultScore = Integer.parseInt(firstTeamScoreTextView.getText().toString());
        int secondTeamResultScore = Integer.parseInt(secondTeamScoreTextView.getText().toString());

        // pass the resulting scores to match summary activity
        matchSummaryIntent.putExtra(FIRST_TEAM_RESULT, firstTeamResultScore);
        matchSummaryIntent.putExtra(SECOND_TEAM_RESULT, secondTeamResultScore);
        startActivity(matchSummaryIntent);

        finish();
    }
}

package com.ni3bobade.khelkabaddi;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;

public class MatchSummaryActivity extends AppCompatActivity {

    TextView congratulationsTextView;
    TextView firstTeamResultScoreTextView;
    TextView secondTeamResultScoreTextView;

    ConstraintLayout matchSummaryConstraintLayout;
    ImageView matchWinnerImageView;
    Button replayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_summary);

        // required for snackbar
        matchSummaryConstraintLayout = findViewById(R.id.match_summary_constraintLayout);

        congratulationsTextView = findViewById(R.id.congratulations_textView);
        firstTeamResultScoreTextView = findViewById(R.id.first_team_result_score_textView);
        secondTeamResultScoreTextView = findViewById(R.id.second_team_result_score_textView);
        matchWinnerImageView = findViewById(R.id.match_winner_imageView);
        replayButton = findViewById(R.id.replay_button);

        // get the match results
        Intent matchSummaryIntent = getIntent();
        int firstTeamResultScore = matchSummaryIntent.getIntExtra(MainActivity.FIRST_TEAM_RESULT, 0);
        int secondTeamResultScore = matchSummaryIntent.getIntExtra(MainActivity.SECOND_TEAM_RESULT, 0);

        firstTeamResultScoreTextView.setText(MessageFormat.format("{0}", firstTeamResultScore));
        secondTeamResultScoreTextView.setText(MessageFormat.format("{0}", secondTeamResultScore));

        // greet the winner
        if (firstTeamResultScore > secondTeamResultScore) {
            congratulationsTextView.setText(R.string.congrats_mumba);
            matchWinnerImageView.setImageResource(R.drawable.u_mumba_logo_fill);
            Snackbar.make(matchSummaryConstraintLayout, R.string.mumba_win, Snackbar.LENGTH_INDEFINITE).show();
        } else if (firstTeamResultScore < secondTeamResultScore) {
            congratulationsTextView.setText(R.string.congrats_paltan);
            matchWinnerImageView.setImageResource(R.drawable.puneri_paltan_logo_fill);
            Snackbar.make(matchSummaryConstraintLayout, R.string.puneri_paltan_win, Snackbar.LENGTH_INDEFINITE).show();
        } else if (firstTeamResultScore == secondTeamResultScore) {
            congratulationsTextView.setText(R.string.match_tie);
            matchWinnerImageView.setImageResource(R.drawable.match_tie_logo_fill);
            Snackbar.make(matchSummaryConstraintLayout, R.string.match_tied, Snackbar.LENGTH_INDEFINITE).show();
        }

        // replay the match
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replayMatchIntent = new Intent(MatchSummaryActivity.this, SplashActivity.class);
                startActivity(replayMatchIntent);
                finish();
            }
        });
    }
}

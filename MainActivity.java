package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //Initialize variable
    TextView playerPosition, playerDuration;
    SeekBar seekBar;
    ImageView btnRew, btnPlay, btnPause, btnFf;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variable
        playerPosition = findViewById(R.id.player_position);
        playerDuration = findViewById(R.id.player_duration);
        seekBar = findViewById(R.id.seek_bar);
        btnRew = findViewById(R.id.btn_rew);
        btnPlay = findViewById(R.id.btn_play);
        btnPause = findViewById(R.id.btn_pause);
        btnFf = findViewById(R.id.btn_ff);

        //Initialize media player
        mediaPlayer = MediaPlayer.create(this, R.raw.music);

        //Initialize runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                //Set progress on seek bar
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                //Handler post delay for 0.5 second
                handler.postDelayed(this, 500);
            }
        };

        //Get duration of medial player
        int duration = mediaPlayer.getDuration();

        //Convert millisecond to minute and second
        String sDuration = convertFormat(duration);

        //Set duration on text view
        playerDuration.setText(sDuration);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide play button
                btnPlay.setVisibility(View.GONE);

                //Show pause button
                btnPause.setVisibility(View.VISIBLE);

                //Start media player
                mediaPlayer.start();

                //Set max on seek bar
                seekBar.setMax(mediaPlayer.getDuration());

                //Start handler
                handler.postDelayed(runnable, 0);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide play button
                btnPause.setVisibility(View.GONE);

                //Show pause button
                btnPlay.setVisibility(View.VISIBLE);

                //Pause medial player
                mediaPlayer.pause();

                //Stop handler
                handler.removeCallbacks(runnable);
            }
        });

        btnFf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current position of media player
                int currentPosition = mediaPlayer.getCurrentPosition();

                //Get duration of media player
                int duration = mediaPlayer.getDuration();

                //Check condition
                if(mediaPlayer.isPlaying() && duration != currentPosition) {
                    //When music is playing and duration is not equal to current position
                    //Fast forward for 5 seconds
                    currentPosition = currentPosition + 5000;

                    //Set current position on text view
                    playerPosition.setText(convertFormat(currentPosition));

                    //Set progress on seek bar
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });

        btnRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current position
                int currentPosition = mediaPlayer.getCurrentPosition();

                //Check condition
                if(mediaPlayer.isPlaying()) { //&& currentPosition > 50000
                    //When music is playing and current position is greater than 5 seconds
                    //Rewind for 5 seconds
                    currentPosition = currentPosition - 5000;

                    //Set current position on text view
                    playerPosition.setText(convertFormat(currentPosition));

                    //Set progress on seek bar
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Check condition
                if(fromUser) {
                    //When drag the seek bar
                    //Set progress on seek bar
                    mediaPlayer.seekTo(progress);
                }
                //Set current position on text view
                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Hide pause button
                btnPause.setVisibility(View.GONE);

                //Show play button
                btnPlay.setVisibility(View.VISIBLE);

                //Set media player to initial posiyion
                mediaPlayer.seekTo(0);
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d"
                , TimeUnit.MILLISECONDS.toMinutes(duration)
                , TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}
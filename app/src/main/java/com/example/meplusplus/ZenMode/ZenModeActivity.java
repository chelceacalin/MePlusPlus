package com.example.meplusplus.ZenMode;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.asp.fliptimerviewlibrary.CountDownClock;
import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;

import io.github.muddz.styleabletoast.StyleableToast;

public class ZenModeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public int COUNTDOWNTIME = 0;
    //Controale
    CountDownClock countDownClock;
    Button activity_zen_mode_5minutes, activity_zen_mode_10minutes, activity_zen_mode_15minutes;
    Button activity_zen_mode_add_1_minute, activity_zen_mode_remove_1_minute;
    ImageView activity_zen_mode_close;
    //Music
    MediaPlayer mediaPlayer;

    Spinner activity_zen_mode_spinner;
    ImageView activity_zen_mode_clock_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zen_mode);
        init();
        initSpinner();

        activity_zen_mode_15minutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                COUNTDOWNTIME = 15000;
                countDownClock.startCountDown(COUNTDOWNTIME * 60);
                countDownClock.setCountdownListener(new CountDownClock.CountdownCallBack() {
                    @Override
                    public void countdownAboutToFinish() {
                    }

                    @Override
                    public void countdownFinished() {
                        new StyleableToast
                                .Builder(ZenModeActivity.this)
                                .text("Good Job!")
                                .textColor(Color.BLACK)
                                .backgroundColor(Color.GREEN)
                                .cornerRadius(25)
                                .iconStart(R.drawable.ic_baseline_check_circle_24)
                                .show();
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT, 650);
                    }
                });
            }
        });

        activity_zen_mode_10minutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                COUNTDOWNTIME = 10000;
                countDownClock.startCountDown(COUNTDOWNTIME * 60);
                countDownClock.setCountdownListener(new CountDownClock.CountdownCallBack() {
                    @Override
                    public void countdownAboutToFinish() {
                    }

                    @Override
                    public void countdownFinished() {
                        new StyleableToast
                                .Builder(ZenModeActivity.this)
                                .text("Good Job!")
                                .textColor(Color.BLACK)
                                .backgroundColor(Color.GREEN)
                                .cornerRadius(25)
                                .iconStart(R.drawable.ic_baseline_check_circle_24)
                                .show();
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
//SUNET CAND SE OPRESTE TIMERUL
                        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT, 650);
                    }
                });
            }
        });


        activity_zen_mode_5minutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                COUNTDOWNTIME = 5000;
                countDownClock.startCountDown(COUNTDOWNTIME * 60);
                countDownClock.setCountdownListener(new CountDownClock.CountdownCallBack() {
                    @Override
                    public void countdownAboutToFinish() {
                    }

                    @Override
                    public void countdownFinished() {
                        new StyleableToast
                                .Builder(ZenModeActivity.this)
                                .text("Good Job!")
                                .textColor(Color.BLACK)
                                .backgroundColor(Color.GREEN)
                                .cornerRadius(25)
                                .iconStart(R.drawable.ic_baseline_check_circle_24)
                                .show();
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT, 650);
                    }
                });
            }
        });


        activity_zen_mode_add_1_minute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                COUNTDOWNTIME += 1000;
                countDownClock.startCountDown(COUNTDOWNTIME * 60L);


                countDownClock.setCountdownListener(new CountDownClock.CountdownCallBack() {
                    @Override
                    public void countdownAboutToFinish() {
                    }

                    @Override
                    public void countdownFinished() {
                        new StyleableToast
                                .Builder(ZenModeActivity.this)
                                .text("Good Job!")
                                .textColor(Color.BLACK)
                                .backgroundColor(Color.GREEN)
                                .cornerRadius(25)
                                .iconStart(R.drawable.ic_baseline_check_circle_24)
                                .show();
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }

                        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT, 650);
                    }
                });

            }
        });


        activity_zen_mode_close.setOnClickListener(view1 -> {
            startActivity(new Intent(ZenModeActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
            finish();
        });

        activity_zen_mode_remove_1_minute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (COUNTDOWNTIME > 1000) {
                    COUNTDOWNTIME -= 1000;
                    countDownClock.startCountDown(COUNTDOWNTIME * 60L);
                    countDownClock.setCountdownListener(new CountDownClock.CountdownCallBack() {
                        @Override
                        public void countdownAboutToFinish() {
                        }

                        @Override
                        public void countdownFinished() {
                            new StyleableToast
                                    .Builder(ZenModeActivity.this)
                                    .text("Good Job!")
                                    .textColor(Color.BLACK)
                                    .backgroundColor(Color.GREEN)
                                    .cornerRadius(25)
                                    .iconStart(R.drawable.ic_baseline_check_circle_24)
                                    .show();
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                                mediaPlayer = null;

                                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT, 650);
                            }
                        }
                    });
                } else {

                    new StyleableToast
                            .Builder(ZenModeActivity.this)
                            .text("Good Job!")
                            .textColor(Color.BLACK)
                            .backgroundColor(Color.GREEN)
                            .cornerRadius(25)
                            .iconStart(R.drawable.ic_baseline_check_circle_24)
                            .show();


                    countDownClock.resetCountdownTimer();
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                        mediaPlayer = null;

                    }
                    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT, 650);
                }

            }
        });

        activity_zen_mode_clock_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                countDownClock.resetCountdownTimer();
                startActivity(new Intent(ZenModeActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
                finish();
            }
        });
    }

    //Controale
    private void init() {
        countDownClock = findViewById(R.id.timerProgramCountdown);
        activity_zen_mode_5minutes = findViewById(R.id.activity_zen_mode_5minutes);
        activity_zen_mode_10minutes = findViewById(R.id.activity_zen_mode_10minutes);
        activity_zen_mode_15minutes = findViewById(R.id.activity_zen_mode_15minutes);
        activity_zen_mode_add_1_minute = findViewById(R.id.activity_zen_mode_add_1_minute);
        activity_zen_mode_remove_1_minute = findViewById(R.id.activity_zen_mode_remove_1_minute);
        activity_zen_mode_spinner = findViewById(R.id.activity_zen_mode_spinner);
        activity_zen_mode_clock_stop = findViewById(R.id.activity_zen_mode_clock_stop);
        activity_zen_mode_close = findViewById(R.id.activity_zen_mode_close);
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ZenModeActivity.this, R.array.musicplayer, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity_zen_mode_spinner.setAdapter(adapter);
        activity_zen_mode_spinner.setOnItemSelectedListener(ZenModeActivity.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        switch (text) {
            case "Play Music":
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(ZenModeActivity.this, R.raw.music);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                            }
                        }
                    });
                }
                mediaPlayer.start();
                break;

            case "Pause Music":
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                }
                break;


            case "Stop Music":
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ZenModeActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
        finish();
        super.onBackPressed();
    }
}
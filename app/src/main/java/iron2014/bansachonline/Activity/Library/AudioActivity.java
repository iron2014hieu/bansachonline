package iron2014.bansachonline.Activity.Library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;

import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;

public class AudioActivity extends AppCompatActivity {

    private View parent_view;
    private AppCompatSeekBar seek_song_progressbar;
    private FloatingActionButton btn_play;
    private TextView tv_song_current_duration, tv_song_total_duration,txtTensach,txtTacgia;
    private CircularImageView image;

    private MediaPlayer mediaPlayer;
    private Handler mHandler = new Handler();

    private MusicUtils utils;
    SessionManager sessionManager;

    String audioUrl,tensach,tentacgia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        addControls();
        sessionManager = new SessionManager(this);
        HashMap<String,String> book = sessionManager.getLinkbook();
        audioUrl= book.get(sessionManager.AUDIO);
        tensach = book.get(sessionManager.TENSACH);
        tentacgia = book.get(sessionManager.TACGIA);

        txtTacgia.setText(tentacgia);
        txtTensach.setText(tensach);
        try {
            if (audioUrl!=null) {
                setMusicPlayerComponents();
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }catch (Exception e){
            Log.e("AUDIO ERR", e.toString());
        }

    }
    private void setMusicPlayerComponents() {


        seek_song_progressbar.setProgress(0);
        seek_song_progressbar.setMax(MusicUtils.MAX_PROGRESS);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btn_play.setImageResource(R.drawable.ic_play_arrow);
            }
        });

        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            AssetFileDescriptor afd = getAssets().openFd("sai.mp3");
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
        } catch (Exception e) {
            Snackbar.make(parent_view, "Cannot load audio file", Snackbar.LENGTH_SHORT).show();
        }

        utils = new MusicUtils();
        seek_song_progressbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
                mediaPlayer.seekTo(currentPosition);
                mHandler.post(mUpdateTimeTask);
            }
        });
        buttonPlayerAction();
        updateTimerAndSeekbar();
    }


    private void buttonPlayerAction() {
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btn_play.setImageResource(R.drawable.ic_play_arrow);
                } else {
                    mediaPlayer.start();
                    btn_play.setImageResource(R.drawable.ic_pause);
                    mHandler.post(mUpdateTimeTask);
                }
                rotateTheDisk();
            }
        });
    }

    public void controlClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_repeat: {
                toggleButtonColor((ImageButton) v);
                Snackbar.make(parent_view, "Repeat", Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.btn_shuffle: {
                toggleButtonColor((ImageButton) v);
                Snackbar.make(parent_view, "Shuffle", Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.btn_prev: {
                toggleButtonColor((ImageButton) v);
                Snackbar.make(parent_view, "Previous", Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.btn_next: {
                toggleButtonColor((ImageButton) v);
                Snackbar.make(parent_view, "Next", Snackbar.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private boolean toggleButtonColor(ImageButton bt) {
        String selected = (String) bt.getTag(bt.getId());
        if (selected != null) { // selected
            bt.setColorFilter(getResources().getColor(R.color.colorDarkOrange), PorterDuff.Mode.SRC_ATOP);
            bt.setTag(bt.getId(), null);
            return false;
        } else {
            bt.setTag(bt.getId(), "selected");
            bt.setColorFilter(getResources().getColor(R.color.colorYellow), PorterDuff.Mode.SRC_ATOP);
            return true;
        }
    }


    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            updateTimerAndSeekbar();
            if (mediaPlayer.isPlaying()) {
                mHandler.postDelayed(this, 100);
            }
        }
    };

    private void updateTimerAndSeekbar() {
        long totalDuration = mediaPlayer.getDuration();
        long currentDuration = mediaPlayer.getCurrentPosition();

        tv_song_total_duration.setText(utils.milliSecondsToTimer(totalDuration));
        tv_song_current_duration.setText(utils.milliSecondsToTimer(currentDuration));

        int progress = (int) (utils.getProgressSeekBar(currentDuration, totalDuration));
        seek_song_progressbar.setProgress(progress);
    }

    private void rotateTheDisk() {
        try {
            if (!mediaPlayer.isPlaying())
                image.animate().setDuration(100).rotation(image.getRotation() + 2f).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rotateTheDisk();
                        super.onAnimationEnd(animation);
                    }
                });
            return;
        }catch (Exception e){

        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
        mediaPlayer.release();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Snackbar.make(parent_view, item.getTitle(), Snackbar.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private void addControls() {
        txtTensach = findViewById(R.id.txtTensach);
        txtTacgia = findViewById(R.id.txtTacgia);

        parent_view = findViewById(R.id.parent_view);
        seek_song_progressbar = findViewById(R.id.seek_song_progressbar);
        btn_play = findViewById(R.id.btn_play);

        tv_song_current_duration =  findViewById(R.id.tv_song_current_duration);
        tv_song_total_duration = findViewById(R.id.total_duration);
        image =  findViewById(R.id.image);
    }

}


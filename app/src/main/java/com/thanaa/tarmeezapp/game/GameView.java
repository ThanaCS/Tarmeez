package com.thanaa.tarmeezapp.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.thanaa.tarmeezapp.EndGameFragment;
import com.thanaa.tarmeezapp.MainActivity;
import com.thanaa.tarmeezapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, score = 0;
    private MediaPlayer mediaPlayer;
    public static float screenRatioX, screenRatioY;
    private Background background1, background2;
    private Flight flight;
    private Random random;
    private List<Bullet> bullets;
    private Bird[] birds;
    private SoundPool soundPool;
    private GameFragment fragment;
    private int sound;
    private Paint paint;

    public GameView(GameFragment fragment, int screenX, int screenY) {
        super(fragment.getContext());
        this.fragment = fragment;
        this.screenX = screenX;
        this.screenY = screenY;
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = size.x/ screenX;
        screenRatioY = size.y / screenY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(fragment.getContext(), R.raw.shoot, 1);

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        flight = new Flight(this, screenY, getResources());

        bullets = new ArrayList<>();

        background2.x = screenX;

        paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        birds = new Bird[2];

        for (int i = 0;i < 2;i++) {

            Bird bird = new Bird(getResources());
            birds[i] = bird;

        }

        random = new Random();

    }


    @Override
    public void run() {
        while (isPlaying) {

            update();
            draw();
            sleep();



        }

    }

    private void update () {

        background1.x -= 10 * screenRatioX;
        background2.x -= 10 * screenRatioX;

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }

        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }

        if (flight.isGoingUp)
            flight.y -= 30 * screenRatioY;
        else
            flight.y += 30 * screenRatioY;

        if (flight.y < 0)
            flight.y = 0;

        if (flight.y >= screenY - flight.height)
            flight.y = screenY - flight.height;

        List<Bullet> trash = new ArrayList<>();

        for (Bullet bullet : bullets) {

            if (bullet.x > screenX)
                trash.add(bullet);

            bullet.x += 50 * screenRatioX;


            for (Bird bird : birds) {

                if (Rect.intersects(bird.getCollisionShape(),
                        bullet.getCollisionShape())) {

                    score++;
                    bird.x = -500;
                    bullet.x = screenX + 500;
                    bird.wasShot = true;

                }

            }



        }

        for (Bullet bullet : trash)
            bullets.remove(bullet);

        for (Bird bird : birds) {

            bird.x -= bird.speed;

            if (bird.x + bird.width < 0) {

                if (!bird.wasShot) {
                    isGameOver = true;

                    return;
                }

                int bound = (int) (15 * screenRatioX);
                bird.speed = random.nextInt(bound);

                if (bird.speed < 10 * screenRatioX)
                    bird.speed = (int) (10 * screenRatioX);

                bird.x = screenX;
                bird.y = random.nextInt(1000);

            }

            if (Rect.intersects(bird.getCollisionShape(), flight.getCollisionShape())) {

                isGameOver = true;
                return;
            }
        }

        }

    private void sleep() {


    }

    void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    private void draw () {

        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);



            canvas.drawText(score + "", screenX / 2f, 164, paint);

            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                canvas.drawText("انتهت اللعبة", (canvas.getWidth()/2)-200, canvas.getHeight()-200, paint);
                getHolder().unlockCanvasAndPost(canvas);
                waitBeforeExiting();
                return;
            }
            for (Bird bird : birds)
                canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            for (Bullet bullet : bullets)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

            getHolder().unlockCanvasAndPost(canvas);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2) {
                    flight.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingUp = false;
                if (event.getX() > screenX / 2)
                    flight.toShoot++;
                break;
        }

        return true;

    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void newBullet() {
        soundPool.play(sound, 1, 1, 0, 0, 1);
        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + (flight.height / 2);
        bullets.add(bullet);
    }

    private void waitBeforeExiting() {
        try {
        Thread.sleep(2500);
            NavController navController = Navigation.findNavController(fragment.getActivity(), R.id.fragment_container);
            navController.navigate(R.id.endGameFraagment);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

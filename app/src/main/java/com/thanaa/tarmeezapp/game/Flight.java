package com.thanaa.tarmeezapp.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.thanaa.tarmeezapp.R;

import static com.thanaa.tarmeezapp.game.GameView.screenRatioX;
import static com.thanaa.tarmeezapp.game.GameView.screenRatioY;

public class Flight {
    int toShoot = 0;
    boolean isGoingUp = false;
    int x, y, width, height, wingCounter = 0, shootCounter = 1;
    Bitmap flight1, flight2, shoot1, shoot2, shoot3, shoot4, shoot5, dead, restart,next;
    private GameView gameView;

    Flight (GameView gameView, int screenY, Resources res) {


        this.gameView = gameView;

        flight1 = BitmapFactory.decodeResource(res, R.drawable.astrountcat);
        flight2 = BitmapFactory.decodeResource(res, R.drawable.astrountcat);

        width = flight1.getWidth();
        height = flight1.getHeight();

        width /= 8;
        height /= 8;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);
        shoot1 = BitmapFactory.decodeResource(res, R.drawable.astrountcat);
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.astrountcat);
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.astrountcat);
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.astrountcat);
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.astrountcat);

        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false);
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false);
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false);
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false);
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false);

        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false);
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false);

        dead = BitmapFactory.decodeResource(res, R.drawable.dead);
        dead = Bitmap.createScaledBitmap(dead, width, height, false);

        restart = BitmapFactory.decodeResource(res, R.drawable.restart);
        restart = Bitmap.createScaledBitmap(restart, width, height, false);

        next = BitmapFactory.decodeResource(res, R.drawable.next);
        next = Bitmap.createScaledBitmap(next, width, height, false);

        y = screenY / 2;
        x = (int) (64 * screenRatioX);

    }
    Bitmap getFlight () {

        if (toShoot != 0) {

            if (shootCounter == 1) {
                shootCounter++;
                return shoot1;
            }

            if (shootCounter == 2) {
                shootCounter++;
                return shoot2;
            }

            if (shootCounter == 3) {
                shootCounter++;
                return shoot3;
            }

            if (shootCounter == 4) {
                shootCounter++;
                return shoot4;
            }

            shootCounter = 1;
            toShoot--;
            gameView.newBullet();
            return shoot5;
        }

        if (wingCounter == 0) {
            wingCounter++;
            return flight1;
        }
        wingCounter--;

        return flight2;
    }
    Rect getCollisionShape () {

        return new Rect(x, y, x + width, y + height);
    }

    Bitmap getDead () {

        return dead;
    }

    Bitmap getRestart () {

        return restart;
    }
    Bitmap getNext () {

        return next;
    }

}

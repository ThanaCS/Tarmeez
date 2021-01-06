package com.thanaa.tarmeezapp.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.thanaa.tarmeezapp.R;

import static com.thanaa.tarmeezapp.game.GameView.screenRatioX;
import static com.thanaa.tarmeezapp.game.GameView.screenRatioY;

public class Flight {
    int toShoot = 0;
    boolean isGoingUp = false;
    int x, y, width, height, wingCounter = 0, shootCounter = 1;
    Bitmap flight1, flight2, shoot1, shoot2, shoot3, shoot4, shoot5, dead;
    private GameView gameView;

    Flight (int screenY, Resources res) {

        this.gameView = gameView;

        flight1 = BitmapFactory.decodeResource(res, R.drawable.rocket);
        flight2 = BitmapFactory.decodeResource(res, R.drawable.rocket);

        width = flight1.getWidth();
        height = flight1.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false);
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false);
        y = screenY / 2;
        x = (int) (64 * screenRatioX);

    }
    Bitmap getFlight () {

        if(wingCounter == 0){
            wingCounter++;
            return flight1;
        }
        wingCounter--;

            return flight2;


    }


}

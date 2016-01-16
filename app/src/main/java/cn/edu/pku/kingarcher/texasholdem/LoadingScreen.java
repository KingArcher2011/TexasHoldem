package cn.edu.pku.kingarcher.texasholdem;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;

import framework.Game;
import framework.Graphics;
import framework.Graphics.PixmapFormat;
import framework.Screen;

/**
 * Created by xtrao on 2016/1/1.
 */
public class LoadingScreen extends Screen {

    private Game mGame;

    public LoadingScreen(Game game) {
        super(game);
        mGame = game;
    }

    @Override
    public void update(float deltaTime) {
        Assets.sTable = mGame.getGraphics().newPixmap(R.raw.table, PixmapFormat.ARGB8888);
        Assets.sCards = mGame.getGraphics().newPixmap(R.raw.cards, PixmapFormat.ARGB8888);
        Assets.sIcon1 = mGame.getGraphics().newPixmap(R.raw.icon1, PixmapFormat.ARGB8888);
        Assets.sIcon2 = mGame.getGraphics().newPixmap(R.raw.icon2, PixmapFormat.ARGB8888);
        Assets.sIcon3 = mGame.getGraphics().newPixmap(R.raw.icon3, PixmapFormat.ARGB8888);
        Assets.sIcon4 = mGame.getGraphics().newPixmap(R.raw.icon4, PixmapFormat.ARGB8888);
        Assets.sIcon5 = mGame.getGraphics().newPixmap(R.raw.icon5, PixmapFormat.ARGB8888);
        Assets.sIcon6 = mGame.getGraphics().newPixmap(R.raw.icon6, PixmapFormat.ARGB8888);
        mGame.setScreen(new TexasHoldemSreen(mGame));
    }

    @Override
    public void present(float deltaTime) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void dispose() {

    }
}

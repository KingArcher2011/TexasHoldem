package cn.edu.pku.kingarcher.texasholdem;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import framework.Game;
import framework.Graphics;
import framework.Input.TouchEvent;
import framework.Pixmap;
import framework.Screen;
import cn.edu.pku.kingarcher.texasholdem.Poker.Card;
import cn.edu.pku.kingarcher.texasholdem.Figure.FigureState;
import cn.edu.pku.kingarcher.texasholdem.PokerTable.PokerState;

/**
 * Created by xtrao on 2016/1/1.
 */
public class TexasHoldemSreen extends Screen {

    Game mGame;
    PokerTable mPokerTable;
    List<Point> mPoints;
    List<Pixmap> iconList;


    public TexasHoldemSreen(Game game) {
        super(game);
        mGame = game;
        mPokerTable = new PokerTable(200);
        mPoints = new ArrayList<>();
        mPoints.add(new Point(470, 350));
        mPoints.add(new Point(270, 350));
        mPoints.add(new Point(150, 220));
        mPoints.add(new Point(270, 100));
        mPoints.add(new Point(670, 100));
        mPoints.add(new Point(790, 220));
        mPoints.add(new Point(680, 350));
        iconList = new ArrayList<>();
        iconList.add(Assets.sIcon1);
        iconList.add(Assets.sIcon2);
        iconList.add(Assets.sIcon3);
        iconList.add(Assets.sIcon4);
        iconList.add(Assets.sIcon5);
        iconList.add(Assets.sIcon6);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> lists = game.getInput().getTouchEvents();
        mPokerTable.update(deltaTime, lists);
    }

    @Override
    public void present(float deltaTime) {
        drawBackground();
        drawComponents();
        drawCards();
        drawActionButton();
        /*switch(mPokerTable.mState) {
            case Idle:
                drawIdleUI();
                break;
            case HoleCards:
                drawHoleCardsUI();
                break;
        }*/
    }

    private void drawActionButton() {
        switch (mPokerTable.mState) {
            case Idle:
                drawIdleButton();
                break;
            case PreFlop:
            case FlopRound:
            case TurnRound:
            case RiverRound:
                int index = mPokerTable.mButton + mPokerTable.mLocation;
                Figure currentFigure = mPokerTable.mFigureList.get(index % 7);
                if(!currentFigure.mIsAI && (currentFigure.mState != FigureState.Fold))
                    drawRoundButton(currentFigure);
                //else
                    //drawNormalButton();
                break;
            default:
                //drawNormalButton();
                break;
        }
    }

    private void drawRoundButton(Figure currentFigure) {
        Graphics g = mGame.getGraphics();
        g.drawRoundRect(440, 480, 140, 60, 10, 10, 0xAFFF0000);
        g.drawText("Fold", 480, 515, 0xFFFFFFFF, 20);
        g.drawRoundRect(620, 480, 140, 60, 10, 10, 0xAF00FF00);
        int minMoney = mPokerTable.mUpperMonney - currentFigure.mBet;
        if (minMoney == 0)
            g.drawText("Check", 670, 515, 0xFFFFFFFF, 16);
        else
            g.drawText("Follow " + ((Integer)minMoney).toString(), 660, 515, 0xFFFFFFFF, 16);
        g.drawRoundRect(800, 480, 140, 60, 10, 10, 0xAF00FFFF);
        if(currentFigure.isRaising) {
            g.drawText(((Integer)currentFigure.tempBet).toString(), 850, 515, 0xFFFFFFFF, 16);
            g.drawCycle(870, 350, 30, 0xAF00FFFF);
            g.drawText("+", 855, 355, 0xFFFFFFFF, 20);
            g.drawCycle(870, 430, 30, 0xAF00FFFF);
            g.drawText("-", 855, 435, 0xFFFFFFFF, 20);
        } else
            g.drawText("Raise", 850, 515, 0xFFFFFFFF, 16);

    }

    private void drawCards() {
        for(int i = 0; i < mPokerTable.mCommunityCards.size(); ++i) {
            drawCard(mPokerTable.mCommunityCards.get(i), 340 + i * 70, 220);
        }
        if(mPokerTable.mState != PokerTable.PokerState.Check) {
            Card cardOne = mPokerTable.mPlayer.mCardOne;
            Card cardTwo = mPokerTable.mPlayer.mCardTwo;
            drawCard(cardOne, 570, 350);
            drawCard(cardTwo, 600, 350);
        } else {
            for(int i = 0; i < 7; ++i) {
                Card cardOne = mPokerTable.mFigureList.get(i).mCardOne;
                Card cardTwo = mPokerTable.mFigureList.get(i).mCardTwo;
                drawCard(cardOne, mPoints.get(i).x, mPoints.get(i).y);
                drawCard(cardTwo, mPoints.get(i).x + 30, mPoints.get(i).y);
            }
        }
    }

    private void drawBackground() {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.sTable, 0, 0);
        for(int i = 0; i < 7; ++i) {
            drawBackgroundFigure(i);
        }
    }

    private void drawComponents() {
        Graphics g = game.getGraphics();
        g.drawText("Pot: " + ((Integer) mPokerTable.mPotMoney).toString(), 500, 200, 0x8FFFFFFF, 16);
        if(mPokerTable.mButton < 2)
            drawButton(60, -45);
        else if(mPokerTable.mButton == 2)
            drawButton(90, 0);
        else if(mPokerTable.mButton == 3)
            drawButton(60, 115);
        else if(mPokerTable.mButton == 4)
            drawButton(0, 115);
        else if(mPokerTable.mButton == 5)
            drawButton(-30, 0);
        else
            drawButton(0, -45);
        for(int i = 0; i < 7; ++i) {
            drawAccount(i);
            drawState(i);
        }
    }

    private void drawState(int i) {
        Graphics g = mGame.getGraphics();
        Figure.FigureState state = mPokerTable.mFigureList.get(i).mState;
        String string = null;
        int color = 0;
        switch (state) {
            case Fold:
                string = "Fold";
                color = 0x4FFFFFFF;
                break;
            case Check:
                string = "Check";
                color = 0xFF00FF00;
                break;
            case Call:
                string = "Call";
                color = 0xFFFFFF00;
                break;
            case Raise:
                string = "Raise";
                color = 0xFFFF0000;
                break;
            case Judge:
                string = "Thinking";
                color = 0xFFFFFFFF;
                break;
            default:
                break;
        }
        if((string != null) && (mPokerTable.mState != PokerState.Check)) {
            g.drawText(string, mPoints.get(i).x + 10, mPoints.get(i).y - 5, color, 16);
        } else if (mPokerTable.mState == PokerState.Check) {
            if(mPokerTable.winnerList.contains(i)) {
                g.drawText("Win", mPoints.get(i).x + 30, mPoints.get(i).y - 5, 0xFFFF8F00, 16);
            } else {
                g.drawText("Lose", mPoints.get(i).x + 25, mPoints.get(i).y - 5, 0xFFFFFFFF, 16);
            }
        }
    }

    private void drawAccount(int i) {
        Graphics g = mGame.getGraphics();
        Integer account = mPokerTable.mFigureList.get(i).mCount;
        g.drawText(account.toString(), mPoints.get(i).x + 15, mPoints.get(i).y + 100, 0xFFFFFFFF, 16);
    }

    private void drawButton(int x, int y) {
        Graphics g = mGame.getGraphics();
        int cx = mPoints.get(mPokerTable.mButton).x + x + 10;
        int cy = mPoints.get(mPokerTable.mButton).y + y + 10;
        g.drawCycle(cx, cy, 10, 0xFFFFFFFF);
        g.drawText("D", cx - 5, cy + 5, 0xFF000000, 16);
    }

    private void drawIdleButton() {
        Graphics g = game.getGraphics();
        g.drawRoundRect(400, 220, 200, 60, 20, 20, 0xFF00FF00);
        g.drawText("Go!", 460, 260, 0xFFFFFFFF, 40);
    }

    /*private void drawHoleCardsUI() {
        Card cardOne = mPokerTable.mPlayer.mCardOne;
        Card cardTwo = mPokerTable.mPlayer.mCardTwo;
        drawCard(cardOne, 570, 350);
        drawCard(cardTwo, 600, 350);
    }*/

    private void drawCard(Card card, int x, int y) {
        Graphics g = game.getGraphics();
        if(card != null) {
            int i = getCardIndex(card);
            g.drawPixmap(Assets.sCards, x, y, (i % 11) * 50, (i / 11) * 80, 50, 80);
        }
    }

    private int getCardIndex(Card card) {
        int index = card.mSuit.ordinal() * 13 + 1 + card.mNumber;
        if(card.mNumber == 14)
            index -= 13;
        return index;
    }

    private void drawBackgroundFigure(int i) {
        int icon = mPokerTable.mFigureList.get(i).mIcon;
        Graphics g = game.getGraphics();
        int x = mPoints.get(i).x;
        int y = mPoints.get(i).y;
        g.drawRoundRect(x, y - 25, 80, 130, 5, 5, 0x8F000000);
        g.drawPixmap(iconList.get(icon), x, y);
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

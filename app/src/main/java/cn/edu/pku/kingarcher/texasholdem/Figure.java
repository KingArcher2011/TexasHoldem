package cn.edu.pku.kingarcher.texasholdem;

import java.util.List;

import cn.edu.pku.kingarcher.texasholdem.Poker.Card;

/**
 * Created by xtrao on 2016/1/1.
 */
public class Figure {

    enum FigureState {
        Idle,
        Judge,
        Check,
        Call,
        Raise,
        Fold
    }

    public int mCount;
    public int mIcon;
    public int ID;
    public boolean mIsAI;
    public int mBet;

    public int tempBet;
    public int lowerBet;
    public int upperBet;
    public boolean isRaising;

    public FigureState mState;

    Card mCardOne = null;
    Card mCardTwo = null;

    public Figure(int id, int count, boolean isAI) {
        ID = id;
        mIsAI = isAI;
        mCount = count;
        mBet = 0;
        mState = FigureState.Idle;
        tempBet = 0;
        lowerBet = 0;
        upperBet = 0;
        isRaising = false;
        if(!mIsAI)
            mIcon = 0;
        else
            mIcon = (int)(Math.random() * 5) + 1;
    }

    public void getFirstCards(Card card) {
        mCardOne = card;
    }

    public void getSecondCards(Card card) {
        mCardTwo = card;
    }

    //AI algorithm need implemented
    public int judgement(List<Card> list, int minMoney) {
        throw new UnsupportedOperationException();
    }

    //Simple AI
    public int simpleJudgement(int minMoney) {
        if(minMoney == 0)
            mState = FigureState.Check;
        else
            mState = FigureState.Call;
        return addBet(minMoney);
    }

    public int addBet(int bet) {
        mBet += bet;
        mCount -= bet;
        return bet;
    }

    public void playerFold() {
        mState = FigureState.Fold;
    }

    public int checkOrCall(int minMoney) {
        if(minMoney == 0)
            mState = FigureState.Check;
        else
            mState = FigureState.Call;
        return addBet(minMoney);
    }

    public void changeToRaise(int minMoney) {
        tempBet = minMoney;
        lowerBet = minMoney;
        upperBet = mCount;
        isRaising = true;
    }

    public void adjustBet(boolean add) {
        if(add && (tempBet < (upperBet - 100))) {
            tempBet += 100;
        } else if(!add && (tempBet > (lowerBet + 100))) {
            tempBet -= 100;
        } else if(add) {
            tempBet = upperBet;
        } else {
            tempBet = lowerBet;
        }
    }

    public int playerRaise() {
        isRaising = false;
        if(tempBet > lowerBet)
            mState = FigureState.Raise;
        else if(tempBet == 0)
            mState = FigureState.Check;
        else {
            mState = FigureState.Call;
        }
        return addBet(tempBet);
    }

    public void reset() {
        mState = FigureState.Idle;
        mBet = 0;
        tempBet = 0;
        upperBet = 0;
        lowerBet = 0;
        mCardOne = null;
        mCardTwo = null;
    }

}

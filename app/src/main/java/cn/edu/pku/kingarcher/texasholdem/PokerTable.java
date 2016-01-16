package cn.edu.pku.kingarcher.texasholdem;

import java.util.ArrayList;
import java.util.List;
import cn.edu.pku.kingarcher.texasholdem.Poker.Card;
import cn.edu.pku.kingarcher.texasholdem.Figure.FigureState;
import cn.edu.pku.kingarcher.texasholdem.Checker.PokerResult;
import framework.Input.TouchEvent;

/**
 * Created by xtrao on 2016/1/2.
 */
public class PokerTable {

    enum PokerState {
        Idle,
        HoleCards,
        PreFlop,
        Flop,
        FlopRound,
        Turn,
        TurnRound,
        River,
        RiverRound,
        Check,
    }

    Checker mChecker;
    Poker mPoker;

    //Play and AI
    Figure mPlayer;
    Figure AIOne;
    Figure AITwo;
    Figure AIThree;
    Figure AIFour;
    Figure AIFive;
    Figure AISix;
    List<Figure> mFigureList;

    int mLimit;
    int mButton;
    float mDelay;
    int mLocation;
    List<Card> mCommunityCards;
    PokerState mState;

    int mPotMoney;
    int mUpperMonney;
    int mEndPosition;

    boolean isChecked;
    List<Integer> winnerList = new ArrayList<>();

    public PokerTable(int limit) {
        mChecker = new Checker();
        mPoker = new Poker();
        mFigureList = new ArrayList<>();
        mPlayer = new Figure(0, 10000, false);
        mFigureList.add(mPlayer);
        AIOne = new Figure(1, 10000, true);
        mFigureList.add(AIOne);
        AITwo = new Figure(2, 10000, true);
        mFigureList.add(AITwo);
        AIThree = new Figure(3, 10000, true);
        mFigureList.add(AIThree);
        AIFour = new Figure(4, 10000, true);
        mFigureList.add(AIFour);
        AIFive = new Figure(5, 10000, true);
        mFigureList.add(AIFive);
        AISix = new Figure(6, 10000, true);
        mFigureList.add(AISix);
        mLimit = limit;
        mButton = 0;
        mLocation = 0;
        mDelay = 0;
        mEndPosition = 0;
        mCommunityCards = new ArrayList<>();
        mState = PokerState.Idle;
        isChecked = false;
    }

    /*public boolean gameStart() {
        if(mState != PokerState.Idle)
            return false;
        else {
            mState = PokerState.HoleCards;
            mPoker.shufflePoker();
            return true;
        }
    }*/

    public void update(float deltatime, List<TouchEvent> lists) {
        switch(mState) {
            case Idle:
                updateIdle(lists);
                break;
            case HoleCards:
                updateHoleCards(deltatime);
                //mState = PokerState.PreFlop;
                break;
            case PreFlop:
                updateRounds(deltatime, lists);
                //mState = PokerState.Flop;
                break;
            case Flop:
                updateFlop(deltatime);
                //mState = PokerState.FlopRound;
                break;
            case FlopRound:
                updateRounds(deltatime, lists);
                //mState = PokerState.Turn;
                break;
            case Turn:
                updateTurn(deltatime);
                //mState = PokerState.TurnRound;
                break;
            case TurnRound:
                updateRounds(deltatime, lists);
                //mState = PokerState.River;
                break;
            case River:
                updateRiver(deltatime);
                //mState = PokerState.RiverRound;
                break;
            case RiverRound:
                updateRounds(deltatime, lists);
                //mState = PokerState.Check;
                break;
            case Check:
                updateCheck(deltatime, lists);
                //mState = PokerState.Idle;
                break;
            default:
                break;
        }

    }

    private void updateIdle(List<TouchEvent> lists) {
        for(TouchEvent event : lists) {
            if((event.type == TouchEvent.TOUCH_DOWN)&&(event.x > 360)&&(event.x < 660)&&
                    (event.y > 200)&&(event.y < 300)) {
                mState = PokerState.HoleCards;
                mPoker.shufflePoker();
                return;
            }
        }
    }

    private void updateHoleCards(float deltatime) {
        //First Round
        mDelay += deltatime;
        if((mDelay > 100)&&(mLocation < 14)) {
            mDelay = 0;
            if(mLocation < 7)
                mFigureList.get((mLocation + mButton) % 7).getFirstCards(mPoker.getNextCard());
            else
                mFigureList.get((mLocation + mButton) % 7).getSecondCards(mPoker.getNextCard());
            ++mLocation;
            mDelay = 0;
        }
        if(mLocation == 14) {
            mPoker.getNextCard();
            mPotMoney += mFigureList.get((mButton + 1) % 7).addBet(mLimit / 2);
            mPotMoney += mFigureList.get((mButton + 2) % 7).addBet(mLimit);
            mUpperMonney = mLimit;
            mLocation = 3;
            mEndPosition = 2;
            mState = PokerState.PreFlop;
        }
    }

    private void updateRounds(float deltatime, List<TouchEvent> events) {
        Figure currentFigure;
        currentFigure = mFigureList.get((mButton + mLocation) % 7);
        if(currentFigure.mState == FigureState.Fold) {
            judgeFold();
            /*mDelay = 0;
            if(mLocation % 7 == mEndPosition) {
                changeToNextState();
                return;
            }
            ++mLocation;
            return;*/
        } else {
            currentFigure.mState = FigureState.Judge;
            mDelay += deltatime;
            if((mDelay > 1000)&&(currentFigure.mIsAI)) {
                judgeAI(currentFigure);
            }
            /*mPotMoney += currentFigure.simpleJudgement(mUpperMonney - currentFigure.mBet);
            if(mUpperMonney < currentFigure.mBet) {
                mEndPosition = mLocation % 7;
                mUpperMonney = currentFigure.mBet;
            } else if(mLocation % 7 == mEndPosition) {
                changeToNextState();
                return;
            }
            ++mLocation;
            mDelay = 0;*/
            else if((!currentFigure.mIsAI)&&(mDelay < 10000)) {
                judgePlayer(currentFigure, events);
            }
                /*int len = events.size();
                for(int i = 0; i < len; ++i) {
                    TouchEvent event = events.get(i);
                    if(event.type == TouchEvent.TOUCH_DOWN) {
                        if((event.x > 440)&&(event.x < 580)&&(event.y > 480)) {
                            currentFigure.playerFold();
                            if(mLocation % 7 == mEndPosition) {
                                changeToNextState();
                                return;
                            }
                            ++mLocation;
                            mDelay = 0;
                        } else if((event.x > 620)&&(event.x < 760)&&(event.y > 480)) {
                            mPotMoney += currentFigure.checkOrCall(mUpperMonney - currentFigure.mBet);
                            if(mLocation % 7 == mEndPosition) {
                                changeToNextState();
                                return;
                            }
                            ++mLocation;
                            mDelay = 0;
                        } else if((event.x > 800)&&(event.x < 940)&&(event.y > 480)&&
                            (currentFigure.mState == FigureState.Judge)) {
                            currentFigure.changeToRaise(mUpperMonney - currentFigure.mBet);
                        } else if((event.x > 840)&&(event.x < 900)&&(event.y > 320)&&(event.y < 380)&&
                            currentFigure.isRaising) {
                            currentFigure.adjustBet(true);
                        } else if((event.x > 840)&&(event.x < 900)&&(event.y > 400)&&(event.y < 460)&&
                            currentFigure.isRaising) {
                            currentFigure.adjustBet(false);
                        } else if((event.x > 800)&&(event.x < 940)&&(event.y > 480)&&
                            currentFigure.isRaising) {
                            mPotMoney += currentFigure.playerRaise();
                            if(mUpperMonney < currentFigure.mBet) {
                                mUpperMonney = currentFigure.mBet;
                                mEndPosition = mLocation % 7;
                            } else if(mLocation % 7 == mEndPosition) {
                                changeToNextState();
                                return;
                            }
                            ++mLocation;
                            mDelay = 0;
                    }
                }
            }*/
            else if((!currentFigure.mIsAI)&&(mDelay > 10000)) {
                timeoutPlayer(currentFigure);
                /*currentFigure.mState = FigureState.Fold;
                if(mLocation % 7 == mEndPosition) {
                    changeToNextState();
                    return;*/
            }
            //++mLocation;
            //mDelay = 0;
        }
        /*if(mLocation == mEndPosition) {
            changeToNextState();
        }*/
    }

    private void timeoutPlayer(Figure currentFigure) {
        currentFigure.mState = FigureState.Fold;
        if(mLocation % 7 == mEndPosition) {
            changeToNextState();
        } else {
            ++mLocation;
            mDelay = 0;
        }
    }


    private void judgePlayer(Figure currentFigure, List<TouchEvent> events) {
        int len = events.size();
        for(int i = 0; i < len; ++i) {
            TouchEvent event = events.get(i);
            if(event.type == TouchEvent.TOUCH_DOWN) {
                if((event.x > 440)&&(event.x < 580)&&(event.y > 480)) {
                    currentFigure.isRaising = false;
                    currentFigure.playerFold();
                    if(mLocation % 7 == mEndPosition) {
                        changeToNextState();
                        return;
                    }
                    ++mLocation;
                    mDelay = 0;
                } else if((event.x > 620)&&(event.x < 760)&&(event.y > 480)) {
                    currentFigure.isRaising = false;
                    mPotMoney += currentFigure.checkOrCall(mUpperMonney - currentFigure.mBet);
                    if(mLocation % 7 == mEndPosition) {
                        changeToNextState();
                        return;
                    }
                    ++mLocation;
                    mDelay = 0;
                } else if((event.x > 800)&&(event.x < 940)&&(event.y > 480)&&
                        (currentFigure.mState == FigureState.Judge)&&(!currentFigure.isRaising)) {
                    currentFigure.changeToRaise(mUpperMonney - currentFigure.mBet);
                } else if((event.x > 840)&&(event.x < 900)&&(event.y > 320)&&(event.y < 380)&&
                        currentFigure.isRaising) {
                    currentFigure.adjustBet(true);
                } else if((event.x > 840)&&(event.x < 900)&&(event.y > 400)&&(event.y < 460)&&
                        currentFigure.isRaising) {
                    currentFigure.adjustBet(false);
                } else if((event.x > 800)&&(event.x < 940)&&(event.y > 480)&&
                        currentFigure.isRaising) {
                    mPotMoney += currentFigure.playerRaise();
                    if(mUpperMonney < currentFigure.mBet) {
                        mUpperMonney = currentFigure.mBet;
                        mEndPosition = (mLocation - 1) % 7;
                    } else if(mLocation % 7 == mEndPosition) {
                        changeToNextState();
                        return;
                    }
                    ++mLocation;
                    mDelay = 0;
                }
            }
        }
    }

    private void judgeFold() {
        mDelay = 0;
        if(mLocation % 7 == mEndPosition) {
            changeToNextState();
        } else {
            ++mLocation;
        }
    }

    private void judgeAI(Figure currentFigure) {
        mPotMoney += currentFigure.simpleJudgement(mUpperMonney - currentFigure.mBet);
        if(mUpperMonney < currentFigure.mBet) {
            mEndPosition = (mLocation - 1) % 7;
            mUpperMonney = currentFigure.mBet;
        } else if(mLocation % 7 == mEndPosition) {
            changeToNextState();
            return;
        }
        ++mLocation;
        mDelay = 0;
    }

    private void updateFlop(float deltatime) {
        mDelay += deltatime;
        if(mDelay > 5000) {
            mPoker.getNextCard();
            mCommunityCards.add(mPoker.getNextCard());
            mCommunityCards.add(mPoker.getNextCard());
            mCommunityCards.add(mPoker.getNextCard());
            mDelay = 0;
            mLocation = 1;
            mEndPosition = 0;
            mState = PokerState.FlopRound;
        }
    }

    private void updateTurn(float deltatime) {
        mDelay += deltatime;
        if(mDelay > 5000) {
            mPoker.getNextCard();
            mCommunityCards.add(mPoker.getNextCard());
            mDelay = 0;
            mLocation = 1;
            mEndPosition = 0;
            mState = PokerState.TurnRound;
        }
    }

    private void updateRiver(float deltatime) {
        mDelay += deltatime;
        if(mDelay > 5000) {
            mPoker.getNextCard();
            mCommunityCards.add(mPoker.getNextCard());
            mDelay = 0;
            mLocation = 1;
            mEndPosition = 0;
            mState = PokerState.RiverRound;
        }
    }

    private void updateCheck(float deltatime, List<TouchEvent> events) {
        if(!isChecked) {
            checkResult();
        }
        mDelay += deltatime;
        if(mDelay > 3000) {
            for (TouchEvent event : events) {
                if (event.type == TouchEvent.TOUCH_UP) {
                    resetGame();
                }
            }
        }
        /*mDelay += deltatime;
        List<Integer> winnerList = new ArrayList<>();
        PokerResult biggest = null;
        PokerResult currentResult;
        Figure currentFigure;
        for(int i = 0; i < 7; i++) {
            currentFigure = mFigureList.get(i);
            if(currentFigure.mState == FigureState.Fold)
                continue;
            List<Card> cards = new ArrayList<>();
            cards.add(currentFigure.mCardOne);
            cards.add(currentFigure.mCardTwo);
            cards.addAll(mCommunityCards);
            currentResult = mChecker.checkRoyalFlush(cards);
            if(biggest == null) {
                biggest = currentResult;
                winnerList.add(i);
            } else if (currentResult.compareTo(biggest) > 0) {
                biggest = currentResult;
                winnerList.clear();
                winnerList.add(i);
            } else if (currentResult.compareTo(biggest) == 0) {
                winnerList.add(i);
            }
        }
        for(int i : winnerList) {
            mFigureList.get(i).mCount += mPotMoney / winnerList.size();
        }*/

        /*if(mDelay > 5000) {
            mDelay = 0;
            ++mButton;
            mButton %= 7;

            mState = PokerState.Idle;
            mPotMoney = 0;
            mCommunityCards.clear();
            for (Figure figure : mFigureList) {
                figure.reset();
            }
        }*/
    }

    private void checkResult() {
        PokerResult biggest = null;
        PokerResult currentResult;
        Figure currentFigure;
        for(int i = 0; i < 7; i++) {
            currentFigure = mFigureList.get(i);
            if(currentFigure.mState == FigureState.Fold)
                continue;
            List<Card> cards = new ArrayList<>();
            cards.add(currentFigure.mCardOne);
            cards.add(currentFigure.mCardTwo);
            cards.addAll(mCommunityCards);
            currentResult = mChecker.checkRoyalFlush(cards);
            if(biggest == null) {
                biggest = currentResult;
                winnerList.add(i);
            } else if (currentResult.compareTo(biggest) > 0) {
                biggest = currentResult;
                winnerList.clear();
                winnerList.add(i);
            } else if (currentResult.compareTo(biggest) == 0) {
                winnerList.add(i);
            }
        }
        for(int i : winnerList) {
            mFigureList.get(i).mCount += mPotMoney / winnerList.size();
        }
        isChecked = true;
        //winnerList.clear();
    }

    private void resetGame() {
        mDelay = 0;
        ++mButton;
        mButton %= 7;
        mLocation = 0;

        mState = PokerState.Idle;
        mPotMoney = 0;
        mCommunityCards.clear();
        isChecked = false;
        winnerList.clear();
        for (Figure figure : mFigureList) {
            figure.reset();
        }
    }

    private void changeToNextState() {
        switch(mState) {
            case PreFlop:
                mState = PokerState.Flop;
                break;
            case FlopRound:
                mState = PokerState.Turn;
                break;
            case TurnRound:
                mState = PokerState.River;
                break;
            case RiverRound:
                mState = PokerState.Check;
                break;
            default:
                mState = PokerState.Idle;
                break;
        }
    }

}

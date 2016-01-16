package cn.edu.pku.kingarcher.texasholdem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by xtrao on 2016/1/1.
 */
public class Poker {

    public static class Card implements Comparable<Card> {
        enum Suit {
            Spades,
            Hearts,
            Clubs,
            Diamonds
        }

        public Suit mSuit;
        public int mNumber;

        public Card(Suit suit, int number) {
            mSuit = suit;
            mNumber = number;
        }

        @Override
        public int compareTo(Card another) {
            if(mNumber > another.mNumber)
                return 1;
            else if (mNumber < another.mNumber)
                return -1;
            else
                return 0;
        }
    }

    public static class SuitPriorComparator implements Comparator<Card> {

        @Override
        public int compare(Card lhs, Card rhs) {
            if(lhs.mSuit.compareTo(rhs.mSuit) > 0)
                return 1;
            else if(lhs.mSuit.compareTo(rhs.mSuit) < 0)
                return -1;
            else
                return lhs.compareTo(rhs);
        }

        @Override
        public boolean equals(Object object) {
            return ((Object)this).equals(object);
        }
    }

    public static class NumberPriorComparator implements Comparator<Card> {

        @Override
        public int compare(Card lhs, Card rhs) {
            if(lhs.compareTo(rhs) != 0)
                return lhs.compareTo(rhs);
            else
                return new SuitPriorComparator().compare(lhs, rhs);
        }

        @Override
        public boolean equals(Object object) {
            return ((Object)this).equals(object);
        }
    }



    public ArrayList<Card> mCards;
    public int mIndex;
    public static NumberPriorComparator numberPrior = new NumberPriorComparator();
    public static SuitPriorComparator suitPrior = new SuitPriorComparator();

    public Poker() {
        mCards = new ArrayList<>(52);
        newPoker();
        mIndex = 0;
    }

    private void newPoker() {
        //Initialize poker
        for(int i = 0; i < 52; ++i ) {
            Card card;
            switch(i/13) {
                case 0:
                    card = new Card(Card.Suit.Spades, i%13+2);
                    break;
                case 1:
                    card = new Card(Card.Suit.Hearts, i%13+2);
                    break;
                case 2:
                    card = new Card(Card.Suit.Diamonds, i%13+2);
                    break;
                case 3:
                    card = new Card(Card.Suit.Clubs, i%13+2);
                    break;
                default:
                    card = new Card(Card.Suit.Spades, 0);
                    break;
            }
            mCards.add(card);
        }
    }

    public void shufflePoker() {
        Collections.shuffle(mCards);
        mIndex = 0;
    }

    public Card getNextCard() {
        if(mIndex >= 52)
            return null;
        return mCards.get(mIndex++);
    }

    /*private void swapCards(int i, int j) {
        if (i == j)
            return;
        Card card = mCards.get(i);
        mCards.set(i, mCards.get(j));
        mCards.set(j, card);
    }*/
}

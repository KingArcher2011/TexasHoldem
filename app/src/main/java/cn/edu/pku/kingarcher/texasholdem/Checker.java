package cn.edu.pku.kingarcher.texasholdem;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import cn.edu.pku.kingarcher.texasholdem.Poker.Card;
import cn.edu.pku.kingarcher.texasholdem.Poker.SuitPriorComparator;
import cn.edu.pku.kingarcher.texasholdem.Poker.NumberPriorComparator;

/**
 * Created by xtrao on 2016/1/1.
 */
public class Checker {

    enum SuitPatterns {
        RoyalFlush,
        StraightFlush,
        FourOfAKind,
        FullHouse,
        Flush,
        Straight,
        ThreeOfAKind,
        TwoPairs,
        OnePair,
        HighCard
    }

    public static class PokerResult implements Comparable<PokerResult> {

        public SuitPatterns mPattern;
        public List<Card> mCardList;

        public PokerResult(SuitPatterns pattern, List<Card> list) {
            mPattern = pattern;
            mCardList = list;
        }

        @Override
        public int compareTo(@NonNull PokerResult pokerResult) {
            if(mPattern.compareTo(pokerResult.mPattern) > 0)
                return -1;
            else if(mPattern.compareTo(pokerResult.mPattern) < 0)
                return 1;
            else if(mCardList.size() != pokerResult.mCardList.size()) {
                Log.v("Checker", "Two PokerResults have same pattern but different list size");
                return 0;
            } else if(mCardList.size() > 0){
                int j = mCardList.size();
                int i = 0;
                int result;
                ListIterator<Card> iterator = mCardList.listIterator();
                ListIterator<Card> otherIterator = pokerResult.mCardList.listIterator();
                while(i < j) {
                    result = iterator.next().compareTo(otherIterator.next());
                    if(result != 0)
                        return result;
                    ++i;
                }
            }
            return 0;
        }
    }

    public PokerResult checkRoyalFlush(List<Card> list) {
        Collections.sort(list, Collections.reverseOrder(new SuitPriorComparator()));
        ListIterator<Card> iterator = list.listIterator();
        Card previous = null;
        Card present;
        int matchNum = 0;
        while(iterator.hasNext()){
            //Find a "A" for the RoyalFlush
            if(previous == null) {
                previous = iterator.next();
                if(previous.mNumber != 14) {
                    previous = null;
                    continue;
                }
                else {
                    matchNum = 1;
                    continue;
                }
            }
            present = iterator.next();
            if((present.mSuit == previous.mSuit)&&(present.mNumber == previous.mNumber - 1)) {
                ++matchNum;
                previous = present;
            } else if(present.mNumber == 14) {
                previous = present;
                matchNum = 1;
                continue;
            } else {
                previous = null;
                matchNum = 0;
            }
            if(matchNum == 5) {
                return new PokerResult(SuitPatterns.RoyalFlush, null);
            }
        }
        return checkStraightFlush(list);
    }

    public PokerResult checkStraightFlush(List<Card> list){
        Collections.sort(list, Collections.reverseOrder(new SuitPriorComparator()));
        ListIterator<Card> iterator = list.listIterator();
        Card previous = null;
        Card present;
        List<Card> resultList = new ArrayList<>();
        int matchNum = 0;
        boolean hasA = false;
        while(iterator.hasNext()) {
            if(previous == null) {
                previous = iterator.next();
                hasA = previous.mNumber == 14;
                matchNum = 1;
                resultList.add(previous);
                continue;
            }
            present = iterator.next();
            if((present.mNumber == previous.mNumber - 1) && (present.mSuit == previous.mSuit)) {
                ++matchNum;
                //resultList.add(present);
                previous = present;
                if(matchNum == 5)
                    return new PokerResult(SuitPatterns.StraightFlush, resultList);
                else if((matchNum == 4)&&(present.mNumber == 2)&& hasA){
                    return new PokerResult(SuitPatterns.StraightFlush, resultList);
                }
            } else if(present.mSuit == previous.mSuit){
                matchNum = 1;
                resultList.clear();
                resultList.add(present);
                previous = present;
            } else {
                matchNum = 1;
                resultList.clear();
                resultList.add(present);
                previous = present;
                if(present.mNumber == 14)
                    hasA = true;
                else
                    hasA = false;
            }
        }
        return checkFourOfAKing(list);
    }

    public PokerResult checkFourOfAKing(List<Card> list) {
        Collections.sort(list, Collections.reverseOrder(new NumberPriorComparator()));
        ListIterator<Card> iterator = list.listIterator();
        Card previous = null;
        Card present;
        List<Card> resultList = new ArrayList<>();
        int matchNum = 0;
        while(iterator.hasNext()) {
            if (previous == null) {
                previous = iterator.next();
                matchNum = 1;
                resultList.add(previous);
                continue;
            }
            present = iterator.next();
            if(present.mNumber == previous.mNumber) {
                ++matchNum;
                if(matchNum == 4)
                    return new PokerResult(SuitPatterns.FourOfAKind, resultList);
            }
            else {
                matchNum = 1;
                resultList.clear();
                resultList.add(present);
                previous = present;
            }
        }
        return checkFullHouse(list);
    }

    public PokerResult checkFullHouse(List<Card> list) {
        Collections.sort(list, Collections.reverseOrder(new NumberPriorComparator()));
        ListIterator<Card> iterator = list.listIterator();
        Card previous = null;
        Card present;
        List<Card> resultList = new ArrayList<>();
        Card numThree = null;
        Card numTwo = null;
        int matchNum = 0;
        while(iterator.hasNext()) {
            if (previous == null) {
                previous = iterator.next();
                matchNum = 1;
                continue;
            }
            present = iterator.next();
            if(present.mNumber == previous.mNumber)
                ++matchNum;
            else {
                if((matchNum == 3)&&(numThree == null))
                    numThree = previous;
                if((matchNum == 2)&&(numTwo == null))
                    numTwo = previous;
                previous = present;
                matchNum = 1;
            }
        }
        if((matchNum == 3)&&(numThree == null))
            numThree = previous;
        if((matchNum == 2)&&(numTwo == null))
            numTwo = previous;
        if((numThree != null)&&(numTwo != null)) {
            resultList.add(numThree);
            resultList.add(numTwo);
            return new PokerResult(SuitPatterns.FullHouse, resultList);
        }
        return checkFlush(list);
    }

    public PokerResult checkFlush(List<Card> list) {
        Collections.sort(list, Collections.reverseOrder(new SuitPriorComparator()));
        ListIterator<Card> iterator = list.listIterator();
        Card previous = null;
        Card present;
        List<Card> resultList = new ArrayList<>();
        int matchNum = 0;
        while(iterator.hasNext()) {
            if (previous == null) {
                previous = iterator.next();
                resultList.add(previous);
                matchNum = 1;
                continue;
            }
            present = iterator.next();
            if (present.mSuit == previous.mSuit) {
                ++matchNum;
                resultList.add(present);
                if (matchNum == 5)
                    return new PokerResult(SuitPatterns.Flush, resultList);
            } else {
                matchNum = 1;
                resultList.clear();
                resultList.add(present);
                previous = present;
            }
        }
        return checkStraight(list);
    }

    public PokerResult checkStraight(List<Card> list) {
        Collections.sort(list, Collections.reverseOrder(new NumberPriorComparator()));
        ListIterator<Card> iterator = list.listIterator();
        Card previous = null;
        Card present;
        List<Card> resultList = new ArrayList<>();
        boolean hasA = false;
        int matchNum = 0;
        while(iterator.hasNext()) {
            if (previous == null) {
                previous = iterator.next();
                if(previous.mNumber == 14)
                    hasA = true;
                resultList.add(previous);
                matchNum = 1;
                continue;
            }
            present = iterator.next();
            if(present.mNumber == previous.mNumber) {
                continue;
            } else if(present.mNumber == previous.mNumber - 1) {
                ++matchNum;
                previous = present;
                if(matchNum == 5) {
                    return new PokerResult(SuitPatterns.Straight, resultList);
                } else if((present.mNumber == 2) && (matchNum == 4) && hasA) {
                    return new PokerResult(SuitPatterns.Straight, resultList);
                }
            } else {
                matchNum = 1;
                previous = present;
                resultList.clear();
                resultList.add(present);
            }
        }
        return checkThreeOfAKind(list);
    }

    public PokerResult checkThreeOfAKind(List<Card> list) {
        Collections.sort(list, Collections.reverseOrder(new NumberPriorComparator()));
        ListIterator<Card> iterator = list.listIterator();
        Card previous = null;
        Card present;
        List<Card> resultList = new ArrayList<>();
        Card numThree = null;
        Card numOne1 = null;
        Card numOne2 = null;
        int matchNum = 0;
        while(iterator.hasNext()) {
            if (previous == null) {
                previous = iterator.next();
                matchNum = 1;
                continue;
            }
            present = iterator.next();
            if(previous.mNumber == present.mNumber) {
                ++matchNum;
                if((matchNum == 3)&&(numThree == null)) {
                    numThree = present;
                    previous = null;
                }
            }
            else {
                if(numOne1 == null)
                    numOne1 = previous;
                else if(numOne2 == null) {
                    numOne2 = previous;
                }
                previous = present;
                matchNum = 1;
            }
            if((numThree != null)&&(numOne1 != null)&&(numOne2 != null)) {
                resultList.add(numThree);
                resultList.add(numOne1);
                resultList.add(numOne2);
                return new PokerResult(SuitPatterns.ThreeOfAKind, resultList);
            }
        }
        return checkTwoPairs(list);
    }

    public PokerResult checkTwoPairs(List<Card> list) {
        Collections.sort(list, Collections.reverseOrder(new NumberPriorComparator()));
        ListIterator<Card> iterator = list.listIterator();
        Card previous = null;
        Card present;
        List<Card> resultList = new ArrayList<>();
        Card numTwo1 = null;
        Card numTwo2 = null;
        Card numOne = null;
        int matchNum = 0;
        while(iterator.hasNext()) {
            if (previous == null) {
                previous = iterator.next();
                matchNum = 1;
                continue;
            }
            present = iterator.next();
            if(present.mNumber == previous.mNumber) {
                ++matchNum;
                if (matchNum == 2) {
                    if (numTwo1 == null)
                        numTwo1 = present;
                    else if (numTwo2 == null)
                        numTwo2 = present;
                    previous = null;
                }
            } else {
                if(numOne == null)
                    numOne = previous;
                matchNum = 1;
                previous = present;
            }
            if((numTwo1 != null)&&(numTwo2 != null)&&(numOne != null)) {
                resultList.add(numTwo1);
                resultList.add(numTwo2);
                resultList.add(numOne);
                return new PokerResult(SuitPatterns.TwoPairs, resultList);
            }
        }
        return checkOnePair(list);
    }

    public PokerResult checkOnePair(List<Card> list) {
        Collections.sort(list, Collections.reverseOrder(new NumberPriorComparator()));
        ListIterator<Card> iterator = list.listIterator();
        Card previous = null;
        Card present;
        List<Card> resultList = new ArrayList<>();
        Card numTwo = null;
        Card numOne1 = null;
        Card numOne2 = null;
        Card numOne3 = null;
        while(iterator.hasNext()) {
            if (previous == null) {
                previous = iterator.next();
                continue;
            }
            present = iterator.next();
            if((present.mNumber == previous.mNumber)&&(numTwo == null)) {
                numTwo = present;
                previous = null;
            } else {
                if(numOne1 == null)
                    numOne1 = previous;
                else if(numOne2 == null)
                    numOne2 = previous;
                else if(numOne3 == null)
                    numOne3 = previous;
                previous = present;
            }
            if((numTwo != null)&&(numOne1 != null)&&(numOne2 != null)&&(numOne3 != null)) {
                resultList.add(numTwo);
                resultList.add(numOne1);
                resultList.add(numOne2);
                resultList.add(numOne3);
                return new PokerResult(SuitPatterns.OnePair, resultList);
            }
        }
        return checkHighCard(list);
    }

    public PokerResult checkHighCard(List<Card> list) {
        Collections.sort(list, Collections.reverseOrder(new NumberPriorComparator()));
        ListIterator<Card> iterator = list.listIterator();
        List<Card> resultList = new ArrayList<>();
        for(int i = 0; i < 5; ++i) {
            resultList.add(iterator.next());
        }
        return new PokerResult(SuitPatterns.HighCard, resultList);
    }
}

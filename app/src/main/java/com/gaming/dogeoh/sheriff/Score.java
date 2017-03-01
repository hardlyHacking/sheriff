package com.gaming.dogeoh.sheriff;

import android.os.Parcel;
import android.os.Parcelable;

class Score implements Parcelable {
    public static final int NUM_PLAYERS = 5;
    private final Integer[][] mScores;
    private final Integer[] mTotals;

    Score() {
        super();
        mScores = new Integer[NUM_PLAYERS][ResourceTypes.NUM_TYPES];
        mTotals = new Integer[] {0, 0, 0, 0, 0};
        for (short i = 0; i < NUM_PLAYERS; i++) {
            mScores[i] = new Integer[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
        }
    }

    private Score(Parcel parcel) {
        super();
        final int[] tArray = new int[NUM_PLAYERS];
        final int[][] sArray = new int[NUM_PLAYERS][ResourceTypes.NUM_TYPES];
        parcel.readIntArray(tArray);
        for (int i = 0; i < NUM_PLAYERS; i++) {
            parcel.readIntArray(sArray[i]);
        }
        mTotals = new Integer[NUM_PLAYERS];
        mScores = new Integer[NUM_PLAYERS][ResourceTypes.NUM_TYPES];
        for (int i = 0; i < NUM_PLAYERS; i++) {
            mTotals[i] = tArray[i];
        }
        for (int i = 0; i < NUM_PLAYERS; i++) {
            for (int j = 0; j < ResourceTypes.NUM_TYPES; j++) {
                mScores[i][j] = sArray[i][j];
            }
        }
    }

    Integer getScore(final int row, final int column) {
        return mScores[row][column];
    }

    void setScore(int playerNum, int resourceType, int resourceNum) {
        if (mScores[playerNum][resourceType] != resourceNum) {
            mScores[playerNum][resourceType] = resourceNum;
        }
    }

    private Integer[] calculateBonuses() {
        // [resource type][Max, # of occurrences, 2nd Max, # of occurrences]
        final int[][] topCounts = new int[ResourceTypes.NUM_LEGAL][4];
        for (Integer[] score : mScores) {
            for (short j = 0; j < ResourceTypes.NUM_LEGAL; j++) {
                if (score[j] == 0) {
                    continue;
                }
                if (topCounts[j][0] == 0 && topCounts[j][2] == 0) {
                    topCounts[j][0] = topCounts[j][2] = score[j];
                    topCounts[j][1] = topCounts[j][3] = 1;
                } else if (score[j] > topCounts[j][0]) {
                    topCounts[j][2] = topCounts[j][0];
                    topCounts[j][3] = topCounts[j][1];
                    topCounts[j][0] = score[j];
                    topCounts[j][1] = 1;
                } else if (score[j] > topCounts[j][2]) {
                    topCounts[j][2] = score[j];
                    topCounts[j][3] = 1;
                } else {
                    if (score[j].equals(topCounts[j][0])) {
                        topCounts[j][1] = (short) (topCounts[j][1] + 1);
                    }
                    if (score[j].equals(topCounts[j][2])) {
                        topCounts[j][3] = (short) (topCounts[j][3] + 1);
                    }
                }
            }
        }

        for (int i = 0; i < mScores.length; i++) {
            for (int j = 0; j < ResourceTypes.NUM_LEGAL; j++) {
                if (mScores[i][j] == 0) {
                    continue;
                }
                if (mScores[i][j].equals(topCounts[j][0])) {
                    mTotals[i] += (ResourceTypes.getPrimary(j) / topCounts[j][1]);
                }
                if (mScores[i][j].equals(topCounts[j][2])) {
                    mTotals[i] += (mTotals[i] + (ResourceTypes.getSecondary(j) / topCounts[j][3]));
                }
            }
        }

        return mTotals;
    }

    Integer[] calculateTotals() {
        for (int i = 0; i < mScores.length; i++) {
            int total = 0;
            for (int j = 0; j < ResourceTypes.NUM_TYPES; j++) {
                total += mScores[i][j] * ResourceTypes.getValue(j);
            }
            mTotals[i] = total;
        }
        return calculateBonuses();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int num) {
        parcel.writeInt(NUM_PLAYERS);
        final int[] totals = new int[NUM_PLAYERS];
        for (int i = 0; i < NUM_PLAYERS; i++) {
            totals[i] = mTotals[i];
        }
        parcel.writeIntArray(totals);
        final int[][] scores = new int[NUM_PLAYERS][ResourceTypes.NUM_TYPES];
        for (int i = 0; i < NUM_PLAYERS; i++) {
            for (int j = 0; j < ResourceTypes.NUM_TYPES; j++) {
                scores[i][j] = mScores[i][j];
            }
        }
        for (int i = 0; i < NUM_PLAYERS; i++) {
            parcel.writeInt(ResourceTypes.NUM_TYPES);
            parcel.writeIntArray(scores[i]);
        }
    }

    public static final Parcelable.Creator<Score> CREATOR = new Parcelable.Creator<Score>() {

        @Override
        public Score createFromParcel(Parcel parcel) {
            return new Score(parcel);
        }

        @Override
        public Score[] newArray(int i) {
            return new Score[0];
        }
    };
}

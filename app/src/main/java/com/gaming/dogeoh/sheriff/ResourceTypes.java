package com.gaming.dogeoh.sheriff;

import java.util.HashMap;

enum ResourceTypes {
    Apple,
    Bread,
    Cheese,
    Chicken,
    Spice,
    Mead,
    Silk,
    Crossbow,
    Cash;

    public static final int NUM_TYPES = 9;
    public static final int NUM_LEGAL = 4;

    private static final HashMap<ResourceTypes, Integer> mValue =
            new HashMap<ResourceTypes, Integer>() {{
                put(ResourceTypes.Apple, 2);
                put(ResourceTypes.Bread, 3);
                put(ResourceTypes.Cheese, 3);
                put(ResourceTypes.Chicken, 4);
                put(ResourceTypes.Spice, 6);
                put(ResourceTypes.Mead, 7);
                put(ResourceTypes.Silk, 8);
                put(ResourceTypes.Crossbow, 9);
                put(ResourceTypes.Cash, 1);
            }};

    private static final HashMap<ResourceTypes, Integer> mPrimary =
            new HashMap<ResourceTypes, Integer>() {{
                put(ResourceTypes.Apple, 20);
                put(ResourceTypes.Bread, 15);
                put(ResourceTypes.Cheese, 15);
                put(ResourceTypes.Chicken, 10);
            }};

    private static final HashMap<ResourceTypes, Integer> mSecondary =
            new HashMap<ResourceTypes, Integer>() {{
                put(ResourceTypes.Apple, 10);
                put(ResourceTypes.Bread, 10);
                put(ResourceTypes.Cheese, 10);
                put(ResourceTypes.Chicken, 5);
            }};

    public static int getValue(final ResourceTypes resource) {
        return mValue.get(resource);
    }

    public static int getValue(final int s) {
        return mValue.get(fromInt(s));
    }

    public static int getPrimary(final ResourceTypes resource) {
        return mPrimary.get(resource);
    }

    public static int getPrimary(final int s) {
        return mPrimary.get(fromInt(s));
    }

    public static int getSecondary(final ResourceTypes resource) {
        return mSecondary.get(resource);
    }

    public static int getSecondary(final int s) {
        return mSecondary.get(fromInt(s));
    }

    public static ResourceTypes fromInt(final int s) {
        switch (s) {
            case 0: {
                return Apple;
            }
            case 1: {
                return Bread;
            }
            case 2: {
                return Cheese;
            }
            case 3: {
                return Chicken;
            }
            case 4: {
                return Spice;
            }
            case 5: {
                return Mead;
            }
            case 6: {
                return Silk;
            }
            case 7: {
                return Crossbow;
            }
            default: {
                return Cash;
            }
        }
    }
}

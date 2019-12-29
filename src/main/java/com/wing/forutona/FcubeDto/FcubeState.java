package com.wing.forutona.FcubeDto;

public enum FcubeState{
    startWait(0),
    play(1),
    finish(2);

    private final int value;
    private FcubeState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static FcubeState  getEnum(int value) {
        for (FcubeState e : FcubeState.values()) {
            if (e.getValue() == value)
                return e;
        }
        return null;//For values out of enum scope

    }
}
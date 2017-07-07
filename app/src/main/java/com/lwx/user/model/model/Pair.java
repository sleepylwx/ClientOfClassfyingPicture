package com.lwx.user.model.model;

import android.support.annotation.NonNull;

/**
 * Created by 36249 on 2017/7/5.
 */

public class Pair implements Comparable<Pair>{

    public String key;
    public int value;

    public Pair(String key,int value){

        this.key = key;
        this.value = value;
    }

    @Override
    public int compareTo(@NonNull Pair o) {
        return o.value - value;
    }
}

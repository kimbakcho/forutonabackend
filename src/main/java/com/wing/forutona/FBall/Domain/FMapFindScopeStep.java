package com.wing.forutona.FBall.Domain;

import javax.persistence.*;

@Entity
@Table(name = "MapFindScopeStep")
public class FMapFindScopeStep {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idx;
    int scopeMeter;

    public long getIdx() {
        return idx;
    }

    public void setIdx(long idx) {
        this.idx = idx;
    }

    public int getScopeMeter() {
        return scopeMeter;
    }

    public void setScopeMeter(int scopeMeter) {
        this.scopeMeter = scopeMeter;
    }
}

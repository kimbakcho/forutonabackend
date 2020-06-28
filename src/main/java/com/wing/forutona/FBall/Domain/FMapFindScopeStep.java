package com.wing.forutona.FBall.Domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "MapFindScopeStep")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public FMapFindScopeStep(long idx,int scopeMeter){
        this.idx = idx;
        this.scopeMeter = scopeMeter;
    }


}

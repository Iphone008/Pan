package com.example.library.bean;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class ViewPosition {
    private  int id;
    private int l, t, r, b;
    private int changeL;
    private int changeT;
    private int changeR;
    private int changeB;
    static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0xD001400, "MY_TAG_ViewPosition");

    public ViewPosition(int id,int l, int t, int r, int b) {
        this.id=id;
        this.changeL = this.l = l;
        this.changeT = this.t = t;
        this.changeR = this.r = r;
        this.changeB = this.b = b;
        HiLog.info(LABEL_LOG, "1 ViewPosition id --> :"+id);
        HiLog.info(LABEL_LOG, "2 ViewPosition l --> :"+l);
        HiLog.info(LABEL_LOG, "3 ViewPosition t --> :"+t);
        HiLog.info(LABEL_LOG, "4 ViewPosition r --> :"+r);
        HiLog.info(LABEL_LOG, "5 ViewPosition b --> :"+b);
    }
    public ViewPosition(int l, int t, int r, int b) {
        this.changeL = l;
        this.changeT = t;
        this.changeR = r;
        this.changeB = b;
    }

    public void change(int newL,int newT,int newR,int newB){
        changeL = newL;
        changeT = newT;
        changeR = newR;
        changeB = newB;
    }

    public void reset(){
        changeL = l;
        changeT = t;
        changeR = r;
        changeB = b;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChangeL() {
        return changeL;
    }

    public void setChangeL(int changeL) {
        this.changeL = changeL;
    }

    public int getChangeT() {
        return changeT;
    }

    public void setChangeT(int changeT) {
        this.changeT = changeT;
    }

    public int getChangeR() {
        return changeR;
    }

    public void setChangeR(int changeR) {
        this.changeR = changeR;
    }

    public int getChangeB() {
        return changeB;
    }

    public void setChangeB(int changeB) {
        this.changeB = changeB;
    }
}



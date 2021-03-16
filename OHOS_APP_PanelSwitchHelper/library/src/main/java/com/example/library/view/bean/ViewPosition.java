package com.example.library.view.bean;

public class ViewPosition {
    private  int id;
    private int changeL;
    private int changeT;
    private int changeR;
    private int changeB;

    public ViewPosition(int id,int l, int t, int r, int b) {
        this.id=id;
        this.changeL = l;
        this.changeT = t;
        this.changeR = r;
        this.changeB = b;
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


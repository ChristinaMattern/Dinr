package com.example.dinr;

public class item {
    int background;
    String title;


    public item(){

    }
    public item(int background, String title) {
        this.background = background;
        this.title = title;
    }
    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}

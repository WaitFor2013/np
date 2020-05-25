package com.np.design.ui.editor;

public class WordNode {
    private int location;
    private String word;

    public WordNode(int location, String str) {
        super();
        this.location = location;
        this.word = str;
    }

    public int getLocation() {
        return location;
    }

    public String getWord() {
        return word;
    }

}
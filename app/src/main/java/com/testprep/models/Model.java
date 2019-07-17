package com.testprep.models;

public class Model {

    private String text;
    private boolean isSelected = false;

    public Model(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

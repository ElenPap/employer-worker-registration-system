package com.cbozan.util;

public class LayoutMetrics {

    public LayoutMetrics(int labelPositionY, int labelPositionX, int textFieldWidth, int textFieldHeight, int labelWidth, int labelHeight, int labelVerticalSpace, int labelTextFieldVerticalSpace, int labelHorizontalSpace, int buttonWidth, int buttonHeight) {
        this.labelPositionY = labelPositionY;
        this.labelPositionX = labelPositionX;
        this.textFieldWidth = textFieldWidth;
        this.textFieldHeight = textFieldHeight;
        this.labelWidth = labelWidth;
        this.labelHeight = labelHeight;
        this.labelVerticalSpace = labelVerticalSpace;
        this.labelTextFieldVerticalSpace = labelTextFieldVerticalSpace;
        this.labelHorizontalSpace = labelHorizontalSpace;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
    }

    private final int labelPositionY;
    private final int labelPositionX;
    private final int textFieldWidth;
    private final int textFieldHeight;
    private final int labelWidth;
    private final int labelHeight;
    private final int labelVerticalSpace;
    private final int labelTextFieldVerticalSpace;
    private final int labelHorizontalSpace;
    private final int buttonWidth;
    private final int buttonHeight;

    public int getLabelPositionY() {
        return labelPositionY;
    }

    public int getLabelPositionX() {
        return labelPositionX;
    }

    public int getTextFieldWidth() {
        return textFieldWidth;
    }

    public int getTextFieldHeight() {
        return textFieldHeight;
    }

    public int getLabelWidth() {
        return labelWidth;
    }

    public int getLabelHeight() {
        return labelHeight;
    }

    public int getLabelVerticalSpace() {
        return labelVerticalSpace;
    }

    public int getLabelTextFieldVerticalSpace() {
        return labelTextFieldVerticalSpace;
    }

    public int getLabelHorizontalSpace() {
        return labelHorizontalSpace;
    }

    public int getButtonWidth() {
        return buttonWidth;
    }

    public int getButtonHeight() {
        return buttonHeight;
    }
}

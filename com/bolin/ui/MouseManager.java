package com.bolin.ui;

import java.awt.*;

/**
 * Created by jonb3_000 on 7/19/2016.
 */
public class MouseManager {

    private Point leftPressPoint, rightPressPoint, middlePressPoint;

    public boolean isRightPressed() {
        return rightPressPoint != null;
    }

    public boolean isLeftPressed() {
        return leftPressPoint != null;
    }

    public boolean isMiddlePressed() {
        return middlePressPoint != null;
    }

    public Point getLeftPressPoint() {
        return leftPressPoint;
    }

    public Point getRightPressPoint() {
        return rightPressPoint;
    }

    public Point getMiddlePressPoint() {
        return middlePressPoint;
    }

    public void setLeftPressPoint(Point p) {
        leftPressPoint = p;
    }

    public void setRightPressPoint(Point p) {
        rightPressPoint = p;
    }

    public void setMiddlePressPoint(Point p) {
        middlePressPoint = p;
    }

}

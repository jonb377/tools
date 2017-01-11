package com.bolin.math.function;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jonb3_000 on 8/22/2016.
 */
public class FunctionPlot {

    public static PlotPanel plot(Function f, double minx, double maxx) {
        JFrame frame;
        frame = new JFrame("Function Plot");
        frame.setSize(1800, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PlotPanel panel = new PlotPanel(f, minx, maxx);
        frame.add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
        return panel;
    }

}

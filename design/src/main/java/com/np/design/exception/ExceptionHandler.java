package com.np.design.exception;

import com.np.design.NoRepeatApp;
import com.np.design.NoRepeatApp;

import javax.swing.*;

public class ExceptionHandler {

    public static void handle(Exception e) {
        JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, e.getMessage(), "错误",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void debug(String msg) {
        JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, msg, "DEBUG",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

package com.jpromi.leaBrowser;

import com.jpromi.leaBrowser.browser.BrowserApplication;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new BrowserApplication().start();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
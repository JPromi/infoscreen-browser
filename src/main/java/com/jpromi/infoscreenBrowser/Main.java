package com.jpromi.infoscreenBrowser;

import com.jpromi.infoscreenBrowser.browser.BrowserApplication;

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
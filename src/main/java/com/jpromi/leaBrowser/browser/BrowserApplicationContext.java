package com.jpromi.leaBrowser.browser;

import org.cef.CefApp;
import org.cef.browser.CefBrowser;

import javax.swing.*;

public class BrowserApplicationContext {
    public final CefApp cefApp;
    public CefBrowser browser;
    public JFrame window;

    public BrowserApplicationContext(CefApp cefApp) {
        this.cefApp = cefApp;
    }
}

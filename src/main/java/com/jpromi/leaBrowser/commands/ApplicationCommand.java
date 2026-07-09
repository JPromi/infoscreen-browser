package com.jpromi.leaBrowser.commands;

import com.jpromi.leaBrowser.annotaions.BrowserClass;
import com.jpromi.leaBrowser.annotaions.BrowserCommand;
import com.jpromi.leaBrowser.browser.BrowserApplicationContext;
import org.cef.CefApp;

@BrowserClass("application")
public class ApplicationCommand {
    private final CefApp cefApp;

    public ApplicationCommand(BrowserApplicationContext context) {
        this.cefApp = context.cefApp;
    }

    @BrowserCommand("exit")
    public void exit() {
        cefApp.dispose();
        System.exit(0);
        return;
    }
}

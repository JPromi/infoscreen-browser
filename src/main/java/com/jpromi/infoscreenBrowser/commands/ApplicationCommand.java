package com.jpromi.infoscreenBrowser.commands;

import com.jpromi.infoscreenBrowser.annotaions.BrowserClass;
import com.jpromi.infoscreenBrowser.annotaions.BrowserCommand;
import com.jpromi.infoscreenBrowser.browser.BrowserApplicationContext;
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

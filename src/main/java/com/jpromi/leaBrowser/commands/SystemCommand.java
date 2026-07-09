package com.jpromi.leaBrowser.commands;

import com.jpromi.leaBrowser.annotaions.BrowserClass;
import com.jpromi.leaBrowser.annotaions.BrowserCommand;

@BrowserClass("system")
public class SystemCommand {

    @BrowserCommand("shutdown")
    public String systemShutdown() {
        // TODO: Shutdown-Logik
        System.out.println("Shutdown Called");
        // System.exit(0);
        return "OK";
    }

    @BrowserCommand("restart")
    public String systemRestart() {
        // TODO: Restart-Logik
        System.out.println("Restart Called");
        return "OK";
    }
}

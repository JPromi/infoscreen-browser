package com.jpromi.leaBrowser.browser;

import com.jpromi.leaBrowser.commands.ApplicationCommand;
import com.jpromi.leaBrowser.commands.SystemCommand;
import com.jpromi.leaBrowser.registry.BrowserCommandRegistry;
import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;
import org.cef.*;
import org.cef.browser.CefBrowser;

import javax.swing.*;
import java.awt.*;

public class BrowserApplication {
    public void start() throws Exception {
        CefAppBuilder builder = new CefAppBuilder();

        builder.addJcefArgs("--kiosk-printing");
        builder.addJcefArgs("--disable-print-preview");
        builder.addJcefArgs("--autoplay-policy=no-user-gesture-required");

        builder.getCefSettings().windowless_rendering_enabled = false;
        builder.setAppHandler(new MavenCefAppHandlerAdapter() {});

        CefApp cefApp = builder.build();
        CefClient client = cefApp.createClient();

        BrowserApplicationContext context = new BrowserApplicationContext(cefApp);

        BrowserCommandRegistry commandRegistry = new BrowserCommandRegistry();
        commandRegistry.register(new ApplicationCommand(context));
        commandRegistry.register(new SystemCommand());

        client.addMessageRouter(
                new BrowserMessageHandler(commandRegistry).createRouter()
        );

        client.addLoadHandler(new LoadHandler(commandRegistry));

        CefBrowser browser = client.createBrowser(
                BrowserConfig.START_URL,
                false,
                false
        );

        context.browser = browser;

        JFrame window = new JFrame("LEA Browser");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(1280, 720);
        window.setLayout(new BorderLayout());
        window.add(browser.getUIComponent(), BorderLayout.CENTER);
        window.setUndecorated(false);
        window.setVisible(true);

        context.window = window;
    }
}

package com.jpromi.infoscreenBrowser.browser;

import com.jpromi.infoscreenBrowser.registry.BrowserCommandAutoLoader;
import com.jpromi.infoscreenBrowser.registry.BrowserCommandRegistry;
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
        builder.addJcefArgs("--enable-gpu");
        builder.addJcefArgs("--enable-smooth-scrolling");

        builder.getCefSettings().windowless_rendering_enabled = false;
        builder.setAppHandler(new MavenCefAppHandlerAdapter() {});

        CefApp cefApp = builder.build();
        CefClient client = cefApp.createClient();

        BrowserApplicationContext context = new BrowserApplicationContext(cefApp);

        BrowserCommandRegistry commandRegistry = new BrowserCommandRegistry();
        BrowserCommandAutoLoader.registerAll(commandRegistry, context);

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

        JFrame window = new JFrame("Infoscreen Browser");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(1280, 720);
        window.setLayout(new BorderLayout());
        window.add(browser.getUIComponent(), BorderLayout.CENTER);
        window.setUndecorated(false);
        window.setVisible(true);

        context.window = window;
    }
}

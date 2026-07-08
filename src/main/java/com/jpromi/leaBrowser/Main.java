package com.jpromi.leaBrowser;

import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefLoadHandlerAdapter;
import org.cef.handler.CefMessageRouterHandlerAdapter;
import org.cef.browser.CefMessageRouter;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class Main {
    private static final String START_URL = "https://dev.jpromi.com";
    private static final String ALLOWED_HOST = "dev.jpromi.com";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                startBrowser();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }

    private static void startBrowser() throws Exception {
        CefAppBuilder builder = new CefAppBuilder();

        builder.addJcefArgs("--kiosk-printing");
        builder.addJcefArgs("--disable-print-preview");
        builder.addJcefArgs("--autoplay-policy=no-user-gesture-required");

        builder.getCefSettings().windowless_rendering_enabled = false;
        builder.setAppHandler(new MavenCefAppHandlerAdapter() {});

        CefApp cefApp = builder.build();
        CefClient client = cefApp.createClient();

        CefMessageRouter router = CefMessageRouter.create();
        router.addHandler(new CefMessageRouterHandlerAdapter() {
            @Override
            public boolean onQuery(
                    CefBrowser browser,
                    CefFrame frame,
                    long queryId,
                    String request,
                    boolean persistent,
                    CefQueryCallback callback
            ) {
                if (!isAllowed(frame.getURL())) {
                    callback.failure(403, "Forbidden origin");
                    return true;
                }

                if (request.contains("\"action\":\"shutdown\"")) {
                    callback.success("OK");
                    System.exit(0);
                    return true;
                }

                callback.failure(400, "Unknown command");
                return true;
            }
        }, true);

        client.addMessageRouter(router);

        CefBrowser browser = client.createBrowser(
                START_URL,
                false,
                false
        );

        client.addLoadHandler(new CefLoadHandlerAdapter() {
            @Override
            public void onLoadEnd(
                    CefBrowser cefBrowser,
                    CefFrame frame,
                    int httpStatusCode
            ) {
                if (!frame.isMain()) {
                    return;
                }

                String url = frame.getURL();

                if (isAllowed(url)) {
                    cefBrowser.executeJavaScript("""
                        window.CUSTOM = {
                            shutdown: function() {
                                window.cefQuery({
                                    request: JSON.stringify({ action: "shutdown" })
                                });
                            },
                        };
                    """, url, 0);
                }
            }
        });

        JFrame window = new JFrame("LEA Browser");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(1280, 720);
        window.setLayout(new BorderLayout());
        window.add(browser.getUIComponent(), BorderLayout.CENTER);

        window.setUndecorated(false);
        // window.setExtendedState(JFrame.MAXIMIZED_BOTH);

        window.setVisible(true);
    }

    private static boolean isAllowed(String url) {
        try {
            URI uri = URI.create(url);
            return "https".equalsIgnoreCase(uri.getScheme())
                    && ALLOWED_HOST.equalsIgnoreCase(uri.getHost());
        } catch (Exception e) {
            return false;
        }
    }
}
package com.jpromi.infoscreenBrowser.browser;

import com.jpromi.infoscreenBrowser.registry.BrowserCommandRegistry;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefLoadHandlerAdapter;

import java.net.URI;

public class LoadHandler extends CefLoadHandlerAdapter {
    private final BrowserCommandRegistry commandRegistry;

    public LoadHandler(BrowserCommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void onLoadEnd(CefBrowser cefBrowser, CefFrame frame, int httpStatusCode) {
        if (!frame.isMain()) return;

        String url = frame.getURL();

        if (!isAllowed(url)) return;

        cefBrowser.executeJavaScript(
                commandRegistry.generateJavaScriptApi(BrowserConfig.JS_GLOBAL_NAME),
                url,
                0
        );
    }

    private static boolean isAllowed(String url) {
        try {
            URI uri = URI.create(url);
            return "https".equalsIgnoreCase(uri.getScheme())
                    && BrowserConfig.ALLOWED_HOST.equalsIgnoreCase(uri.getHost());
        } catch (Exception e) {
            return false;
        }
    }
}

package com.jpromi.leaBrowser.browser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.jpromi.leaBrowser.registry.BrowserCommandRegistry;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

import java.net.URI;

public class BrowserMessageHandler {

    private final BrowserCommandRegistry commandRegistry;

    public BrowserMessageHandler(BrowserCommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    public CefMessageRouter createRouter() {
        CefMessageRouter.CefMessageRouterConfig config =
                new CefMessageRouter.CefMessageRouterConfig();

        config.jsQueryFunction = "cefQuery";
        config.jsCancelFunction = "cefQueryCancel";

        CefMessageRouter router = CefMessageRouter.create(config);

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

                try {
                    String command = parseCommand(request);

                    String result = commandRegistry.execute(command);
                    callback.success(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.failure(400, e.getMessage());
                }

                return true;
            }
        }, true);

        return router;
    }

    private static String parseCommand(String request) {
        try {
            JsonObject json = JsonParser.parseString(request).getAsJsonObject();

            if (!json.has("action") || json.get("action").isJsonNull()) {
                throw new IllegalArgumentException("Missing action");
            }

            return json.get("action").getAsString();

        } catch (JsonSyntaxException | IllegalStateException e) {
            throw new IllegalArgumentException("Invalid JSON");
        }
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
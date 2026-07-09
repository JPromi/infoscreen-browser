package com.jpromi.infoscreenBrowser.registry;

import com.jpromi.infoscreenBrowser.annotaions.BrowserClass;
import com.jpromi.infoscreenBrowser.annotaions.BrowserCommand;

import java.lang.reflect.Method;
import java.util.*;

public class BrowserCommandRegistry {

    private final Map<String, RegisteredCommand> commands = new LinkedHashMap<>();

    public void register(Object commandObject) {
        Class<?> clazz = commandObject.getClass();

        BrowserClass browserClass = clazz.getAnnotation(BrowserClass.class);

        if (browserClass == null) {
            throw new IllegalArgumentException(
                    "Missing @BrowserClass on " + clazz.getName()
            );
        }

        String className = browserClass.value();

        for (Method method : clazz.getDeclaredMethods()) {
            BrowserCommand browserCommand = method.getAnnotation(BrowserCommand.class);

            if (browserCommand == null) {
                continue;
            }

            method.setAccessible(true);

            String commandPath = className + "." + browserCommand.value();

            commands.put(commandPath, new RegisteredCommand(commandObject, method));
        }
    }

    public String execute(String commandPath) throws Exception {
        RegisteredCommand command = commands.get(commandPath);

        if (command == null) {
            throw new IllegalArgumentException("Unknown command: " + commandPath);
        }

        Object result = command.method.invoke(command.instance);
        return result != null ? result.toString() : "OK";
    }

    public String generateJavaScriptApi(String globalName) {
        StringBuilder js = new StringBuilder();

        js.append("window.").append(globalName)
                .append(" = window.").append(globalName).append(" || {};\n");

        for (String commandPath : commands.keySet()) {
            String[] parts = commandPath.split("\\.");

            String className = parts[0];
            String commandName = parts[1];

            js.append("window.").append(globalName).append(".").append(className)
                    .append(" = window.").append(globalName).append(".").append(className)
                    .append(" || {};\n");

            js.append("window.").append(globalName).append(".").append(className).append(".").append(commandName)
                    .append(" = function() {\n")
                    .append("  return new Promise(function(resolve, reject) {\n")
                    .append("    window.cefQuery({\n")
                    .append("      request: JSON.stringify({ action: \"").append(commandPath).append("\" }),\n")
                    .append("      onSuccess: function(response) { resolve(response); },\n")
                    .append("      onFailure: function(code, message) { reject({ code: code, message: message }); }\n")
                    .append("    });\n")
                    .append("  });\n")
                    .append("};\n");
        }

        return js.toString();
    }

    private record RegisteredCommand(Object instance, Method method) {}
}
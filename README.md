# Infoscreen Browser

## Development

## Neuer JavaScript Befehl
1. Erstelle eine neue Klasse in `commands`
Beispiel:
```Java
@BrowserClass("test")
public class TestCommand {
    private final BrowserApplicationContext context;

    public TestCommand(BrowserApplicationContext context) {
        this.context = context;
    }

    @BrowserCommand("hello")
    public void exit() {
        System.out.println("Hello World");
        return;
    }
}
```
2. Rufe den neuen Befehl auf deiner Website auf
Beispiel:
```JavaScript
window.INFOSCREENCOM.tset.hello();
```
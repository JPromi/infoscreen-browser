# Commands
You can run the following commands on your website with the following JavaScript code:
```javascript
window.INFOSCREENCOM.CLASS.COMMAND();
```

In Typescreen you need to declare it:
```typescript
// infoscreencom.d.ts
export {};

declare global {
    const INFOSCREENCOM: {
        system: {
            shutdown: () => void;
            restart: () => void;
            // ...
        },
        // ...
    };
}
```

## Application
| Command              | Description           |
|----------------------|-----------------------|
| `application.exit()` | Stops the Application |

## System
| Command             | Description          |
|---------------------|----------------------|
| `system.shutdown()` | Shutdown host System |
| `system.restart()`  | Restart host System  |


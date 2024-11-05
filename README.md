# Tips to run the tasks
## In Task 6 remember to set up port forwarding between two emulators using adb, you can follow these steps:

### Identify the Emulator Ports:
* Each Android emulator instance uses unique ports. To check which ports are being used by each emulator, use:

```
adb devices
```

* This will list all connected emulators along with their unique identifiers (e.g., emulator-5554, emulator-5556). These identifiers can help when directing port forwarding.

### Forward Ports for Communication:

* Suppose your server is running on emulator-5554 and is listening on port 12345. You want to forward this port to make it accessible from the client on emulator-5556.
* Use the following command to forward the port from emulator-5554 to localhost on emulator-5556:

```
adb -s emulator-5554 forward tcp:12345 tcp:12345
```

This command forwards traffic from port 12345 on emulator-5554 to port 12345 on the client emulator.

The client can now send messages to the server!

# ShowTrak Android Demo

This project is a simple demo app for the ShowTrak Android SDK.

It connects to a ShowTrak Server and registers five test actions:

- Set Box Red (icon `circle-fill`, red)
- Set Box Green (icon `circle-fill`, green)
- Set Box Blue (icon `circle-fill`, blue)
- Reset Box (icon `arrow-counterclockwise`, orange)
- Feedback Demo (icon `broadcast`, purple — demonstrates `ack.feedback()`)

When one of the colour actions is triggered from ShowTrak, the color box in the app updates.

Each action names a [Bootstrap Icons](https://icons.getbootstrap.com) glyph via
`EventOptions(icon = ...)`, which ShowTrak draws beside it in the menu, tinted with
the action's colour. The three colour actions share a plain filled circle so the
colour is what distinguishes them. The icon is optional — omit it and the action
shows the default `terminal` glyph.

## What is in this repo

- `app/`: the demo Android app (`io.showtrak.sample`)

SDK dependency:
- `io.github.showtrak:showtrak-sdk:1.3.0`

SDK repository:
- https://github.com/ShowTrak/ShowTrakClient-SDK-Android

## Troubleshooting

If Gradle sync fails resolving `io.github.showtrak:showtrak-sdk`, verify that
`mavenCentral()` is available in `settings.gradle.kts` repositories and that
you have network access to Maven Central.

## Requirements

- Android Studio
- A running ShowTrak Server

## Run the demo

1. Open this folder in Android Studio.
2. Let Gradle sync.
3. Run the `app` configuration on an emulator or device.

## Connect to ShowTrak

In the app, enter:

- Server IP
- Server Port (usually `3000`)
- Client ID (optional, can be left blank)

Then tap Connect.

If the server is running on the same computer as the Android emulator, use `10.0.2.2` as the Server IP.

## Adopt the device

After connecting for the first time, the client appears in ShowTrak as pending adoption.
Adopt it in ShowTrak, then the app will move to Online.

## Try the demo actions

In ShowTrak, right-click the integrated client and run:

- Set Box Red
- Set Box Green
- Set Box Blue
- Reset Box

You should see the box color change in the app, and each action listed under
"Remote Events" with the icon it registered.

## Try the progress feedback

Run **Feedback Demo**. It calls `ack.feedback()` five times, one call a second,
and each message says exactly which call it is — there is no simulated work,
just the calls themselves spaced out far enough to watch. In ShowTrak's script
execution popup the row's status text updates live as each call lands, then the
row completes when the handler calls `ack.success()`. The app shows the same
messages under the box.

Feedback is optional — the other four actions use none. To see a timeout
instead, drop `timeoutMs` below `5000` in `registerFeedbackDemoEvent()`: the ack
resolves as `RESOLVED_TIMEOUT`, the handler notices via `ack.isResolved()` and
stops early, and the row fails in ShowTrak.

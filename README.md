<div align="center">

<a href="https://showtrak.co.uk"><img src="https://showtrak.co.uk/images/logo.png" alt="ShowTrak Android SDK Demo logo" width="128" height="128"></a>

# ShowTrak Android SDK Demo

**A sample Android app showing what the ShowTrak Android SDK can do.**

[Website](https://showtrak.co.uk) · [Android SDK](https://github.com/ShowTrak/ShowTrakClient-SDK-Android) · [Discord](https://discord.gg/DACmwsbSGW)

[![Android 7.0+][badge-android7]](https://showtrak.co.uk) [![Kotlin][badge-kotlin]](https://showtrak.co.uk) [![CI status][badge-ci]](https://github.com/ShowTrak/ShowTrak-SDK-Android-Demo/actions/workflows/ci.yml)

</div>

## About the demo

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

## Part of ShowTrak

| App | What it does |
| --- | --- |
| **[ShowTrak Server](https://showtrak.co.uk/software/server)** | Monitor and control every computer and show device from one screen. |
| **[ShowTrak Client](https://showtrak.co.uk/software/client)** | The lightweight app that lets ShowTrak Server watch and control a computer. |
| **[ShowTrak Timers](https://showtrak.co.uk/software/timers)** | Show timers run from the desk, a Stream Deck, QLab or any browser. |
| **[ShowTrak Portal](https://showtrak.co.uk)** | Show reports, schedules and team collaboration for live productions. |

Find everything at **[showtrak.co.uk](https://showtrak.co.uk)**.

## Support

Questions, ideas or problems? Join the [ShowTrak Discord](https://discord.gg/DACmwsbSGW) or [open an issue](https://github.com/ShowTrak/ShowTrak-SDK-Android-Demo/issues).

[badge-android7]: https://img.shields.io/badge/Android%207.0%2B-3DDC84?style=for-the-badge&logo=android&logoColor=white
[badge-kotlin]: https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white
[badge-ci]: https://img.shields.io/github/actions/workflow/status/ShowTrak/ShowTrak-SDK-Android-Demo/ci.yml?branch=main&style=for-the-badge&logo=githubactions&logoColor=white&label=CI

# ShowTrak Android Demo

This project is a simple demo app for the ShowTrak Android SDK.

It connects to a ShowTrak Server and registers three test actions:

- Set Box Red
- Set Box Green
- Set Box Blue

When one of those actions is triggered from ShowTrak, the color box in the app updates.

## What is in this repo

- `app/`: the demo Android app (`io.showtrak.sample`)
- `../ShowTrak-SDK-Android/showtrak-sdk/`: the shared SDK module used by the demo

SDK repository:
- https://github.com/ShowTrak/ShowTrak-SDK-Android

The demo references the SDK module from the sibling SDK repo in `settings.gradle.kts`.

## Download with submodules

Clone the demo with submodules:

```bash
git clone --recurse-submodules https://github.com/ShowTrak/ShowTrak-SDK-Android-Demo.git
```

If you already cloned it without submodules, run:

```bash
git submodule update --init --recursive
```

## Troubleshooting

If Gradle sync fails with an error that the `showtrak-sdk` module cannot be found, the SDK submodule is probably missing.

Run:

```bash
git submodule update --init --recursive
```

Then sync Gradle again in Android Studio.

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
- Server Port (usually `8000`)
- Client ID (optional, can be left blank)

Then tap Connect.

If the server is running on the same Mac as the Android emulator, use `10.0.2.2` as the Server IP.

## Adopt the device

After connecting for the first time, the client appears in ShowTrak as pending adoption.
Adopt it in ShowTrak, then the app will move to Online.

## Try the demo actions

In ShowTrak, right-click the integrated client and run:

- Set Box Red
- Set Box Green
- Set Box Blue

You should see the box color change in the app.

## Notes

- This app is intentionally small.
- It is meant as a reference/demo, not a full production client.

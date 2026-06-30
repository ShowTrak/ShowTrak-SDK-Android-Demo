# ShowTrak Android Demo

This project is a simple demo app for the ShowTrak Android SDK.

It connects to a ShowTrak Server and registers three test actions:

- Set Box Red
- Set Box Green
- Set Box Blue

When one of those actions is triggered from ShowTrak, the color box in the app updates.

## What is in this repo

- `app/`: the demo Android app (`io.showtrak.sample`)

SDK dependency:
- `io.github.showtrak:showtrak-sdk:1.0.2`

SDK repository:
- https://github.com/ShowTrak/ShowTrak-SDK-Android

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

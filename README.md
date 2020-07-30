# React Native Snackbar

Material Design "Snackbar" component for Android and iOS.
Supports custom colors, fonts, background, position and languages.

Snackbars are used for displaying a brief message to the user, along with an optional action.
They animate up from the bottom of the screen and then disappear shortly afterward.

See Google's [Material Design guidelines](https://material.io/guidelines/components/snackbars-toasts.html) for more info on Snackbars
and when to use them.

## How it works

```js
Snackbar.show({
  text: "Hello world",
  duration: Snackbar.LENGTH_SHORT,
});
```

to include an action button:

```js
Snackbar.show({
  text: "Hello world",
  duration: Snackbar.LENGTH_INDEFINITE,
  action: {
    text: "UNDO",
    textColor: "green",
    onPress: () => {
      /* Do something. */
    },
  },
});
```

maybe you need change the position:

```js
Snackbar.show({
  text: "Hello world",
  duration: Snackbar.LENGTH_INDEFINITE,
  position: Snackbar.POSITION_TOP,
});
```

Also you can set a image as background:

Valid formats: jpg, png, svg

```js
Snackbar.show({
  text: "Hello world",
  duration: Snackbar.LENGTH_INDEFINITE,
  backgroundImage: "your image location",
});
```

## Installation

1. Install:

   - Using [npm](https://www.npmjs.com/#getting-started): `npm install rn-snackbak --save`
   - Using [Yarn](https://yarnpkg.com/): `yarn add rn-snackbak`

2. [Link](https://facebook.github.io/react-native/docs/linking-libraries-ios.html):

   - RN >= 0.60 supports [autolinking](https://github.com/react-native-community/cli/blob/master/docs/autolinking.md): first `cd ios && pod install && cd ..`
   - RN < 0.60: `react-native link rn-snackbak`
   - Or if that fails, link manually using [these steps](https://github.com/cooperka/rn-snackbak/wiki/Manual-Installation)
   - Note that because this is a native module, Expo does not support it -- to use with Expo you need to [eject to ExpoKit](https://docs.expo.io/versions/latest/expokit/eject/)

3. Import it in your JS:

   ```js
   import Snackbar from "rn-snackbak";
   ```

## Usage

### Snackbar.show(options)

Shows a Snackbar, dismissing any existing Snackbar first. Accepts an object with the following options:

| Key               | Data type                  | Default value?                                               | Description                                                                                                                                                                                                                                               |
| ----------------- | -------------------------- | ------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `text`            | `string`                   | Required.                                                    | The message to show.                                                                                                                                                                                                                                      |
| `duration`        | See below                  | `Snackbar.LENGTH_SHORT`                                      | How long to display the Snackbar.                                                                                                                                                                                                                         |
| `position`        | See below                  | `Snackbar.POSITION_TOP` Where you want display the Snackbar. |
| `textColor`       | `string` or `style`        | `'white'`                                                    | The color of the message text.                                                                                                                                                                                                                            |
| `backgroundColor` | `string` or `style`        | `undefined` (dark gray)                                      | The background color for the whole Snackbar.                                                                                                                                                                                                              |
| `backgroundImage` | `image location`           | `undefined` (without image)                                  | The background image, (this replace the color) for the whole Snackbar.                                                                                                                                                                                    |
| `fontFamily`      | `string`                   | `undefined`                                                  | [Android only] The basename of a `.ttf` font from `assets/fonts/` (see [setup guide](https://github.com/facebook/react-native/issues/25852) and [example app](/example), remember to `react-native link` after).                                          |
| `rtl`             | `boolean`                  | `false`                                                      | [Android only, API 17+] Whether the Snackbar should render right-to-left (requires `android:supportsRtl="true"`, see [setup guide](https://android-developers.googleblog.com/2013/03/native-rtl-support-in-android-42.html) and [example app](/example)). |
| `action`          | `object` (described below) | `undefined` (no button)                                      | Optional config for the action button (described below).                                                                                                                                                                                                  |

Where `duration` can be one of the following (timing may vary based on device):

- `Snackbar.LENGTH_SHORT` (just over a second)
- `Snackbar.LENGTH_LONG` (about three seconds)
- `Snackbar.LENGTH_INDEFINITE` (stays on screen until dismissed, replaced, or action button is tapped)

Where `position` can be one of the following (timing may vary based on device):

- `Snackbar.POSITION_TOP` (Display snackbar on top)
- `Snackbar.POSITION_BOTTOM` (Display snackbar on bottom)

Where `backgroundImage` can be one of the following formats:

- `JPG`
- `PNG`
- `SVG`

Note: the `text` will ellipsize after 2 lines of text on most platforms. See [#110](https://github.com/cooperka/rn-snackbak/issues/110) if you need to display more lines.

The optional `action` object can contain the following options:

| Key         | Data type           | Default value?                             | Description                                   |
| ----------- | ------------------- | ------------------------------------------ | --------------------------------------------- |
| `text`      | `string`            | Required.                                  | The button text.                              |
| `textColor` | `string` or `style` | `'white'`                                  | The color of the button text.                 |
| `onPress`   | `function`          | `undefined` (Snackbar is simply dismissed) | A callback for when the user taps the button. |

Deprecation note: The old keys `title` and `color` have been replaced by `text` and `textColor` for consistency.
The old keys will continue to work for now but are deprecated and may be removed at any time.

### Snackbar.dismiss()

Dismisses any existing Snackbars.

## Troubleshooting

#### Compiling [Android]

If you have issues compiling for Android after linking this library,
please try updating your Gradle and Android configs to the latest versions. For example:

In your `android/build.gradle`:

- `com.android.tools.build:gradle:3.4.1` (or higher)

In your `android/app/build.gradle`:

- `compileSdkVersion 28` (or higher)
- `buildToolsVersion "28.0.3"` (or higher)

#### Compiling [iOS]

Make sure your Deployment Target is iOS 9.0 or above.

---

Integrating React Native with existing Android Apps efficiently
I have create a simple demo project from Android Studio to show how to integrate React Native with Auto-Linking enabled to support platform-specific (native) code.
Features of this setup,
This setup is very helpful when you are working with big mobile team having Native Android Modules, React Native and Flutter all in same App.
Even you can add multiple React Native Bundle for different different projects support in the same single app.
Runtime also We can download the bundle in Native side and load the React Native Bundle from it's downloaded path.



---

1. Set up directory structure
To ensure a smooth experience, create a new folder for your integrated React Native project, then copy your existing Android project to an /android subfolder.
2. Create Package.json
Go to the root directory for your project and create a new package.json file with the following contents:
{
  "name": "MyReactNativeApp",
  "version": "0.0.1",
  "private": true,
  "scripts": {
    "start": "yarn react-native start"
  }
}
3. Install JavaScript dependencies
Install the react and react-native packages. Open a terminal or command prompt, then navigate to the directory with your package.json file and run:
Run below command 
$ yarn add react-native
This will print a message similar to the following (scroll up in the yarn output to see it):
warning "react-native@0.52.2" has unmet peer dependency "react@16.2.0".
This is OK, it means we also need to install React:
$ yarn add react@version_printed_above
Choose a version of react from list and press Enter to install the choose version.
Yarn has created a new /node_modules folder. This folder stores all the JavaScript dependencies required to build your project.
It should look like below
Add node_modules/ to your .gitignore file.
4. Adding React Native to your app
Configuring maven
Add the React Native dependency to your app's build.gradle file:
dependencies {
    ...
    //React Native Dependencies Setup Here...
    implementation "com.facebook.react:react-native:+"

}
If you want to ensure that you are always using a specific React Native version in your native build, replace + with an actual React Native version you've downloaded from npm or yarn.
Add an entry for the local React Native maven directory to project level build.gradle. Be sure to add it to the "allprojects" block, above other maven repositories:
allprojects {
    repositories {
       ...
        maven {
            // All of React Native (JS, Android binaries) is installed from npm
            url "$rootDir/../node_modules/react-native/android"
        }
        ...
    }
    ...
}
5. Configuring Permissions
Next, make sure you have the Internet permission in your AndroidManifest.xml:
<uses-permission android:name="android.permission.INTERNET" />
If you need to access to the DevSettingsActivity add to your AndroidManifest.xml:
<activity android:name="com.facebook.react.devsupport.DevSettingsActivity" />
This is only used in dev mode when reloading JavaScript from the development server, so you can strip this in release builds if you need to.
6. Cleartext Traffic (API level 28+)
Starting with Android 9 (API level 28), cleartext traffic is disabled by default; this prevents your application from connecting to the Metro bundler. The changes below allow cleartext traffic in debug builds.
Apply the usesCleartextTraffic option to your Debug AndroidManifest.xml
<!-- ... -->
<application
  android:usesCleartextTraffic="true" tools:targetApi="28" >
  <!-- ... -->
</application>
<!-- ... -->
7. Code integration
Now we will actually modify the native Android application to integrate React Native.
The React Native component
The first bit of code we will write is the actual React Native code for the new "HelloWorld" screen that will be integrated into our application.
1. Create a index.js file. This file will be the entry point for React Native App.
import React from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';
class HelloWorld extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.hello}>Hello, World from React Native</Text>
      </View>
    );
  }
}
var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center'
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10
  }
});
AppRegistry.registerComponent(
  'MyReactNativeApp',
  () => HelloWorld
);


2. Now the structure should look like below,
8. Configuration to start the React Native runtime
Let's add some native code in order to start the React Native runtime and tell it to render your JS component. 
To do this, we're going to create an Application Class named as MainApplication

package com.soumya.sethy.reactnativesetupandroid;

import android.app.Application;
import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {

    private final ReactNativeHost mReactNativeHost =
            new ReactNativeHost(this) {
                @Override
                public boolean getUseDeveloperSupport() {
                    return BuildConfig.DEBUG;
                }

                @Override
                protected List<ReactPackage> getPackages() {
                    @SuppressWarnings("UnnecessaryLocalVariable")
                    List<ReactPackage> packages = new PackageList(this).getPackages();
                    // Packages that cannot be autolinked yet can be added manually here, for example:
                    // packages.add(new MyReactNativePackage());
                    return packages;
                }

                @Override
                protected String getJSMainModuleName() {
                    return "index";
                }

                @Override
                protected String getJSBundleFile() {
                    return "assets://index.android.bundle";
                }
            };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, false);
    }

}
2. To solve Cannot resolve symbol 'PackageList' add below code to your app's build.gradle at bottom of the file :
//(MainApplication) Manual ReactPackage Setup here...
 apply from: file("../../node_modules/@react-native-community/cli-platform-android/native_modules.gradle");
 applyNativeModulesAppBuildGradle(project)
3. After adding above code do sync now. Now it should solve 'PackageList' issue.
4. Now, we're going to create an Activity that starts a React application inside it and sets it as the main content view.
Let's name it as ReactNativeActivity
package com.soumya.sethy.reactnativesetupandroid;

import com.facebook.react.ReactActivity;

public class ReactNativeActivity extends ReactActivity {
    @Override
    protected String getMainComponentName() {
        return "MyReactNativeApp";
    }
}
5. Add ReactNativeActivity and MainApplication to your AndroidManifest.
Now AndroidManifest should look like below,
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.soumya.sethy.reactnativesetupandroid">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:name=".MainApplication"
        android:usesCleartextTraffic="true"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity android:name="com.facebook.react.devsupport.DevSettingsActivity" />
        <activity
            android:name=".ReactNativeActivity"
            android:label="@string/app_name"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar"
            android:configChanges="keyboard|keyboardHidden|orientation|screenSize"
            android:windowSoftInputMode="adjustResize" />
    </application>

</manifest>
9. Navigating to React Native Activity
1. Add Button in (eg. activity_main.xml file)
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Open React Native Page"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView" />


</androidx.constraintlayout.widget.ConstraintLayout>
2. Add Button OnClickListener (eg. MainActivity)
let's create a function which navigates to ReactNativeActivity and bind it to onClick event of Button.
package com.soumya.sethy.reactnativesetupandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRNPage();
            }
        });
    }

    private void startRNPage() {
        Intent intent = new Intent(this, ReactNativeActivity.class);
        startActivity(intent);
        finish();
    }
}
10. Hermes / JavaScriptCore(JSC) Setup
Hermes is an open-source JavaScript engine optimized for running React Native apps on Android. For many apps, enabling Hermes will result in improved start-up time, decreased memory usage, and smaller app size. At this time Hermes is an opt-in React Native feature, and this guide explains how to enable it.
 React Native team has to rely on an engine to interpret it so that it can run in a native mobile application. In the current architecture, the team chose to use JavaScriptCore (JSC) directly.
Note: Here I have enabled Hermes for release build and JSC for debug build.
Read more about Hermes here and JSC here
Alternatively We can use V8 as well. You can do full research on React Native Memory profiling (JSC vs V8 vs Hermes). Read more here


add below code to project level build.gradle file:

maven {
   url("$rootDir/../node_modules/jsc-android/dist")
}
Now the project level build.gradle file should be like below,
2. Add below code to app level build.gradle file:
/**************** Hermes Setup ********************/
project.ext.react = [
       enableHermes: false
]
def jscFlavor = 'org.webkit:android-jsc:+'
def enableHermes = project.ext.react.get("enableHermes", false);
/**************** End of Hermes Setup***************/
and
dependencies {
    ...

    //Hermes Dependencies Setup here...
   if (enableHermes) {
       def hermesPath = "../../node_modules/hermes-engine/android/";
       debugImplementation files(hermesPath + "hermes-debug.aar")
       releaseImplementation files(hermesPath + "hermes-release.aar")
   } else {
       implementation jscFlavor
   }
}
Now the app level build.gradle file should be like below,
11. Support for Auto-Linking
React Native libraries often come with platform-specific (native) code. 
Auto-Linking is a mechanism that allows your project to discover and use this code.
# install example
$ yarn add @react-native-community/async-storage

# run
yarn react-native run-android


That's it. No more editing build config files to use native code.
Auto-Linking is a replacement for react-native link. If you have been using React Native before version 0.60, please unlink native dependencies if you have any from a previous install.
To support Auto-Linking add below code to settings.gradle
//auto-linking after React native 0.60+
apply from: file("../node_modules/@react-native-community/cli-platform-android/native_modules.gradle");
applyNativeModulesSettingsGradle(settings)
//End of auto-linking after React native 0.60+
12. Test your integration
You have now done all the basic steps to integrate React Native with your current application. Now we will start the Metro bundler to build the index.bundle package and the server running on localhost to serve it.
1. Run the packager
To run your app, you need to first start the development server. To do this, run the following command in the root directory of your React Native project:
//running start script of package.json
$ yarn start
//or else run the full script
$ yarn react-native start
Now in terminal metro builder should start running.
2. Now to run Android App, Open new tab in terminal and run the following command in the root directory of your React Native project:
$ npx react-native run-android
We can also use Android Studio to run Android App directly too




Now When we click on Open React Native Page, Metro builder should starts bundling like below,




---

I have a sample Android host project here, that you can use to test the above steps.
soumyasethy/react-native-setup-android
Contribute to soumyasethy/react-native-setup-android development by creating an account on GitHub.github.com
My articles are free, but you know you can press the clap👏 button 50 times? The higher you go, the more it motivates me to write more stuff for you!
if you like this blog and follow me to get notification of my future blogs.
Feeling super happy? Buy me a coffee. 😋
Hello World, I am Soumya Sethy. I design and develop scalable products for early stage startups. You can find me on Linkedin or stalk me on GitHub. 
Have a nice reactive day!

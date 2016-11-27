# android-shortcut-gradle-plugin
Android Gradle plugin generates App Shortcuts shortcuts.xml for build flavors with different applicationId.
## When it useful?
If you look at the official documentation of static [App Shortcuts](https://developer.android.com/preview/shortcuts.html), you'll see that for each intent you need to set the **android:targetPackage**. But what if you have different applicationId in different flavors? 
```gradle
android {
    defaultConfig {
        applicationId "ru.solodovnikov.shortcutgradlehelper"
    }
    productFlavors {
        dev {
            applicationIdSuffix "dev"
        }
        prod {
            applicationId "ru.solodovnikov.shortcut"
        }
    }
}
```
In this example you'll need copy your shortcuts.xml in two folder **dev** and **prod** with single difference. And if you create a new flavor with different applicationId you need to copy it again. Since at this moment we do not have any built-in merge tools applicationId from build.gradle to the shortcuts.xml this gradle plugin will be useful.
## Example
1. Create shortcuts.xml (or name it how you want) and put it in your project. **Do not** set any android:targetPackage.

  ```xml
  <shortcuts xmlns:android="http://schemas.android.com/apk/res/android">
      <shortcut android:shortcutId="test1" android:enabled="true" android:icon="@mipmap/ic_launcher"
          android:shortcutShortLabel="@string/app_name" android:shortcutLongLabel="@string/app_name"
          android:shortcutDisabledMessage="@string/app_name">
          <intent android:action="android.intent.action.VIEW"
              android:targetClass="ru.solodovnikov.shortcuthelper.MainActivity" />
          <intent android:action="android.intent.action.VIEW"
              android:targetClass="ru.solodovnikov.shortcuthelper.SecondActivity" />
          <categories android:name="android.shortcut.conversation" />
      </shortcut>
  </shortcuts>
  ```
  
2. Apply **android-shortcut-gradle-plugin**.

  ```gradle
buildscript {
      repositories {
          jcenter()
      }
      dependencies {
          classpath 'com.github.zellius:android-shortcut-gradle-plugin:0.1.0'
      }
}

apply plugin: 'com.android.application'
apply plugin: 'com.github.zellius.shortcut-helper'
    ```
3. Set path to your shortcuts.xml.

  ```gradle
   shortcutHelper.filePath = '../shortcuts.xml'
   ```
4. Set shortcuts.xml in your manifest. 

   ```xml
    <meta-data android:name="android.app.shortcuts"
               android:resource="@xml/shortcuts" />
    ```
Android Studio can mark @xml/shortcuts as "cannot resolve"...but it is ok :)
5. PROFIT!!!!

You can try an example from the repository.

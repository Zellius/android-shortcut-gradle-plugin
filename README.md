# android-shortcut-gradle-plugin
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-android--shortcut--gradle--plugin-blue.svg?style=flat)](https://android-arsenal.com/details/1/6373)

Android Gradle plugin generates App Shortcuts shortcuts.xml for build flavors with different applicationId.
## When it useful?
If you look at the official documentation of static [App Shortcuts](https://developer.android.com/preview/shortcuts.html), you'll see that for each intent you need to set the **android:targetPackage**. But, what if you have different applicationId in different flavors? 
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
In this example, you'll need copy your shortcuts.xml in two folder **dev** and **prod** with single difference. And if you create a new flavor with different applicationId you need to copy it again. Since at this moment we do not have any built-in merge tools applicationId from build.gradle to the shortcuts.xml this gradle plugin will be useful.
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
      classpath 'com.github.zellius:android-shortcut-gradle-plugin:0.1.2'
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

## License

```
The MIT License (MIT)

Copyright (c) 2017 Sergey Solodovnikov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
# AppJoint

**FORK FROM [PrototypeZ/AppJoint](https://github.com/PrototypeZ/AppJoint/)**

Thanks for his brilliant idea and initiate implementation.

---

[![Download](https://jitpack.io/v/alwaystest/AppJoint.svg)](https://jitpack.io/#alwaystest/AppJoint)

![Logo](https://rawcdn.githack.com/PrototypeZ/AppJoint/master/app-joint-logo.png)

[中文文档](https://github.com/alwaystest/AppJoint/blob/master/README_zh.md)

Simple tool to make your multi-module Android development easier!

Only **3** annotations and **1** function call are included.

## Getting started

1. Add JitPack Repo in buildScript block

```groovy
buildScript {
    repositories {
        mavenCentral()
        maven { url "https://jitpack.io" }
    }
}
```

2. Add the **AppJoint** plugin dependency to the `build.gradle` file in project root:

```groovy
buildscript {
    ...
    dependencies {
        ...
        classpath 'com.github.alwaystest.AppJoint:app-joint:{latest_version}'
    }
}
```

3. Add the **AppJoint** dependency to every module：

```groovy
dependencies {
    ...
    implementation "com.github.alwaystest.AppJoint:app-joint-core:{latest_version}"
}
```

> Currently the latest version is： [![Download](https://jitpack.io/v/alwaystest/AppJoint.svg)](https://jitpack.io/#alwaystest/AppJoint)

4. Apply the **AppJoint** plugin to your main app module：

```groovy
apply plugin: 'com.android.application'
apply plugin: 'app-joint'
```

## Cross module method invocation

Assuming the `router` module is a common module that all the other modules depend on it. Then we can define interfaces that each module wish to provide for other modules to use in the `router` module. For example, the `module1` module provides the `Module1Service` interface for other modules to use:

```kotlin
interface Module1Service {

  /**
   * start Activity from moduel1
   */
  fun startActivityOfModule1(context: Context)

  /**
   * get Fragment from module1
   */
  fun obtainFragmentOfModule1(): Fragment

  /**
   * call synchronous method from module1
   */
  fun callMethodSyncOfModule1(): String

  /**
   * call asynchronous method from module1
   */
  fun callMethodAsyncOfModule1(callback: Module1Callback<Module1Entity>)

  /**
   * get RxJava Observable from module1
   */
  fun observableOfModule1(): Observable<Module1Entity>
}
```

Then we write an implementation of it inside the `module1` module：

```kotlin
@ServiceProvider
class Module1ServiceImpl : Module1Service {
  override fun startActivityOfModule1(context: Context) {
    Module1Activity.start(context)
  }

  override fun obtainFragmentOfModule1(): Fragment {
    return Module1Fragment.newInstance()
  }

  override fun callMethodSyncOfModule1(): String {
    return "syncMethodResultModule1"
  }

  override fun callMethodAsyncOfModule1(callback: Module1Callback<Module1Entity>) {
    Thread { callback.onResult(Module1Entity("asyncMethodResultModule1")) }.start()
  }

  override fun observableOfModule1(): Observable<Module1Entity> {
    return Observable.just(Module1Entity("rxJavaResultModule1"))
  }
}
```

Note that we add a `@ServiceProvider` annotation on the class。

Now, we can get the instance of `Module1Service` anywhere in any module，as long as we write the codes below：

```java
Module1Service service = AppJoint.service(Module1Service.class);
```

## Merge custom `Application` logic of modules

You can create a custom `Application` class for each module in order to run the module standalone, for example：

```kotlin

// it supports initializing module applications by priority,
// if priorities are not defined, they are initialized with an unknown sequence
@ModuleSpec( priority = 1)
class Module1Application : Application() {

  override fun onCreate() {
    super.onCreate()
    // do module1 initialization
    Log.i("module1", "module1 init is called")
  }
}
```

Note that the custom `Application` class is annotated with the `@ModuleSpec` annotation.

Then add the `@AppSpec` annotation to the custom `Application` class of your main application module.

```kotlin
@AppSpec
class App : Application() {
  override fun onCreate() {
    super.onCreate()
    Log.i("app", "app init is called")
  }
}
```

**AppJoint** can ensure that, when the lifecycle methods(such as `onCreate`, `attachBaseContext`) of the class annotated with `@AppSpec` are called, the same lifecycle methods of the class annoatated with `@ModuleSpec` will be called, too.

## Multi-implementation for cross module interfaces

We can name implementations for an specific interface to provide more implementations for this interface.

```kotlin
@ServiceProvider("anotherImpl")
class Module1ServiceAnotherImpl : Module1Service {
  ...
}
```

Then we only need to provide the name of an implementation to get proper implementation of the interface:

```kotlin
Module1Service service = AppJoint.service(Module1Service.class, "anotherImpl");
```

## FAQ

+ Q: Does AppJoint support Instant Run?

  A: Yes，it's based on Transform API, so no problem.

+ Q: Do I need to add any Proguard rules for release?

  A: No, there's no reflection, so you are safe to use Proguard.

## TroubleShooting

+ Error in compile time, `AppJoint class file not found, please check "com.github.alwaystest.AppJoint:app-joint-core:{latest_version}" is in your dependency graph.`

  Solution: First, make sure `"com.github.alwaystest.AppJoint:app-joint-core:{latest_version}"` can be visited in your main app module. Second, make sure line `apply plugin: 'app-joint'` is right below `apply plugin: 'com.android.application'` before other plugins.

## LICENSE

    Copyright (c) 2016-present, AppJoint Contributors.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

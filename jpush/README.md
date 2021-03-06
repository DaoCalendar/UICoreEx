# jpush
2020-6-23

```kotlin
JPush.init(content)
```

极光推送 **需要绑定应用包名**

# Android SDK 集成指南
https://docs.jiguang.cn/jpush/client/Android/android_guide/

说明：若没有 res/drawable-xxxx/jpush_notification_icon 这个资源默认使用应用图标作为通知 icon，
在 5.0 以上系统将应用图标作为 statusbar icon 可能显示不正常，
用户可定义没有阴影和渐变色的 icon 替换这个文件，文件名不要变。

# 错误码说明

https://docs.jiguang.cn/jpush/client/Android/android_api/#_153

# 使用

1.配置[jpush_key]

2.调用初始化

```kotlin
com.angcyo.jpush.JPush.init
```

3.配置脚本

```groovy
//需要在app工程也是用这个脚本
apply from: allGradle.jpush
```

4.通过[JPushModel]监听消息

```kotlin
vmCore<JPushModel>()
```
# cine  :movie_camera: 
[![](https://jitpack.io/v/Daio-io/cine.svg)](https://jitpack.io/#Daio-io/cine)

## What?

Cine is a light weight library (or trick) to enable rich view notifications currently not supported by RemoteViews. e.g. video playback.

## Why?

There was a desire to get video into notifications and instead of being jealous of iOS I wrote this. Maybe in the future Android will support this, but right now, there is Cine.
Although Cine's main use case is for video, it does not provide a video player. The aim was to be light weight and allow developers to provide and attach their own embedded video player to the Cine container. This is because video playback is usually fairly custom. Things like DRM and other highly optimised an custom playback is not something for Cine to care about. The aim of Cine is to get any rich content in a notification(ish). 

## How?

Since RemoteViews do not support a lot of layout types and its fairly limited when you want to display
something like video this library mimics the look and feel of a notification to allow developers to provide this richer
content to users in a digestable notification style.

The library simply provides an abstract Activity with a container to attach your rich notification content.

#### Adding Library

To add the library fist add JitPack as a repo to your main `build.gradle`.

```
    repositories {
        maven { url 'https://jitpack.io' }
    }
```

Then in your app `build.gradle` add cine as a dependency

`implementation 'com.github.Daio-io:cine:v0.1.0'`

#### Implementing

Create an activity extending from `CineNotificationActivity<Parcelable>` and override the 
two abstract methods `onReady` and `onDismiss`.

```CineNotificationActivity``` Set your data class or pojo as the generic type when extending the activity.
This will be need to be a Parcelable type and will be 
sent to the activity intent so that it is available during the `onReady`call.

e.g. 
data class or pojo

```kotlin
@Parcelize
  data class MyVideoDataItem(val title: String,
                       val url: String) : Parcelable

```
Extend the activity
```kotlin
class MyVideoViewNotificationActivity : CineNotificationActivity<MyVideoDataItem>() {

    override fun onReady(container: ViewGroup, content: VideoItem) {
    }


    override fun onDismiss() {
        finish()
    }
}
```

After you have created your activity make sure you register it in your `AndroidManifest.xml`
```xml
<activity
          android:name=".path.to.my.MyVideoViewNotificationActivity"
          android:configChanges="orientation|screenSize"
          android:excludeFromRecents="true"
          android:launchMode="singleTask"
          android:theme="@style/Theme.Transparent" />
```
            
Make sure you use the `android:theme="@style/Theme.Transparent"`. 
This will ensure the background is transparent and the view looks like a notification.

The `onReady` call will deliver your data class and the container 
once it has wired up and is ready for you to attach your view to the container.

`onDismiss` will be called when the activity is being destroyed or has been swiped away.
Use this to do any clean up. 



You can also override a few more method to customise as you please:
```kotlin
        setNotificationBodyText(content.title)
        setAppName("My App")
        setAppIcon(R.drawable.abc_ic_star_black_36dp)
        setButtons(CineButton("OK", {
            finish()
        }), CineButton("Close", {
            finish()
        }))
}
```

You can also override any of the usual activity methods if you want to do anything more custom.

See a full example [here](https://github.com/Daio-io/cine/blob/master/app/src/main/java/io/daio/richnotificationssample/VideoViewNotificationActivity.kt) 

#### Usage

In order to display your notification activity you need to apply a content pending intent
to your normal Notification content intent. 
The same way you would launch your app from a notification.

First use the `createNotificationIntent` helper function to create an intent with the parcelable 
data class and your `CineNotificationActivity` you created earlier. Like so:
```kotlin
val videoItem = MyVideoDataItem("My Video", "https://myvideo.mp4")
val intent = createNotificationIntent(this, videoItem, MyVideoViewNotificationActivity::class.java)
```
And simply create a PendingIntent, add to the notification builder and display as normal.

```kotlin
        val resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, "default")
                .setContentIntent(resultPendingIntent)
```   
You should now be able to launch your CineNotification from your usual notifications.

See a full example in the [sample app](https://github.com/Daio-io/cine/blob/master/app)

# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#---------------------------------基本指令以及一些固定不混淆的代码--开始--------------------------------

#<基本指令>
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#忽略警告
-ignorewarnings
#记录生成的日志数据,gradle build时在本项目根目录输出apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
#</基本指令>
#<基础>
-keep public class * extends android.app.Activity
-keep public class * extends androidx.appcompat.app.AppCompatActivity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class * extends android.view.LayoutInflater
-keep public class * extends android.view.LayoutInflater
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * implements androidx.viewbinding.ViewBinding
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-keep class *.*.*.databinding.*{*;}
-keep class *.*.*.bean.**{*;}
#</基础>
#<view相关>
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * {
   public void *(android.view.View);
}
#</view相关>
#<Serializable、Parcelable>
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep public class * implements java.io.Serializable {*;}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#</Serializable、Parcelable>
#<R文件>
-keep class **.R$* {
 *;
}
#</R文件>
#<enum>
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#</enum>
#<natvie>
-keepclasseswithmembernames class * {
    native <methods>;
}
#</natvie>
#---------------------------------基本指令以及一些固定不混淆的代码--结束-----------
#---------------------------------第三方包--开始-------------------------------
#<okhttp3.x>
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
#</okhttp3.x>
#<retrofit2.x>
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**
#</retrofit2.x>
#</okhttp3.x>
#<ButterKnife 7.0 以上>
 -keep class butterknife.** { *; }
 -dontwarn butterknife.internal.**
 -keep class **$$ViewBinder { *; }
 -keepclasseswithmembernames class * {
  @butterknife.* <fields>;
 }
 -keepclasseswithmembernames class * {
 @butterknife.* <methods>;
 }
#</ButterKnife 7.0 以上>
#<eventbus 3.0>
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#</eventbus 3.0>
#<Gson>
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
#</Gson>
#<glide>
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#</glide>
#<Rxjava RxAndroid>
-dontwarn rx.*
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
#</Rxjava RxAndroid>
#----------------------------------第三方包--结束--------------------------
-keep class com.aleyn.mvvm.**{*;}
#---------------------------------一些不要混淆的代码--开始-------------------
-keep class net.arvin.afbaselibrary.nets.** { *; }
-keep class net.arvin.afbaselibrary.data.** { *; }
#<反射>
#</反射>
#<js>
#</js>
#<自定义View的类>
-keep class net.arvin.afbaselibrary.ui.views.** {*;}
#</自定义View的类>
#---------------------------------一些不要混淆的代码--结束-------------------
-keep class com.example.bean.**{*;}
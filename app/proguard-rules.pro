# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/versions/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keepattributes *Annotation*

## CRASHLYTICS
-keepattributes SourceFile,LineNumberTable
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

## RETROFIT
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
-keep class com.gershad.gershad.model.** { *; }
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**


## PARCELAR
-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }
-keep class org.parceler.Parceler$$Parcels

## AMAZON
# Class names are needed in reflection
-keepnames class com.amazonaws.**
-keepnames class com.amazon.**
# Request handlers defined in request.handlers
-keep class com.amazonaws.services.**.*Handler
# The following are referenced but aren't required to run
-dontwarn com.fasterxml.jackson.**
-dontwarn org.apache.commons.logging.**
# Android 6.0 release removes support for the Apache HTTP client
-dontwarn org.apache.http.**
# The SDK has several references of Apache HTTP client
-dontwarn com.amazonaws.http.**
-dontwarn com.amazonaws.metrics.**
-keep class org.apache.log.** { *; }
-dontwarn org.apache.log.**
-keep class org.apache.log4j.** { *; }
-dontwarn org.apache.log4j.**
-keep class org.apache.avalon.** { *; }
-dontwarn org.apache.avalon.**
-keep class javax.servlet.** { *; }
-dontwarn javax.servlet.**

#TEST
-dontwarn org.junit.**
-dontwarn android.test.**
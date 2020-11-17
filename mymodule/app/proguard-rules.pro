
#/////MVP模式//////
-keep public class * extends com.uweic.lib_common.mvp.MvpModel { *; }
-keep public class * extends com.uweic.lib_common.mvp.MvpPresenter { *; }
#/////MVP模式//////

#ToastUtils https://github.com/getActivity/ToastUtils
-keep class com.hjq.toast.** {*;}

#LoadSir https://github.com/KingJA/LoadSir
-dontwarn com.kingja.loadsir.**
-keep class com.kingja.loadsir.** {*;}

#lottie  没有说要添加混淆 https://github.com/airbnb/lottie-android

#SmartRefreshLayout 不需要添加混淆过滤代码  https://github.com/scwang90/SmartRefreshLayout

#BaseRecyclerViewAdapterHelper此资源库自带混淆规则

#permissionx 不需要添加混淆过滤代码   https://github.com/guolindev/PermissionX

 # immersionbar框架 https://github.com/gyf-dev/ImmersionBar
 -keep class com.gyf.immersionbar.* {*;}
 -dontwarn com.gyf.immersionbar.**


 #########################rxeasyhttp https://github.com/zhou-you/RxEasyHttp #######
 #okhttp
 -dontwarn com.squareup.okhttp3.**
 -keep class com.squareup.okhttp3.** { *;}
 -dontwarn okio.**

 # Retrofit
 -dontwarn retrofit2.**
 -keep class retrofit2.** { *; }
 -keepattributes Exceptions

 # Retrolambda
 -dontwarn java.lang.invoke.*

 # RxJava RxAndroid
 -dontwarn sun.misc.**
 -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
     long producerIndex;
     long consumerIndex;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode producerNode;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode consumerNode;
 }
 ###rxandroid-1.2.1
 -keepclassmembers class rx.android.**{*;}

 # Gson
 -keep class com.google.gson.stream.** { *; }
 -keepattributes EnclosingMethod
 -keep class org.xz_sale.entity.**{*;}
 -keep class com.google.gson.** {*;}
 -keep class com.google.**{*;}
 -keep class sun.misc.Unsafe { *; }
 -keep class com.google.gson.stream.** { *; }
 -keep class com.google.gson.examples.android.model.** { *; }

 #RxEasyHttp
 -keep class com.zhouyou.http.model.** {*;}
 -keep class com.zhouyou.http.cache.model.** {*;}
 -keep class com.zhouyou.http.cache.stategy.**{*;}
 #########################rxeasyhttp https://github.com/zhou-you/RxEasyHttp #######
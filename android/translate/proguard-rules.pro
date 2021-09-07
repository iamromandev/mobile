#debugging and stack trace
#-repackageclasses
-keepattributes SourceFile, LineNumberTable
-keepattributes *Annotation*, Signature, Exception
#-optimizations !method/removal/parameter
-ignorewarnings
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#-renamesourcefileattribute SourceFile

-keep class com.dreampany.translate.data.model.** { *; }
-keepclassmembers class com.dreampany.translate.data.model.** { *; }

-keep class com.dreampany.translate.ui.model.** { *; }
-keepclassmembers class com.dreampany.translate.ui.model.** { *; }

-keep class com.dreampany.translate.misc.** { *; }
-keepclassmembers class com.dreampany.translate.misc.** { *; }

-keep class com.dreampany.frame.data.model.** { *; }
-keepclassmembers class com.dreampany.frame.data.model.** { *; }

-keep class com.dreampany.frame.ui.model.** { *; }
-keepclassmembers class com.dreampany.frame.ui.model.** { *; }

-keep class com.dreampany.frame.misc.** { *; }
-keepclassmembers class com.dreampany.frame.misc.** { *; }
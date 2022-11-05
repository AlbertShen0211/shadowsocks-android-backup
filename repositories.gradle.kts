rootProject.extra.apply {
    set("androidPlugin", "com.android.tools.build:gradle:7.2.1")
    set("kotlinVersion", "1.7.10")
}

repositories {
    google()
    jcenter()
    mavenCentral()
//    maven { url 'https://maven.aliyun.com/repository/google' }
//    maven { url 'https://maven.aliyun.com/repository/public' }
//    maven { url 'https://maven.aliyun.com/repository/central' }
//    maven { url 'https://maven.aliyun.com/repository/gradle-plugin' }
}

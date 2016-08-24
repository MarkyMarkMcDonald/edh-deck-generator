buildscript {
    repositories {
        gradleScriptKotlin()
        mavenCentral()
    }

    dependencies {
        classpath(kotlinModule("gradle-plugin"))
    }
}

apply {
    plugin("kotlin")
    plugin<ApplicationPlugin>()
}

configure<ApplicationPluginConvention> {
    mainClassName = "Driver"
}

repositories {
    gradleScriptKotlin()
    jcenter()
}

dependencies {
    compile(kotlinModule("stdlib"))
    compile("com.github.salomonbrys.kotson:kotson:2.1.0")
    compile("org.jsoup:jsoup:1.9.1")

    testCompile(kotlinModule("test"))
    testCompile("junit:junit:4.11")
    testCompile("org.amshove.kluent:kluent:1.4")
}

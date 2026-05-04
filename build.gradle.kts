// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    id("com.google.gms.google-services") version "4.4.4" apply false
    id("com.google.devtools.ksp") version "2.2.0-2.0.2" apply false
    /*
     O formato da versão do KSP mudou — o segundo número agora é a versão do KSP em si
    , não mais um sufixo 1.0.x. A versão correta para Kotlin 2.2.0 é 2.2.0-2.0.2,
     que está disponível no Maven Central
     */
}
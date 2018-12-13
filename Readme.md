---
title: 一步步教你搭建一个基于Dagger2+RxJava+Retrofit+MVP的Android框架
tags: Android框架,Dagger2,Retrofit,RxJava
grammar_cjkRuby: true
---
[toc]

## 关于本框架

**[GitHub地址](https://github.com/Dream97/BaseFrameworkDemo.git)**

每次在开始一个新的项目的时候，都要先开始搭建自己的框架，特别是在结合几种框架使用的时候。其中架构的设计和细节的实现总得花上一段时间。所以在我搭建自己的基础框架之后，这篇博客就随之而来了。本框架采用MVP模式进行开发，首先使用Dagger2来实现依赖注入，主要用于网络请求的model注入到presenter和mvp模式中presenter注入view中。在网络请求方面是使用Retrofit网络请求框架，并结合RxJava来异步实现。最后还加上了图片加载库Glide的依赖配置。基本上能实现普通的开发需求。

## 本框架架构分析

首先我们分析一下我们需要一个什么样的框架。

-  在设计模式上我们采用MVP模式进行开发
	[MVP模式使用总结](https://blog.csdn.net/qq_34261214/article/details/80575171)
- 为提供代码的复用性，测试性和维护性采用Dagger2依赖注入框架
	[详解Dagger2](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0519/2892.html)
- 网络请求框架Retrofit，对okHttp3的进一步封装
	[基于OkHttp3的Retrofit使用实践](https://blog.csdn.net/qq_34261214/article/details/80621320)
- 使用RxJava与Retrofit配合使用，更好的异步实现
	[等我写出来](https://blog.csdn.net/qq_34261214)
- Glide强大的图片加载库
	[Glide使用详解](https://www.jianshu.com/p/7ce7b02988a4)
- ButterKnife更快、更简洁的View注入框架
	[ButterKnife使用方法详解](https://blog.csdn.net/zyw0101/article/details/80399225)
	
	
	
在决定完使用的开源库之后，我们需要对它进行依赖配置
 ```groovy
     //dagger
    implementation 'com.google.dagger:dagger:2.19'
    annotationProcessor 'com.google.dagger:dagger-compiler:2.19'
    implementation 'com.google.dagger:dagger-android-support:2.19'
    //rxjava
    implementation 'io.reactivex.rxjava2:rxjava:2.1.3'
    implementation 'io.reactivex:rxandroid:1.1.0'
    implementation 'com.trello.rxlifecycle2:rxlifecycle:2.2.1'
    implementation 'com.trello.rxlifecycle2:rxlifecycle-components:2.2.1'

    //retrofit
    implementation 'com.squareup.retrofit2:retrofit:2.5.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.5.0'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.5.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:3.12.0'
    //butterknife
    implementation 'com.jakewharton:butterknife:8.8.1'
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'
    compileOnly 'org.glassfish:javax.annotation:10.0-b28'
    //Glide
    implementation 'com.github.bumptech.glide:glide:4.8.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.8.0'
 ```
 
再看一下的我们的项目结构图 

![Android开发框架图解](https://img-blog.csdnimg.cn/20181213095715746.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM0MjYxMjE0,size_16,color_FFFFFF,t_70)

看完这些图，我们心里就有个底了，在开发之前，原来我们需要做这么多的工作。
---
title: 一步步教你搭建一个基于Dagger2+RxJava+Retrofit+MVP的Android框架
tags: Android框架,Dagger2,Retrofit,RxJava
grammar_cjkRuby: true
---
[toc]

## 关于本框架
每次在开始一个新的项目的时候，都要先开始搭建自己的框架，特别是在结合几种框架使用的时候。其中架构的设计和细节的实现总得花上一段时间。所以在我搭建自己的基础框架之后，这篇博客就随之而来了。本框架采用MVP模式进行开发，首先使用Dagger2来实现依赖注入，主要用于网络请求的model注入到presenter和mvp模式中presenter注入view中。在网络请求方面是使用Retrofit网络请求框架，并结合RxJava来异步实现。最后还加上了图片加载库Glide的依赖配置。基本上能实现普通的开发需求。
 
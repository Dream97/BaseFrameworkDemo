---
title: 实习周记五
tags: google支付,AIDL
grammar_cjkRuby: true
---
[toc]
 ## 本周知识清单

 - google pay

## google pay

- 更新应用的依赖项。
- 连接到Google Play。
- 查询应用内商品详细信息。
- 允许购买应用内商品。
- 查询购买的商品。
- 添加一次性产品特定或订阅特定代码（在单独的页面上介绍）。

[官方文档](https://developer.android.google.cn/google/play/billing/billing_reference)

### google play Billing 支持的一次性产品
使用google在购买某个商品后,如果没有对该商品进行消费，是不可以再次购买的，需要对已购商品进行消费后才能再次购买

- 非消耗性一次性产品 
>提供永久性效果的产品，不能重新购买，不应表明为已消费

- 可消耗的一次性产品
>提供临时利益，可以回购的产品

### 商品购买流程
![商品购买流程图](https://developer.android.google.cn/images/in-app-billing/v3/iab_v3_purchase_flow.png)

Version 3 API 中的典型购买流程如下所示：

1.您的应用向 Google Play 发送 isBillingSupported 请求，以确定您当前使用的 In-app Billing API 目标版本是否受支持。

2.当您的应用启动或用户登录时，最好向 Google Play 进行查询，确定该用户拥有哪些商品。 要查询用户的应用内购买，请发送 getPurchases 请求。 如果请求成功，Google Play 会返回一个 Bundle，其中包含所购商品的商品 ID 列表、各项购买详情的列表以及购买签名的列表。

3.通常情况下，您需要将可供购买的商品通知用户。 要查询您在 Google Play 中定义的应用内商品的详细信息，应用可以发送 getSkuDetails 请求。 您必须在查询请求中指定商品 ID 列表。如果该请求成功，Google Play 会返回一个包含产品详情（包括商品的价格、标题、说明和购买类型）的 Bundle。

4.如果用户还未拥有某种应用内商品，您可以提示购买。 为了发起购买请求，您的应用会发送 getBuyIntent 请求，指定要购买商品的商品 ID 以及其他参数。 当您在 Developer Console 中创建新的应用内商品时，应记录其商品 ID。

    a.Google Play 返回的 Bundle 中包含 PendingIntent，您的应用可用它来启动购买结账 UI。

    b.您的应用通过调用 startIntentSenderForResult 方法启动待定 Intent。

    c.结账流程结束后（即用户成功购买商品或取消购买），Google Play 会向您的 onActivityResult 方法发送响应 Intent。 onActivityResult 的结果代码中有一个代码将用于表明购买是成功还是已取消。 响应 Intent 中包含所购商品的相关信息，包括 Google Play 为了对此次购买交易进行唯一标识而生成的 purchaseToken 字符串。 Intent 中还包含使用您的开发者私钥签署的购买签名。


### google pay 实现流程

#### 1. 添加依赖
```groovy
dependencies {
    ...
    implementation 'com.android.billingclient:billing:1.2'
}
```

#### 2. 连接到Google Play

创建BillingClient实例并实现监听，开始与google play的连接

```java
// create new Person
private BillingClient mBillingClient;
...
mBillingClient = BillingClient.newBuilder(mActivity).setListener(this).build();
mBillingClient.startConnection(new BillingClientStateListener() {
    @Override
    public void onBillingSetupFinished(@BillingResponse int billingResponseCode) {
        if (billingResponseCode == BillingResponse.OK) {
            // The billing client is ready. You can query purchases here.
        }
    }
    @Override
    public void onBillingServiceDisconnected() {
        // Try to restart the connection on the next request to
        // Google Play by calling the startConnection() method.
    }
});
```

#### 3. 查询商品
同过商品的id来查询对应的商品信息
```java
List skuList = new ArrayList<> ();
skuList.add("xxxxx"); //商品id
skuList.add("xxxxx"); //商品id
SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
params.setSkusList(skuList).setType(SkuType.INAPP);
mBillingClient.querySkuDetailsAsync(params.build(),
    new SkuDetailsResponseListener() {
        @Override
        public void onSkuDetailsResponse(int responseCode, List skuDetailsList) {
            // Process the result.
        }
    });
```

#### 4. 唤起支付页面
在发起支付页面之前，需要通过isFeatureSupported方法检查设备是否支持该商品的销售，可以通过BillingClient.FeatureType查询商品类型
```java
BillingFlowParams flowParams = BillingFlowParams.newBuilder()
         .setSku(skuId)
         .setType(SkuType.INAPP) // SkuType.SUB for subscription
         .build();
int responseCode = mBillingClient.launchBillingFlow(flowParams);
```

#### 5. 回调监听
在建立连接那一步中，通过setListener(this),使当前类实现了回调监听的接口

```java
@Override
void onPurchasesUpdated(@BillingResponse int responseCode, List purchases) {
    if (responseCode == BillingResponse.OK
            && purchases != null) {
        for (Purchase purchase : purchases) {
            handlePurchase(purchase);
        }
    } else if (responseCode == BillingResponse.USER_CANCELED) {
        // Handle an error caused by a user cancelling the purchase flow.
    } else {
        // Handle any other error codes.
    }
}
```

#### 6.查询已购商品
当用户购买商品完成后，可以获取到已购的商品列表
```java
mBillingClient.queryPurchaseHistoryAsync(SkuType.INAPP,
                                         new PurchaseHistoryResponseListener() {
    @Override
    public void onPurchaseHistoryResponse(@BillingResponse int responseCode,
                                          List purchasesList) {
        if (responseCode == BillingResponse.OK
                && purchasesList != null) {
            for (Purchase purchase : purchasesList) {
                // Process the result.
            }
         }
    }
});
```

#### 7. 消费已购商品
在购买完成之后，可以获取到已购商品的purchaseToken，consumeAsync消费已购商品
```java
ConsumeResponseListener listener = new ConsumeResponseListener() {
    @Override
    public void onConsumeResponse(@BillingResponse int responseCode, String outToken) {
            if (responseCode == BillingResponse.OK) {
                // Handle the success of the consume operation.
                // For example, increase the number of coins inside the user&#39;s basket.
    }
};
mBillingClient.consumeAsync(purchaseToken, listener);
```
## 使用AIDL实现Google pay 支付

上面知道，我们可以通过依赖google pay库实现，通过SDK实现支付。除此之外还有另外一种实现方式，也就是通过定义AIDL接口，实现应用间的跨进程通信，从而实现支付

### 什么是AIDL
AIDL，Android接口定义语言，是为了两个不同的应用间通过Serivce进行通信。也就是说google play对外提供远程Service，其他应用可以并发地访问该Service

### 服务端中的AIDL(如：google play应用)

一般来说，AIDL文件放在main/com.xxx.xx/下面，然后然后定义对外提供的方法，但注意，它有自己的语法

- 每个aidl文件只能定义一个接口（单一接口）；
- 默认Java中的基本数据类型都支持，如：int, long, char, boolean；
- 默认支持String和CharSequence；
- 默认支持List（可以选择加泛型，需要引入List所在的包），但是List中存储的数据也只能是Java基本数据类型，而且另外一边（客户端或服务端）接受的是ArrayList类型；
- 默认支持Map（可以选择加泛型，但泛型只能是基本数据类型或String和CharSequence，需要引入Map所在的包），但同样，Map中存储的数据也只能是Java基本数据类型，而且另外一边（客户端或服务端）接受的是HashMap类型；
- 所有aidl文件中的注释都会出现在自动生成的IBinder接口java文件中（除了导入包语句之前的注释）；
- aidl接口只支持方法，不支持变量。

例如 IServerSerivce.aidl
```java
interface IServerSerivce {
    String getName(int id);
}
```
重新构建一下project会在builde-resource-gen...下面生成一个IServerSerivce.java文件
这个文件里面有一个内部静态抽象类Stub,接下来我们需要实现它

例如ServerSerivce.java
```java
import com.xxx.xx.IServerSerivce；
public class ServerSerivce extends Service {
    /**
     * 返回ServerSerivce代理对象IBinder给客户端使用
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IServerSerivce.Stub mBinder = new IServerSerivce.Stub() {
        @Override
        public String getName(int id) thorws RemoteException {
            return new String("同学"+id);
        }
    }
}
```
在AndroidManifest.xml中注册该Serivice。其中还要action属性，用于隐式启动该服务
```xml
<service
    android:name=".service.ServerService"
    android:exported="true">
        <intent-filter>
            <action android:name="com.xxx.xxx"/>
            <category android:name="android.intent.category.DEFAULT"/>
        </intent-filter>
</service>
```
### 第三方应用中的AIDL

在第三方应用中同样需要AIDL文件，而且和服务端的AIDL一模一样。

**第一步** 建立服务连接
```java
private ServiceConnection conn = new ServiceConnection() {
    
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 这里的IBinder对象service是代理对象，所以必须调用下面的方法转换成AIDL接口对象
            mServerService = IRemoteService.Stub.asInterface(service);
            System.out.println("bind success! " + mServerService.toString());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServerService = null;
            System.out.println(mServerService.toString() +" disconnected! ");
        }
};
```

**第二步** 通过隐式意图绑定远程服务
```java
// 连接绑定远程服务
Intent intent = new Intent();
// action值为远程服务的action，即上面我们在服务端应用清单文件的action
intent.setAction("com.xx.xxx"); //指定的action
intent.setPackage("com.xxx.xx"); //服务端应用的包名
isConnSuccess = bindService(intent, conn, Context.BIND_AUTO_CREATE); //
```

**第三步** 在onDestroy解除服务
```java
protected void onDestroy() {
    super.onDestroy();
    unbindService(conn);
}
```

接下来就可以通过mServerService来访问数据。

### 使用AIDL实现google支付

首先在下载google给出的[示例代码](https://github.com/googlesamples/android-play-billing.git)
把项目里面的AIDL文件拷贝到自己的项目中，文件路径也要一致
把util的类都拷贝到自己的项目中。service的连接，访问都被封装到了IabHelper类中

**第一步** 查询记录回调
```java
  // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(purchaseId);
            mPurchase = premiumPurchase;
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
            alert("User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

            updateUi();
            setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };
```
在调用查询方法后回调，需要根据自己的情况对查询结果进行处理

**第二步** 购买成功回调

```java
    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                setWaitScreen(false);
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(purchaseId)) {
                // bought the premium upgrade!
                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                alert("Thank you for upgrading to premium!");
                mIsPremium = true;
                updateUi();
                setWaitScreen(false);
            }
        }
    };
```

**第三步** 消费商品回调
```java

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");
//                mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
                saveData();
                alert("Consumption successful");
            }
            else {
                complain("Error while consuming: " + result);
            }
            updateUi();
            setWaitScreen(false);
            Log.d(TAG, "End consumption flow.");
        }
    };
```

**第四步** 初始化和解除

```java
public void init() {
     mHelper = new IabHelper(mContext, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
//                mBroadcastReceiver = new IabBroadcastReceiver(MainActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
//                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });
}



@Override
public void onDestroy() {
        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }
```
**第四步** 调用支付、查询、消费等方法

|方法名|类型|所需参数|含义|
|:---|:---|:---|:----|
|launchPurchaseFlow|void|Activity act, String sku,  int requestCode,OnIabPurchaseFinishedListener,String extraData|唤起支付页面|
|queryInventoryAsync|void|QueryInventoryFinishedListener listener|查询订单记录|
|consumeAsync|void|Purchase purchase, OnConsumeFinishedListener listener|消费指定商品|

**关于IabHelper** 
在IabHelper中，它已经帮我们把与远程服务通信的连接，等操作给给封装起来，可以让我们使用者只需要关注实现方法本身，而不用处理服务的连接等，还有一些逻辑的处理等，接下来看看它封装了哪些方法吧

startSetup
```java
public void startSetup(final OnIabSetupFinishedListener listener) {
        // If already set up, can't do it again.
        checkNotDisposed();
        if (mSetupDone) throw new IllegalStateException("IAB helper is already set up.");

        // Connection to IAB service
        logDebug("Starting in-app billing setup.");
        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                logDebug("Billing service disconnected.");
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if (mDisposed) return;
                logDebug("Billing service connected.");
                mService = IInAppBillingService.Stub.asInterface(service);
                String packageName = mContext.getPackageName();
                try {
                    logDebug("Checking for in-app billing 3 support.");

                    // check for in-app billing v3 support
                    int response = mService.isBillingSupported(3, packageName, ITEM_TYPE_INAPP);
                    if (response != BILLING_RESPONSE_RESULT_OK) {
                        if (listener != null) listener.onIabSetupFinished(new IabResult(response,
                                "Error checking for billing v3 support."));

                        // if in-app purchases aren't supported, neither are subscriptions
                        mSubscriptionsSupported = false;
                        mSubscriptionUpdateSupported = false;
                        return;
                    } else {
                        logDebug("In-app billing version 3 supported for " + packageName);
                    }

                    // Check for v5 subscriptions support. This is needed for
                    // getBuyIntentToReplaceSku which allows for subscription update
                    response = mService.isBillingSupported(5, packageName, ITEM_TYPE_SUBS);
                    if (response == BILLING_RESPONSE_RESULT_OK) {
                        logDebug("Subscription re-signup AVAILABLE.");
                        mSubscriptionUpdateSupported = true;
                    } else {
                        logDebug("Subscription re-signup not available.");
                        mSubscriptionUpdateSupported = false;
                    }

                    if (mSubscriptionUpdateSupported) {
                        mSubscriptionsSupported = true;
                    } else {
                        // check for v3 subscriptions support
                        response = mService.isBillingSupported(3, packageName, ITEM_TYPE_SUBS);
                        if (response == BILLING_RESPONSE_RESULT_OK) {
                            logDebug("Subscriptions AVAILABLE.");
                            mSubscriptionsSupported = true;
                        } else {
                            logDebug("Subscriptions NOT AVAILABLE. Response: " + response);
                            mSubscriptionsSupported = false;
                            mSubscriptionUpdateSupported = false;
                        }
                    }

                    mSetupDone = true;
                }
                catch (RemoteException e) {
                    if (listener != null) {
                        listener.onIabSetupFinished(new IabResult(IABHELPER_REMOTE_EXCEPTION,
                                "RemoteException while setting up in-app billing."));
                    }
                    e.printStackTrace();
                    return;
                }

                if (listener != null) {
                    listener.onIabSetupFinished(new IabResult(BILLING_RESPONSE_RESULT_OK, "Setup successful."));
                }
            }
        };

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        List<ResolveInfo> intentServices = mContext.getPackageManager().queryIntentServices(serviceIntent, 0);
        if (intentServices != null && !intentServices.isEmpty()) {
            // service available to handle that Intent
            mContext.bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
        }
        else {
            // no service available to handle that Intent
            if (listener != null) {
                listener.onIabSetupFinished(
                        new IabResult(BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE,
                                "Billing service unavailable on device."));
            }
        }
    }
```

在startSetup中，它判断了应用是否支持google pay，然后实现了Service的连接

launchPurchaseFlow
```java 
public void launchPurchaseFlow(Activity act, String sku, String itemType, List<String> oldSkus,
            int requestCode, OnIabPurchaseFinishedListener listener, String extraData)
        throws IabAsyncInProgressException {
        checkNotDisposed();
        checkSetupDone("launchPurchaseFlow");
        flagStartAsync("launchPurchaseFlow");
        IabResult result;

        if (itemType.equals(ITEM_TYPE_SUBS) && !mSubscriptionsSupported) {
            IabResult r = new IabResult(IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE,
                    "Subscriptions are not available.");
            flagEndAsync();
            if (listener != null) listener.onIabPurchaseFinished(r, null);
            return;
        }

        try {
            logDebug("Constructing buy intent for " + sku + ", item type: " + itemType);
            Bundle buyIntentBundle;
            if (oldSkus == null || oldSkus.isEmpty()) {
                // Purchasing a new item or subscription re-signup
                buyIntentBundle = mService.getBuyIntent(3, mContext.getPackageName(), sku, itemType,
                        extraData);
            } else {
                // Subscription upgrade/downgrade
                if (!mSubscriptionUpdateSupported) {
                    IabResult r = new IabResult(IABHELPER_SUBSCRIPTION_UPDATE_NOT_AVAILABLE,
                            "Subscription updates are not available.");
                    flagEndAsync();
                    if (listener != null) listener.onIabPurchaseFinished(r, null);
                    return;
                }
                buyIntentBundle = mService.getBuyIntentToReplaceSkus(5, mContext.getPackageName(),
                        oldSkus, sku, itemType, extraData);
            }
            int response = getResponseCodeFromBundle(buyIntentBundle);
            if (response != BILLING_RESPONSE_RESULT_OK) {
                logError("Unable to buy item, Error response: " + getResponseDesc(response));
                flagEndAsync();
                result = new IabResult(response, "Unable to buy item");
                if (listener != null) listener.onIabPurchaseFinished(result, null);
                return;
            }

            PendingIntent pendingIntent = buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT);
            logDebug("Launching buy intent for " + sku + ". Request code: " + requestCode);
            mRequestCode = requestCode;
            mPurchaseListener = listener;
            mPurchasingItemType = itemType;
            act.startIntentSenderForResult(pendingIntent.getIntentSender(),
                    requestCode, new Intent(),
                    Integer.valueOf(0), Integer.valueOf(0),
                    Integer.valueOf(0));
        }
        catch (SendIntentException e) {
            logError("SendIntentException while launching purchase flow for sku " + sku);
            e.printStackTrace();
            flagEndAsync();

            result = new IabResult(IABHELPER_SEND_INTENT_FAILED, "Failed to send intent.");
            if (listener != null) listener.onIabPurchaseFinished(result, null);
        }
        catch (RemoteException e) {
            logError("RemoteException while launching purchase flow for sku " + sku);
            e.printStackTrace();
            flagEndAsync();

            result = new IabResult(IABHELPER_REMOTE_EXCEPTION, "Remote exception while starting purchase flow");
            if (listener != null) listener.onIabPurchaseFinished(result, null);
        }
    }
```

由上，在我们的程序调用了launchPurchaseFlow方法后，它会先对参数进行判断，判断商品的类型，然后调用service中的购买商品的方法，接收回调，对回调进行分析处理

接下来的方法就不再介绍了，基本是大同小异。相信总结完这么多也基本可以理解使用了。

## 代码调试
代码调试在开发中是一个非常常见的一种操作，在Android Studio中有着丰富的调试快捷键，工欲善其事必先利其器，下面就列出来常用的debug快捷键吧
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181207111838613.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM0MjYxMjE0,size_16,color_FFFFFF,t_70)

## 异常追踪
在自己开发测试过程中，如果发现出现crash问题，我们可以查看logcat上面的错误信息，最后得出分析结论，很容易查出问题原因。但是，当 如果线上版本，用户使用过程中出现crash问题，并不是在自己的开发环境出现的错误，难道我们就能视而不见吗？当然不是，我们还可以通过收集这些错误信息，定时上传到我们的服务器上。

接下来的步骤参考**任玉刚**大佬的《Android程序Crash时的异常上报》，但可能不太一样的是，当时他那篇文章写的时候是2013年，现在是2018快到2019年了，他把异常信息放在SD卡的文件上，但现在Android版本的更新和手机吃的发展，很多手机基本上废弃SD卡了，而且在读写访问的权限上也格外的严格，所以需要注意技术的更新。

### 关于异常捕获
在Thread类中有以下一个方法
```java
   /**
     * Sets the default uncaught exception handler. This handler is invoked in
     * case any Thread dies due to an unhandled exception.
     *
     * @param handler
     *            The handler to set or null.
     */
    public static void setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler handler) {
        Thread.defaultUncaughtHandler = handler;
    }
```
我们可以通过该方法在application中设置UI线程捕获到异常信息，同样也可以通过该方法在应用闪退前提醒用户，接下来我们就可以通过捕获到的异常信息保存到自己的本地文件，然后上传到自己的服务器上。

### 第一步 定义异常处理Handler
定义一个专门用户收集错误信息的类，每当发生crash时候，把发生crash异常信息的时间，所用的手机设备信息，和具体错误信息记录下来。
```java
public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;
 
    private static final String PATH ；// = Environment.getExternalStorageDirectory().getPath() + "/ryg_test/log/";
    private static final String FILE_NAME = "crash";
 
    //log文件的后缀名
    private static final String FILE_NAME_SUFFIX = ".trace";
 
    private static CrashHandler sInstance = new CrashHandler();
 
    //系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    private UncaughtExceptionHandler mDefaultCrashHandler;
 
    private Context mContext;
 
    //构造方法私有，防止外部构造多个实例，即采用单例模式
    private CrashHandler() {
    }
 
    public static CrashHandler getInstance() {
        return sInstance;
    }
 
    //这里主要完成初始化工作
    public void init(Context context) {
        //获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        //获取Context，方便内部使用
        mContext = context.getApplicationContext();
    }
 
    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            //导出异常信息到SD卡中
            dumpExceptionToSDCard(ex);
            //这里可以通过网络上传异常信息到服务器，便于开发人员分析日志从而解决bug
            uploadExceptionToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        //打印出当前调用栈信息
        ex.printStackTrace();
 
        //如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            Process.killProcess(Process.myPid());
        }
 
    }
 
    private void dumpExceptionToSDCard(Throwable ex) throws IOException {
        //判断外部存储是否可用
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {
        	PATH = getExternalFilesDir("log").getAbsolutePath();
    	}else{//没外部存储就使用内部存储
        	PATH = getFilesDir()+File.separator+"log";
    	}
 
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        //以当前时间创建log文件
        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);
 
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            //导出发生异常的时间
            pw.println(time);
 
            //导出手机信息
            dumpPhoneInfo(pw);
 
            pw.println();
            //导出异常的调用栈信息
            ex.printStackTrace(pw);
 
            pw.close();
        } catch (Exception e) {
            Log.e(TAG, "dump crash info failed");
        }
    }
 
    private void dumpPhoneInfo(PrintWriter pw) throws NameNotFoundException {
        //应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
 
        //android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
 
        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
 
        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
 
        //cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }
 
    private void uploadExceptionToServer() {
        //TODO Upload Exception Message To Your Web Server
    }
 
}
```
其中需要申请的权限
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.INTERNET"/>
```
动态申请权限
```java
    //权限
    private String[] allPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
	
    /**
     * 动态申请权限
     */
    public void applyPermission(){
        if (VERSION.SDK_INT>= 23) {
            ActivityCompat.requestPermissions(this,allPermissions,1);
        }
    }
```

### 第二步 为ui线程添加默认异常事件Handler
既然是在主线程中添加异常处理器，那也就是在ui线程中添加异常处理Handler，我们推荐大家在Application中添加而不是在Activity中添加。Application标识着整个应用，在Android声明周期中是第一个启动的，早于任何的Activity、Service等。

```java
public class TestApp extends Application {
 
    private static TestApp sInstance;
 
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
 
        //在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    } 
    public static TestApp getInstance() {
        return sInstance;
    }

}
```
其实到这里捕获异常，上传异常就部署完成了，你可以在程序中去抛出一个异常测试一下是否能够捕获异常

## 代码混淆
我们知道，打包的APK可以通过反编译工具来获得应用的源代码，所以为了防止某些意图不轨的采花大盗对应用进行反编译，利用漏洞进行不法勾当。我们往往会对应用进行代码混淆，这样就算能对应用进行反编译，但得到也是命名无意义的类和变量。增加分析难度。

### 第一步 混淆配置
混淆是对app/proguard-rules.pro里面进行编写混淆规则，然后在app的build.gradle文件中引用混淆文件
```groovy
buildTypes {
	release {
		//打包是否混淆
		minifyEnabled true
		proguardFiles getDefaultProguardFile('proguard-android.txt'),'proguard-rules.pro'
	}
}

```
### 第二步 混淆规则
 在配置之前，我们需要搞清楚要对哪些代码进行混淆
 
 一般以下情况都会不混淆： 

- 使用了自定义控件那么要保证它们不参与混淆  
- 使用了枚举要保证枚举不被混淆 
- 对第三方库中的类不进行混淆 
- 运用了反射的类也不进行混淆 
- 使用了 Gson 之类的工具要使 JavaBean 类即实体类不被混淆 
- 在引用第三方库的时候，一般会标明库的混淆规则的，建议在使用的时候就把混淆规则添加上去，免得到最后才去找 
- 有用到 WebView 的 JS 调用也需要保证写的接口方法不混淆，原因和第一条一样 
- Parcelable 的子类和 Creator 静态成员变量不混淆，否则会产生 Android.os.BadParcelableException 异常

**1. 实体类**
```java
-keep class com.xx.xx.entity.** { *; }
```
**2. 第三方包**
```java
#eventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.** { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
```
**3.反射相关的类和方法**
```
-keep class com.xx.xx.xx.xx.view.** { *; }
-keep class com.xx.xx.xx.xx.xx.** { *; }
```
**4.继承某类不被混淆**
```
避免所有继承Activity的类被混淆
```
**5.固定模块
```

#############################################
#
# 对于一些基本指令的添加
#
#############################################
# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5

# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames

# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose

# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers

# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify

# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses

# 避免混淆泛型
-keepattributes Signature

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*


#############################################
#
# Android开发中一些需要保留的公共部分
#
#############################################

# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService


# 保留support下的所有类及其内部类
-keep class android.support.** {*;}

# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

# 保留R下面的资源
-keep class **.R$* {*;}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
```
## 参考资料
[Android程序Crash时的异常上报](https://blog.csdn.net/singwhatiwanna/article/details/17289479)
[(通用)Android App代码混淆终极解决方案](https://blog.csdn.net/mazhidong/article/details/64821047#comments)
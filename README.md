[![Download](https://api.bintray.com/packages/smartnsoft/maven/monerominer/images/download.svg)](https://bintray.com/smartnsoft/maven/monerominer/_latestVersion)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![TeamCity status](https://ci.smartnsoft.com/app/rest/builds/buildType(id:android_monero_miner)/statusIcon)](https://ci.smartnsoft.com/viewType.html?buildTypeId=android_monero_miner)
 
# android-monero-miner

A minimal SDK that lets an integrator add a [Monero](http://monero.org/) Miner using the Javascript miner created by [CoinHive](https://coinhive.com/).
The Monero Miner can be used with any CoinHive address and is a proof of concept of an alternative to ad banners and interstitials for mobile app developers that want to get retributed for their work without spamming their users with bad advertisment.

## Prerequisites

You **must** create a CoinHive account

<https://coinhive.com>

Get then your public Site key

<https://coinhive.com/settings/sites>

This will be used to initialize your Monero Miner in Android.

## How it works

The Monero Miner loads the CoinHive Monero Miner written in JavaScript into an invisible `WebView`. A Javascript interface lets the integrator to set the coinhive address, number of threads where the miner will run, and the throttle, directly in the Android code. The instructions are then injected into the Javascript code. 

## Initialization

The `MoneroMiner` follows the Singleton pattern.
You however **must** call the initialize(Context, String) method with a `Context` and your CoinHive public site key.
You can do this in the `onCreate()` method of your `Application`

```java
MoneroMiner.INSTANCE.initialize(getApplicationContext(), COINHIVE_ADDRESS);
```

## Use the Monero Miner

Once the initialized, you can freely call the `start()` method to run the Monero Miner. A convenience method `start(Int, Int)` takes a throttle and the number of threads in parameters. When not used anymore, call the `stop()` method.

## Parameters

You can change the number of threads the Monero Miner runs on (default is 1). Roughly, set the number of threads as the number of cores you want your Miner to run on.

```java
MoneroMiner.setThreads(1);
```

You can change the throttle of the Monero Miner (default is 20). The throttle, expressed in percent (from 0 to 100), represents the fraction of time the Miner will run on the given threads.

```java
MoneroMiner.setThrottle(20);
```

_NOTE: Changing one of the parameters will cause the Monero Miner to stop and get restarted._

## Download

### Gradle

```groovy
implementation("com.smartnsoft:monerominer:0.1")
```

## Further improvements

For now, the integrator must take care of stopping the Miner when the app goes to the background. A further improvement may be to make this automatically. Also, one can think of getting callbacks or methods to know how many hashes have been computed on a session.

## License

This SDK is under the MIT license.

## Author

This Monero Miner was proudly made at [Smart&Soft](https://smartnsoft.com/), Paris FRANCE

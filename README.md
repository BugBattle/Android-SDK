# Bugbattle Android SDK
[![](https://jitpack.io/v/BugBattle/BugBattle-Android-SDK.svg)](https://jitpack.io/#BugBattle/BugBattle-Android-SDK) [![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://github.com/BugBattle/Android-SDK/graphs/commit-activity) [![Documentation Status](https://readthedocs.org/projects/ansicolortags/badge/?version=latest)](https://docs.bugbattle.io)

![Bugbattle iOS SDK Intro](https://github.com/BugBattle/iOS-SDK/blob/master/imgs/bugbattle-intro.png)



With BugBattle we’ve put the lame task of bug fixing upside down and turned it into a gaming experience for you and the whole development team. As if by magic the quality of your apps and websites improves – and your customers will be delighted.

## How to start
Open your App in your preferred development tool, for e.g. Android Studio.
### Add the repository
As already mentioned, it’s very simple to include our SDK. Add the following repository to your root build.gradle, to get access to the JitPack repository, which hosts our SDK.
```
allprojects {
  repositories {
    ...
    maven {
    url 'https://jitpack.io'
    }
  }
}
```
Add the dependency
In the same file ( build.gradle ) add the dependency to include the library.
```
dependencies {
  ...
  implementation 'com.github.BugBattle:BugBattle-Android-SDK:1.3.2'
}
```
You are now ready to use our SDK in your App. Let's carry on with the initialisation 🎉

### Initialise the SDK
Open your preferred development tool and add the following imports.
```
import bugbattle.io.bugbattle.BugBattle;
import bugbattle.io.bugbattle.BugBattleActivationMethod;
```
Add the initialise method to your main activity's onCreate method.
```
@Override protected void onCreate(Bundle savedInstanceState) {
  ....
  BugBattle.initialise("5c5d811215244ab6e48e9751", BugBattleActivationMethod.SHAKE, this);
} 
```
The initialise method takes two parameters. The api key and the activation method. There are two ways to start the bug reporting flow. The default option is to activate the flow by shaking the device (*BugBattleActivationMethod.SHAKE*). You can also choose *BugBattleActivationMethod.NONE* and start the flow by yourself in order to implement a custom integration. Another option is BugBattleActivationMethod.THREE_FINGER_DOUBLE_TAB. It can be used if the shake gesture is already taken by another library.

### Simulator Restrictions
Please notice, that the shake gesture wont work in the simulator.

#### Save & run 🚀
Need help? Check out our documentation or contact us.

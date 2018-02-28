# 微信自动添加好友
调用Android AccessibilityService服务

实现自动添加好友

微信好友来源于在线数据

AccessibilityService是什么
AccessibilityService是什么，官网是这样解释的

Accessibility services are intended to assist users with disabilities in using Android devices and apps. They run in the background and receive callbacks by the system whenAccessibilityEvents are fired.

也就是说这是个辅助功能，目的是辅助人们去使用Android设备和应用。它在后台运行，可以接收系统的回调。
可见AccessibilityService的出现Google的初衷是辅助人们去使用Android设备和应用，但是当你对它足够了解了之后你会发现它的作用不仅仅只是这样。对windows编程有了解的同学肯定知道hook（钩子），AccessibilityService和windows下的hook有那么一点的相似。AccessibilityService可以拦截到系统发出的一些消息（比如窗体状态的改变，通知栏状态的改变，View被点击了等等），当拦截到这些事件我们就可以去做一些我们想做的事儿了~~~
AccessibilityService能做些什么呢？ 比如自动化测试、自动抢红包、自动安装等等。在带来便利的同时，也还是有需要注意的地方：当你开启了辅助功能之后会对你的隐私信息带来一些风险，所以还是需要谨慎的去开启第三方的辅助功能。当然这对于我们开发者而言都不是事，因为我们完全可以自己去开发属于我们的辅助功能。






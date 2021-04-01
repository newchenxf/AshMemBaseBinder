这个项目，是一个app（AshMemClient）和一个Native进程（shm_binder_service ）共享内存的例子。

AshMemClient 用AndroidStudio打开。

shm_binder_service 需要基于android源码编译。因为binder不支持ndk编译，如果使用共享内存的进程是纯Native实现，那只能是android源码编译了。本次提交代码，android 7.0下编译通过。

如果共享内存完全是2个app级别的通信，那可以参考这里的代码：
https://github.com/dev-area/ashmem


如果是2个native进程，则可以参考这里：
https://cloud.tencent.com/developer/article/1490314

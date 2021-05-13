package org.group9.stumgr.util.android;

public final class PermissionManager
   extends PlatformSupportManager<RequestPermissionInterface> {

   //根据SDK版本添加权限申请实现类
   public PermissionManager() {
      super(RequestPermissionInterface.class, new RequestPermissionPImpl());
      addImplementationClass(30, RequestPermissionRImpl.class.getName());
      addImplementationClass(28, RequestPermissionPImpl.class.getName());
   }
}


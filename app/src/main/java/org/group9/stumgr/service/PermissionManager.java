package org.group9.stumgr.service;

public final class PermissionManager extends PlatformSupportManager<RequestPermissionInterface>
    {

        public PermissionManager() {
            super(RequestPermissionInterface.class, new RequestPermissionPImpl());
            addImplementationClass(30,RequestPermissionRImpl.class.getName());
            addImplementationClass(28,RequestPermissionPImpl.class.getName());
        }
    }


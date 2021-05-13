package org.group9.stumgr.context;

import android.app.Application;

public class G9StuMgrApplication extends Application {
   private static G9StuMgrApplication instance;

   public static G9StuMgrApplication getInstance() {
      return instance;
   }

   @Override
   public void onCreate() {
      super.onCreate();

      instance = this;
   }


}

package org.group9.stumgr.bean;

// TODO
public class StudentCriteriaImpl extends StudentCriteria {

   private String nameFragment;

   public void setDefault() {
      nameFragment = null;
   }

   public String getNameFragment() {
      return nameFragment;
   }

   public void setNameFragment(String nameFragment) {
      this.nameFragment = nameFragment;
   }

   @Override
   public boolean test(Student student) {
      return false;
   }
}

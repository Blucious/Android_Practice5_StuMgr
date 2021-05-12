package org.group9.stumgr.bean;

import org.apache.commons.lang3.StringUtils;
import org.group9.stumgr.util.ChnCharUtils;

import java.util.HashMap;
import java.util.Map;

public class SimpleStudentCriteria extends StudentCriteria {

   /**
    * 名字过滤相关字段
    */
   private String nameFragment;
   private boolean isNameFragmentContainsChnChar;
   // 有内存泄漏风险，如果外部没有及时调用clearCache
   private Map<String, StudentNamePinyinData> studentNamePinyinDataCache;

   private static class StudentNamePinyinData {
      private boolean isNameContainsChnChar;
      private String pinyinalizedName;
      private Character[][] pinyinFirstSpellGroups;
   }

   public SimpleStudentCriteria setDefault() {
      nameFragment = null;
      isNameFragmentContainsChnChar = false;
      studentNamePinyinDataCache = new HashMap<>();
      return this;
   }

   public String getNameFragment() {
      return nameFragment;
   }

   public void setNameFragment(String theNameFragment) {
      nameFragment = theNameFragment;
      if (nameFragment != null) {
         nameFragment = nameFragment.toLowerCase();
         isNameFragmentContainsChnChar = ChnCharUtils.isContainsChnChar(nameFragment);
      } else {
         isNameFragmentContainsChnChar = false;
      }
   }

   public void clearPinyinCache() {
      studentNamePinyinDataCache = new HashMap<>();
   }

   public StudentNamePinyinData getPinyinDataForName(String name) {
      StudentNamePinyinData data = studentNamePinyinDataCache.get(name);
      if (data == null) {
         data = new StudentNamePinyinData();
         data.isNameContainsChnChar = ChnCharUtils.isContainsChnChar(name);
         if (data.isNameContainsChnChar) {
            data.pinyinalizedName = ChnCharUtils.convertChnCharsToPinyin(name);
            data.pinyinFirstSpellGroups = ChnCharUtils.strToPinyinFirstSpellGroups(name);
         }
         studentNamePinyinDataCache.put(name, data);
      }
      return data;
   }

   @Override
   public boolean test(Student student) {

      if (!StringUtils.isEmpty(nameFragment)) {

         String name = student.getName().toLowerCase();
         StudentNamePinyinData pinyinData = getPinyinDataForName(name);

         if (!isNameFragmentContainsChnChar
            && pinyinData.isNameContainsChnChar) {

            if (!pinyinData.pinyinalizedName.contains(nameFragment)) {
               if (!ChnCharUtils.isPinyinFirstSpellGroupsContainsFirstSpells(
                  pinyinData.pinyinFirstSpellGroups, nameFragment)) {
                  return false;
               }
            }
         } else if (!name.contains(nameFragment)) {
            return false;
         }
      }

      return true;
   }
}

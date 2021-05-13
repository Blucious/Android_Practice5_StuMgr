package org.group9.stumgr.bean;

import org.apache.commons.lang3.StringUtils;
import org.group9.stumgr.util.ChnCharUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 学生过滤条件实现类
 */
public class SimpleStudentCriteria extends StudentCriteria {

   /**
    * 名字过滤相关字段
    *
    * 辅助字段 {@code studentNamePinyinDataCache}学生名拼音数据缓存。
    * 若如果外部没有适时用clearCache，则会造成内存泄漏。
    */
   private String nameFragment = null;
   private boolean isNameFragmentContainsChnChar = false;
   private Map<String, StudentNamePinyinData> studentNamePinyinDataCache = new HashMap<>();
   private int studentNamePinyinDataCacheMaxCount = 2000;

   private static class StudentNamePinyinData {
      private boolean isNameContainsChnChar;
      private String pinyinalizedName;
      private Character[][] pinyinFirstSpellGroups;
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

   private void tryReducePinyinCache() {
      if (studentNamePinyinDataCache.size() > studentNamePinyinDataCacheMaxCount) {
         clearPinyinCache();
      }
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

      tryReducePinyinCache();

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

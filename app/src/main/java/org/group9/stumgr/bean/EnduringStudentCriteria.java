package org.group9.stumgr.bean;

import androidx.collection.LruCache;

import org.apache.commons.lang3.StringUtils;
import org.group9.stumgr.util.ChnCharUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 学生过滤条件实现类
 */
public class EnduringStudentCriteria extends StudentCriteria {

   /**
    * 名字过滤相关字段
    * <p>
    * 辅助字段 {@code studentNamePinyinDataCache}学生名拼音数据缓存。
    */
   private String nameFragment = null;
   private boolean isNameFragmentContainsChnChar = false;
   private final LruCache<String, StudentNamePinyinData> studentNamePinyinDataCache =
      new LruCache<>(1000);

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

      // 生成辅助信息
      if (nameFragment != null) {
         nameFragment = nameFragment.toLowerCase();
         isNameFragmentContainsChnChar = ChnCharUtils.isContainsChnChar(nameFragment);
      } else {
         isNameFragmentContainsChnChar = false;
      }
   }

   public void clearPinyinCache() {
      studentNamePinyinDataCache.evictAll();
   }

   /**
    * 获取{@code name}的拼音数据，如果缓存中有则从缓存中取，否则直接生成
    */
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

         /*
         如果(1)用户学生名包含中文，且(2){@code nameFragment}不包含中文，则采用拼音搜索。
         (1)理由：学生名不包含中文，使用拼音匹配则无意义
         (2)理由：不符合使用规律，若用户要用拼音查找，一般会在一开始就输入拼音或首拼，
              不会输入几个中文字符后再输入拼音。
          */
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

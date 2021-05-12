package org.group9.stumgr.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * 中文字符工具
 */
public class ChnCharUtils {


   /**
    * 为提高性能，而设置的空字符串数组
    */
   private final static String[] EMPTY_STRING_ARRAY = new String[0];
   private final static char[] EMPTY_CHAR_ARRAY = new char[0];
   private final static Character[] EMPTY_CHARACTER_ARRAY = new Character[0];

   private static final HanyuPinyinOutputFormat PYOF = new HanyuPinyinOutputFormat();

   static {
      PYOF.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
      PYOF.setCaseType(HanyuPinyinCaseType.LOWERCASE);
      PYOF.setVCharType(HanyuPinyinVCharType.WITH_V);
   }

   private final static StringBuilder FIXED_SB = new StringBuilder(500);

   private static StringBuilder getProperStrBuilder(String s) {
      if (s.length() <= FIXED_SB.length()) {
         FIXED_SB.setLength(0);
         return FIXED_SB;
      } else {
         return new StringBuilder(s.length());
      }
   }


   /**
    * 判断一个字符是否是汉字
    */
   public static boolean isChnChar(char ch) {
      return 0x4e00 <= ch && ch <= 0x9fa5;
   }

   /**
    * 判断一个字符串是否仅包含汉字
    */
   public static boolean isChnStr(String s) {
      if (s.isEmpty()) {
         return false;
      }

      final int len = s.length();

      for (int i = 0; i < len; i++) {
         if (!isChnChar(s.charAt(i))) {
            return false;
         }
      }

      return true;
   }

   /**
    * 判断一个字符串是否包含汉字
    */
   public static boolean isContainsChnChar(String s) {
      final int len = s.length();

      for (int i = 0; i < len; i++) {
         if (isChnChar(s.charAt(i))) {
            return true;
         }
      }

      return false;
   }

   /**
    * 移除字符串中非汉字的字符
    */
   public static String removeNonChnChars(String s) {
      StringBuilder sb = new StringBuilder();

      final int len = s.length();

      for (int i = 0; i < len; i++) {
         char ch = s.charAt(i);
         if (isChnChar(ch)) {
            sb.append(ch);
         }
      }

      return sb.toString();
   }

   /**
    * 不能保证线程安全
    *
    * @return 返回字符 {@code ch} 对应的拼音（如果有多种拼法，则返回第一种），没有对应则返回空串
    */
   public static String chnCharToPinyin(char ch) {
      try {
         String[] pys = PinyinHelper.toHanyuPinyinStringArray(ch, PYOF);
         if (pys != null && pys.length >= 1) {
            return pys[0];
         }
      } catch (BadHanyuPinyinOutputFormatCombination ignored) {
      }

      return "";
   }

   /**
    * 不能保证线程安全
    *
    * @return 返回字符 {@code ch} 对应的拼音首拼，没有对应则返回空串
    */
   public static String chnCharToPinyinFirstSpell(char ch) {
      String pinyin = chnCharToPinyin(ch);
      if (pinyin.length() > 0) {
         return pinyin.substring(0, 1);
      } else {
         return "";
      }
   }

   public static String[] chnCharToPinyinGroup(char ch) {
      String[] pinyinArray = null;
      try {
         pinyinArray = PinyinHelper.toHanyuPinyinStringArray(ch, PYOF);
      } catch (BadHanyuPinyinOutputFormatCombination ignored) {
      }

      if (pinyinArray == null || pinyinArray.length == 0) {
         return EMPTY_STRING_ARRAY;
      }

      if (pinyinArray.length == 1) {
         // 若pinyinArray内有元素，则每个元素的长度必>=1
         return pinyinArray;
      } else if (pinyinArray.length == 2) {
         if (pinyinArray[0].equals(pinyinArray[1])) {
            return new String[]{pinyinArray[0]};
         } else {
            return pinyinArray;
         }
      } else if (pinyinArray.length == 3) {
         if (pinyinArray[0].equals(pinyinArray[1])) {
            // a a ?
            if (pinyinArray[0].equals(pinyinArray[2])) {
               // a a a
               return new String[]{pinyinArray[0]};
            } else {
               // a a b
               return new String[]{pinyinArray[0], pinyinArray[2]};
            }
         } else {
            // a b ?
            if (pinyinArray[0].equals(pinyinArray[2])) {
               // a b a
               return new String[]{pinyinArray[0], pinyinArray[1]};
            } else if (pinyinArray[1].equals(pinyinArray[2])) {
               // a b b
               return new String[]{pinyinArray[0], pinyinArray[1]};
            } else {
               // a b c
               return pinyinArray;
            }
         }
      } else {
         return Arrays.stream(pinyinArray)
             .distinct()
             .toArray(String[]::new);
      }
   }


   /**
    * 将中文字符转换为拼音首拼数组，内包含有所有可能的拼音首拼
    */
   public static Character[] chnCharToPinyinFirstSpellGroup(char ch) {
      String[] pinyinArray = null;
      try {
         pinyinArray = PinyinHelper.toHanyuPinyinStringArray(ch, PYOF);
      } catch (BadHanyuPinyinOutputFormatCombination ignored) {
      }

      if (pinyinArray == null || pinyinArray.length == 0) {
         return EMPTY_CHARACTER_ARRAY;
      }

      if (pinyinArray.length == 1) {
         // 若pinyinArray内有元素，则每个元素的长度必>=1
         return new Character[]{pinyinArray[0].charAt(0)};
      } else if (pinyinArray.length == 2) {
         if (pinyinArray[0].charAt(0) == pinyinArray[1].charAt(0)) {
            return new Character[]{pinyinArray[0].charAt(0)};
         } else {
            return new Character[]{pinyinArray[0].charAt(0), pinyinArray[1].charAt(0)};
         }
      } else if (pinyinArray.length == 3) {
         if (pinyinArray[0].charAt(0) == pinyinArray[1].charAt(0)) {
            // a a ?
            if (pinyinArray[0].charAt(0) == pinyinArray[2].charAt(0)) {
               // a a a
               return new Character[]{pinyinArray[0].charAt(0)};
            } else {
               // a a b
               return new Character[]{pinyinArray[0].charAt(0), pinyinArray[2].charAt(0)};
            }
         } else {
            // a b ?
            if (pinyinArray[0].charAt(0) == pinyinArray[2].charAt(0)) {
               // a b a
               return new Character[]{pinyinArray[0].charAt(0), pinyinArray[1].charAt(0)};
            } else if (pinyinArray[1].charAt(0) == pinyinArray[2].charAt(0)) {
               // a b b
               return new Character[]{pinyinArray[0].charAt(0), pinyinArray[1].charAt(0)};
            } else {
               // a b c
               return new Character[]{pinyinArray[0].charAt(0), pinyinArray[1].charAt(0),
                   pinyinArray[2].charAt(0)};
            }
         }
      } else {
         return Arrays.stream(pinyinArray)
             .map(pinyin -> pinyin.charAt(0))
             .distinct()
             .toArray(Character[]::new);
      }
   }

   public static Character[][] strToPinyinFirstSpellGroups(String s) {
      final int len = s.length();
      Character[][] arr = new Character[len][];

      for (int i = 0; i < len; i++) {
         char ch = s.charAt(i);
         if (isChnChar(ch)) {
            arr[i] = chnCharToPinyinFirstSpellGroup(s.charAt(i));
         } else {
            arr[i] = new Character[]{ch};
         }
      }

      return arr;
   }

   /**
    * n = chnStr.length()
    * m = pinyinFirstSpells.length()
    * O((n-m)*m*2.5)
    */
   public static boolean isStrContainsPinyinFirstSpells(String str,
                                                        String pinyinFirstSpells) {

      if (str.isEmpty() || pinyinFirstSpells.isEmpty()
          || pinyinFirstSpells.length() > str.length()) {
         return false;
      }

      Character[][] pinyinFirstSpellGroups = strToPinyinFirstSpellGroups(str);
      return isPinyinFirstSpellGroupsContainsFirstSpells(pinyinFirstSpellGroups, pinyinFirstSpells);
   }

   public static boolean isPinyinFirstSpellGroupsContainsFirstSpells(Character[][] pinyinFirstSpellGroups,
                                                                     String pinyinFirstSpells) {

      if (pinyinFirstSpellGroups.length == 0 || pinyinFirstSpells.isEmpty()
          || pinyinFirstSpells.length() > pinyinFirstSpellGroups.length) {
         return false;
      }

      int pinyinFirstSpellsLength = pinyinFirstSpells.length();

      for (int i = 0; i <= pinyinFirstSpellGroups.length - pinyinFirstSpellsLength; i++) {
         int j;
         for (j = 0; j < pinyinFirstSpellsLength; j++) {
            if (!ArrayUtils.contains(pinyinFirstSpellGroups[i + j], pinyinFirstSpells.charAt(j))) {
               break;
            }
         }
         if (j == pinyinFirstSpellsLength) {
            return true;
         }
      }

      return false;
   }

   /**
    * 非线程安全
    */
   public static String convertChnCharsToPinyin(String s) {
      StringBuilder sb = getProperStrBuilder(s);
      final int len = s.length();
      int chnCharCount = 0;

      for (int i = 0; i < len; i++) {
         char ch = s.charAt(i);
         if (isChnChar(ch)) {
            chnCharCount++;
            sb.append(chnCharToPinyin(ch));
         } else {
            sb.append(ch);
         }
      }

      if (chnCharCount > 0) {
         return sb.toString();
      } else {
         return s;
      }
   }

   /**
    * 非线程安全
    */
   public static String convertChnCharsToPinyinFirstSpells(String s) {
      StringBuilder sb = getProperStrBuilder(s);
      final int len = s.length();
      int chnCharCount = 0;

      for (int i = 0; i < len; i++) {
         char ch = s.charAt(i);
         if (isChnChar(ch)) {
            chnCharCount++;
            sb.append(chnCharToPinyinFirstSpell(ch));
         } else {
            sb.append(ch);
         }
      }

      if (chnCharCount > 0) {
         return sb.toString();
      } else {
         return s;
      }
   }

}

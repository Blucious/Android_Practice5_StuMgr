package org.group9.stumgr.service;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.group9.stumgr.R;
import org.group9.stumgr.bean.Student;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

public class StudentGenerator {
   private static final String TAG = StudentGenerator.class.getSimpleName();

   private static StudentGenerator instance;
   private static List<String> names = Collections.emptyList();

   private StudentGenerator() {
   }

   public static synchronized StudentGenerator getInstance(Resources resources) {
      if (instance == null) {
         loadNamesList(resources);
         instance = new StudentGenerator();
      }
      return instance;
   }

   public static void loadNamesList(Resources resources) {
      try {
         InputStream is = resources.openRawResource(R.raw.person_names);
         List<String> lines = IOUtils.readLines(is, StandardCharsets.UTF_8);
         names = lines.stream()
            .map(e -> StringUtils.strip(e, " \n"))
            .collect(Collectors.toList());
      } catch (IOException e) {
         Log.e(TAG, "loadNamesList: ", e);
      }
   }

   public String genName() {
      int index = (int) (Math.random() * names.size());
      return names.get(index);
   }

   public String genPhoneNumber() {
      StringBuilder sb = new StringBuilder(11);
      sb.append('1');
      for (int i = 0; i < 10; i++) {
         sb.append(((int) (Math.random() * 10)));
      }
      return sb.toString();
   }

   @SuppressLint("DefaultLocale")
   public String genAddress() {
      return String.format("湖北省武汉市洪山区 %d 号",
         (1 + (int) (Math.random() * 10000)));
   }

   public Student genStudent() {
      Student stu = new Student();
      // 个人信息
      stu.setName(genName());
      stu.setPhoneNumber(genPhoneNumber());
      stu.setAddress(genAddress());
      // 成绩
      if (Math.random() > 0.81) {
         if (Math.random() > 0.65) {
            setScores(stu, () -> 100);
         } else {
            setScores(stu, () -> 80);
         }
      } else {
         setScores(stu, () -> randInt(50, 100));
      }
      return stu;
   }

   public List<Student> genStudentsList(int n) {

      ArrayList<Student> l = new ArrayList<>(n);
      for (int i = 0; i < n; i++) {
         l.add(genStudent());
      }

      return l;
   }

   public int randInt(int start, int end) {
      if (start == end) {
         return start;
      } else {
         return start + (int) (Math.random() * (end - start + 1));
      }
   }

   public void setScores(Student stu, IntSupplier supplier) {
      stu.setEtAnswerScore(supplier.getAsInt());
      stu.setEtDemonstrationScore(supplier.getAsInt());
      stu.setEtProjectScore(supplier.getAsInt());
      stu.setNmAnswerScore(supplier.getAsInt());
      stu.setNmAttendanceScore(supplier.getAsInt());
      stu.setNmDemonstrationScore(supplier.getAsInt());
      stu.setNmSpeakingScore(supplier.getAsInt());
   }
}

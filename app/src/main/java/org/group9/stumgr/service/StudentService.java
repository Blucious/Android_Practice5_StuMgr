package org.group9.stumgr.service;


import org.group9.stumgr.bean.Student;

import java.util.Arrays;
import java.util.List;

public class StudentService {

   public static List<Student> getAllStudents() {
      Student[] students = {
         new Student("艾边城", "电话 139* * * * 8888\n湖北省武汉市洪山区 1 号"),
         new Student("艾承旭", "电话 139* * * * 2222\n湖北省武汉市洪山区 2 号"),
         new Student("马小云", "电话 139* * * * 3333\n湖北省武汉市洪山区 3 号"),
         new Student("王小强", "电话 139* * * * 6666\n湖北省武汉市洪山区 4 号"),
      };
      return Arrays.asList(students);
   }

}

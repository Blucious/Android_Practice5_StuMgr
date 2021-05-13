package org.group9.stumgr.bean;

import android.annotation.SuppressLint;

import org.apache.commons.math3.util.Precision;

import java.io.Serializable;

public class Student implements Serializable {
   // 个人信息
   private String name;
   private String phoneNumber;
   private String address;

   /*
    平时成绩、 期末成绩、个人得分不保存，而是通过计算得出
    成绩计算方法：
    总成绩=平时成绩*0.4+期末成绩*0.6，取值范围[0,100]
      平时成绩=考勤*0.3+发言*0.2+演示*0.3+回答*0.2，取值范围[0,100]
      期末成绩=项目得分*0.7+个人得分*0.3，取值范围[0,100]
        个人得分=展示*0.6+提问*0.4，取值范围[0,100]
    */

   // 平时成绩，nm=Normal
   private Integer nmAttendanceScore;
   private Integer nmSpeakingScore;
   private Integer nmDemonstrationScore;
   private Integer nmAnswerScore;

   // 期末成绩，et=End of Term
   private Integer etProjectScore;
   private Integer etDemonstrationScore;
   private Integer etAnswerScore;

   public Student() {
   }

   public Student(String name, String phoneNumber) {
      this.name = name;
      this.phoneNumber = phoneNumber;
   }

   public Student setDefault() {
      nmAttendanceScore = 0;
      nmSpeakingScore = 0;
      nmDemonstrationScore = 0;
      nmAnswerScore = 0;

      etProjectScore = 0;
      etDemonstrationScore = 0;
      etAnswerScore = 0;

      return this;
   }

   // ---------------- 辅助方法 开始 ----------------
   public double roundScore(double score) {
      return Precision.round(score, 2);
   }
   // ---------------- 辅助方法 结束 ----------------

   // ---------------- 计算型Getter 开始 ----------------
   public Double getNmScore() {
      double r = nmAnswerScore * 0.3
         + nmSpeakingScore * 0.2
         + nmDemonstrationScore * 0.3
         + nmAnswerScore * 0.2;
      return roundScore(r);
   }

   @SuppressLint("DefaultLocale")
   public String getDisplayNmScore() {
      return String.format("%.2f\n" +
            "=(考勤%d*0.3=%.1f)\n" +
            "+(发言%d*0.2=%.1f)\n" +
            "+(演示%d*0.3=%.1f)\n" +
            "+(回答%d*0.2=%.1f)",
         getNmScore(),
         nmAnswerScore, nmAnswerScore * 0.3,
         nmSpeakingScore, nmSpeakingScore * 0.2,
         nmDemonstrationScore, nmDemonstrationScore * 0.3,
         nmAnswerScore, nmAnswerScore * 0.2);
   }

   public Double getEtIndividualScore() {
      double r = etDemonstrationScore * 0.6
         + etAnswerScore * 0.4;
      return roundScore(r);
   }

   @SuppressLint("DefaultLocale")
   public String getDisplayEtIndividualScore() {
      return String.format("%.2f\n" +
            "=(展示%d*0.6=%.2f)\n" +
            "+(提问%d*0.4=%.2f)",
         getEtIndividualScore(),
         etDemonstrationScore, etDemonstrationScore * 0.6,
         etAnswerScore, etAnswerScore * 0.4);
   }

   public Double getEtScore() {
      double r = etProjectScore * 0.7
         + getEtIndividualScore() * 0.3;
      return roundScore(r);
   }

   @SuppressLint("DefaultLocale")
   public String getDisplayEtScore() {
      Double etIndividualScore = getEtIndividualScore();
      return String.format("%.2f\n" +
            "=(项目得分%d*0.7=%.2f)\n" +
            "+(个人得分%.2f*0.3=%.2f)",
         getEtScore(),
         etProjectScore, etDemonstrationScore * 0.7,
         etIndividualScore, etIndividualScore * 0.3);
   }

   public Double getTotalScore() {
      double r = getNmScore() * 0.4
         + getEtScore() * 0.6;
      return roundScore(r);
   }

   @SuppressLint("DefaultLocale")
   public String getDisplayTotalScore() {
      Double nmScore = getNmScore();
      Double etScore = getEtScore();
      return String.format("%.2f\n" +
            "=(平时成绩%.2f*0.4=%.2f)\n" +
            "+(期末成绩%.2f*0.6=%.2f)",
         getTotalScore(),
         nmScore, nmScore * 0.4,
         etScore, etScore * 0.6);
   }
   // ---------------- 计算型Getter 结束 ----------------


   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public Integer getNmAttendanceScore() {
      return nmAttendanceScore;
   }

   public void setNmAttendanceScore(Integer nmAttendanceScore) {
      this.nmAttendanceScore = nmAttendanceScore;
   }

   public Integer getNmSpeakingScore() {
      return nmSpeakingScore;
   }

   public void setNmSpeakingScore(Integer nmSpeakingScore) {
      this.nmSpeakingScore = nmSpeakingScore;
   }

   public Integer getNmDemonstrationScore() {
      return nmDemonstrationScore;
   }

   public void setNmDemonstrationScore(Integer nmDemonstrationScore) {
      this.nmDemonstrationScore = nmDemonstrationScore;
   }

   public Integer getNmAnswerScore() {
      return nmAnswerScore;
   }

   public void setNmAnswerScore(Integer nmAnswerScore) {
      this.nmAnswerScore = nmAnswerScore;
   }

   public Integer getEtProjectScore() {
      return etProjectScore;
   }

   public void setEtProjectScore(Integer etProjectScore) {
      this.etProjectScore = etProjectScore;
   }

   public Integer getEtDemonstrationScore() {
      return etDemonstrationScore;
   }

   public void setEtDemonstrationScore(Integer etDemonstrationScore) {
      this.etDemonstrationScore = etDemonstrationScore;
   }

   public Integer getEtAnswerScore() {
      return etAnswerScore;
   }

   public void setEtAnswerScore(Integer etAnswerScore) {
      this.etAnswerScore = etAnswerScore;
   }

   @Override
   public String toString() {
      return "Student{" +
         "name='" + name + '\'' +
         ", phoneNumber='" + phoneNumber + '\'' +
         ", address='" + address + '\'' +
         ", nmAttendanceScore=" + nmAttendanceScore +
         ", nmSpeakingScore=" + nmSpeakingScore +
         ", nmDemonstrationScore=" + nmDemonstrationScore +
         ", nmAnswerScore=" + nmAnswerScore +
         ", etProjectScore=" + etProjectScore +
         ", etDemonstrationScore=" + etDemonstrationScore +
         ", etAnswerScore=" + etAnswerScore
         + ", getEtScore() = " + getEtScore()
         + ", getEtIndividualScore() = " + getEtIndividualScore()
         + ", getNmScore() = " + getNmScore()
         + ", getTotalScore() = " + getTotalScore()
         + '}';
   }
}

package models;

import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class StudentInfo implements Serializable {
    public String getMonth() {
        return month;
    }

    public double getScholSum() {
        return scholSum;
    }

    public int getNOfDays() {
        return nOfDays;
    }

    public double getCoef() {
        return coef;
    }

    public double getAvgScore() {
        return avgScore;
    }



    private String month;
    private double scholSum;
    private int nOfDays;
    private double coef;
    private double avgScore;

    public static StudentInfo parse(String temp) {
        StudentInfo si = new StudentInfo();
        String[] sa = temp.split("\\|");
        si.month = sa[0];
        si.avgScore = Double.parseDouble(sa[1]);
        si.nOfDays = Integer.parseInt(sa[2]);
        si.coef = Double.parseDouble(sa[3]);
        si.scholSum = Double.parseDouble(sa[4]);
        return si;
    }

    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        month = (String) in.readObject();
        avgScore = (double) in.readObject();
        nOfDays = (int) in.readObject();
        coef = (double) in.readObject();
        scholSum = (double) in.readObject();
    }
    public String toString()
    {
        return month+"\t|\t"+ avgScore +"\t|\t"+nOfDays+"\t|\t"+ coef +"\t|\t"+"\t|\t"+round(scholSum,2);
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

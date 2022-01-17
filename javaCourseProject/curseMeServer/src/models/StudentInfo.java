package models;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class StudentInfo implements Serializable {
    private Date month;
    private double avgScore;
    private int nOfDays;
    private double coefSum;
    private double finalScholarship;

    static public void parseResult(ResultSet rs, PrintWriter pw) throws SQLException {
        while (rs.next())
        {
            StudentInfo temp =new StudentInfo();
            temp.month = (Date) rs.getDate("date");
            temp.avgScore = (double) rs.getDouble("score");
            temp.nOfDays = (int) rs.getInt("nOfD");

            if (temp.avgScore>6 && temp.avgScore<7)
                temp.coefSum = 1.0;
            else  if (temp.avgScore>=7 && temp.avgScore<8)
                temp.coefSum = 1.2;
            else if (temp.avgScore>=8 && temp.avgScore<9)
                temp.coefSum = 1.4;
            else if (temp.avgScore>=9 && temp.avgScore<=10)
                temp.coefSum = 1.6;
            else temp.coefSum=0.0;

            temp.finalScholarship = 96.37 * temp.coefSum;

            System.out.println("\t\t\t\t\t| " + temp);
            pw.println(temp.toString());
        }
        System.out.println("\t\t\t\t\t| end");
        pw.println("end");
    }
    public String toString()
    {
        return month+"|"+avgScore+"|"+nOfDays+"|"+coefSum+"|"+finalScholarship;
    }
}

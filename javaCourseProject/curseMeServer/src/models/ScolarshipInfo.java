package models;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScolarshipInfo {
    private int id;
    private String name;
    private String date;
    private Double nOfD;
    //private Double deductible;
    private Double coef;
    private Double scholSum;
    private Double avgScore;
    public static void parseResult(ResultSet rs, PrintWriter toClient) throws SQLException {
        while (rs.next())
        {
            ScolarshipInfo temp =new ScolarshipInfo();
            temp.id = rs.getInt("id");
            temp.name = rs.getString("name").strip();
            temp.date = rs.getString("date").strip();
            temp.avgScore = Double.valueOf(rs.getString("avgScore"));
            temp.nOfD = Double.valueOf(rs.getString("nOfD"));
            if (temp.avgScore>6 && temp.avgScore<7)
                temp.coef = 1.0;
            else  if (temp.avgScore>=7 && temp.avgScore<8)
                temp.coef = 1.2;
            else if (temp.avgScore>=8 && temp.avgScore<9)
                temp.coef = 1.4;
            else if (temp.avgScore>=9 && temp.avgScore<=10)
                temp.coef = 1.6;
            else temp.coef=0.0;

            temp.scholSum = Double.valueOf(rs.getString("scholSum"));
            temp.scholSum = temp.coef * temp.scholSum;
            System.out.println("\t\t\t\t\t| " + temp);
            toClient.println(temp.toString());
        }
        System.out.println("\t\t\t\t\t| end");
        toClient.println("end");
    }
    @Override
    public String toString() {
        return id+"|"+name+"|"+avgScore+"|"+date+"|"+nOfD+"|"+coef+"|"+scholSum+"|";
    }
}

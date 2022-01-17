package models;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CoefInfo {
    private int id;
    private String descr;
    private Double value;
    public static void parseResult(ResultSet rs, PrintWriter toClient) throws SQLException {
        while (rs.next())
        {
            CoefInfo temp =new CoefInfo();
            temp.id = rs.getInt("id");
            temp.descr = rs.getString("descr").strip();
            temp.value = Double.valueOf(rs.getString("coef").strip());
            System.out.println("\t\t\t\t\t| " + temp);
            toClient.println(temp.toString());
        }
        System.out.println("\t\t\t\t\t| end");
        toClient.println("end");
    }
    @Override
    public String toString() {
        return id+"|"+descr+"|"+value+"|";
    }
}

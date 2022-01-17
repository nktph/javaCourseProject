package models;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HRInfo {
    private int id;
    private String name;
    private String faculty;
    private String speciality;

    public static void parseResult(ResultSet rs, PrintWriter toClient) throws SQLException {
        while (rs.next())
        {
            HRInfo temp =new HRInfo();
            temp.id = rs.getInt("id");
            temp.name = rs.getString("name").strip();
            temp.faculty = rs.getString("fac").strip();
            temp.speciality = rs.getString("spec").strip();
            System.out.println("\t\t\t\t\t| " + temp);
            toClient.println(temp.toString());
        }
        System.out.println("\t\t\t\t\t| end");
        toClient.println("end");
    }
    @Override
    public String toString()
    {
        return id+"|"+name+"|"+faculty+"|"+speciality;
    }
}

package models;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FacultyInfo {
    private int id;
    private String name;
    public static void parseResult(ResultSet rs, PrintWriter toClient) throws SQLException {
        while (rs.next())
        {
            FacultyInfo temp =new FacultyInfo();
            temp.id = (int) rs.getInt("faculty_id");
            temp.name = rs.getString("name").strip();
            System.out.println("\t\t\t\t\t| " + temp);
            toClient.println(temp.toString());
        }
        System.out.println("\t\t\t\t\t| end");
        toClient.println("end");
    }


    @Override
    public String toString() {
        return id+"|"+name;
    }
}

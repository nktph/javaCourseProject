package models;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfo {
    private int id;
    private String login;
    private String role;
    private String isBlocked;
    private int studId;
    private String studName;

    public static void parseResult(ResultSet rs, PrintWriter toClient) throws SQLException {
        while (rs.next())
        {
            UserInfo temp =new UserInfo();
            temp.id = rs.getInt("user_id");
            temp.login = rs.getString("login").strip();
            temp.role = rs.getString("role").strip();
            temp.isBlocked = rs.getString("isBlocked").strip();
            temp.studId = rs.getInt("studId");
            temp.studName = rs.getString("studName");
            System.out.println("\t\t\t\t\t| " + temp);
            toClient.println(temp.toString());
        }
        System.out.println("\t\t\t\t\t| end");
        toClient.println("end");
    }
    @Override
    public String toString()
    {
        return id+"|"+login+"|"+role+"|"+isBlocked+"|"+studId+"|"+studName;
    }
}

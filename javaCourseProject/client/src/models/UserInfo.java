package models;

public class UserInfo{
    private int id;
    private String login;
    private String role;
    private String isBlocked;
    private int studId;
    private String studName;

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getRole() {
        return role;
    }

    public String getIsBlocked() {
        return isBlocked;
    }

    public int getStudId() {
        return studId;
    }

    public String getStudName() {
        return studName;
    }

    public static UserInfo parse(String temp)
    {
        UserInfo si = new UserInfo();
        String[] sa = temp.strip().split("\\|");
        si.id = Integer.parseInt(sa[0]);
        si.login = sa[1];
        si.role = sa[2];
        si.isBlocked = sa[3];
        si.studId = Integer.parseInt(sa[4]);
        si.studName = sa[5];
        return si;
    }
    @Override
    public String toString()
    {
        return id+"|"+login+"|"+role+"|"+isBlocked+"|"+ studId +"|"+ studName;
    }
}

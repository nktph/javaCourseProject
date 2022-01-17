package models;

public class FacultyInfo {
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private int id;
    private String name;
    public static FacultyInfo parse(String temp)
    {
        FacultyInfo si = new FacultyInfo();
        String[] sa = temp.split("\\|");
        si.id = Integer.parseInt(sa[0]);
        si.name = sa[1];
        return si;
    }

    @Override
    public String toString() {
        return id+"\t|\t"+name;
    }
}

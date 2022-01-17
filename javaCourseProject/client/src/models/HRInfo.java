package models;

public class HRInfo {
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getSpeciality() {
        return speciality;
    }

    private int id;
    private String name;
    private String faculty;
    private String speciality;
    public static HRInfo parse(String temp)
    {
        HRInfo si = new HRInfo();
        String[] sa = temp.split("\\|");
        si.id = Integer.parseInt(sa[0]);
        si.name = sa[1];
        si.faculty = sa[2];
        si.speciality = sa[3];
        return si;
    }

    @Override
    public String toString() {
        return id+"|"+name+"|"+ faculty +"|"+ speciality;
    }
}

package models;

public class CoefInfo {
    public int getId() {
        return id;
    }

    public String getDescr() {
        return descr;
    }

    public Double getValue() {
        return value;
    }

    private int id;
    private String descr;
    private Double value;
    public static CoefInfo parse(String temp)
    {
        CoefInfo si = new CoefInfo();
        String[] sa = temp.split("\\|");
        si.id = Integer.parseInt(sa[0]);
        si.descr = sa[1];
        si.value = Double.valueOf(sa[2]);
        return si;
    }

    @Override
    public String toString() {
        return id+"|"+descr+"|"+ value +"|";
    }
}

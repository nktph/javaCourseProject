package models;

public class ScholarshipInfo {
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Double getNOfD() {
        return nOfD;
    }

//    public Double getDeduct() {
//        return deductible;
//    }

    public Double getCoef() {
        return coef;
    }

    public Double getScholSum() {
        return scholSum;
    }

    public Double getValue() {
        return value;
    }

    public Double getAvgScore() {
        return avgScore;
    }

    private int id;
    private String name;
    private String date;
    private Double nOfD;
//    private Double deductible;
    private Double coef;
    private Double scholSum;
    private Double value;
    private Double avgScore;
    public static ScholarshipInfo parse(String temp)
    {
        ScholarshipInfo si = new ScholarshipInfo();
        String[] sa = temp.split("\\|");
        si.id = Integer.parseInt(sa[0]);
        si.name = sa[1];
        si.date = sa[3];
        si.avgScore = Double.valueOf(sa[2]);
        si.nOfD = Double.valueOf(sa[4]);
        si.coef = Double.valueOf(sa[5]);
        si.scholSum = Double.valueOf(sa[6]);
        return si;
    }

    @Override
    public String toString() {
        return id+"|"+name+"|"+date+"|"+nOfD+"|"+ coef +"|"+ scholSum;
    }
}

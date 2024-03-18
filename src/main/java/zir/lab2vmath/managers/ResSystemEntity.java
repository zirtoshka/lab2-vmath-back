package zir.lab2vmath.managers;

public class ResSystemEntity {

    private String x;
    private String y;
    private int numberOfIterations;
    private String errorVectorX;
    private String errorVectorY;


    public ResSystemEntity(String x, String y, int numberOfIterations, String errorVectorX, String errorVectorY) {
        this.x = x;
        this.y = y;
        this.numberOfIterations = numberOfIterations;
        this.errorVectorX = errorVectorX;
        this.errorVectorY=errorVectorY;
    }


}

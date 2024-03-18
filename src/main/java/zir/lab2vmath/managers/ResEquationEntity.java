package zir.lab2vmath.managers;

public class ResEquationEntity {
    private String uknownX;
    private String fun;
    private int numberOfIterations;


    public ResEquationEntity(String uknownX, String fun, int numberOfIterations) {
        this.uknownX = uknownX;
        this.fun = fun;
        this.numberOfIterations=numberOfIterations;
    }
}

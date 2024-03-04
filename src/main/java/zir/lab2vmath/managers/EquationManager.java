package zir.lab2vmath.managers;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Setter
@Getter
public class EquationManager {

    private int func;

    private int method;
    private BigDecimal firstBoundaryOfInterval;
    private BigDecimal secondBoundaryOfInterval;
    private BigDecimal inaccuracy;

    Map<Integer, Function<BigDecimal, BigDecimal>> functionMap = new HashMap<>();
    Map<Integer, Supplier<String>> methodMap = new HashMap<>();

    public EquationManager() {
        functionMap.put(1, EquationManager::firstEquation);
        methodMap.put(1, EquationManager::methodOfHalfDivision);
        methodMap.put(2, EquationManager::methodFromNewton);
        methodMap.put(3, EquationManager::simpleIterationMethod);
    }




    public String getSolvingEquation() {
        if (!checkExistenceOfRoots()) {
            return "Ваш интервал слишком сложный для меня: на нем либо несколько корней, либо нет вообще. Можно что-то попроще?";
        }
        System.out.println("dsdsd");
        System.out.println(methodMap.get(method));
        return methodMap.get(method).get();

    }

    private boolean checkExistenceOfRoots() {
        BigDecimal res = functionMap.get(func).apply(firstBoundaryOfInterval)
                .multiply(functionMap.get(func).apply(secondBoundaryOfInterval));

        if (res.compareTo(BigDecimal.ZERO) >= 0) {
            return false;
        }
        return true;
    }


    private static String methodOfHalfDivision() {
        return "dd";
    }
    private static String methodFromNewton(){
        return "newton";
    }
    private static String simpleIterationMethod(){
        return "simple";
    }

    private static BigDecimal firstEquation(BigDecimal x) {
        return x.pow(3).multiply(new BigDecimal(-2.7))
                .add(x.pow(2).multiply(new BigDecimal(-1.48)))
                .add(x.multiply(new BigDecimal(19.23)))
                .add(new BigDecimal(6.35));
    }

    private BigDecimal derivativeFirstEquation(BigDecimal x) {
        return x.pow(2).multiply(new BigDecimal(-2.7))
                .add(x.pow(1).multiply(new BigDecimal(-1.48)))
                .add(new BigDecimal(19.23));
    }


}

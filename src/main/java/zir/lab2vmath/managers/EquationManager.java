package zir.lab2vmath.managers;

import exc.BadIntervalException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static zir.lab2vmath.managers.Config.*;

@Setter
@Getter
public class EquationManager {


    private int method;
    private int func;
    private HashMap<String, BigDecimal> args = new HashMap<>();

    private Map<Integer, Function<BigDecimal, BigDecimal>> functionMap = new HashMap<>();
    private Map<Integer, Function<BigDecimal, BigDecimal>> derivativeFunctionMap = new HashMap<>();
    private Map<Integer, Function<BigDecimal, BigDecimal>> secondDerivativeFunctionMap = new HashMap<>();
    private Map<Integer, Function<HashMap<String, BigDecimal>, ResEntity>> methodMap = new HashMap<>();


    public EquationManager() {
        args.put(firstBoundaryOfInterval, null);
        args.put(secondBoundaryOfInterval, null);
        args.put(inaccuracy, null);
//        args.put(Config.func,null);

        functionMap.put(1, x -> firstEquation(x));
        functionMap.put(2, x -> secondEquation(x));
        functionMap.put(3, x -> thirdEquation(x));


        derivativeFunctionMap.put(1, x -> derivativeFirstEquation(x));
        derivativeFunctionMap.put(2, x -> derivativeSecondEquation(x));
        derivativeFunctionMap.put(3, x -> derivativeThirdEquation(x));


        secondDerivativeFunctionMap.put(1, x -> secondDerivativeFirstEquation(x));
        secondDerivativeFunctionMap.put(2, x -> secondDerivativeSecondEquation(x));
        secondDerivativeFunctionMap.put(3, x -> secondDerivativeThirdEquation(x));


        methodMap.put(1, args -> methodOfHalfDivision(args));
        methodMap.put(2, args -> methodFromNewton(args));
        methodMap.put(3, args -> simpleIterationMethod(args));
    }


    public ResEntity getSolvingEquation() throws BadIntervalException {
        checkRangeOfAcceptableValues();
        if (  !checkExistenceOfRoots()  ) {
            throw new BadIntervalException("Ваш интервал слишком сложный для меня: на нем либо несколько корней, либо нет вообще. Можно что-то попроще?");

        }
        return methodMap.get(method).apply(args);

    }

    private void checkRangeOfAcceptableValues() throws BadIntervalException{
        if (func == 3 && (args.get(firstBoundaryOfInterval).compareTo(new BigDecimal(-2)) <= 0 ||
                args.get(secondBoundaryOfInterval).compareTo(new BigDecimal(-2)) <= 0)) {
            throw new BadIntervalException("ну из одз зачем выходить-то?");
        }
    }

    private boolean checkExistenceOfRoots() {
        BigDecimal res = functionMap.get(func).apply(args.get(firstBoundaryOfInterval))
                .multiply(functionMap.get(func).apply(args.get(secondBoundaryOfInterval)));

        if (res.compareTo(BigDecimal.ZERO) >= 0) {
            return false;
        }
        return true;
    }


    private ResEntity methodOfHalfDivision(HashMap<String, BigDecimal> args) {
        BigDecimal firstBoundaryOfInterval = args.get(Config.firstBoundaryOfInterval);
        BigDecimal secondBoundaryOfInterval = args.get(Config.secondBoundaryOfInterval);
        BigDecimal inaccuracy = args.get(Config.inaccuracy);
        BigDecimal x = firstBoundaryOfInterval.add(secondBoundaryOfInterval).divide(new BigDecimal(2));
        while (functionMap.get(func).apply(x).abs().compareTo(inaccuracy) >= 0) {
            if (functionMap.get(func).apply(x)
                    .multiply(functionMap.get(func).apply(firstBoundaryOfInterval))
                    .compareTo(BigDecimal.ZERO) >= 0) { //if same sign
                firstBoundaryOfInterval = x;
            } else {
                secondBoundaryOfInterval = x;
            }
            x = firstBoundaryOfInterval.add(secondBoundaryOfInterval).divide(new BigDecimal(2));


        }
//        x = x.setScale(inaccuracy.scale(), RoundingMode.UP);
        ResEntity res = new ResEntity(x.toString(), functionMap.get(func).apply(x).setScale(inaccuracy.scale() + 3, RoundingMode.UP).toString());


        return res;
    }

    private ResEntity methodFromNewton(HashMap<String, BigDecimal> args) {
        BigDecimal firstBoundaryOfInterval = args.get(Config.firstBoundaryOfInterval);
        BigDecimal secondBoundaryOfInterval = args.get(Config.secondBoundaryOfInterval);
        BigDecimal inaccuracy = args.get(Config.inaccuracy);
        BigDecimal x;
        if (functionMap.get(func).apply(firstBoundaryOfInterval)
                .multiply(secondDerivativeFunctionMap.get(func).apply(firstBoundaryOfInterval))
                .compareTo(BigDecimal.ZERO) > 0) {
            x = firstBoundaryOfInterval;
        } else if (functionMap.get(func).apply(secondBoundaryOfInterval)
                .multiply(secondDerivativeFunctionMap.get(func).apply(secondBoundaryOfInterval))
                .compareTo(BigDecimal.ZERO) > 0) {
            x = secondBoundaryOfInterval;
        } else {
            //todo wtf
//            System.out.println(33333);
            x = firstBoundaryOfInterval;
//            return null;
        }

        //todo chodimost'
        while (functionMap.get(func).apply(x).abs().compareTo(inaccuracy) >= 0) {

            x = x.subtract(functionMap.get(func).apply(x).divide(derivativeFunctionMap.get(func).apply(x), MathContext.DECIMAL32));
        }
        System.out.println(x.toString());
        ResEntity res = new ResEntity(x.toString(), functionMap.get(func).apply(x).setScale(inaccuracy.scale() + 3, RoundingMode.UP).toString());
        return res;
    }

    private ResEntity simpleIterationMethod(HashMap<String, BigDecimal> args) {
        BigDecimal firstBoundaryOfInterval = args.get(Config.firstBoundaryOfInterval);
        BigDecimal secondBoundaryOfInterval = args.get(Config.secondBoundaryOfInterval);
        BigDecimal inaccuracy = args.get(Config.inaccuracy);
        BigDecimal x = derivativeFunctionMap.get(func).apply(firstBoundaryOfInterval).abs()
                .compareTo(derivativeFunctionMap.get(func).apply(secondBoundaryOfInterval).abs()) >= 0 ? firstBoundaryOfInterval : secondBoundaryOfInterval;
        BigDecimal lambda;
        if (derivativeFunctionMap.get(func).apply(firstBoundaryOfInterval).compareTo(BigDecimal.ZERO) >= 0) {
            lambda = new BigDecimal(-1);
        } else {
            lambda = BigDecimal.ONE;
        }
        lambda = lambda.divide(
                derivativeFunctionMap.get(func).apply(firstBoundaryOfInterval).abs()
                        .max(derivativeFunctionMap.get(func).apply(secondBoundaryOfInterval).abs()), new MathContext(inaccuracy.scale() + 2));
        System.out.println("lambda " + lambda.toString());
        int k = 0;
        while (functionMap.get(func).apply(x).abs().compareTo(inaccuracy) >= 0) {
            k++;
            System.out.println("koko " + functionMap.get(func).apply(x));

            x = lambda.multiply(functionMap.get(func).apply(x)).setScale(inaccuracy.scale() + 2, RoundingMode.UP).add(x);
            System.out.println(x.toString());
        }
        ResEntity res = new ResEntity(x.toString(), functionMap.get(func).apply(x).setScale(inaccuracy.scale() + 3, RoundingMode.UP).toString());

        return res;
    }

    //1st
    private BigDecimal firstEquation(BigDecimal x) {
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

    private BigDecimal secondDerivativeFirstEquation(BigDecimal x) {
        return x.pow(1).multiply(new BigDecimal(-2.7))
                .add((new BigDecimal(-1.48)));

    }

    //2nd
    private BigDecimal secondEquation(BigDecimal x) {
        return x.pow(3).subtract(x).add(new BigDecimal(4));
    }

    private BigDecimal derivativeSecondEquation(BigDecimal x) {
        return x.pow(2).multiply(new BigDecimal(3)).subtract(BigDecimal.ONE);
    }

    private BigDecimal secondDerivativeSecondEquation(BigDecimal x) {
        return x.multiply(new BigDecimal(6));

    }

    //3th
    private BigDecimal thirdEquation(BigDecimal x) {
        return new BigDecimal(Math.log10(x.doubleValue() + 2)).multiply(new BigDecimal(5)).subtract(new BigDecimal(3));
    }

    private BigDecimal derivativeThirdEquation(BigDecimal x) {
        return new BigDecimal(5).divide(x.add(new BigDecimal(2)), new MathContext(5)).divide(BigDecimal.valueOf(Math.log(10)), new MathContext(5));
    }

    private BigDecimal secondDerivativeThirdEquation(BigDecimal x) {
        return new BigDecimal(-5).divide(x.add(new BigDecimal(2)), new MathContext(5)).divide(x.add(new BigDecimal(2)), new MathContext(5)).divide(BigDecimal.valueOf(Math.log(10)), new MathContext(5));

    }

}

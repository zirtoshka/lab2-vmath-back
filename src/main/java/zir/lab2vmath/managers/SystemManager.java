package zir.lab2vmath.managers;

import exc.СonvergenceСonditionException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static zir.lab2vmath.managers.Config.*;


@Getter
@Setter
public class SystemManager {
    private int method;
    private int system;
    private HashMap<String, BigDecimal> args = new HashMap<>();

    private Map<Integer, Function<BigDecimal[], BigDecimal>> systemMapX = new HashMap<>();
    private Map<Integer, Function<BigDecimal[], BigDecimal>> systemMapY = new HashMap<>();

    //    private Map<Integer, Function<BigDecimal, BigDecimal>> derivativeFunctionMap = new HashMap<>();
//    private Map<Integer, Function<BigDecimal, BigDecimal>> secondDerivativeFunctionMap = new HashMap<>();
    private Map<Integer, Function<BigDecimal[], Boolean>> checkSystemMap = new HashMap<>();
//    private Map<Integer, Function<HashMap<String, BigDecimal>, ResSystemEntity>> methodMap = new HashMap<>();

    public SystemManager() {
        args.put(initialApproximationByX, null);
        args.put(initialApproximationByY, null);
        args.put(inaccuracy, null);

        systemMapX.put(1, this::firstSystemX);
        systemMapY.put(1, this::firstSystemY);

        systemMapX.put(2, this::secondSystemX);
        systemMapY.put(2, this::secondSystemY);


        checkSystemMap.put(2, this::checkSecondSystem);
        checkSystemMap.put(1, this::checkFirstSystem);
    }

    public ResSystemEntity getSolvingSystem() throws СonvergenceСonditionException {
        if (!checkSystemMap.get(system).apply(args.values().toArray(new BigDecimal[0]))) {
            throw new СonvergenceСonditionException("не выполняется условие сходимости для метода");

        }

        System.out.println("sdfsdfsfds");
        return simpleIterationsMethod();
//        return methodMap.get(method).apply(args);

    }

    private ResSystemEntity simpleIterationsMethod() {

        BigDecimal x = args.get(initialApproximationByX);
        BigDecimal y = args.get(initialApproximationByY);
        System.out.println(x + "   " + y);
        BigDecimal inaccuracy = args.get(Config.inaccuracy);
        BigDecimal[] xy = new BigDecimal[]{x, y};
        int numberOfIterations = 0;
        do {
            numberOfIterations++;
            xy[0] = x;
            xy[1] = y;
            x=systemMapX.get(system).apply(xy).setScale(inaccuracy.scale() + 5, RoundingMode.UP);
//            x = secondSystemX(xy).setScale(inaccuracy.scale() + 5, RoundingMode.UP);
            y = systemMapY.get(system).apply(xy).setScale(inaccuracy.scale() + 5, RoundingMode.UP);

        }
        while (x.subtract(xy[0]).abs().compareTo(inaccuracy) >= 0 ||
                y.subtract(xy[1]).abs().compareTo(inaccuracy) >= 0);


        return new ResSystemEntity(x.toString(), y.toString(), numberOfIterations);

    }

    private boolean checkFirstSystem(BigDecimal[] xy) {
        BigDecimal x = xy[0];
        BigDecimal y = xy[1];

        if ((BigDecimal.valueOf(Math.sin(y.doubleValue())/3) .abs()).compareTo(BigDecimal.ONE) > 0) {
            System.out.println("111111ssss");
            return false;
        }
        if (
                BigDecimal.valueOf(Math.cos((5*x.doubleValue()-3)/5))
                        .abs().compareTo(BigDecimal.ONE) > 0
        ) {
            System.out.println("akakaka");
            return false;
        }
        System.out.println("kokokoo");
        return true;
    }

    private BigDecimal firstSystemX(BigDecimal[] xy) {
        return BigDecimal.valueOf(Math.cos(xy[1].doubleValue()))
                .divide(BigDecimal.valueOf(3), MathContext.DECIMAL32)
                .add(BigDecimal.valueOf(0.3));
    }

    private BigDecimal firstSystemY(BigDecimal[] xy) {
        return BigDecimal.valueOf(Math.sin(xy[0].doubleValue()-0.6))
                .add(BigDecimal.valueOf(-1.6));
    }




//    private boolean checkFirstSystem(BigDecimal[] xy) {
//        BigDecimal x = xy[0];
//        BigDecimal y = xy[1];
//        double a =  x.multiply(y).add(BigDecimal.valueOf(0.3)).doubleValue();
//
//        if(Math.tan(a)<0){
//            return false;
//        }
////        System.out.println(Math.tan(a));
//        BigDecimal b = BigDecimal.valueOf(2 * Math.cos(a) * Math.cos(a) * Math.sqrt(Math.tan(a)));
////        System.out.println(b);
//        if (y.divide(b, MathContext.DECIMAL32).abs().add(
//                x.divide(b, MathContext.DECIMAL32).abs()).compareTo(BigDecimal.ONE) > 0) {
//            System.out.println("111111ssss");
//            return false;
//        }
//        if (
//                BigDecimal.valueOf(-0.45)
//                        .multiply(BigDecimal.valueOf(Math.sqrt(2)))
//                        .multiply(x)
//                        .divide(BigDecimal.valueOf(Math.sqrt(1 - 0.9 * x.multiply(x).doubleValue())), MathContext.DECIMAL32)
//                        .abs().compareTo(BigDecimal.ONE) > 0
//        ) {
//            System.out.println("akakaka");
//            return false;
//        }
//        System.out.println("kokokoo");
//        return true;
//    }
//
//    private BigDecimal firstSystemX(BigDecimal[] xy) {
//        return BigDecimal.valueOf(Math.sqrt(Math.tan(xy[0].multiply(xy[1]).add(BigDecimal.valueOf(0.3)).doubleValue())));
//    }
//
//    private BigDecimal firstSystemY(BigDecimal[] xy) {
//        return BigDecimal.valueOf(Math.sqrt(
//                BigDecimal.ONE.subtract(
//                        xy[0].pow(2).multiply(BigDecimal.valueOf(0.9))).divide(BigDecimal.valueOf(2),MathContext.DECIMAL32).doubleValue()
//        ));
//    }


    private BigDecimal secondSystemX(BigDecimal[] xy) {
        return new BigDecimal(0.3).
                add(xy[0].pow(2).multiply(BigDecimal.valueOf(-0.1))).
                add(xy[1].pow(2).multiply(BigDecimal.valueOf(-0.2)));
    }

    private BigDecimal secondSystemY(BigDecimal[] xy) {
        return new BigDecimal("0.7").
                add(xy[0].pow(2).multiply(BigDecimal.valueOf(-0.2))).
                add(xy[0].multiply(xy[1]).multiply(BigDecimal.valueOf(-0.1)));
    }

    private boolean checkSecondSystem(BigDecimal[] xy) {
        //проверка условия сходимости
        BigDecimal x = xy[0];
        BigDecimal y = xy[1];
        if (x.multiply(BigDecimal.valueOf(-0.2)).abs()
                .add(y.multiply(BigDecimal.valueOf(-0.4)).abs()).compareTo(BigDecimal.ONE) > 0) {
            System.out.println("gogogog");
            return false;
        }
        if ((x.multiply(BigDecimal.valueOf(-0.4))
                .add(y.multiply(BigDecimal.valueOf(-0.1)))).abs()
                .add(x.multiply(BigDecimal.valueOf(-0.1)).abs()).compareTo(BigDecimal.ONE) > 0
        ) {
            System.out.println("akakaka");
            return false;
        }
        System.out.println("kokokoo");
        return true;
    }
}
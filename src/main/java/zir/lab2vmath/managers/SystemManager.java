package zir.lab2vmath.managers;

import exc.СonvergenceСonditionException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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

    private Map<String, Function<BigDecimal[], BigDecimal>> systemMap = new HashMap<>();
    //    private Map<Integer, Function<BigDecimal, BigDecimal>> derivativeFunctionMap = new HashMap<>();
//    private Map<Integer, Function<BigDecimal, BigDecimal>> secondDerivativeFunctionMap = new HashMap<>();
    private Map<Integer, Function<BigDecimal[], Boolean>> checkSystemMap = new HashMap<>();
//    private Map<Integer, Function<HashMap<String, BigDecimal>, ResSystemEntity>> methodMap = new HashMap<>();

    public SystemManager() {
        args.put(initialApproximationByX, null);
        args.put(initialApproximationByY, null);
        args.put(inaccuracy, null);

        checkSystemMap.put(2, this::checkSecondSystem);
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
        int k = 0;
        do {
            k++;
            xy[0] = x;
            xy[1] = y;
            x = secondSystemX(xy).setScale(inaccuracy.scale() + 5, RoundingMode.UP);
            y = secondSystemY(xy).setScale(inaccuracy.scale() + 5, RoundingMode.UP);

        }
        while (x.subtract(xy[0]).abs().compareTo(inaccuracy) >= 0 ||
                y.subtract(xy[1]).abs().compareTo(inaccuracy) >= 0);


        return new ResSystemEntity(x.toString(), y.toString());

    }

    public boolean checkFirstSystem(BigDecimal[] xy) {
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


    public BigDecimal secondSystemX(BigDecimal[] xy) {
        return new BigDecimal(0.3).
                add(xy[0].pow(2).multiply(BigDecimal.valueOf(-0.1))).
                add(xy[1].pow(2).multiply(BigDecimal.valueOf(-0.2)));
    }

    public BigDecimal secondSystemY(BigDecimal[] xy) {
        return new BigDecimal("0.7").
                add(xy[0].pow(2).multiply(BigDecimal.valueOf(-0.2))).
                add(xy[0].multiply(xy[1]).multiply(BigDecimal.valueOf(-0.1)));
    }

    public boolean checkSecondSystem(BigDecimal[] xy) {
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
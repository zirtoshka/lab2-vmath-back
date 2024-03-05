package zir.lab2vmath.contr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import exc.BadIntervalException;
import exc.СonvergenceСonditionException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import zir.lab2vmath.managers.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static zir.lab2vmath.managers.Config.*;

@RestController
@RequestMapping("/app-controller")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor

public class AppController {
    private final EquationManager equationManager = new EquationManager();
    private final SystemManager systemManager=new SystemManager();

    @GetMapping
    public ResponseEntity<String> sayHello() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        System.out.println("it's method sayHello");
        return new ResponseEntity<>("{\"message\": \"Hello from secured endpoint\"}", httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/equation")
    public ResponseEntity<String> equation(@Valid @RequestBody RequestEquationInfo requestEquationInfo) throws BadIntervalException, JsonProcessingException {
        final HttpHeaders httpHeaders = new HttpHeaders();
        equationManager.setFunc(requestEquationInfo.getFunc());

        equationManager.setMethod(requestEquationInfo.getMethod());
        System.out.println(requestEquationInfo.getFirstBoundaryOfInterval());
        equationManager.getArgs().put(firstBoundaryOfInterval, new BigDecimal(requestEquationInfo.getFirstBoundaryOfInterval().replace(",", ".")));
        equationManager.getArgs().put(secondBoundaryOfInterval, new BigDecimal(requestEquationInfo.getSecondBoundaryOfInterval().replace(",", ".")));
        equationManager.getArgs().put(inaccuracy, new BigDecimal(requestEquationInfo.getInaccuracy().replace(",", ".")));
//                System.out.println(equationManager.getArgs().get(firstBoundaryOfInterval)+" fdfd "+equationManager.getArgs().get(func));

//            System.out.println(equationManager.getSolvingEquation()[0] + "  " + equationManager.getSolvingEquation()[1]);
        ResEquationEntity res = equationManager.getSolvingEquation();
        if (res == null) {
            throw new BadIntervalException("не выполняется условие сходимости для метода");
        }
        Gson gson = new Gson();
        String json = gson.toJson(res);

        return new ResponseEntity<>(json, httpHeaders, HttpStatus.OK);
    }
    @PostMapping("/system")
    public ResponseEntity<String> system(@Valid @RequestBody RequestSystemInfo requestSystemInfo) throws JsonProcessingException, СonvergenceСonditionException {
        final HttpHeaders httpHeaders = new HttpHeaders();
        systemManager.setSystem(requestSystemInfo.getSystem());
        systemManager.setMethod(requestSystemInfo.getMethod());

        systemManager.getArgs().put(initialApproximationByX, new BigDecimal(requestSystemInfo.getInitialApproximationByX().replace(",", ".")));
        systemManager.getArgs().put(initialApproximationByY, new BigDecimal(requestSystemInfo.getInitialApproximationByY().replace(",", ".")));
        systemManager.getArgs().put(inaccuracy, new BigDecimal(requestSystemInfo.getInaccuracy().replace(",", ".")));
//                System.out.println(equationManager.getArgs().get(firstBoundaryOfInterval)+" fdfd "+equationManager.getArgs().get(func));

//            System.out.println(equationManager.getSolvingEquation()[0] + "  " + equationManager.getSolvingEquation()[1]);
        ResSystemEntity res = systemManager.getSolvingSystem();
        if (res == null) {
            throw new СonvergenceСonditionException("не выполняется условие сходимости для метода");
        }
        Gson gson = new Gson();
        String json = gson.toJson(res);

        return new ResponseEntity<>(json, httpHeaders, HttpStatus.OK);
    }
    @ExceptionHandler(BadIntervalException.class)
    public ResponseEntity<?> handleValidationExceptions(BadIntervalException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(СonvergenceСonditionException.class)
    public ResponseEntity<?> handleValidationExceptions(СonvergenceСonditionException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(  MethodArgumentNotValidException ex) {
        System.out.println("eroooooooooor");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}

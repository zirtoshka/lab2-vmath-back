package zir.lab2vmath.contr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import exc.BadIntervalException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import zir.lab2vmath.managers.EquationManager;
import zir.lab2vmath.managers.RequestInfo;
import zir.lab2vmath.managers.ResEntity;

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

    @GetMapping
    public ResponseEntity<String> sayHello() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        System.out.println("it's method sayHello");
        return new ResponseEntity<>("{\"message\": \"Hello from secured endpoint\"}", httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/equation")
    public ResponseEntity<String> equation(@Valid @RequestBody RequestInfo requestInfo) throws BadIntervalException, JsonProcessingException {
        final HttpHeaders httpHeaders = new HttpHeaders();
        equationManager.setFunc(requestInfo.getFunc());

        equationManager.setMethod(requestInfo.getMethod());
        System.out.println(requestInfo.getFirstBoundaryOfInterval());
        equationManager.getArgs().put(firstBoundaryOfInterval, new BigDecimal(requestInfo.getFirstBoundaryOfInterval().replace(",", ".")));
        equationManager.getArgs().put(secondBoundaryOfInterval, new BigDecimal(requestInfo.getSecondBoundaryOfInterval().replace(",", ".")));
        equationManager.getArgs().put(inaccuracy, new BigDecimal(requestInfo.getInaccuracy().replace(",", ".")));
//                System.out.println(equationManager.getArgs().get(firstBoundaryOfInterval)+" fdfd "+equationManager.getArgs().get(func));

//            System.out.println(equationManager.getSolvingEquation()[0] + "  " + equationManager.getSolvingEquation()[1]);
        ResEntity res = equationManager.getSolvingEquation();
        if (res == null) {
            throw new BadIntervalException("не выполняется условие сходимости для метода");
        }
        Gson gson = new Gson();
        String json = gson.toJson(equationManager.getSolvingEquation());

        return new ResponseEntity<>(json, httpHeaders, HttpStatus.OK);

    }

    @ExceptionHandler(BadIntervalException.class)
    public ResponseEntity<?> handleValidationExceptions(BadIntervalException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

package zir.lab2vmath.contr;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import zir.lab2vmath.managers.EquationManager;
import zir.lab2vmath.managers.RequestInfo;

import java.math.BigDecimal;

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
    public ResponseEntity<String> equation(@Valid @RequestBody RequestInfo requestInfo) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        equationManager.setFunc(requestInfo.getFunc());
        equationManager.setMethod(requestInfo.getMethod());
        System.out.println(requestInfo.getFirstBoundaryOfInterval());
        equationManager.setFirstBoundaryOfInterval(new BigDecimal(requestInfo.getFirstBoundaryOfInterval().replace(",",".")));
        equationManager.setSecondBoundaryOfInterval(new BigDecimal(requestInfo.getSecondBoundaryOfInterval().replace(",",".")));
        equationManager.setInaccuracy(new BigDecimal(requestInfo.getInaccuracy().replace(",",".")));
                System.out.println(equationManager.getFirstBoundaryOfInterval()+" fdfd "+equationManager.getSecondBoundaryOfInterval());

        return new ResponseEntity<>("{\"message\":" + equationManager.getSolvingEquation()+"}", httpHeaders, HttpStatus.OK);
    }
}

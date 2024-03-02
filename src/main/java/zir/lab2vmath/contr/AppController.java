package zir.lab2vmath.contr;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app-controller")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AppController {
    @GetMapping
    public ResponseEntity<String> sayHello() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        System.out.println("it's method sayHello");
        return new ResponseEntity<>("{\"message\": \"Hello from secured endpoint\"}", httpHeaders, HttpStatus.OK);
    }
    @PostMapping("/equation")
    public ResponseEntity<String> equation() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>("{\"message\": \"Hello from secured endpoint\"}", httpHeaders, HttpStatus.OK);
    }
}

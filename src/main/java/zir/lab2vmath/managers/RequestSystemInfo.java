package zir.lab2vmath.managers;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RequestSystemInfo {

    @NotNull(message = "function must not be null")
    @DecimalMin(value = "1", message = "id function must be greater than or equal to 1")
    @DecimalMax(value = "2", message = "id function must be less than or equal to 5")
    private int system;
    @NotNull(message = "method must not be null")
    @DecimalMin(value = "1", message = "id method must be greater than or equal to 1")
    @DecimalMax(value = "1", message = "id method must be less than or equal to 3")
    private int method;

    @NotNull(message = "the initial approximation by X must not be null")
    @Pattern(regexp = "^[-+]?[0-9]*[\\.,]?[0-9]+([eE][-+]?[0-9]+)?$")
    private String initialApproximationByX;
    @NotNull(message = "the initial approximation by Y must not be null")
    @Pattern(regexp = "^[-+]?[0-9]*[\\.,]?[0-9]+([eE][-+]?[0-9]+)?$")
    private String initialApproximationByY;
    @NotNull
    @DecimalMin(value = "0", message = "inaccuracy must be greater than or equal to 0")
    @DecimalMax(value = "0.1", message = "inaccuracy must be less than or equal to 0.1")
    @Pattern(regexp = "^[-+]?[0-9]*[\\.,]?[0-9]+([eE][-+]?[0-9]+)?$")
    private String inaccuracy;

}

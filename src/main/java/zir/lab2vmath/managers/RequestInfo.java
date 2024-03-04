package zir.lab2vmath.managers;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;


@Setter
@Getter
@AllArgsConstructor
public class RequestInfo {
    @NotNull(message = "function must not be null")
    @DecimalMin(value = "1", message = "id function must be greater than or equal to 1")
    @DecimalMax(value = "5", message = "id function must be less than or equal to 5")
    private int func;
    @NotNull(message = "method must not be null")
    @DecimalMin(value = "1", message = "id method must be greater than or equal to 1")
    @DecimalMax(value = "3", message = "id method must be less than or equal to 3")
    private int method;

    @NotNull(message = "the first boundary of the interval must not be null")
    @Pattern(regexp = "-?\\\\d+([\\\\.,]\\\\d+)?")
    private String firstBoundaryOfInterval;
    @NotNull(message = "the second boundary of the interval must not be null")
    @Pattern(regexp = "-?\\\\d+([\\\\.,]\\\\d+)?")
    private String secondBoundaryOfInterval;
    @NotNull
    @DecimalMin(value = "0", message = "inaccuracy must be greater than or equal to 0")
    @DecimalMax(value = "0.1", message = "inaccuracy must be less than or equal to 0.1")
    @Pattern(regexp = "-?\\\\d+([\\\\.,]\\\\d+)?")
    private String inaccuracy;
}

package com.marketplace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Schema(
        name = "ErrorResponse",
        description = "Schema to hold error response information"
)
@Data
@AllArgsConstructor
public class ErrorResponseDto {
    @Schema(
            description = "API path invoked by client"
    )
    private String apiPath;
    @Schema(
            description = "Error code representing the error happend"
    )
    private HttpStatus httpStatus;
    @Schema(
            description = "Error message representing the error happend"
    )
    private String errorMessage;
    @Schema(
            description = "Time representing the error happend"
    )
    private LocalDateTime errorTime;
}

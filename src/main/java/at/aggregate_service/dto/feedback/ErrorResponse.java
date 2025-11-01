package at.aggregate_service.dto.feedback;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse { // Represents error details when part of the data could not be retrieved
    private String message;
    private String reason;
}

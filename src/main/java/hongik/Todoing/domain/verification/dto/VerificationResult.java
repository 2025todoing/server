package hongik.Todoing.domain.verification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerificationResult {
    private final boolean success;
    private final double confidence;
}

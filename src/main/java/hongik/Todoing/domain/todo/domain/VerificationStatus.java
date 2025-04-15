package hongik.Todoing.domain.todo.domain;

public enum VerificationStatus {
    NONE("인증 안됨"),
    SUCCESS("인증 성공"),
    FAIL("인증 실패");

    private final String status;

    VerificationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

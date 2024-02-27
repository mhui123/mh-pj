package com.bandi.mhProject.constants;

public enum ErrorCode {
    ERROR_CODE_900("900:에러발생"),
    ERROR_CODE_901("901:유저를 찾을 수 없습니다."),
    ERROR_CODE_902("902:기존 비밀번호가 다릅니다."),
    ERROR_CODE_903("903:기존 아이디가 존재합니다."),
    ERROR_CODE_904("904:비밀번호 초기화 실패한 계정이 존재합니다."),
    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

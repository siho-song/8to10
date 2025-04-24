package com.eighttoten.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {
    INVALID_REQUEST(1000, "잘못된 요청입니다."),
    WRITER_NOT_EQUAL_MEMBER(1001, "작성자와 클라이언트가 일치하지 않습니다."),
    NOT_FOUND_MEMBER(1002, "유저를 찾을 수 없습니다."),
    SIGNUP_FAILED(1003, "회원가입 실패"),


    NOT_FOUND_SCHEDULE(2000, "일정을 찾을 수 없습니다."),
    NOT_FOUND_F_SCHEDULE(2001, "고정일정을 찾을 수 없습니다"),
    NOT_FOUND_F_DETAIL(2011, "고정일정 세부사항을 찾을 수 없습니다"),

    NOT_FOUND_V_SCHEDULE(2500, "변동일정을 찾을 수 없습니다."),

    NOT_FOUND_N_SCHEDULE(3001, "일반일정을 찾을 수 없습니다"),
    INVALID_N_SCHEDULE_CREATION(3002, "일반 일정을 생성할 수 없습니다. 일정을 조율해주세요."),
    NOT_FOUND_N_DETAIL(3011, "일반일정을 세부사항을 찾을 수 없습니다"),
    NOT_EXIST_N_DETAIL(3012, "업데이트 하려는 일반일정이 존재하지 않습니다."),
    NOT_WITHIN_WORK_TIME(3100, "일과시간 내에 존재하지않는 타임슬롯이 있습니다."),


    NOT_EQUAL_POST(3501, "부모댓글의 게시글과 대댓글의 게시글이 일치하지 않습니다."),
    DUPLICATED_POST_HEART(3502, "이미 좋아요 한 게시글입니다."),
    NOT_FOUND_POST(3503, "게시글을 찾을 수 없습니다."),
    NOT_FOUND_POST_HEART(3504, "게시글 좋아요를 찾을 수 없습니다."),
    NOT_FOUND_POST_SCRAP(3505, "게시글 스크랩을 찾을 수 없습니다."),
    DUPLICATED_POST_SCRAP(3506, "이미 스크랩 한 게시글입니다."),

    INVALID_REPLY_SAVE(4001, "댓글을 생성할 수 없습니다"),
    NOT_FOUND_REPLY(4011, "댓글을 찾을 수 없습니다."),
    INVALID_REPLY_LEVEL(4012, "대댓글에는 댓글을 달 수 없습니다."),
    NOT_FOUND_REPLY_HEART(4021, "댓글 좋아요를 찾을 수 없습니다."),
    DUPLICATED_REPLY_HEART(4022, "이미 좋아요 한 댓글입니다."),

    REQUIRE_IMAGE_FILE(4501, "이미지 파일만 등록 가능합니다."),
    REQUIRE_FILE_NAME(4502, "파일 이름은 필수입니다."),
    REQUIRE_CONTENT_TYPE(4503, "파일의 Content-Type은 필수 입니다."),

    NOT_FOUND_NOTIFICATION(5001,"알림을 찾을 수 없습니다."),

    NOT_FOUND_ACHIEVEMENT(5501,"성취도를 찾을 수 없습니다"),
    INVALID_ACHIEVEMENT_AMOUNT(5502, "유효하지 않은 성취량입니다."),
    NOT_EQUAL_DATE(5503, "업데이트 하려는 날짜와 업데이트 하려는 일정의 날짜가 일치해야 합니다."),

    NOT_FOUND_REFRESH_TOKEN(8001, "REFRESH TOKEN 을 찾을 수 없습니다"),
    INVALID_ACCESS_TOKEN(8002,"유효하지 않은 ACCESS TOKEN 입니다."),
    INVALID_REFRESH_TOKEN(8003,"유효하지 않은 REFRESH TOKEN 입니다."),
    USER_AUTHENTICATE_FAIL(8004, "사용자의 인증에 실패하였습니다."),
    INVALID_PASSWORD(8005, "비밀번호가 유효하지 않습니다"),
    INVALID_EMAIL(8006, "이메일이 유효하지 않습니다."),

    INVALID_REDIS_MESSAGE(9001, "유효하지 않은 Redis Message 입니다."),
    FAILED_SSE_NOTIFICATION_SEND(9002, "알림전송에 실패하였습니다."),
    FAILED_FILE_DELETE(9100,"파일 삭제 실패"),
    INTERNAL_SERVER_ERROR(9999,"서버 내부 오류");

    private final int code;
    private final String message;
}
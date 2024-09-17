package onyx.challenge.domain;

public enum ChallengeResultStatus {
    ON_GOING, // 기간 내 진행 중
    WAITING, // (기간 종료로 인한) 결과 대기 중
    SUCCEEDED, // 성공
    FAILED, // 실패
}

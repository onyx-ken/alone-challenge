package onyx.challenge.framework.adapter.inbound.web.api;

import lombok.Getter;

@Getter
public enum ApiStatus {

    SUCCESS(200, "Success"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int statusCode;
    private final String defaultMessage;

    ApiStatus(int statusCode, String defaultMessage) {
        this.statusCode = statusCode;
        this.defaultMessage = defaultMessage;
    }
}

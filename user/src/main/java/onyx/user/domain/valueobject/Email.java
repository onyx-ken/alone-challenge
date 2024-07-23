package onyx.user.domain.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    private String localPart;

    private String domainPart;

    public static Email create(String localPart, String domainPart) {
        return new Email(localPart, domainPart);
    }

    public static Email createFromFullAddress(String fullEmailAddress) {
        if (!isValidEmail(fullEmailAddress)) {
            throw new IllegalArgumentException("Invalid email address format.");
        }
        String[] parts = fullEmailAddress.split("@");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Email address must contain exactly one '@' character.");
        }
        return new Email(parts[0], parts[1]);
    }

    public String getEmailAddress() {
        return localPart + "@" + domainPart;
    }

    public String getLocalPart() {
        return localPart;
    }

    public String getDomainPart() {
        return domainPart;
    }

    private static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private Email(String localPart, String domainPart) {
        if (!isValidEmail(localPart + "@" + domainPart)) {
            throw new IllegalArgumentException("Invalid email address format.");
        }
        this.localPart = localPart;
        this.domainPart = domainPart;
    }

}

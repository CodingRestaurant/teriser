/*
 * Author : 나상혁 : Kasania, 박찬형
 * Filename : Email
 * Desc :
 */
package com.codrest.teriser.developers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.regex.Pattern.matches;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class Email {
    private String address;

    public Email(String address){
        checkArgument(isNotEmpty(address), "address must not empty");
        checkArgument(address.length() >= 4 && address.length() <= 50,
                "address length must be between " + 4 +
                        " and " + 50 + " characters");
        checkArgument(checkAddress(address), "invalid email address");

        this.address = address;
    }

    public static Email of(String address) {
        return new Email(address);
    }

    private boolean checkAddress(String address){
        return matches("[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", address);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.SHORT_PREFIX_STYLE)
                .append("address", address)
                .toString();
    }
}

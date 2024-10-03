package org.skrmnj.membermanagement.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static boolean arrayContains(String checkString, String... checkAgainst) {
        if (null == checkString || null == checkAgainst || 0 == checkAgainst.length) {
            return false;
        }
        return Arrays.stream(checkAgainst).anyMatch(checkString::equalsIgnoreCase);
    }

}

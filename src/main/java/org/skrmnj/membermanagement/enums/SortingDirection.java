package org.skrmnj.membermanagement.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.skrmnj.membermanagement.utilities.Utils;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SortingDirection {

    NONE(""),
    ASCENDING("ASC"),
    DESCENDING("DESC"),
    ASC("ASC"),
    DESC("DESC");

    private final String dbDirection;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SortingDirection fromSortingDirection(String sortingDirection) {
        return Arrays.stream(SortingDirection.values()).filter(x -> Utils.arrayContains(sortingDirection, x.name(), x.getDbDirection())).findFirst().orElse(NONE);
    }

    public boolean match(String... sortingDirection) {
        return Arrays.stream(sortingDirection).anyMatch(s -> Utils.arrayContains(s, this.name(), this.dbDirection));
    }

}

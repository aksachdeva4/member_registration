package org.skrmnj.membermanagement.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.skrmnj.membermanagement.utilities.Utils;

import java.util.Arrays;

@Slf4j
@Getter
@AllArgsConstructor
public enum MemberListSortingColumns {

    NONE("userid"), MEMBER_ID("userid"), FIRST_NAME("first_name"), LAST_NAME("last_name"), ADDITIONAL_MEMBERS_COUNT("additional_members_count");

    private final String columnName;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static MemberListSortingColumns fromMemberListSortingColumns(String columnName) {
        log.debug(columnName);
        return Arrays.stream(MemberListSortingColumns.values()).filter(x -> Utils.arrayContains(columnName, x.name(), x.getColumnName())).findFirst().orElse(NONE);
    }

    public boolean match(String... columnNames) {
        return Arrays.stream(columnNames).anyMatch(s -> Utils.arrayContains(s, this.name(), this.columnName));
    }

    public boolean match(MemberListSortingColumns... columnNames) {
        return Arrays.stream(columnNames).anyMatch(s -> Utils.arrayContains(s.name(), this.name()));
    }

}

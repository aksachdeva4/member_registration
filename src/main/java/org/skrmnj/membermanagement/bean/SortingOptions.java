package org.skrmnj.membermanagement.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SortingOptions {

    private String columnName;
    private String sortingDirection;

}

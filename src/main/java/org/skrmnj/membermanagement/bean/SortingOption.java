package org.skrmnj.membermanagement.bean;

import lombok.Data;
import lombok.experimental.Accessors;
import org.skrmnj.membermanagement.enums.SortingDirection;

@Data
@Accessors(chain = true)
public class SortingOption<E extends Enum<E>> {

    private E sortingColumn;
    private SortingDirection sortingDirection;

}

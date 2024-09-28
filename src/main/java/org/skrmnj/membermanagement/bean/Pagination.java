package org.skrmnj.membermanagement.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Pagination {

    private long totalRows;
    private int currentPage;
    private int rowsPerPage;
    private int totalPages;
    private int loadPage;

}

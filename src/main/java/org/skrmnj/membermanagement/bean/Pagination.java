package org.skrmnj.membermanagement.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Pagination {

    private long totalRows;
    private long currentPage;
    private long rowsPerPage;
    private long totalPages;
    private long loadPage;

    public Pagination setTotalRows(long totalRows) {
        this.totalRows = totalRows;

        if (totalRows > 0) {
            this.totalPages = (int) Math.ceil((double) totalRows / rowsPerPage);
        }
        return this;
    }
}

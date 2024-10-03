import {Component, QueryList, ViewChildren} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {DecimalPipe, NgForOf} from "@angular/common";
import {HttpClient} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {NgbNavModule, NgbPaginationModule} from "@ng-bootstrap/ng-bootstrap";
import {NgbdSortableHeader} from "../custom-components/table-sort-helper/ngbd-sortable-header.directive";
import {SortDirection, SortEvent} from "../custom-components/table-sort-helper/sort-event";

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, DecimalPipe, NgForOf, FormsModule, NgbNavModule, NgbPaginationModule, NgbdSortableHeader],
    templateUrl: './app.component.html',
    styleUrl: './app.component.scss'
})
export class AppComponent {
    title = 'frontend';

    firstName = '';
    lastName = '';
    active = 2;
    sortingOption: { sortingColumn: string, sortingDirection: SortDirection } = {
        sortingColumn: 'id',
        sortingDirection: "asc"
    }
    members: any = []
    pagination = {
        totalRows: 0,
        currentPage: 1,
        rowsPerPage: 20,
        totalPages: 0,
        loadPage: 1,
    }

    @ViewChildren(NgbdSortableHeader) headers?: QueryList<NgbdSortableHeader>;

    constructor(private httpClient: HttpClient) {
        this.filterMembers();
    }

    filterMembers() {
        const params = {
            lastName: this.lastName,
            firstName: this.firstName,
            pagination: this.pagination,
            sortingOption: this.sortingOption,
        };

        this.httpClient.post("http://localhost:8080/member/find", params).subscribe((response: any) => {
            this.members = response.members;
            this.pagination = response.pagination;
        });
    }

    loadPage(loadPageNumber: number) {
        console.log(loadPageNumber);
        this.pagination.loadPage = loadPageNumber;
        this.filterMembers();
    }

    onSort = ({column, direction}: SortEvent): void => {
        console.log(column);

        const headers1 = this.headers || [];
        for (const header of headers1) {
            if (header.sortable !== column) {
                header.direction = '';
            }
        }

        this.sortingOption.sortingColumn = column;
        this.sortingOption.sortingDirection = direction;
        this.filterMembers();
    }

}

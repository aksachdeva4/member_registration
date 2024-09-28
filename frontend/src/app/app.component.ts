import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {DecimalPipe, NgForOf} from "@angular/common";
import {HttpClient} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {NgbNavModule, NgbPaginationModule} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, DecimalPipe, NgForOf, FormsModule, NgbNavModule, NgbPaginationModule],
    templateUrl: './app.component.html',
    styleUrl: './app.component.scss'
})
export class AppComponent {
    title = 'frontend';

    firstName = '';
    lastName = '';
    active = 2;

    members: any = []
    pagination = {
        totalRows: 0,
        currentPage: 1,
        rowsPerPage: 20,
        totalPages: 0,
        loadPage: 1,
    }

    constructor(private httpClient: HttpClient) {
        this.filterMembers();
    }

    getAllMembers() {
        this.httpClient.get("http://localhost:8080/member/get-all").subscribe((response) => this.members = response);
    }

    filterMembers() {
        const params = {
            lastName: this.lastName,
            firstName: this.firstName,
            pagination: this.pagination,
        };
        this.pagination.loadPage = this.pagination.currentPage;
        this.httpClient.post("http://localhost:8080/member/find", params).subscribe((response: any) => {
            this.members = response.members;
            this.pagination = response.pagination;
        });
    }

    loadPage() {
        if (this.pagination.currentPage != this.pagination.loadPage) {
            this.filterMembers();
        }
    }

}

import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {DecimalPipe, NgForOf} from "@angular/common";
import {HttpClient} from "@angular/common/http";
import {FormsModule} from "@angular/forms";

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, DecimalPipe, NgForOf, FormsModule],
    templateUrl: './app.component.html',
    styleUrl: './app.component.scss'
})
export class AppComponent {
    title = 'frontend';

    firstName = '';
    lastName = '';

    members: any = []

    constructor(private httpClient: HttpClient) {
    }

    getAllMembers() {
        this.httpClient.get("http://localhost:8080/member/get-all").subscribe((response) => this.members = response);
    }

    filterMembers() {
        console.log(this.lastName);
        this.httpClient.get("http://localhost:8080/member/find-by-last-name?lastName=" + this.lastName).subscribe((response) => this.members = response);
    }

}

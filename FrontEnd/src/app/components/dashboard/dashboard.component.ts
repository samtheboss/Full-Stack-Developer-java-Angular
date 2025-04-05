import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  studentCount = 0;
  constructor(private http: HttpClient) {}
  ngOnInit() {
    this.http.get<number>("http://localhost:8081/api/students/studentCount")
        .subscribe(count => this.studentCount = count);
  }
}

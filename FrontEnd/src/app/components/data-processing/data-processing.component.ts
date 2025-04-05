import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-data-processing',
  templateUrl: './data-processing.component.html',
  styleUrls: ['./data-processing.component.scss']
})
export class DataProcessingComponent {
  isLoading = false;
  message = '';

  constructor(private http: HttpClient) {}

  processData() {
    this.isLoading = true;
    this.message = '';

    this.http.post('http://localhost:8081/api/excel/process', {}, { responseType: 'text' })
        .subscribe({
          next: (res: string) => {
            this.message = res;
            this.isLoading = false;
          },
          error: (err) => {
            console.error(err);
            this.message = 'Failed to process data.';
            this.isLoading = false;
          }
        });
  }
}

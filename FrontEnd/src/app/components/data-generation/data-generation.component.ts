import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-data-generation',
  templateUrl: './data-generation.component.html',
  styleUrls: ['./data-generation.component.scss'],
})
export class DataGenerationComponent {
  recordCount: number = 0;
  isLoading = false;
  message = '';

  constructor(private http: HttpClient) {}
    generateData() {
    this.isLoading = true;
    this.message = '';

    this.http.post('http://localhost:8081/api/excel/generate', {
      recordCount: this.recordCount
    }, { responseType: 'text' })
        .subscribe({
          next: (res: string) => {
            this.message = res;
            this.isLoading = false;
          },
          error: (err) => {
            this.message = 'Error: ' + (err.error?.message || 'Failed to generate data');
            this.isLoading = false;
          }
        });
  }

}

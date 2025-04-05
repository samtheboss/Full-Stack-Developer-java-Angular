import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
    selector: 'app-data-upload',
    templateUrl: './data-upload.component.html',
    styleUrls: ['./data-upload.component.scss']
})
export class DataUploadComponent {
    selectedFile!: File;
    isLoading = false;
    message = '';

    constructor(private http: HttpClient) {}

    onFileSelected(event: Event) {
        const input = event.target as HTMLInputElement;

        if (input.files && input.files.length > 0) {
            this.selectedFile = input.files[0];
            console.log('Selected file:', this.selectedFile.name);
            this.message = '';
        } else {
            console.warn('No file selected');
            this.message = 'No file selected!';
        }
    }
    uploadFile() {
        if (!this.selectedFile) return;

        const formData = new FormData();
        formData.append('file', this.selectedFile);

        this.isLoading = true;
        this.message = '';

        this.http.post('http://localhost:8081/api/excel/upload', formData, { responseType: 'text' })
            .subscribe({
                next: (res: string) => {
                    this.message = res;
                    this.isLoading = false;
                },
                error: (err) => {
                    console.error(err);
                    this.message = 'Failed to upload file.';
                    this.isLoading = false;
                }
            });
    }
}

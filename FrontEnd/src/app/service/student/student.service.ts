import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Student} from "../../models/student";
import {StudentFilter} from "../../models/StudentFilter";
import {Page} from "../../models/Page";

@Injectable({
    providedIn: 'root'
})
export class StudentService {
    private baseUrl = 'http://localhost:8081/api/students';

    constructor(private http: HttpClient) {
    }

    // getStudents(filters: any, page: number, size: number): Observable<Student[]> {
    //     let params = new HttpParams()
    //         .set('page', page)
    //         .set('size', size);
    //
    //     if (filters.studentId) params = params.set('studentId', filters.studentId);
    //     if (filters.studentClass) params = params.set('class', filters.studentClass);
    //     if (filters.dobStart) params = params.set('dobStart', filters.dobStart);
    //     if (filters.dobEnd) params = params.set('dobEnd', filters.dobEnd);
    //
    //     return this.http.get<Student[]>(this.baseUrl, {params});
    // }
    getStudents(filter: StudentFilter): Observable<Page<Student>> {
        return this.http.post<Page<Student>>(`${this.baseUrl}`+"/students", filter);
    }

    searchStudents(filter: StudentFilter): Observable<any> {
        return this.http.post<any>(this.baseUrl, filter);
    }

    getStudentById(studentId: number): Observable<Student> {
        return this.http.get<Student>(`${this.baseUrl}/${studentId}`);
    }

    deleteStudent(studentId: number): Observable<any> {
        return this.http.delete(`${this.baseUrl}/${studentId}`);
    }

    updateStudent(studentId: number, student: Student): Observable<Student> {
        return this.http.put<Student>(`${this.baseUrl}/update/${studentId}`, student);
    }

    uploadPhoto(studentId: number, file: File): Observable<string> {
        const formData = new FormData();
        formData.append('file', file);
        return this.http.post<string>(
            `${this.baseUrl}/upload-photo/${studentId}`,
            formData,
            {responseType: 'text' as 'json'}
        );
    }

    exportStudents(): Observable<Blob> {
        return this.http.get(`${this.baseUrl}/export`, {responseType: 'blob'});
    }
}

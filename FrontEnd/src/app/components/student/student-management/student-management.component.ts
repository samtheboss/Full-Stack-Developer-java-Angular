import { Component, OnInit } from '@angular/core';
import { Student } from '../../../models/student';
import { StudentService } from '../../../service/student/student.service';
import {StudentFilter} from "../../../models/StudentFilter";
import {Page} from "../../../models/Page";

@Component({
  selector: 'app-student-management',
  templateUrl: './student-management.component.html',
  styleUrls: ['./student-management.component.scss']
})
export class StudentManagementComponent implements OnInit {
  students: Student[] = [];
  filters: StudentFilter = new StudentFilter();
  page: number = 0;
  size: number = 10;
  totalPages: number = 0;

  classOptions: string[] = ['Class1', 'Class2', 'Class3', 'Class4', 'Class5'];

  constructor(private studentService: StudentService) {}

  ngOnInit(): void {
    this.fetchStudents();
  }
  fetchStudents(): void {
    this.filters.page = this.page;
    this.filters.size = this.size;

    this.filters.dobStart = this.filters.dobStart ? this.formatDate(this.filters.dobStart) : null;
    this.filters.dobEnd = this.filters.dobEnd ? this.formatDate(this.filters.dobEnd) : null;

    this.studentService.getStudents(this.filters).subscribe({
      next: (response: Page<Student>) => {
        this.students = response.content;
        this.totalPages = response.totalPages;
      },
      error: err => {
        console.error('Error loading students:', err);
      }
    });
  }

  formatDate(date: any): string {
    const d = new Date(date);
    const month = ('0' + (d.getMonth() + 1)).slice(-2);
    const day = ('0' + d.getDate()).slice(-2);
    return `${d.getFullYear()}-${month}-${day}`;
  }

  applyFilters(): void {
    this.page = 0;
    this.fetchStudents();
  }

  nextPage(): void {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.fetchStudents();
    }
  }
  prevPage(): void {
    if (this.page > 0) {
      this.page--;
      this.fetchStudents();
    }
  }
  deleteStudent(studentId: number): void {
    if (confirm('Are you sure you want to delete this student?')) {
      this.studentService.deleteStudent(studentId).subscribe(() => {
        this.fetchStudents();
      });
    }
  }
  exportToExcel(): void {
    this.studentService.exportStudents().subscribe(blob => {
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = 'students.xlsx';
      link.click();
    });
  }

  getStudentPhotoUrl(photoPath: string): string {
    return photoPath && photoPath.trim() !== ''
        ? `http://localhost:8080/api/students/photo/${photoPath}`
        : 'assets/avatar.png';
  }
}

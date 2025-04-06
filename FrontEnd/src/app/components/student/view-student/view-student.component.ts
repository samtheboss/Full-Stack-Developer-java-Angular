import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {Student} from "../../../models/student";
import {StudentService} from "../../../service/student/student.service";

@Component({
  selector: 'app-view-student',
  templateUrl: './view-student.component.html',
  styleUrls: ['./view-student.component.scss']
})
export class ViewStudentComponent implements OnInit {
  studentId!: number;
  student!: Student;
  imagePreviewUrl: string = '';

  constructor(
      private route: ActivatedRoute,
      private studentService: StudentService
  ) {}

  ngOnInit(): void {
    this.studentId = +this.route.snapshot.paramMap.get('id')!;
    this.studentService.getStudentById(this.studentId).subscribe(data => {
      this.student = data;
      this.setImagePreview();
    });
  }

  // setImagePreview() {
  //   if (this.student.photoPath && this.student.photoPath.trim() !== '') {
  //     this.imagePreviewUrl = `assets/student-photos/${this.student.photoPath}`;
  //   } else {
  //     this.imagePreviewUrl = 'assets/avatar.png'; // fallback avatar
  //   }
  // }
  setImagePreview() {
    if (this.student.photoPath && this.student.photoPath.trim() !== '') {
      this.imagePreviewUrl = `http://localhost:8081/api/students/photo/${this.student.photoPath}`;
    } else {
      this.imagePreviewUrl = 'assets/avatar.png'; // fallback avatar
    }
  }
}

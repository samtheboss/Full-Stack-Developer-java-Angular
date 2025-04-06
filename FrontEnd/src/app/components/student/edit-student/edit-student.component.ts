import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {StudentService} from "../../../service/student/student.service";
import {Student} from "../../../models/student";

@Component({
  selector: 'app-edit-student',
  templateUrl: './edit-student.component.html',
  styleUrls: ['./edit-student.component.scss']
})
export class EditStudentComponent implements OnInit {
  studentId!: number;
  student: Student = {
    studentId: 0,
    firstName: '',
    lastName: '',
    dob: '',
    studentClass: '',
    score: 0,
    status: 1,
    photoPath: ''
  };
  selectedFile: File | null = null;
  imagePreviewUrl: string = '';
  classOptions = ['Class1', 'Class2', 'Class3', 'Class4', 'Class5'];

  constructor(
      private route: ActivatedRoute,
      private router: Router,
      private studentService: StudentService
  ) {}

  ngOnInit(): void {
    this.studentId = +this.route.snapshot.paramMap.get('id')!;
    this.studentService.getStudentById(this.studentId).subscribe(data => {
      this.student = data;
      this.setImagePreview();
    });
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];

    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreviewUrl = reader.result as string;
    };
    if (this.selectedFile) {
      reader.readAsDataURL(this.selectedFile);
    }
  }

  onSubmit() {
    this.studentService.updateStudent(this.student.studentId, this.student).subscribe(() => {
      if (this.selectedFile) {
        this.studentService.uploadPhoto(this.student.studentId, this.selectedFile).subscribe({
          next: (photoFileName: string) => {
            this.student.photoPath = photoFileName;
            alert('Student updated and photo uploaded successfully!');
            this.router.navigate(['/student-management']);
          },
          error: () => {
            alert('Student updated but failed to upload photo');
            this.router.navigate(['/student-management']);
          }
        });
      } else {
        alert('Student updated successfully!');
        this.router.navigate(['/student-management']);
      }
    });
  }

  setImagePreview() {
    if (this.student.photoPath && this.student.photoPath.trim() !== '') {
      this.imagePreviewUrl = `assets/student-photos/${this.student.photoPath}`;
    } else {
      this.imagePreviewUrl = 'assets/avatar.png';
    }
  }
}

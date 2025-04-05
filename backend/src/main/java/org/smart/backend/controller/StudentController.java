package org.smart.backend.controller;

import org.smart.backend.entity.Student;
import org.smart.backend.entity.StudentFilter;
import org.smart.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/students")
public class StudentController {
  @Autowired private StudentService studentService;

  @GetMapping
  public List<Student> getAllStudents() {
    return studentService.getAllStudents();
  }

  @GetMapping("/{id}")
  public Optional<Student> getStudentById(@PathVariable Long id) {
    return studentService.getStudentById(id);
  }

  @PutMapping("/update")
  public Student updateStudent(@RequestBody Student student) {
    return studentService.updateStudent(student);
  }

  @DeleteMapping("/{id}")
  public Map<String, String> deleteStudent(@PathVariable Long id) {
    studentService.deleteStudent(id);
    return Map.of("message", "Student deleted successfully!");
  }

  @GetMapping("/filter/id/{studentId}")
  public List<Student> filterByStudentId(@PathVariable Long studentId) {
    return studentService.filterByStudentId(studentId);
  }

  @GetMapping("/filter/class/{studentClass}")
  public List<Student> filterByClass(@PathVariable String studentClass) {
    return studentService.filterByClass(studentClass);
  }

  @GetMapping("/filter/dob")
  public List<Student> filterByDobRange(
      @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
    return studentService.filterByDobRange(startDate, endDate);
  }

  @GetMapping("/export")
  public Map<String, String> exportToExcel() {
    String message = studentService.exportToExcel();
    return Map.of("message", message);
  }

  @PostMapping("/upload-photo/{id}")
  public ResponseEntity<String> uploadPhoto(
      @PathVariable Long id, @RequestParam("file") MultipartFile file) {
    return studentService.uploadStudentPhoto(id, file);
  }

  @PostMapping("/students")
  public Page<Student> searchStudents(@RequestBody StudentFilter filter) {
    Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
    return studentService.getFilteredStudents(filter, pageable);
  }

  @GetMapping("/studentCount")
  public Long studentCount() {
    return studentService.totalsStudent();
  }
}

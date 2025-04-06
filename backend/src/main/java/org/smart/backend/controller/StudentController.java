package org.smart.backend.controller;

import org.smart.backend.entity.Student;
import org.smart.backend.entity.StudentFilter;
import org.smart.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
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

  @PutMapping("/update/{studentId}")
  public ResponseEntity<Student> updateStudent(
          @PathVariable Long studentId,
          @RequestBody Student student
  ) {
    Student updated = studentService.updateStudent(studentId, student);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public Map<String, String> deleteStudent(@PathVariable Long id) {
    studentService.deleteStudent(id);
    return Map.of("message", "Student deleted successfully!");
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
  @GetMapping("/photo/{filename:.+}")
  public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {
    try {
      String[] parts = filename.split("-", 2);
      if (parts.length < 2) {
        return ResponseEntity.badRequest().build();
      }
      long studentId = Long.parseLong(parts[0]);
      Path photoPath = Paths.get("C:/var/log/applications/API/StudentPhotos").resolve(filename).normalize();
      Resource resource = new UrlResource(photoPath.toUri());
      if (!resource.exists()) {
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok()
              .contentType(MediaType.IMAGE_JPEG)
              .body(resource);

    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
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

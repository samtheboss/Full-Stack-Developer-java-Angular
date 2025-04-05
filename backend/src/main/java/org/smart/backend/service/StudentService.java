package org.smart.backend.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.smart.backend.entity.Student;
import org.smart.backend.entity.StudentFilter;
import org.smart.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
  @Autowired private StudentRepository studentRepository;
  private static final String EXPORT_PATH =
      "C:/var/log/applications/API/StudentReports/students.xlsx";

  public List<Student> getAllStudents() {
    return studentRepository.findAll();
  }

  public Page<Student> getFilteredStudents(StudentFilter filter, Pageable pageable) {
    return studentRepository.findStudents(filter, pageable);
  }

  public Optional<Student> getStudentById(Long id) {
    return studentRepository.findById(id);
  }

  public Student updateStudent(Student student) {
    return studentRepository.save(student);
  }

  public void deleteStudent(Long id) {
    Student student = studentRepository.findById(id).orElseThrow();
    student.setStatus(0);
    studentRepository.save(student);
  }

  public List<Student> filterByStudentId(Long studentId) {
    return studentRepository.findByStudentId(studentId);
  }

  public Long totalsStudent() {
    return studentRepository.count();
  }

  public List<Student> filterByClass(String studentClass) {
    return studentRepository.findByStudentClass(studentClass);
  }

  public List<Student> filterByDobRange(LocalDate startDate, LocalDate endDate) {
    return studentRepository.findByDobBetween(startDate, endDate);
  }

  public String exportToExcel() {
    List<Student> students = studentRepository.findAll();
    if (students.isEmpty()) {
      return "No data available for export!";
    }
    try (Workbook workbook = new XSSFWorkbook();
        FileOutputStream fos = new FileOutputStream(EXPORT_PATH)) {
      Sheet sheet = workbook.createSheet("Students");
      Row header = sheet.createRow(0);
      String[] columns = {
        "StudentID", "FirstName", "LastName", "DOB", "Class", "Score", "Status", "PhotoPath"
      };
      for (int i = 0; i < columns.length; i++) {
        header.createCell(i).setCellValue(columns[i]);
      }
      int rowNum = 1;
      for (Student student : students) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(student.getStudentId());
        row.createCell(1).setCellValue(student.getFirstName());
        row.createCell(2).setCellValue(student.getLastName());
        row.createCell(3).setCellValue(student.getDob().toString());
        row.createCell(4).setCellValue(student.getStudentClass());
        row.createCell(5).setCellValue(student.getScore());
        row.createCell(6).setCellValue(student.getStatus());
        row.createCell(7).setCellValue(student.getPhotoPath());
      }

      workbook.write(fos);
    } catch (IOException e) {
      throw new RuntimeException("Error exporting to Excel", e);
    }

    return "Excel exported successfully at: " + EXPORT_PATH;
  }

  public ResponseEntity<String> uploadStudentPhoto(Long id, MultipartFile file) {
    Optional<Student> studentOptional = studentRepository.findById(id);
    if (studentOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
    }
    Student student = studentOptional.get();
    if (!file.getContentType().startsWith("image/")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only images are allowed");
    }
    if (file.getSize() > 5 * 1024 * 1024) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File size exceeds 5MB");
    }
    String storagePath = "C:/var/log/applications/API/StudentPhotos/";
    File directory = new File(storagePath);
    if (!directory.exists() && !directory.mkdirs()) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Could not create storage directory");
    }
    String newFileName = student.getStudentId() + "-" + file.getOriginalFilename();
    Path filePath = Paths.get(storagePath, newFileName);
    try {
      Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
      student.setPhotoPath(newFileName);
      studentRepository.save(student);
      return ResponseEntity.ok("Photo uploaded successfully");
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error saving file: " + e.getMessage());
    }
  }
}

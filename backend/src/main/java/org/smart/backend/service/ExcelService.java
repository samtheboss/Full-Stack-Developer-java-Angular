package org.smart.backend.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.smart.backend.entity.Student;
import org.smart.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.smart.backend.Utils.Utils.ensureDirectoryExists;

@Service
public class ExcelService {

  @Autowired private StudentRepository studentRepository;

  private static final String[] CLASSES = {"Class1", "Class2", "Class3", "Class4", "Class5"};

  private static final String WINDOWS_PATH = "C:\\var\\log\\applications\\API\\dataprocessing\\";
  private static final String LINUX_PATH = "/var/log/applications/API/dataprocessing/";

  private String getFilePath(String filename) {
    String os = System.getProperty("os.name").toLowerCase();
    return os.contains("win") ? WINDOWS_PATH + filename : LINUX_PATH + filename;
  }

  private final String FILE_PATH = getFilePath("students.xlsx");

  private final String CSV_PATH = getFilePath("students.csv");

  public String generateStudentData(int count) {
    List<Student> students =
        IntStream.range(0, count)
            .mapToObj(
                i ->
                    new Student(
                        null,
                        randomString(3, 8),
                        randomString(3, 8),
                        randomDOB(),
                        CLASSES[new Random().nextInt(CLASSES.length)],
                        new Random().nextInt(31) + 55, // Score between 55 and 85
                        1,
                        ""))
            .collect(Collectors.toList());

    writeToExcel(students);
    return "Excel file created successfully at: " + FILE_PATH;
  }

  private void writeToExcel(List<Student> students) {
    ensureDirectoryExists();
    try (SXSSFWorkbook workbook = new SXSSFWorkbook(100); // keep 100 rows in memory at a time
        FileOutputStream fileOut = new FileOutputStream(FILE_PATH)) {

      Sheet sheet = workbook.createSheet("Students");
      Row header = sheet.createRow(0);
      String[] columns = {
        "Student ID", "First Name", "Last Name", "DOB", "Class", "Score", "Status", "Photo Path"
      };
      for (int i = 0; i < columns.length; i++) {
        header.createCell(i).setCellValue(columns[i]);
      }
      int rowNum = 1;
      for (Student student : students) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(rowNum);
        row.createCell(1).setCellValue(student.getFirstName());
        row.createCell(2).setCellValue(student.getLastName());
        row.createCell(3).setCellValue(student.getDob().toString());
        row.createCell(4).setCellValue(student.getStudentClass());
        row.createCell(5).setCellValue(student.getScore());
        row.createCell(6).setCellValue(student.getStatus());
        row.createCell(7).setCellValue(student.getPhotoPath());
        rowNum++;
      }
      workbook.write(fileOut);
      workbook.dispose();
    } catch (IOException e) {
      throw new RuntimeException("Error writing Excel file", e);
    }
  }

  //  private void writeToExcel(List<Student> students) {
  //    ensureDirectoryExists();
  //    try (Workbook workbook = new XSSFWorkbook();
  //        FileOutputStream fileOut = new FileOutputStream(FILE_PATH)) {
  //      Sheet sheet = workbook.createSheet("Students");
  //      Row header = sheet.createRow(0);
  //      String[] columns = {
  //        "Student ID", "First Name", "Last Name", "DOB", "Class", "Score", "Status", "Photo Path"
  //      };
  //      for (int i = 0; i < columns.length; i++) {
  //        header.createCell(i).setCellValue(columns[i]);
  //      }
  //
  //      int rowNum = 1;
  //      for (Student student : students) {
  //        Row row = sheet.createRow(rowNum++);
  //        row.createCell(0).setCellValue(rowNum - 1);
  //        row.createCell(1).setCellValue(student.getFirstName());
  //        row.createCell(2).setCellValue(student.getLastName());
  //        row.createCell(3).setCellValue(student.getDob().toString());
  //        row.createCell(4).setCellValue(student.getStudentClass());
  //        row.createCell(5).setCellValue(student.getScore());
  //        row.createCell(6).setCellValue(student.getStatus());
  //        row.createCell(7).setCellValue(student.getPhotoPath());
  //      }
  //
  //      workbook.write(fileOut);
  //    } catch (IOException e) {
  //      throw new RuntimeException("Error writing Excel file", e);
  //    }
  //  }

  private String randomString(int min, int max) {
    int length = new Random().nextInt(max - min + 1) + min;
    return new Random()
        .ints(97, 123) // ASCII values for 'a' to 'z'
        .limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }

  private LocalDate randomDOB() {
    int year = 2000 + new Random().nextInt(11);
    int month = 1 + new Random().nextInt(12);
    int day = 1 + new Random().nextInt(28);
    return LocalDate.of(year, month, day);
  }

  public String uploadStudentData(MultipartFile file) {
    List<Student> students = readFromExcel(file);
    if (students.isEmpty()) {
      return "No data found in uploaded Excel file!";
    }
    for (Student data : students) {
      data.setScore(data.getScore() + 5);
    }
    studentRepository.saveAll(students);
    return "Student data successfully updated and saved to the database!";
  }

  private void writeToCSV(List<Student> students) {
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_PATH))) {
      writer.write("StudentID,FirstName,LastName,DOB,Class,Score,Status,PhotoPath");
      writer.newLine();

      for (Student student : students) {
        writer.write(
            String.format(
                "%d,%s,%s,%s,%s,%d,%d,%s",
                student.getStudentId(),
                student.getFirstName(),
                student.getLastName(),
                student.getDob(),
                student.getStudentClass(),
                student.getScore(),
                student.getStatus(),
                student.getPhotoPath()));
        writer.newLine();
      }
    } catch (IOException e) {
      throw new RuntimeException("Error writing CSV file", e);
    }
  }

  private List<Student> readFromExcel(MultipartFile file) {
    List<Student> students = new ArrayList<>();
    try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
      Sheet sheet = workbook.getSheetAt(0);
      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        Student student = new Student();
        student.setFirstName(row.getCell(1).getStringCellValue());
        student.setLastName(row.getCell(2).getStringCellValue());
        student.setDob(LocalDate.parse(row.getCell(3).getStringCellValue()));
        student.setStudentClass(row.getCell(4).getStringCellValue());
        student.setScore((int) row.getCell(5).getNumericCellValue());
        student.setStatus((int) row.getCell(6).getNumericCellValue());
        student.setPhotoPath(row.getCell(7).getStringCellValue());
        students.add(student);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error reading Excel file", e);
    }
    return students;
  }

  private List<Student> readFromExcel() {
    List<Student> students = new ArrayList<>();
    try (Workbook workbook = new XSSFWorkbook(new FileInputStream(FILE_PATH))) {
      Sheet sheet = workbook.getSheetAt(0);
      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        Student student = new Student();
        student.setStudentId((long) row.getCell(0).getNumericCellValue());
        student.setFirstName(row.getCell(1).getStringCellValue());
        student.setLastName(row.getCell(2).getStringCellValue());
        student.setDob(LocalDate.parse(row.getCell(3).getStringCellValue()));
        student.setStudentClass(row.getCell(4).getStringCellValue());
        student.setScore((int) row.getCell(5).getNumericCellValue());
        student.setStatus((int) row.getCell(6).getNumericCellValue());
        student.setPhotoPath(row.getCell(7).getStringCellValue());
        students.add(student);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error reading Excel file", e);
    }
    return students;
  }

  public String processStudentData() {
    List<Student> students = readFromExcel();
    if (students.isEmpty()) {
      return "No data found in Excel file!";
    }
    students.forEach(student -> student.setScore(student.getScore() + 10));
    writeToCSV(students);
    return "CSV file created successfully at: " + CSV_PATH;
  }
}

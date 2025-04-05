package org.smart.backend.controller;

import org.smart.backend.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/excel")
public class ExcelController {
  @Autowired private ExcelService excelService;

//  @PostMapping("/generate")
//  public Map<String, String> generateData(@RequestParam int count) {
//    String message = excelService.generateStudentData(count);
//    return Map.of("message", message);
//  }



  @PostMapping("/generate")
  public ResponseEntity<String> generateData(@RequestBody Map<String, Integer> request) {
    int recordCount = request.get("recordCount");
    try {
      String message = excelService.generateStudentData(recordCount);
      System.out.println(message);
      return ResponseEntity.ok("Excel file created at: " + message);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Failed to generate file");
    }
  }


  @PostMapping("/upload")
  public Map<String, String> uploadData( @RequestParam("file") MultipartFile file) {
    String message = excelService.uploadStudentData(file);
    return Map.of("message", message);
  }
  @PostMapping("/process")
  public Map<String, String> processData() {
    String message = excelService.processStudentData();
    return Map.of("message", message);
  }
}

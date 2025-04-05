package org.smart.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class StudentFilter {
    int page;
    int size;
    String studentId;
    String studentClass;
    LocalDate startDate;
    LocalDate endDate;
}

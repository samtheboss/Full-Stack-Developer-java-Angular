package org.smart.backend.entity;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class StudentFilter {
    int page;
    int size;
    String studentId;
    String studentClass;
    LocalDate startDate;
    LocalDate endDate;
}

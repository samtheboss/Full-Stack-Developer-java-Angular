package org.smart.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "students")
public class Student {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long studentId;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  private LocalDate dob;

  @Column(nullable = false)
  private String studentClass;

  @Column(nullable = false)
  private int score;

  @Column(nullable = false)
  private int status = 1;

  private String photoPath;
}

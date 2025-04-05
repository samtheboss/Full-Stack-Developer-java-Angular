package org.smart.backend.repository;

import org.smart.backend.entity.Student;
import org.smart.backend.entity.StudentFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
  List<Student> findByStudentId(Long studentId);

  List<Student> findByStudentClass(String studentClass);

  @Query("SELECT s FROM Student s WHERE s.dob BETWEEN :startDate AND :endDate")
  List<Student> findByDobBetween(
      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

  @Query(
      "SELECT s FROM Student s WHERE "
          + "(:#{#filter.studentId} IS NULL OR s.studentId = :#{#filter.studentId}) AND "
          + "(:#{#filter.studentClass} IS NULL OR s.studentClass = :#{#filter.studentClass}) AND "
          + "(:#{#filter.startDate} IS NULL OR s.dob >= :#{#filter.startDate}) AND "
          + "(:#{#filter.endDate} IS NULL OR s.dob <= :#{#filter.endDate})")
  Page<Student> findStudents(@Param("filter") StudentFilter filter, Pageable pageable);
}

package com.example.bachelorarbeit.repository.lesson;

import com.example.bachelorarbeit.models.lesson.Lesson;
import com.example.bachelorarbeit.models.user_management.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCreator(User creator);

    Optional<Lesson> findByLessonId(Long id);

    @Query("SELECT l FROM lessons l WHERE " +
            "(l.metaInformation.name LIKE CONCAT('%',:query, '%')" +
            "Or l.metaInformation.keywords LIKE CONCAT('%', :query, '%'))" +
            "And l.metaInformation.isPublic = TRUE")
    List<Lesson> searchLesson(String query);
}

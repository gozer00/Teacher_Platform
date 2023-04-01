package com.example.bachelorarbeit.repository.lesson;

import com.example.bachelorarbeit.models.lesson.Lesson;
import com.example.bachelorarbeit.models.lesson.Phase;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface PhaseRepository extends JpaRepository<Phase, Long> {

    @Transactional
    @Modifying
    @Query("delete from Phase p where p.phaseId not in ?1 and p.lesson = ?2")
    void deleteRemovedPhases(Collection<Long> phaseIds, Lesson lesson);
}
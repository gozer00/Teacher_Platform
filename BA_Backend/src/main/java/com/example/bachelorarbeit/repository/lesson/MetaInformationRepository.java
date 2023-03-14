package com.example.bachelorarbeit.repository.lesson;

import com.example.bachelorarbeit.models.lesson.MetaInformation;
import com.example.bachelorarbeit.models.lesson.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaInformationRepository extends JpaRepository<MetaInformation, Long> {
}

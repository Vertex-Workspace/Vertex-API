package com.vertex.vertex.file.repository;

import com.vertex.vertex.file.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository
        extends JpaRepository<File, Long> {
}

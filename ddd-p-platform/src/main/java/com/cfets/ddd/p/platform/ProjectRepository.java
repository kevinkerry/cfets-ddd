package com.cfets.ddd.p.platform;

import com.cfets.ddd.d.platform.repository.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 持久化领域模型
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

}

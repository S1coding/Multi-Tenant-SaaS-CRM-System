package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TasksRepo extends JpaRepository<Tasks, String> {
	Optional<Tasks> findByTitleAndAssignedTo(String title, String assignedTo);
	List<Tasks> findByAssignedToAndStatus(String assignedTo, String status);
	List<Tasks> findByAssignedTo(String assignedTo);
	List<Tasks> findByTaskProjectAndStatus(String taskProject, String status);

}

package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.task.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportsRepo extends JpaRepository<Reports, String> {
	Reports findByForProject(String project); //This was changed
	Optional<Reports> findByForProjectAndMadeBy(String forProject, String madeBy);
	List<Reports> findByMadeBy(String email);
}

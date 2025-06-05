package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.deal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DealsRepo extends JpaRepository<Deals, String> {

	Optional<Deals> findByMadeToAndMadeBy(String madeTo, String madeBy);
	Optional<List<Deals>> findByMadeBy(String madeBy);
}

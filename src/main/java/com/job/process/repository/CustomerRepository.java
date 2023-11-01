package com.job.process.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.job.process.entity.CustomerMaster;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerMaster, Integer> {

}

package com.job.process.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.job.process.entity.CustomerMaster;
import com.job.process.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;

	public List<CustomerMaster> getAll() {
		return customerRepository.findAll();
	}

	// sort by dynamic field
	public List<CustomerMaster> getAllCutomersSortedByField(String field) {
		return customerRepository.findAll(Sort.by(Sort.Direction.ASC, field));
	}

	public Page<CustomerMaster> getAllCutomersByPages(Integer offSet, Integer page) {
		return customerRepository.findAll(PageRequest.of(offSet, page));
	}

	public Page<CustomerMaster> getAllCutomersByPagesAndSorted(Integer offSet, Integer page, String field) {
		return customerRepository.findAll(PageRequest.of(offSet, page).withSort(Sort.by(field)));
	}

}

package com.job.process.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.job.process.service.CustomerService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
@Tag(name = "Custom Pagination Apis")
public class CustomerApis {

	private final CustomerService customerService;

	@GetMapping("/{field}")
	public ResponseEntity<Map<Object, Object>> getAllCutomersSortedByField(@PathVariable String field) {
		Map<Object, Object> map = new HashMap<>();
		try {
			map.put("Success", true);
			map.put("Data", customerService.getAllCutomersSortedByField(field));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(map);
	}

	@GetMapping
	public ResponseEntity<Map<Object, Object>> getAll() {
		Map<Object, Object> map = new HashMap<>();
		try {
			map.put("Success", true);
			map.put("Data", customerService.getAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(map);
	}

	@GetMapping("/pageSize/{pageSize}/pageNumber/{offSet}")
	public ResponseEntity<Map<Object, Object>> getAllByPages(@PathVariable Integer pageSize,
			@PathVariable Integer offSet) {
		Map<Object, Object> map = new HashMap<>();
		try {
			map.put("Success", true);
			map.put("Data", customerService.getAllCutomersByPages(offSet, pageSize));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(map);
	}

	@GetMapping("/pageSize/{pageSize}/pageNumber/{offSet}/sortBy/{field}")
	public ResponseEntity<Map<Object, Object>> getAllByPagesWithSorting(@PathVariable Integer pageSize,
			@PathVariable Integer offSet, @PathVariable String field) {
		Map<Object, Object> map = new HashMap<>();
		try {
			map.put("Success", true);
			map.put("Data", customerService.getAllCutomersByPagesAndSorted(offSet, pageSize, field));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(map);
	}

}

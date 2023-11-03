package com.job.process.api;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/process")
public class ProcessApi {

	private final JobLauncher jobLauncher;

	private final Job job;

	private final String TEMP_STORAGE = "C:\\Users\\Prasad Pansare\\Desktop\\";

	@PostMapping
	public ResponseEntity<Map<Object, Object>> processJob(@RequestParam("file") MultipartFile file)
			throws IllegalStateException, IOException {
		Map<Object, Object> map = new HashMap<>();
		try {
			// file -> path we don't know so it showing error file not exist.
			// copy the file to some storage in your VM : get the file path
			// copy the file to DB : get the file path

			String originalFileName = file.getOriginalFilename();
			File fileToImport = new File(TEMP_STORAGE + originalFileName);
			file.transferTo(fileToImport);

			JobParameters jobParameters = new JobParametersBuilder()
					.addString("filePath", TEMP_STORAGE + originalFileName)
					.addLong("startAt", System.currentTimeMillis()).toJobParameters();

			JobExecution run = jobLauncher.run(job, jobParameters);
			map.put("Success", true);
			map.put("Exit Status", run.getExitStatus());
			map.put("Status", run.getStatus());
			map.put("startTime", run.getStartTime());
			map.put("endTime", run.getEndTime());
			map.put("totalTime", Duration.between(run.getStartTime(), run.getEndTime()));

		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		} finally {
			// delete the file from the TEMP_STORAGE (increase some I/O operation)
			// Files.deleteIfExists(Paths.get(TEMP_STORAGE + file.getOriginalFilename()));
		}
		return ResponseEntity.ok(map);

	}
}

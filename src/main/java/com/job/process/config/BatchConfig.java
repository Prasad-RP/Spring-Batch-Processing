package com.job.process.config;

import java.io.File;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.job.process.entity.CustomerMaster;
import com.job.process.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

	private final CustomerRepository customerRepository;

	// Getiing file path from controller
	@Bean
	@StepScope
	public FlatFileItemReader<CustomerMaster> reader(@Value("#{jobParameters[filePath]}") String file) {
		return new FlatFileItemReaderBuilder<CustomerMaster>()
				.name("CustomerItemReader")
				.resource(new FileSystemResource(new File(file)))
				.delimited()
				.names("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob")
				.linesToSkip(1)
				.fieldSetMapper(new BeanWrapperFieldSetMapper<>() {
					{setTargetType(CustomerMaster.class);}})
				.build();
		
		/* basic flow implementation
		@Bean
		public FlatFileItemReader<CustomerMaster> reader() {
			FlatFileItemReader<CustomerMaster> itemReader = new FlatFileItemReader<>();
	
			itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
			itemReader.setName("csvReader");
			itemReader.setLinesToSkip(1);
			itemReader.setLineMapper(lineMapper());
			return itemReader;
		}
		*/
	}

	@Bean
	public LineMapper<CustomerMaster> lineMapper() {
		DefaultLineMapper<CustomerMaster> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		// csv file delimeter
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		// headers of csv file
		lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");
		BeanWrapperFieldSetMapper<CustomerMaster> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(CustomerMaster.class);
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}

	@Bean
	public CustomerProcessor processor() {
		return new CustomerProcessor();
	}

	@Bean
	public RepositoryItemWriter<CustomerMaster> writer() {
		RepositoryItemWriter<CustomerMaster> writer = new RepositoryItemWriter<>();
		writer.setRepository(customerRepository);
		writer.setMethodName("save");
		return writer;
	}

	@Bean
	public Step step(ItemReader<CustomerMaster> reader, ItemProcessor<CustomerMaster, CustomerMaster> processor,
			ItemWriter<CustomerMaster> writer, JobRepository jobRepository,
			PlatformTransactionManager transactionManager) {
		return new StepBuilder("step", jobRepository).<CustomerMaster, CustomerMaster>chunk(10, transactionManager)
				.reader(reader)
				// Use when you Wants to enable custom processing
				// .processor(processor)
				.writer(writer)
				// Custom Task executor
				.taskExecutor(taskExecutor()).build();
	}

	@Bean
	public Job job(Step step, JobRepository jobRepository) {
		return new JobBuilder("job", jobRepository)
				.incrementer(new RunIdIncrementer())
				.flow(step)
				.end()
				.build();
	}

	/**
	 * Custom task executor to execute 100 threds at a time asynchronoualy.
	 * 
	 * @return taskExecutor
	 */
	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
		asyncTaskExecutor.setConcurrencyLimit(100);
		return asyncTaskExecutor;
		
		/* Another Implementation
		 return () -> {
	        return new SimpleAsyncTaskExecutor() {{
	            setConcurrencyLimit(10);
	        }};
	    };
		*/
	}


}

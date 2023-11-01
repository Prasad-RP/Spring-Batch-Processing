package com.job.process.config;

import org.springframework.batch.item.ItemProcessor;
import com.job.process.entity.CustomerMaster;

/**
 * 
 * @author Prasad Pansare Custom processor class for Processing items.
 */
public class CustomerProcessor implements ItemProcessor<CustomerMaster, CustomerMaster> {

	// Logic to insert custom processor
	@Override
	public CustomerMaster process(CustomerMaster customer) throws Exception {
		//  if (customer.getCountry().equals("United States")) {
		//		return customer;
		//	} else {
		//		return null;
		//	}
		return customer;
	}
}

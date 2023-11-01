package com.job.process.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customer_master")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerMaster {

	@Id
	@Column(name = "CUSTOMER_ID")
	private int id;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "GENDER")
	private String gender;

	@Column(name = "CONTACT")
	private String contactNo;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "DOB")
	private String dob;
}

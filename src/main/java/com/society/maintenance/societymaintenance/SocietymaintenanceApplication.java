package com.society.maintenance.societymaintenance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SocietymaintenanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocietymaintenanceApplication.class, args);
	}

}

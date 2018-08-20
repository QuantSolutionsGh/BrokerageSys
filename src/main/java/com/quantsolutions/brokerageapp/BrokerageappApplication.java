package com.quantsolutions.brokerageapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;


@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
/* Because I have included BIRT which has some mongo DB classes, spring boot is automatically configuring MongoDB against
 * my wishes. The above stops that*/
public class BrokerageappApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrokerageappApplication.class, args);
	}
}

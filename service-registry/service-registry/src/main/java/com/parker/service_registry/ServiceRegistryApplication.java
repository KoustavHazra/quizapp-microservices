package com.parker.service_registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegistryApplication.class, args);
	}

}

/*
In application.resources we have added these lines

spring.application.name= service-registry  // set a unique name for our server.
server.port= 8761  // set our own port number

eureka.instance.hostname= localhost  // set our own hostname
eureka.client.fetch-registry= false  // won't let fetch registry
eureka.client.register-with-eureka= false  // won't let eureka register with itself

We also need to register the microservices into this one, by default none of the services will be
registered.
 */
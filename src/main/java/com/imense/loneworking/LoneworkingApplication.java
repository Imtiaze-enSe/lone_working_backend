package com.imense.loneworking;

import com.imense.loneworking.application.dto.RegistrationDto;
import com.imense.loneworking.application.service.serviceInterface.AuthService;
import com.imense.loneworking.domain.entity.Enum.UserRole;
import com.imense.loneworking.domain.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.imense.loneworking.domain.entity.Enum.UserRole.ADMIN;
import static com.imense.loneworking.domain.entity.Enum.UserRole.WORKER;

@SpringBootApplication
public class LoneworkingApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoneworkingApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AuthService authService
//	){
//		return args -> {
//			var admin = RegistrationDto.builder()
//					.email("admin@example.com")
//					.password("test")
//					.role(ADMIN)
//					.site_id(0)
//					.build();
//			System.out.println("Admin token : " + authService.registerUser(admin).getFcm_token());
//			var worker = RegistrationDto.builder()
//					.email("worker@example.com")
//					.password("test")
//					.role(WORKER)
//					.site_id(0)
//					.build();
//			System.out.println("WORKER token : " + authService.registerUser(worker).getFcm_token());
//		};
//	}
}

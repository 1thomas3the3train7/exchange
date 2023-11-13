package com.udsu.updater;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class UpdaterApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(UpdaterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}

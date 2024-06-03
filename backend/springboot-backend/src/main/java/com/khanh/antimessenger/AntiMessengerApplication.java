package com.khanh.antimessenger;

import com.khanh.antimessenger.constant.SecurityConstant;
import com.khanh.antimessenger.service.impl.MessAccountServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static com.khanh.antimessenger.constant.FileConstant.DIRECTORY_CREATED;
import static com.khanh.antimessenger.constant.FileConstant.USER_FOLDER;


@SpringBootApplication
@Slf4j
@EnableAsync
public class AntiMessengerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AntiMessengerApplication.class, args);
		if (!Files.exists(Path.of(USER_FOLDER))) {
			var created = new File(USER_FOLDER).mkdirs();
			log.info(String.format(DIRECTORY_CREATED + created));
		}
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			log.info(String.format("user home la gi: %s", System.getProperty("user.home").toString()));
			log.info(String.format("user dir la gi: %s", System.getProperty("user.dir").toString()));
		};
	}

}

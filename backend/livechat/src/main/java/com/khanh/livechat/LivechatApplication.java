package com.khanh.livechat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.khanh.livechat.model.ChatUser;
import com.khanh.livechat.repository.ChatUserRepository;
import com.khanh.livechat.service.ChatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.File;
import java.util.Arrays;
import java.util.List;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
//		MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@SpringBootApplication
@Slf4j
public class LivechatApplication {

	public static void main(String[] args) {
		SpringApplication.run(LivechatApplication.class, args);
	}

//	@Bean
	CommandLineRunner runner(ChatUserService service) {
		return  args -> {
			var objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			List<ChatUser> users = Arrays.asList(
					objectMapper.readValue(new File("formongo.json"), ChatUser[].class)
			);
			users.stream().forEach(System.out::println);
			users.stream().forEach(user -> service.saveUser(user));
		};
	}
}

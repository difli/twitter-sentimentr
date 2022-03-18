package io.flickd.twitter.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class TwitterUiApplication {
    
	public static void main(String[] args) {
		SpringApplication.run(TwitterUiApplication.class, args);
	}
}


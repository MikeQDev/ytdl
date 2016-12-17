package com.ytdl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YtdlApplication {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid arguments. Usage: java -jar ytdl.jar <mp3Dir>");
			System.exit(0);
		}
		YtdlConstants.setContentDirectory(args[0]);
		SpringApplication.run(YtdlApplication.class, args);
	}
}

package com.ll.StudyJava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class StudyJavaApplication {

	public static void main(String[] args) {
		System.out.println("환영합니다");

		Scanner scanner = new Scanner(System.in);  // 사용자 입력을 위한 Scanner
		System.out.print("입력 : ");
		String input = scanner.nextLine();

		System.out.println("입력값 : " + input);

		SpringApplication.run(StudyJavaApplication.class, args);
	}
}
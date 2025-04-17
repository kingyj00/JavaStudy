package com.ll.StudyJava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class StudyJavaApplication {

	public static void main(String[] args) {
		System.out.println("환영합니다");

		Scanner scanner = new Scanner(System.in);
		List<String> phoneBookLog = new ArrayList<>();

		while (true) {
			System.out.print("입력 (번호 입력시 저장 / 종료 입력시 종료) : ");
			String input = scanner.nextLine();

			if ("종료".equalsIgnoreCase(input)) {
				System.out.println("프로그램을 종료합니다.");
				break;
			} else if ("번호".equals(input)) {
				System.out.print("이름을 입력하세요: ");
				String name = scanner.nextLine();

				System.out.print("전화번호를 입력하세요: ");
				String phoneNumber = scanner.nextLine();

				String entry = "이름: " + name + ", 전화번호: " + phoneNumber;
				phoneBookLog.add(entry);

				System.out.println("저장 완료!");
				System.out.println("=== 현재까지 저장된 정보 ===");
				for (String log : phoneBookLog) {
					System.out.println(log);
				}
				System.out.println("=======================");
			} else {
				System.out.println("입력값 : " + input);
			}
		}

		SpringApplication.run(StudyJavaApplication.class, args);
	}
}
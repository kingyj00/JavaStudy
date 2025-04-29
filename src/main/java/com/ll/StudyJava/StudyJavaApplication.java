package com.ll.StudyJava;

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

		// 기본 등록 예시
		phoneBookLog.add("이름: 홍길동, 전화번호: 010-0000-0000");
		phoneBookLog.add("이름: 홍길순, 전화번호: 010-1111-1111");

		printPhoneBook(phoneBookLog);

		while (true) {
			System.out.print("입력 (저장 / 삭제 / 수정 / 종료 입력) : ");
			String input = scanner.nextLine();

			if ("종료".equalsIgnoreCase(input)) {
				System.out.println("프로그램을 종료합니다.");
				break;
			} else if ("저장".equalsIgnoreCase(input)) {
				System.out.print("이름을 입력하세요: ");
				String name = scanner.nextLine();

				System.out.print("전화번호를 입력하세요: ");
				String phoneNumber = scanner.nextLine();

				String entry = "이름: " + name + ", 전화번호: " + phoneNumber;
				phoneBookLog.add(entry);

				System.out.println("저장 완료!");
				printPhoneBook(phoneBookLog);

			} else if ("삭제".equalsIgnoreCase(input)) {
				if (phoneBookLog.isEmpty()) {
					System.out.println("삭제할 데이터가 없습니다.");
					continue;
				}

				printPhoneBook(phoneBookLog);
				System.out.print("삭제할 번호를 입력하세요: ");
				String numberInput = scanner.nextLine();

				try {
					int number = Integer.parseInt(numberInput);
					if (number < 1 || number > phoneBookLog.size()) {
						System.out.println("잘못된 번호입니다.");
					} else {
						String removedEntry = phoneBookLog.remove(number - 1);
						System.out.println("삭제 완료: " + removedEntry);
					}
				} catch (NumberFormatException e) {
					System.out.println("숫자를 입력해야 합니다.");
				}

				printPhoneBook(phoneBookLog);

			} else if ("수정".equalsIgnoreCase(input)) { // [추가]
				if (phoneBookLog.isEmpty()) {
					System.out.println("수정할 데이터가 없습니다.");
					continue;
				}

				printPhoneBook(phoneBookLog);
				System.out.print("수정할 번호를 입력하세요: ");
				String numberInput = scanner.nextLine();

				try {
					int number = Integer.parseInt(numberInput);
					if (number < 1 || number > phoneBookLog.size()) {
						System.out.println("잘못된 번호입니다.");
					} else {
						System.out.print("새로운 이름을 입력하세요: ");
						String newName = scanner.nextLine();

						System.out.print("새로운 전화번호를 입력하세요: ");
						String newPhoneNumber = scanner.nextLine();

						String newEntry = "이름: " + newName + ", 전화번호: " + newPhoneNumber;
						phoneBookLog.set(number - 1, newEntry);
						System.out.println("수정 완료!");
					}
				} catch (NumberFormatException e) {
					System.out.println("숫자를 입력해야 합니다.");
				}

				printPhoneBook(phoneBookLog);

			} else {
				System.out.println("잘못된 명령어입니다.");
			}
		}
	}

	private static void printPhoneBook(List<String> phoneBookLog) {
		System.out.println("=== 현재까지 저장된 정보 ===");
		if (phoneBookLog.isEmpty()) {
			System.out.println("등록된 정보가 없습니다.");
		} else {
			for (int i = 0; i < phoneBookLog.size(); i++) {
				System.out.println((i + 1) + ". " + phoneBookLog.get(i));
			}
		}
		System.out.println("=======================");
	}
}
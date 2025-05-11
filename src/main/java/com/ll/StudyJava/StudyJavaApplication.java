package com.ll.StudyJava;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

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
			System.out.print("입력 (저장 / 삭제 / 수정 / 검색 / 전체삭제 / 정렬 / 종료 입력) : ");
			String input = scanner.nextLine();

			if ("종료".equalsIgnoreCase(input)) {
				System.out.println("프로그램을 종료합니다.");
				break;

			} else if ("저장".equalsIgnoreCase(input)) {
				System.out.print("이름을 입력하세요: ");
				String name = scanner.nextLine();

				System.out.print("전화번호를 입력하세요 (숫자만, 예: 01012345678): ");
				String phoneNumber;
				while (true) {
					phoneNumber = scanner.nextLine();
					if (!phoneNumber.matches("\\d+")) {
						System.out.print("숫자만 입력해주세요. 다시 입력하세요: ");
						continue;
					}

					String formattedPhone = formatPhoneNumber(phoneNumber);
					boolean isDuplicate = false;
					for (String entry : phoneBookLog) {
						if (entry.contains("전화번호: " + formattedPhone)) {
							isDuplicate = true;
							break;
						}
					}

					if (isDuplicate) {
						System.out.print("⚠ 이미 등록된 전화번호입니다. 다른 번호를 입력하세요: ");
					} else {
						phoneNumber = formattedPhone;
						break;
					}
				}

				String entry = "이름: " + name + ", 전화번호: " + phoneNumber;
				phoneBookLog.add(entry);
				System.out.println("저장 완료!");
				printPhoneBook(phoneBookLog);

			} else if ("삭제".equalsIgnoreCase(input)) {
				if (phoneBookLog.isEmpty()) {
					System.out.println("삭제할 데이터가 없습니다.");
					continue;
				}

				System.out.print("삭제할 번호를 입력하세요: ");
				String numberInput = scanner.nextLine();

				try {
					int number = Integer.parseInt(numberInput);
					if (number < 1 || number > phoneBookLog.size()) {
						System.out.println("######### 없는 번호입니다. #########");
					} else {
						String removedEntry = phoneBookLog.remove(number - 1);
						System.out.println("삭제 완료: " + removedEntry);
					}
				} catch (NumberFormatException e) {
					System.out.println("숫자를 입력해야 합니다.");
				}

				printPhoneBook(phoneBookLog);

			} else if ("수정".equalsIgnoreCase(input)) {
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
						System.out.println("없는 번호입니다.");
					} else {
						System.out.print("새로운 이름을 입력하세요: ");
						String newName = scanner.nextLine();

						System.out.print("새로운 전화번호를 입력하세요 (숫자만, 예: 01012345678): ");
						String newPhoneNumber;
						while (true) {
							newPhoneNumber = scanner.nextLine();
							if (!newPhoneNumber.matches("\\d+")) {
								System.out.print("숫자만 입력해주세요. 다시 입력하세요: ");
								continue;
							}

							String formattedPhone = formatPhoneNumber(newPhoneNumber);

							boolean isDuplicate = false;
							for (int i = 0; i < phoneBookLog.size(); i++) {
								if (i == number - 1) continue;
								if (phoneBookLog.get(i).contains("전화번호: " + formattedPhone)) {
									isDuplicate = true;
									break;
								}
							}

							if (isDuplicate) {
								System.out.print("⚠ 이미 등록된 전화번호입니다. 다른 번호를 입력하세요: ");
							} else {
								newPhoneNumber = formattedPhone;
								break;
							}
						}

						String newEntry = "이름: " + newName + ", 전화번호: " + newPhoneNumber;
						phoneBookLog.set(number - 1, newEntry);
						System.out.println("수정 완료!");
					}
				} catch (NumberFormatException e) {
					System.out.println("숫자를 입력해야 합니다.");
				}

				printPhoneBook(phoneBookLog);

			} else if ("검색".equalsIgnoreCase(input)) {
				System.out.print("검색할 이름 또는 전화번호 일부를 입력하세요: ");
				String keyword = scanner.nextLine();
				int count = 0;

				System.out.println("=== 검색 결과 ===");
				for (int i = 0; i < phoneBookLog.size(); i++) {
					if (phoneBookLog.get(i).contains(keyword)) {
						System.out.println((i + 1) + ". " + phoneBookLog.get(i));
						count++;
					}
				}

				if (count == 0) {
					System.out.println("일치하는 결과가 없습니다.");
				} else {
					System.out.println("총 " + count + "개의 결과가 검색되었습니다.");
				}
				System.out.println("================");

			} else if ("전체삭제".equalsIgnoreCase(input)) {
				if (phoneBookLog.isEmpty()) {
					System.out.println("삭제할 데이터가 없습니다.");
				} else {
					System.out.print("정말 모두 삭제할까요? (Y/N): ");
					String confirm = scanner.nextLine();
					if (confirm.equalsIgnoreCase("Y")) {
						phoneBookLog.clear();
						System.out.println("전체 삭제 완료!");
					} else {
						System.out.println("삭제가 취소되었습니다.");
					}
				}

			} else if ("정렬".equalsIgnoreCase(input)) {
				if (phoneBookLog.isEmpty()) {
					System.out.println("정렬할 데이터가 없습니다.");
					continue;
				}

				System.out.print("정렬 기준을 선택하세요 (이름 / 번호): ");
				String sortKey = scanner.nextLine();

				if ("이름".equalsIgnoreCase(sortKey)) {
					phoneBookLog.sort(Comparator.comparing(entry -> {
						int start = entry.indexOf("이름: ") + 4;
						int end = entry.indexOf(",");
						return entry.substring(start, end);
					}));
					System.out.println("이름 기준으로 정렬되었습니다.");

				} else if ("전화번호".equalsIgnoreCase(sortKey)) {
					phoneBookLog.sort(Comparator.comparing(entry -> {
						int start = entry.indexOf("번호: ") + 6;
						return entry.substring(start).replaceAll("[^\\d]", "");
					}));
					System.out.println("전화번호 기준으로 정렬되었습니다.");

				} else {
					System.out.println("⚠ 잘못된 정렬 기준입니다.");
				}
			} else {
				System.out.println("잘못된 명령어입니다.");
			}

			printPhoneBook(phoneBookLog);
		}
	}

	private static String formatPhoneNumber(String number) {
		if (number.length() == 11) {
			return number.substring(0, 3) + "-" + number.substring(3, 7) + "-" + number.substring(7);
		} else if (number.length() == 10) {
			return number.substring(0, 3) + "-" + number.substring(3, 6) + "-" + number.substring(6);
		} else {
			return number;
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
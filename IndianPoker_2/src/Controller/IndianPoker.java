package Controller;

import java.util.*;

/**
 * p1은 나(이름 입력한 플레이어) p2는 가상의 플레이어(여기서 임의로 돌림)
 */
public class IndianPoker {
	public String name; // 플레이어이름
	public int point; // 내 현재 점수
	public int op_cardNum; // 상대방 카드번호
	public int turn; // 게임진행횟수

	/* 설계서와 다르게 추가된 변수 */
	public int betpoint; // 내가 베팅한 포인트
	public int op_point; // 상대방 점수
	public int op_betpoint; // 상대방이 베팅한 포인트
	public int setbpoint = 0; // 무승부시 저장할 포인트
	public int p1_win = 0;// 내가 이긴 횟수
	public int p2_win = 0;// 상대가 이긴횟수
	public int result = 0; // 결과
	public int my_cardNum; // 나의 카드 번호
	public int add_bpoint; // 상대와 내가 베팅한 점수의 합

	public static Rank rank;
	public static Card card;
	public static Turn turn_class;

	public IndianPoker() {
		rank = new Rank();
		card = new Card();
		turn_class = new Turn();
	}

	/**
	 * @param name
	 */
	public void start(String name) {
		// TODO implement here
		point = rank.getPoint(); // 기본점수 200점 부여
		op_point = rank.getPoint();
		boolean ok = false; // betting 플래그 (베팅시 true)

		while (true) {
			Scanner s = new Scanner(System.in);

			turn = turn_class.getTurn();

			if (turn == 10) {
				System.out.println("10회 완료로 게임을 종료하였습니다.");
				return;
			}
			if (point == 0) {
				System.out.println("나의 점수가 0점이 되어 게임을 종료하였습니다.");
				return;
			}
			if (op_point == 0) {
				System.out.println("상대 점수가 0점이 되어 게임을 종료하였습니다.");
				return;
			}

			ok = false;
			turn = turn + 1;
			turn_class.turn = turn;

			System.out.println(turn + "번째 게임입니다.");

			if (setbpoint != 0) { // 이전 판이 무승부였다는 것
				add_bpoint = setbpoint; // 무승부의 경우 다음턴으로 점수 넘겨줘야해서 add_bpoint에
										// setbpoint저장
			}

			card.setCard(); // 카드 뽑기
			op_cardNum = card.getCard(); // 상대방의 카드 번호 가져오기

			System.out.println("상대방의 카드는 " + op_cardNum + "입니다."); // 상대방의 카드 번호
																	// 출력

			while (ok == false) {
				System.out.print("현재 점수 : " + point + ", 베팅할 점수를 입력하세요. 0:포기");
				betpoint = s.nextInt(); // 베팅할 점수 입력받기

				op_betpoint = (int) ((Math.random() * 10) + 1) * 10; // 상대방이
																		// 베팅하는
																		// 점수를
																		// 임의로
																		// 지정

				System.out.println("상대의 betting 점수 : " + op_betpoint);

				if ((betpoint == 0) || (op_betpoint == 0)) { // 게임 포기
					giveUp();
					result = 1; // 1:패배, 2:승리, 3:무승부
					ok = true;
				} else {
					ok = betting(name, betpoint, op_betpoint, point, op_point);
					if (ok) {
						add_bpoint = betpoint + op_betpoint;
						result = compare(add_bpoint);
					}
				}
			}
			my_cardNum = card.getMyCard();
			System.out.println(name + "님의 카드는 " + my_cardNum + "입니다.");

			if (result == 1) {
				System.out.println("패배하였습니다.");
				p2_win += 1;
			} else if (result == 2) {
				System.out.println("승리하였습니다.");
				p1_win += 1;
			} else if (result == 3) {
				System.out.println("무승부입니다.");
			}
			System.out.println(name + "님의 남은 포인트는 " + point + "이고, " + p1_win + "번 이겼습니다.");
			System.out.println("상대 플레이어의 남은 포인트는 " + op_point + "이고, " + p2_win + "번 이겼습니다.");
			System.out.println("------------------------------------------------------");
		}

	}

	/**
	 * @param name
	 * @param betpoint
	 * @return 베팅 점수가 현재 가지고 있는 점수보다 큰지 아닌지 비교해주는 함수 (근데인자 이렇게 많이 필요하지 않은거같아요
	 *         지워도 괜찮을거같지않나요?)
	 */
	public boolean betting(String name, int betpoint, int op_betpoint, int point, int op_point) {
		// TODO implement here
		this.betpoint = betpoint;
		this.op_betpoint = op_betpoint;
		this.point = point;
		this.op_point = op_point;

		if (betpoint > point) {
			System.out.println("현재 점수보다 베팅 점수가 더 큽니다. (오류)");
			return false;
		}

		if (op_betpoint > op_point) {
			System.out.println("상대의 현재 점수보다 베팅 점수가 더 큽니다. (오류)");
			return false;
		}

		this.point = point - betpoint; // 각 플레이어가 베팅한 점수만큼 원래 점수에서 차감
		this.op_point = op_point - op_betpoint;

		return true;
	}

	/**
	 * 상대방 카드와 내 카드를 비교하여 결과를 출력해주는 함수 (근데 인자 필요없을것같아용 ---- 없애도 되겠죠?)
	 */
	public int compare(int add_bpoint) {
		this.add_bpoint = add_bpoint;

		int result = 0;
		// TODO implement here
		op_cardNum = card.getCard(); // 상대방 카드 번호 가져오기
		my_cardNum = card.getMyCard(); // 내 카드 번호 가져오기

		if (my_cardNum < op_cardNum) { //
			op_point = op_point + add_bpoint + setbpoint;
			setbpoint = 0;
			result = 1;
		} else if (my_cardNum > op_cardNum) {
			point = point + add_bpoint + setbpoint;
			setbpoint = 0;
			result = 2;
		} else {
			setbpoint = add_bpoint;
			result = 3;
		}
		return result;
	}

	/**
	 * 베팅을 포기하는 경우 - 만약 베팅 포기자의 카드가 10인 경우, 포기자의 점수가 100점 차감된다.
	 */
	public void giveUp() {
		// TODO implement here
		int my_cardNum;

		my_cardNum = card.getMyCard();
		if (betpoint == 0) {
			if (my_cardNum == 10) {
				point -= 100; // 내가 베팅을 포기했는데 내 카드의 숫자가 10일 경우 -- 나는 100점 차감
			}
		} else if (op_betpoint == 0) {
			if (op_cardNum == 10) {
				op_point -= 100; // 상대가 베팅을 포기했는데 상대 카드의 숫자가 10인 경우 --상대 점수
									// 100점차감
			}
		}
	}

	/**
	 * 
	 */
	public void updateDisplay() {
		// TODO implement here
	}

	/**
	 * 
	 */
	public void addObserver() {
		// TODO implement here
	}

	/**
	 * 
	 */
	public void notifyObservers() {
		// TODO implement here
	}

	public static void main(String[] args) {
		String pname;
		Scanner s = new Scanner(System.in);
		IndianPoker ip = new IndianPoker();

		System.out.print("플레이어의 이름을 입력해주세요 : ");
		pname = s.next();
		ip.start(pname);
	}

}
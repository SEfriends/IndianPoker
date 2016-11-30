package server;

import java.io.IOException;
import java.util.Vector;

import Model.IndianPoker;
import Model.Player;

public class IPServer extends AbstractServer {
	int playcnt = 0;
	int turn = 1;
	Vector<ConnectionToClient> cli = new Vector<>(); // 접속한 클라이언트들
	String[][] userList;

	public IPServer(int port) {
		super(port);
	}

	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String m = msg.toString();
		switch (m) {
		case "Play":
			play(m, client);
			break;

		case "Rank":
			rank(m, client);
			break;

		case "Exit":
			System.out.println(findClient(client) + "님이 종료하였습니다." + client);
			break;

		default:
			if (m.startsWith("Login")) {
				login(m, client);
			} else if (m.startsWith("Cancel")) {
				m = m.substring(6);
				if (m.equals("Login")) {
					System.out.println("Client가 login 취소요청을 하였습니다." + client);
				}
			} else if (m.startsWith("Insert")) {
				newID(m, client);
			} else if (m.startsWith("betting")) {
				betting(m, client);
			} else if (m.startsWith("giveUp")){
				giveup(m, client);
			} else {
				try {
					client.sendToClient("올바른 명령어가 아닙니다." + msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;
		}
	}

	/**
	 * Hook method called each time a new client connection is accepted. The
	 * default implementation does nothing.
	 * 
	 * @param client
	 *            the connection connected to the client.
	 */
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("Client가 접속하였습니다.");
		cli.add(client);
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("서버가 포트 " + getPort() + "에서 연결을 기다리는 중입니다...");
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * stops listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("서버가 중단되었습니다.");
	}

	private String findClient(ConnectionToClient client) {
		String id;
		if (client.equals(cli.get(0))) {
			id = "[Player1] " + IndianPoker.getInstance().p1.name;
		} else
			id = "[Player2] " + IndianPoker.getInstance().p2.name;

		return id;
	}

	void play(String m, ConnectionToClient client) { // 게임 시작 버튼을 눌렀을때 실행되는 메소드
		System.out.println("Client로부터  play 요청을 받았습니다." + client);
		System.out.println(cli.size()+"");
		System.out.println(cli.get(0));
		playcnt++;
		if (cli.size() % 2 == 1) {
			System.out.println("인원이 부족하여 게임을 시작할 수 없습니다. 다른 클라이언트 접속을 기다립니다...");
			System.out.println(cli.get(0));
			try {
				client.sendToClient("msg:wait" + "다른 플레이어가 접속할 때 까지 기다려야합니다...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(playcnt%2==0){
			try {
				IndianPoker.getInstance().p1.card.selectCard(); // 카드 랜덤 선택
				IndianPoker.getInstance().p2.card.selectCard(); // 카드 랜덤 선택
				
				cli.get(0)
						.sendToClient("msg:play" + "Player1(" + IndianPoker.getInstance().p1.name + ")과 " + "Player2("
								+ IndianPoker.getInstance().p2.name + ")의 게임을 시작합니다." + "@"
								+ IndianPoker.getInstance().p2.card.cardValue + "@" + turn +"@"+"player1"
								+ "@"+IndianPoker.getInstance().p1.name
								+ "@"+ IndianPoker.getInstance().p1.card.cardValue);// 메세지와 상대방 카드번호, 턴수, 현재차례, player1이름, 자신의 카드번호 보냄
				cli.get(1)
				.sendToClient("msg:play" + "Player1(" + IndianPoker.getInstance().p1.name + ")과 " + "Player2("
						+ IndianPoker.getInstance().p2.name + ")의 게임을 시작합니다." + "@"
						+ IndianPoker.getInstance().p1.card.cardValue + "@" + turn +"@"+"player1"
						+ "@"+IndianPoker.getInstance().p1.name
						+ "@"+ IndianPoker.getInstance().p2.card.cardValue); // 메세지와 상대방 카드번호, 턴수, 현재차례, player1이름, 자신의 카드번호 보냄
			
				
				System.out.println("게임을 시작합니다.");
			} catch (Exception e) {
				System.out.println("11예기치 못한 오류가 발생되었습니다." + e);
			}
		}
	}

	void rank(String m, ConnectionToClient client) { // 랭킹 보기 요청을 받았을 때
		System.out.println(findClient(client) + "님으로부터 Rank보기 요청을 받았습니다." + client);
		try {
			String rk = "";
			rk = IndianPoker.getInstance().db.Ranking();
			client.sendToClient("msg:rank" + rk);
		} catch (Exception e) {
			System.out.println("예기치 못한 오류가 발생되었습니다. " + e);
		}
	}

	void login(String m, ConnectionToClient client) { //
		String id = "";
		String pwd = "";

		id = m.substring(5, m.indexOf("!"));
		pwd = m.substring(m.indexOf("!") + 1);

		boolean hasid = false;
		boolean ispwd = false;

		System.out.println("Client로부터 login 요청을 받았습니다." + client);
		String[] s1 = IndianPoker.getInstance().db.getList().split("!");
		userList = new String[s1.length][3]; // 0:id, 1:pwd 2:score
		String[] s2 = null;
		int sc = 0;
		for (int i = 0; i < s1.length; i++) {
			s2 = s1[i].split("@");
			for (int j = 0; j < 3; j++) {
				userList[i][j] = new String();
				userList[i][j] = s2[j];
			}
		}
		for (int i = 0; i < userList.length; i++) {
			if (userList[i][0].equals(id)) { // 회원 목록에 아이디 있음
				hasid = true;
				if (userList[i][1].equals(pwd)) { // 패스워드 일치
					ispwd = true;
					sc = Integer.parseInt(userList[i][2]);
				}
			}
		}

		if (hasid) {
			if (ispwd) {
				if (cli.size() % 2 == 1) {
					IndianPoker.getInstance().p1 = new Player(id, sc);
				} else {
					IndianPoker.getInstance().p2 = new Player(id, sc);
				}
				System.out.println(findClient(client) + "님이 로그인하였습니다." + client);

				try {
					client.sendToClient("msg:LoginSuccess" + "login");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("비밀번호 오류 - 로그인 실패" + client);
				try {
					client.sendToClient("msg:quit잘못된 비밀번호입니다. 다시 로그인하세요.");
					client.close();
					cli.remove(client);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("미등록 사용자 - 로그인 실패" + client);
			try {
				client.sendToClient("msg:quit등록된 사용자가 아닙니다. 다시 로그인하세요.");
				client.close();
				cli.remove(client);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	void newID(String m, ConnectionToClient client) {
		m = m.substring(6);
		String[] n = m.split("!");
		System.out.println("Client로부터 회원가입 요청을 받았습니다." + client);
		IndianPoker.getInstance().db.newID(n[0], n[1]);
		if (cli.size() % 2 == 1) {
			IndianPoker.getInstance().p1 = new Player(n[0], 0);
		} else {
			IndianPoker.getInstance().p2 = new Player(n[0], 0);
		}
		System.out.println(findClient(client) + "님이 로그인하였습니다." + client);
		try {
			client.sendToClient("msg:LoginSuccess"+"newLogin");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void betting(String m, ConnectionToClient client) {
		int pnum = 0;
		int ckBet = Integer.parseInt(m.substring(7));

		if (cli.get(0).equals(client)) { // player1인경우
			pnum = 1;
			if (!IndianPoker.getInstance().p1.isbetting(ckBet)) {
				try {
					client.sendToClient("msg:warn" + "베팅 점수가 현재 점수보다 큽니다. 베팅점수를 다시 입력하세요.");
					System.out.println(findClient(client) + "의 베팅 점수 오류가 발생하였습니다." + client);
					return;
				} catch (IOException e) {
				}
			}
			if (ckBet < IndianPoker.getInstance().p2.betPoint) {
				try {
					client.sendToClient("msg:warn" + "현재 베팅점수가 상대방이 건 점수보다 작습니다. 더 큰 점수를 입력하세요");
					System.out.println(findClient(client) + "의 베팅 점수가 상대방 점수보다 작습니다." + client);
					return;
				} catch (IOException e) {
				}
			}
		}

		if (cli.get(1).equals(client)) { // player2인경우
			pnum = 2;
			if (!IndianPoker.getInstance().p2.isbetting(ckBet)) {
				try {
					client.sendToClient("msg:warn" + "베팅 점수가 현재 점수보다 큽니다. 베팅점수를 다시 입력하세요.");
					System.out.println(findClient(client) + "의 베팅 점수 오류가 발생하였습니다." + client);
					return;
				} catch (IOException e) {
				}
			}
			if (ckBet < IndianPoker.getInstance().p1.betPoint) {
				try {
					client.sendToClient("msg:warn" + "현재 베팅점수가 상대방이 건 점수보다 작습니다. 더 큰 점수를 입력하세요");
					System.out.println(findClient(client) + "의 베팅 점수가 상대방 점수보다 작습니다." + client);
					return;
				} catch (IOException e) {
				}
			}
		}
		String nowTurn = "";
		
		// 베팅 점수에 오류가 없다면
		switch (pnum) {
		case 1:
			IndianPoker.getInstance().p1.betPoint = ckBet;
			IndianPoker.getInstance().p1.point -= ckBet;
			nowTurn = "player2";
			break;
		case 2:
			IndianPoker.getInstance().p2.betPoint = ckBet;
			IndianPoker.getInstance().p2.point -= ckBet;
			nowTurn = "player1";
			break;
		default:
			break;
		}
		IndianPoker.getInstance().currentBP += ckBet;

		try {
			cli.get(0).sendToClient("msg:bet" 
		+ IndianPoker.getInstance().currentBP + "@" + IndianPoker.getInstance().p1.point +"@"+nowTurn
		+"@" + IndianPoker.getInstance().p2.point); // 현재 베팅점수, 내점수, 현재차례, 상대방점수 보냄
			cli.get(1).sendToClient("msg:bet" 
		+ IndianPoker.getInstance().currentBP + "@" + IndianPoker.getInstance().p2.point +"@"+nowTurn
		+"@" + IndianPoker.getInstance().p1.point); // 현재 베팅점수, 내점수, 현재차례, 상대방점수 보냄
		} catch (IOException e) {
		}

		if (IndianPoker.getInstance().p1.betPoint == IndianPoker.getInstance().p2.betPoint) {
			// player1과 player2의 베팅포인트가 같을 경우
			nextTurn();
		}
	}

	void giveup(String m, ConnectionToClient client) {
		// 포기버튼을 눌렀을 때

		if (cli.get(0).equals(client)) { // player 1이 포기한 경우
			if (IndianPoker.getInstance().p1.card.cardValue == 10) {
				IndianPoker.getInstance().p1.point -= 100; // Player1이 포기했는데 카드번호가 10인 경우 100점 차감
				if (IndianPoker.getInstance().p1.point < 0)
					IndianPoker.getInstance().p1.point = 0; // 차감했는데 점수가 -값인 경우 underflow로 0점처리
			}
			IndianPoker.getInstance().p2.win++; // 상대방의 승리
			IndianPoker.getInstance().p2.point += IndianPoker.getInstance().currentBP; //점수+
			IndianPoker.getInstance().currentBP = 0;
			
			try {
				cli.get(0).sendToClient("msg:win" + "턴 패배!");
				cli.get(1).sendToClient("msg:win" + "턴 승리!");
			} catch (IOException e) {
			}

			if (turnOver() == 1) { // 턴종료
				return; // 아예 게임 종료
			}
			try {
				cli.get(0).sendToClient("msg:turn" 
			+ IndianPoker.getInstance().p2.card.cardValue + "@" + turn
			+ "@" +IndianPoker.getInstance().p1.point + "@" +IndianPoker.getInstance().p2.point
			+ "@" +IndianPoker.getInstance().p1.card.cardValue); // 상대방 카드번호,턴수, 상대방 점수, 나의카드번호  보냄
				cli.get(1).sendToClient("msg:turn" 
			+ IndianPoker.getInstance().p1.card.cardValue + "@" + turn
			+ "@" +IndianPoker.getInstance().p2.point + "@" +IndianPoker.getInstance().p1.point
			+ "@" +IndianPoker.getInstance().p2.card.cardValue); // 상대방 카드번호,턴수 상대방점수, 나의카드번호  보냄
			} catch (IOException e) {
			}
		}

		if (cli.get(1).equals(client)) { // player 2가 포기한 경우
			IndianPoker.getInstance().p2.betPoint = 0;
			if (IndianPoker.getInstance().p2.card.cardValue == 10) {
				IndianPoker.getInstance().p2.point -= 100; // Player1이 포기했는데 카드번호가 10인 경우 100점 차감
				if (IndianPoker.getInstance().p2.point < 0)
					IndianPoker.getInstance().p2.point = 0;// 차감했는데 점수가 -값인 경우 underflow로 0점처리
			}
			IndianPoker.getInstance().p1.win++; // 상대방의 승리
			IndianPoker.getInstance().p1.point += IndianPoker.getInstance().currentBP; //점수+
			IndianPoker.getInstance().currentBP = 0;

			try {
				cli.get(0).sendToClient("msg:win" + "턴 승리!");
				cli.get(1).sendToClient("msg:win" + "턴 패배!");
			} catch (IOException e) {
			}

			if (turnOver() == 1) { // 턴종료
				return; // 아예 게임 종료
			}

			try {
				cli.get(0).sendToClient("msg:turn" 
						+ IndianPoker.getInstance().p2.card.cardValue + "@" + turn
						+ "@" +IndianPoker.getInstance().p1.point + "@" +IndianPoker.getInstance().p2.point
						+ "@" +IndianPoker.getInstance().p1.card.cardValue); // 상대방 카드번호,턴수, 상대방 점수, 나의카드번호  보냄
				cli.get(1).sendToClient("msg:turn" 
						+ IndianPoker.getInstance().p1.card.cardValue + "@" + turn
						+ "@" +IndianPoker.getInstance().p2.point + "@" +IndianPoker.getInstance().p1.point
						+ "@" +IndianPoker.getInstance().p2.card.cardValue); // 상대방 카드번호,턴수 상대방점수, 나의카드번호  보냄
			} catch (IOException e) {
			}
		}

	}

	void nextTurn() { // 두사람의 베팅포인트가 같을 때 실행되는 메소드
		int result = IndianPoker.getInstance().compare();
		
		try {
			switch (result) {
			case 1: // player1 승리
				cli.get(0).sendToClient("msg:win" + "턴 승리!");
				cli.get(1).sendToClient("msg:win" + "턴 패배!");
				break;
			case 2: // player2 승리
				cli.get(0).sendToClient("msg:win" + "턴 패배!");
				cli.get(1).sendToClient("msg:win" + "턴 승리!");
				break;
			case 0: // 무승부
				cli.get(0).sendToClient("msg:win" + "무승부!\n현재 베팅포인트는 다음 턴으로 넘어갑니다.");
				cli.get(1).sendToClient("msg:win" + "무승부!\n현재 베팅포인트는 다음 턴으로 넘어갑니다.");
				break;
			}
			if (turnOver() == 1) { // 턴종료
				return; // 아예 게임 종료
			}
			cli.get(0).sendToClient("msg:turn" 
					+ IndianPoker.getInstance().p2.card.cardValue + "@" + turn
					+ "@" +IndianPoker.getInstance().p1.point + "@" +IndianPoker.getInstance().p2.point
					+ "@" +IndianPoker.getInstance().p1.card.cardValue); // 상대방 카드번호,턴수, 상대방 점수, 나의카드번호  보냄
			cli.get(1).sendToClient("msg:turn" 
					+ IndianPoker.getInstance().p1.card.cardValue + "@" + turn
					+ "@" +IndianPoker.getInstance().p2.point + "@" +IndianPoker.getInstance().p1.point
					+ "@" +IndianPoker.getInstance().p2.card.cardValue); // 상대방 카드번호,턴수 상대방점수, 나의카드번호  보냄		
			} catch (IOException e) {
		}
	}

	boolean isGameOver() {
		if (turn == 11)
			return true;
		if (IndianPoker.getInstance().p1.point == 0 || IndianPoker.getInstance().p2.point == 0) {
			return true;
		}
		return false;
	}

	int turnOver() {
		// 턴이 종료됐으므로 초기화
		IndianPoker.getInstance().p1.betPoint = 0;
		IndianPoker.getInstance().p2.betPoint = 0;

		turn++;

		if (isGameOver()) {
			gameOver();
			return 1;
		}
		IndianPoker.getInstance().p1.card.selectCard();
		IndianPoker.getInstance().p2.card.selectCard();

		return 0;
	}

	void gameOver() {
		int score1 = IndianPoker.getInstance().p1.win * 10 + IndianPoker.getInstance().p1.point;
		int score2 = IndianPoker.getInstance().p2.win * 10 + IndianPoker.getInstance().p2.point;

		if (turn == 11) { // 10번의 턴이 모두 진행되어 게임이 종료되는 경우
			System.out.println("턴이 모두 종료되었으므로 게임을 종료합니다.");

			if (score1 > score2) { // player1의 최종승리!
				System.out.println(findClient(cli.get(0)) + "가 최종 승리하였습니다.");
				IndianPoker.getInstance().p1.score += 100;
				IndianPoker.getInstance().db.UpdateScore(IndianPoker.getInstance().p1.name,
						IndianPoker.getInstance().p1.score);
			} else if (score1 < score2) { // player2의 최종승리!
				System.out.println(findClient(cli.get(1)) + "가 최종 승리하였습니다.");
				IndianPoker.getInstance().p2.score += 100;
				IndianPoker.getInstance().db.UpdateScore(IndianPoker.getInstance().p2.name,
						IndianPoker.getInstance().p2.score);
			} else { // 무승부
				System.out.println("무승부입니다.");
				IndianPoker.getInstance().p1.score += 50;
				IndianPoker.getInstance().p2.score += 50;

				IndianPoker.getInstance().db.UpdateScore(IndianPoker.getInstance().p1.name,
						IndianPoker.getInstance().p1.score);
				IndianPoker.getInstance().db.UpdateScore(IndianPoker.getInstance().p2.name,
						IndianPoker.getInstance().p2.score);
			}

			try {
				cli.get(0).sendToClient("msg:finish" + "turnOver");
				cli.get(1).sendToClient("msg:finish" + "turnOver");
			} catch (IOException e) {
			}
		}

		if (IndianPoker.getInstance().p1.point == 0) { // player1의 점수가 0점이 되어 게임이 종료되는 경우
			System.out.println(findClient(cli.get(0)) + "의 점수가 0점이 되어 게임을 종료합니다.");
			System.out.println(findClient(cli.get(1)) + "가 최종 승리하였습니다."); // player2 승리
			IndianPoker.getInstance().db.UpdateScore(IndianPoker.getInstance().p2.name,
					IndianPoker.getInstance().p2.score);

			try {
				cli.get(0).sendToClient("msg:finish" + "p1_pt0");
				cli.get(1).sendToClient("msg:finish" + "p1_pt0");
			} catch (IOException e) {
			}

		}

		if (IndianPoker.getInstance().p2.point == 0) { // player2의 점수가 0점이 되어 게임이 종료되는 경우
			System.out.println(findClient(cli.get(1)) + "의 점수가 0점이 되어 게임을 종료합니다.");
			System.out.println(findClient(cli.get(0)) + "가 최종 승리하였습니다."); // player1 승리
			IndianPoker.getInstance().db.UpdateScore(IndianPoker.getInstance().p1.name,
					IndianPoker.getInstance().p1.score);
			try {
				cli.get(0).sendToClient("msg:finish" + "p2_pt0");
				cli.get(1).sendToClient("msg:finish" + "p2_pt0");
			} catch (IOException e) {
			}
		}
	}
}
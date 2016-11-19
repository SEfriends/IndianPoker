package Controller;

import java.util.Scanner;

public class IndianPoker {
	public static Card card;
	public static Turn turn;
	public static GameRule gamerule;
	String name;
	int point;
	int betpoint;
	int op_cardNum;
	int my_cardNum;
	int result = 0;	//결과
	int gameTurn;	//게임 횟수
	int win = 0;		//승리한 횟수
	
	public IndianPoker(){
		card = new Card();
		turn = new Turn();
		gamerule = new GameRule(card);
	}

	public void start(String name){
		Scanner s = new Scanner(System.in);
		boolean ok = false;	//베팅을 했을때 true 안했을때 false
		point = 200;		//기본점수 200점 지정
		
		while(true){
		gameTurn = turn.getTurn();
		if(gameTurn == 10){	//10번 게임 후 종료
			System.out.println("게임종료!! 점수 :"+ point);
			return;
		}
		if(point == 0){		//점수를 모두 잃었을때 게임 종료
			System.out.println("점수를 모두 잃었습니다. 총"+win+"번 이겼습니다.");
			return;
		}
		ok = false;			//매번 시작때 마다 배팅 false로 지정
		gameTurn = gameTurn + 1;//게임 턴 +1
		turn.setTurn(gameTurn);//게임 턴 저장 -> 게임 클래스가 따로 필요한 이유가??
		
		System.out.println(gameTurn +"번째 게임입니다.");
		
		card.setCard();	//카드 뽑기
		op_cardNum = card.getopCard();
		System.out.println("상대방의 카드는 "+op_cardNum+"입니다.");
		
		while (ok == false){
			System.out.println("현재점수 : "+point+", 베팅할 점수를 입력하세요. 0:포기");
			betpoint = s.nextInt();
			if(betpoint == 0){	//게임 포기
				gamerule.giveUp(point);
				result = 1;	//1: 패배, 2: 승리, 3: 무승부
				ok = true;
			}
			else{
				ok = gamerule.betting(name, betpoint, point);
				result = gamerule.compare();	//결과 정보 저장
			}
		}
		
		my_cardNum = card.getmyCard();
		System.out.println(name +"님의 카드는 "+my_cardNum+"입니다.");
		if(result == 1){
			System.out.println("패배하였습니다.");
		}
		else if(result == 2){
			System.out.println("승리하였습니다.");
			win = win + 1;
		}
		else if(result == 3){
			System.out.println("무승부입니다.");
		}
		
		point = gamerule.getPoint();
		System.out.println("남은 포인트는 "+ point + "이고 " + win + "번 이겼습니다.\n");
		}
	}
	
	public static void main(String[] args){
		String pname;
		Scanner s = new Scanner(System.in);
		IndianPoker ip = new IndianPoker();
		
		System.out.println("플레이어의 이름을 입력해주세요");
		pname = s.next();
		ip.start(pname);
	}
	
}

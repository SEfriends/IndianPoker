package Model;

import IO.Database;

public class IndianPoker { 
	private static IndianPoker uniqueInstance; //싱글톤패턴

	public Player p1;
	public Player p2;
	public Database db;
	public int currentBP; //현재 게임에 걸려있는 베팅 포인트

	private IndianPoker(){	
		db = new Database();
		currentBP = 0;
	}
	
	public static IndianPoker getInstance(){
		if(uniqueInstance == null){
			uniqueInstance = new IndianPoker();
		}
		return uniqueInstance;
	}
	
	public int compare(){
		if(p1.card.cardValue > p2.card.cardValue){ //Player1 승리
			System.out.println("Player1의 승리");
			p1.point += currentBP;
			currentBP = 0;
			p1.win++;
			return 1;
		}else if(p1.card.cardValue < p2.card.cardValue){ //Player2 승리
			System.out.println("Player2의 승리");
			p2.point += currentBP;
			currentBP = 0;
			p2.win++;
			return 2;
		}
		//무승부인 경우 아무것도 하지 않음
		System.out.println("무승부");
		return 0;
	}
}
package Model;

import java.util.Scanner;

public class Player {
	public String name;
	public Card card;
	public int point;
	public int betPoint;	
	public int win;
	
	public Player(String n){
		name = n; //생성자에서 이름을 매개변수로 받아 초기화
		card = new Card(); //해당 플레이어가 사용할 카드
		point = 200; //기본 점수 200점
		betPoint = 0; //베팅 포인트 초기화
		win = 0; //이긴 횟수		
	}
	public boolean isbetting(){
		return point >= betPoint; // 현재 점수보다 베팅포인트가 작거나 같으면 문제 없으므로 True 아니면 False
	}
	public int Betting(){
		Scanner s = new Scanner(System.in);
		while(true){
			System.out.println("베팅할 점수를 입력하세요 (0은 포기): ");
			betPoint = s.nextInt();
			if(isbetting()){
				break;
			}else{
				System.out.println("베팅 점수가 현재 점수보다 큽니다. 베팅점수를 다시 입력하세요.");
			}
		}
		return betPoint;		
	}
}

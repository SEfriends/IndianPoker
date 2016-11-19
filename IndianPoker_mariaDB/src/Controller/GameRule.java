package Controller;

public class GameRule {
	public static Card card;
	int point;		//현재 포인트
	int betpoint;	//베팅한 포인트
	int setbpoint;	//무승부일때 저장되는 포인트
	int op_cardNum;
	int my_cardNum;
	
	public GameRule(Card card){
		this.card = card;
	}
	
	public int getPoint(){
		return point;
	}
	//게임 베팅할때
	public boolean betting(String name, int betpoint, int point){
		this.point = point;
		this.betpoint = betpoint;
		
		if(betpoint > point){	//베팅점수가 남은 점수보다 클때 다시 배팅
			System.out.println("배팅점수가 남은 점수보다 큽니다.");
			return false;
		}
		this.point = point - betpoint;	//베팅후 점수를 저장
		
		return true;
	}
	
	public int compare(){
		int result = 0;
		
		op_cardNum = card.getopCard();
		my_cardNum = card.getmyCard();
		
		if(op_cardNum > my_cardNum){		//상대카드가 큰 경우
			result = 1;
			setbpoint = 0;		//무승부시 저장한 점수 제거
		}
		else if(op_cardNum < my_cardNum){	//내 카드가 큰 경우
			result = 2;
			point = point + betpoint*2 + setbpoint*2;	//배팅한 포인트의 2배, 저장된 점수의 2배를 합산(게임룰 임의지정)
			setbpoint = 0;
		}
		else{								//같은 경우
			result = 3;
			setbpoint = betpoint;	//베팅 점수를 저장
		}
		
		return result;
	}
	
	public void giveUp(int point){
		my_cardNum = card.getmyCard();
		this.point = point;
		
		if(my_cardNum == 10){
			this.point = this.point - 10; //최고 카드일 때 패널티
		}
	}

}

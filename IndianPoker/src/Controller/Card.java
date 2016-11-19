package Controller;

public class Card {
	int p1_CardNum, p2_CardNum;
	int[] card = new int[10];
	
	public Card(){
		for(int i=0; i<10; i++)
			card[i] = 0;		//카드 0으로 초기화
		p1_CardNum = 1;
		p2_CardNum = 1;
	}
	public void setCard(){
		p1_CardNum = (int)(Math.random()*10) + 1;
		p2_CardNum = (int)(Math.random()*10) + 1;
		
		while(card[p1_CardNum-1] == 2){
			p1_CardNum = (int)(Math.random()*10) + 1;
		}
		while(card[p2_CardNum-1] == 2){
			p2_CardNum = (int)(Math.random()*10) + 1;
		}
		card[p1_CardNum-1]++;
		card[p2_CardNum-1]++;
		
	}
	public int getmyCard(){
		
		return p1_CardNum;
	}
	public int getopCard(){
		
		return p2_CardNum;
	}

}

package Model;

import java.util.Vector;

/**
 * 
 * Card 클래스에서 구현해야 할 부분
 * 카드는 한 게임당 총 2벌의 카드가 필요
 * 즉, 1~10까지의 카드가 각각 2번씩 나올 수 있음 
 * Vector나 List로 구현 후 
 * 해당 인덱스를 난수발생 시켜 인덱스의 값을 리턴하면 중복을 피할 수 있고
 * 한 클래스의 객체로 2벌의 카드 구성 가능
 * 
 */

public class Card {
	Vector<Integer> card;
	public int cardValue;
	
	public Card(){
		card = new Vector<>(); //객체 생성
		for(int i = 0; i<10; i++) //1~10까지로 초기화
			card.add(i+1);
		cardValue = 0; //cardValue 초기화
	}
	
	public int selectCard(){
		int index = (int)(Math.random()*card.size()); // Vector 카드의 크기범위 내의 난수를 발생시켜 해당 인덱스를 카드값으로 정함
		cardValue = card.get(index);
		card.remove(index);
		
		return cardValue;
	}
}

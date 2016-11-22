package Controller;

import java.util.*;

/**
 * 
 */
public class Card {

	public int p1_CardNum; // 내 번호
	public int p2_CardNum; // 상대방 번호
	public Vector<Integer> op_vector;
	public Vector<Integer> my_vector;

	public Card() {
		op_vector = new Vector<Integer>();
		my_vector = new Vector<Integer>();
		p1_CardNum = 1;
		p2_CardNum = 1;
	}

	/**
	 * @return
	 */
	public void setCard() { // 카드 뽑는 부분 (중복된거 없앨때 편하게 하려고 배열 대신 Vector사용)
		p1_CardNum = (int) (Math.random() * 10) + 1;
		p2_CardNum = (int) (Math.random() * 10) + 1;

		if (my_vector.contains((Integer) p1_CardNum)) { // 이미 뽑았던 카드 다시 뽑으면 다시
														// 난수생성해서 vector에 추가

			while (true) {
				if (!(my_vector.contains((Integer) p1_CardNum))) {
					my_vector.add(p1_CardNum);
					break;
				}
				p1_CardNum = (int) (Math.random() * 10) + 1;
			}
		} else {
			my_vector.add(p1_CardNum);
		}

		if (op_vector.contains((Integer) p2_CardNum)) {

			while (true) {
				if (!(op_vector.contains((Integer) p2_CardNum))) {
					op_vector.add(p2_CardNum);
					break;
				}
				p2_CardNum = (int) (Math.random() * 10) + 1;
			}
		} else {
			op_vector.add(p2_CardNum);
		}

	}

	public int getMyCard() {
		return p1_CardNum;
	}

	public int getCard() {
		// TODO implement here
		return p2_CardNum;
	}

}
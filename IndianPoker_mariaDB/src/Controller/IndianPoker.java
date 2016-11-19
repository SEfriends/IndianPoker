package Controller;

import java.util.Scanner;

import IO.Database;

public class IndianPoker {
	static Database db=new Database();
	static IndianPoker ip = new IndianPoker();
	
	public static Card card;
	public static Turn turn;
	public static GameRule gamerule;
	
	static String id;
	static String pw;//로그인할 id,pw
	
	String name;
	int point;
	int betpoint;
	int op_cardNum;
	int my_cardNum;
	int result = 0;	//결과
	int gameTurn;	//게임 횟수
	int win = 0;		//승리한 횟수
	public static String[][] list;
	static boolean joinnew = false; //새로운회원인지 로그인한회원인지 판단

	public IndianPoker(){
		card = new Card();
		turn = new Turn();
		gamerule = new GameRule(card);
		
		String[] s1 = db.getList().split("!");// 회원정보를 나누는 구분자
		list = new String[s1.length][2]; // 0:id, 1:pw
		String[] s;
		
		for(int i = 0;i<s1.length;i++){
			s = s1[i].split("@"); //id,pw 나누는 구분자
			for(int j = 0 ;j < 2;j++){
				list[i][j] = new String();
				list[i][j] = s[j];
			}
		}
	}
	public static void join(String id,String pw){
		boolean doubleid = false;
		joinnew = true;
		db.newID(id, pw); //login테이블에 추가
		for(int i=0;i<IndianPoker.list.length;i++){
			if(IndianPoker.list[i][0].equals(id)){
				doubleid=true;
				ip.start(id);
			}
		}
		if(doubleid){
			System.out.println("이미존재하는 id입니다.");
		}
		
	}
	public static void login(String id,String pw){
		boolean hasid = false;
		boolean ispwd = false;
		Scanner s=new Scanner(System.in);
		
		for(int i = 0;i<IndianPoker.list.length;i++){
			if(IndianPoker.list[i][0].equals(id)){ //회원인지 확인
				hasid = true;
				if(IndianPoker.list[i][1].equals(pw)){ //회원의 아이디확인
					ispwd = true;
				}	
			}
		}
		if(hasid){
			if(ispwd){
				System.out.println(id+"회원이 로그인하였습니다.");
				System.out.println("게임시작0번 랭킹보기1번");
				int menu2=s.nextInt();
				if(menu2==0){
					System.out.println("게임을시작합니다."); 
					ip.start(id);

				}
				if(menu2==1){
					System.out.println("<<랭킹>>");
					System.out.println(ip.viewRanking());
				}
			}
			else{
				System.out.println("비밀번호가 일치하지 않습니다.");
			}
		}else{
			System.out.println("존재하는 id가 없습니다.");
		}
		
	}
	public String viewRanking(){
		String rank=db.Ranking();
		return rank;
	}
	public void start(String id){
		Scanner s = new Scanner(System.in);
		boolean ok = false;	//베팅을 했을때 true 안했을때 false
		point = 200;		//기본점수 200점 지정
		
		while(true){
		gameTurn = turn.getTurn();
		if(gameTurn == 10){	//10번 게임 후 종료 후 디비저장
			System.out.println("게임종료!! 점수 :"+ point +" 이긴횟수 : "+win);
			if(joinnew == true)
				db.insert(id, win); //새로운회원이면 삽입
			else
				db.UpdateScore(id, win); //왓던회원이면 업데이트
			return;
		}
		if(point == 0){		//점수를 모두 잃었을때 게임 종료 후 디비저장
			System.out.println("점수를 모두 잃었습니다. 총"+win+"번 이겼습니다.");
			if(joinnew == true)
				db.insert(id, win); 
			else
				db.UpdateScore(id, win);
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
				ok = gamerule.betting(id, betpoint, point);
				result = gamerule.compare();	//결과 정보 저장
			}
		}
		
		my_cardNum = card.getmyCard();
		System.out.println(id +"님의 카드는 "+my_cardNum+"입니다.");
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
		System.out.println("회원가입은0번 로그인은1번");
		Scanner s = new Scanner(System.in);
		
		int menu=s.nextInt();
		if(menu==0){
			System.out.println("회원가입을 시작합니다. id와 pw를 입력해주세요");
			id = s.next();
			pw = s.next();
			join(id,pw);
			ip.start(id); //회원가입하면 바로 게임시작됨
		}
		if(menu==1){
			System.out.println("로그인을 시작합니다. id와 pw를 입력해주세요");
			id = s.next();
			pw = s.next();
			login(id,pw);
			
		}
	}

	
}

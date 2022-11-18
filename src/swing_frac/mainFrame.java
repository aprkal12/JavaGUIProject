package swing_frac;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// 22.11.18
// 쓰레드를 여기서 만들고 러너블도 여기서 만들어야할것 같다.
// 이벤트 리스너는 포커스 옮겨서 가능하니 그걸 활용하자
// setfocusable과 request포커스로 여러개의 이벤트리스너 구현가능한거 확인
// 구현 목표 :
// entryframe + entryScreen을 Thread1으로,
// mainFrame을 Thread2 로 해서
// Thread1이 끝나고 nickname설정이 완료되면 Thread2 실행시키기
// 현재 닉네임 추출 흐름까지는 성공
// ----- 성공, 플레이어 프레임 사이즈랑 라벨 사이즈 조절, 맵 넘어가는 x,y좌표 수정해주면 됌
// 클래스도 분리해서 정리하자

// 캐릭터 객체 => 하나의 패널로 해서 이미지 씌워야할듯

// 22.11.14 update
// 기본 라벨에 관한 세팅은 다시 objectsettings로 넘김
// 맵 자체에 라벨 세팅 만듦, 텍스트만 바꿀 수 있도록 설정

// 22.11.13 update
// 맵
// ㅁ  ㅁ
// ㅁㅁㅁ
// ㅁ  ㅁ
// 이렇게 만들고, 이동불가능 반경 설정해서 따로 클래스화 함
// moveTomap생성되고, 이벤트리스너부분 변경됌
// + 맵별 라벨추가

// 22.11.02
// 캐릭터의 이동 불가능한 영역 지정 
// rectangle 클래스 intersects 찾아보기
// 방법 1. 맵(패널)별 이동불가 위치 개별 저장 -> 각 맵별 분기문이 달라야함 개선필요해보임 
// 방법 2. 구현 가능여부는 모르지만 각 패널 별 객체 위치와 캐릭터의 충돌을 판단해 이동불가 하게 만들기
// 방법 3. 공간 해시 그리드 광범위 충돌검사 -> 한 패널의 그리드를 쪼개서 특정 그리드별로 객체 검사 판단을 함 => 블로그 내용 참고

// 22.11.04
// 쓰레드로 초기화면에서 입력받는 동안 기다리자
// 일단 이 프레임 구조도 바꿔야할 것 같다.
// 맵을 그냥 하나의 프레임으로 바꿔야하나

public class mainFrame extends JFrame{
	static final int Width = 800, Height = 600;
	static boolean upP = false;
	static boolean downP = false;
	static boolean leftP = false;
	static boolean rightP = false;
	private Container cPane;
	private moveToMap mControl;

	private objectSettings objtest;
	private map mapP[];
	public player player;
	//static map1Object3 t3 = new map1Object3();
	//static map1Object1 t1 = new map1Object1();
	//static map1Object2 t2 = new map1Object2();
	private entryFrame startP;
	private Thread th1;
	private Thread th2;
	static int cur = 3;

	// 부드러운 캐릭터 이동을 위한 변수선언
	public mainFrame() {
		cPane = getContentPane();
		mControl = new moveToMap();
		objtest = new objectSettings();
		mapP = new map[7];
		player = new player();
		startP = new entryFrame();
		Setframe();
		Setpanel();
		entryEventset();
		testrunna runna = new testrunna();
		th1 = new Thread(runna);
		th1.start();
		try{
			th1.join();
		}catch(InterruptedException e){
			System.out.println("조인 끝");
		}
		startP.setVisible(false);
		cPane.remove(startP);
		cPane.add(mapP[3]);
		JLabel l1 = new JLabel();
		l1.setFont(new Font("Consolas 굵게", Font.BOLD, 15));
		l1.setBounds(0, 0, 200, 20);
		l1.setBackground(Color.BLACK);
		l1.setText(startP.setNickname.nickname);
		System.out.println(l1.getText());
		player.add(l1);
		mapEventset();
	}
	public void Setframe() {
		setTitle("test1");
		setSize(Width, Height);
		
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void Setpanel() {
		for(int i = 0; i<7; i++) {
			mapP[i] = new map();
		}
		cPane.add(startP);
		System.out.println(startP.setNickname.nickname);
		//cPane.add(mapP[3]);

		mapP[3].add(player);
		// 오브젝트 추가 테스트
		//mapP[3].add(t3);
		//mapP[3].add(t1);
		//mapP[3].add(t2);
		
		//labelSet[] la = new labelSet[7];
		//for(int i = 0; i<7; i++){
		//	la[i] = new labelSet();
		//}
		mapP[0].setLabeltext("102, 101 복도");
		mapP[1].setLabeltext("디정 후문");
		mapP[2].setLabeltext("연구실들");
		mapP[3].setLabeltext("디정 중앙");
		mapP[4].setLabeltext("디셈 복도");
		mapP[5].setLabeltext("디정 정문");
		mapP[6].setLabeltext("116 복도");

		// 22.11.11
		// 맵별 라벨, 폰트 따로 모듈화 필요해보임

		// 22.11.13
		// 라벨 이름 설정하는거 따로 함수 필요해보임

		// 22.11.14
		// 그냥 맵 클래스에 라벨을 자체적으로 포함시킴
		// 라벨 함수는 컴포넌트세팅쪽으로 넘겨야겠음

		//mapP[3].setBackground(Color.BLUE);
		mapP[1].setBackground(Color.WHITE);
		mapP[2].setBackground(Color.GREEN);
		mapP[0].setBackground(Color.YELLOW);
		mapP[4].setBackground(Color.ORANGE);
		
		mapP[3].setVisible(true);
	}
	public void leftright(int target){

		mapP[cur].setVisible(false); // 이동 전 맵 안보이게
		cPane.remove(mapP[cur]); // 이동 전 맵 삭제
		cur = target;
		cPane.add(mapP[cur]); // 이동하려는 맵 추가
		mapP[cur].setVisible(true); // 이동하려는 맵 보이게
		player.setLocation(740-player.getX(), player.getY()); // 이동 시 캐릭터 위치
		mapP[cur].add(player); // 캐릭터 맵 이동시키기
		System.out.println("current map : " + cur);
		player.setFocusable(true); // 맵을 이동시키면 그 패널에서의 포커서블이 풀리기때문에
		player.requestFocus(); // 다시 설정해줘야함
	}
	public void topbottom(int target){
		mapP[cur].setVisible(false); // 이동 전 맵 안보이게
		cPane.remove(mapP[cur]); // 이동 전 맵 삭제
		cur = target;
		cPane.add(mapP[cur]); // 이동하려는 맵 추가
		mapP[cur].setVisible(true); // 이동하려는 맵 보이게
		player.setLocation(player.getX(), 500 - player.getY()); // 이동 시 캐릭터 위치
		mapP[cur].add(player); // 캐릭터 맵 이동시키기
		System.out.println("current map : " + cur);
		player.setFocusable(true);
		player.requestFocus();
	}
	public void mapEventset(){
		player.setFocusable(true);
		player.requestFocus();
		player.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) { //키 눌렀을때
				// TODO Auto-generated method stub
				switch (e.getKeyCode()) {//키 코드로 스위치
					case KeyEvent.VK_DOWN: //방향키(아래) 눌렀을때
						downP = true;
						break;
					case KeyEvent.VK_UP: //방향키(위)눌렀을때
						upP = true;
						break;
					case KeyEvent.VK_LEFT:// 방향키(왼)눌렀을때
						leftP = true;
						break;
					case KeyEvent.VK_RIGHT:// 방향키(오른)눌렀을때
						rightP = true;
						break;
					// 좌표의 기준이 패널의 좌측하단끝 인것 같음
					default:
						break;
				}
				if (upP) {
					if(!(player.getX() < 100 && player.getY() < 110)) {
						if(mControl.CanIGoTopMap(cur, player.getY())){
							mControl.MoveToTopMap();
							topbottom(mControl.getTarget());
						}
						else if(player.getY() != 0){
							player.setLocation(player.getX(), player.getY()-10);
						}
					}
				}
				if (downP) {
					if(mControl.CanIGoBottomMap(cur, player.getY())){
						mControl.MoveToBottomMap();
						topbottom(mControl.getTarget());
					}
					else if(player.getY() != 500){
						player.setLocation(player.getX(), player.getY()+10);
					}
				}
				if (leftP) {
					if(!(player.getX() < 110 && player.getY() < 100)) {
						if(mControl.CanIGoLeftMap(cur, player.getX())) {
							mControl.MoveToLeftMap();
							leftright(mControl.getTarget());
						}
						// out of index 방지
						else if(player.getX() != 0) {
							player.setLocation(player.getX()-10, player.getY());
						}
					}
				}
				if (rightP) {
					if(mControl.CanIGoRightMap(cur, player.getX())) {
						mControl.MoveToRightMap();
						leftright(mControl.getTarget());
					}
					else if(player.getX() != 740) {
						player.setLocation(player.getX()+10, player.getY());
					}
				}
				System.out.println("keypressed X : " + player.getX() + " Y : " + player.getY());
			}
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode()==KeyEvent.VK_UP) {
					upP = false;
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					downP = false;
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					leftP = false;
				}
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					rightP = false;
				}
			}
		});
		System.out.println(cur);
	}
	public void entryEventset() {
		startP.setNickname.t1.addKeyListener(new KeyAdapter(){
			// 22.11.18 여기서 만들어서 쓰레드 돌려보자 여기서 리스너 둘 다 만들고
			// 쓰레드 끝나면 포커스를 넘기는 식으로 하면 될듯요~
			// 잘 된다 나이스
			// 클래스 정리좀 하자 ㅋㅋㅋㅋㅋ
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					//th1.interrupt();
					startP.setNickname.nickname = startP.setNickname.t1.getText();
					System.out.println(startP.setNickname.nickname);
					th1.interrupt();
					//JOptionPane.showMessageDialog(null, startP.setNickname.nickname);
				}
			}
		});

	}
	class testrunna implements Runnable{
		private JLabel l1;
		public testrunna(){
			objectSettings objset = new objectSettings();
			l1 = objset.getLabel();
		}
		public void run(){
			while(true){
				try{
					Thread.sleep(500);
					System.out.println("쓰레드 테스트 중 nickname = " + startP.setNickname.nickname);
					l1.setText(startP.setNickname.nickname);
				}catch(InterruptedException e){
					System.out.println("thread1 끝");
					return;
				}
			}
		}
	}
	public static void main(String[] args) {
		new mainFrame();
	}
}


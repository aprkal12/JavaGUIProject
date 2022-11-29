package swing_frac;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;
import java.util.List;
import javax.swing.*;

// 22.11.27
// 멀티 제대로 되도록 함
// 하지만 중복 플레이어가 자꾸 쌓이는 문제가 생긴다.
// 유저가 많아질 수록 문제는 점점 커짐
// 작동은 제대로 되지만 연산속도를 위해 개선 필요해 보임

// 22.11.24
// 어찌저찌 반영이 되는 방법을 찾긴 했는데 어설픈 부분이 한두 곳이 아니다
// 금욜 오전 근로때 마저 찾아보자

// 22.11.23
// 리드라인을 이용해서 서로 데이터 가져와서 반영해보자

// 22.11.23
// mainFrame이 client고 ServerAdmin이 server인 상황임
// 일단 클라이언트가 접속할 때마다 서버에 캐릭터 추가까지는 했다..
// 해결해야할 문제
// 문제는 각 클라이언트들도 추가된 캐릭터들이 보여야 한다는 점
// 그리고 각 추가된 캐릭터들도 각각 이벤트리스너를 적용시켜줘야하고,
// 또 방향 이동하면 그것도 서버-클라이언트 전부 실시간 반영되어야 한다.

// 생각해본 방법
// 서버에 추가될 때마다 닉네임 정보를 클라이언트로 쏴줘서 그걸 받은 클라이언트가
// 본인의 맵에 다른 유저의 캐릭터를 추가하고 다시 그린다.
// 캐릭터는 또 이동해야하니까 이동해서 캐릭터 위치가 바뀔 때마다 다시 위치반영해서
// 각각 클라이언트마다 다시 그려야함

// 생각해본 구현방식
//

// 22.11.21
// 서버구현, 멀티실행 intellij 설정(구글링 참고)

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
// 22.11.11
// 맵별 라벨, 폰트 따로 모듈화 필요해보임

// 22.11.13
// 라벨 이름 설정하는거 따로 함수 필요해보임

// 22.11.14
// 그냥 맵 클래스에 라벨을 자체적으로 포함시킴
// 라벨 함수는 컴포넌트세팅쪽으로 넘겨야겠음

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

public class ClientMain extends JFrame{
	static final int WIDTH = 800, HEIGHT = 600;
	static boolean upP = false;
	static boolean downP = false;
	static boolean leftP = false;
	static boolean rightP = false;
	private Container cPane;
	private moveToMap mControl;

	private objectSettings objTest;
	static public map mapP[];
	public player player;
	//static map1Object3 t3 = new map1Object3();
	//statictField = new map1Object1();
	//static map1Object2 t2 = new map1Object2();
	private entryFrame entryP;
	private entryScreen setNicknameP;
	private JTextField tField;
	private StringBuffer longString;
	public ChatPanel chatPanel;
	public String userName;
	private Thread entryThread;
	static int curMap = 3;
	static Vector<String> playersName = new Vector<String>();
	static Vector<player> players = new Vector<player>();
	connectServer connectServer = new connectServer();
	static List<player> otherPlayers = Collections.synchronizedList(new ArrayList<player>());
	static List<player> targetNotAdd = Collections.synchronizedList(new ArrayList<player>());
	public InformationPanel ip;

	// 부드러운 캐릭터 이동을 위한 변수선언
	public ClientMain() throws IOException {
		Socket socket = null;
		BufferedReader in = null;

		// GUI 부분 초기화
		cPane = getContentPane();
		mControl = new moveToMap(curMap);
		objTest = new objectSettings();
		mapP = new map[7];
		player = new player();
		entryP = new entryFrame();
		setNicknameP = new entryScreen();
		tField = setNicknameP.getTextField();
		userName = setNicknameP.getNickname();
		chatPanel = new ChatPanel();

		longString = new StringBuffer();

		playersName = connectServer.playersName;

		setFrame();
		setPanel();
		entryEventSet();

		// 실행 제어
		entryRunnable runna = new entryRunnable();
		entryThread = new Thread(runna);
		entryThread.start();
		try{
			entryThread.join();
		}catch(InterruptedException e){
			System.out.println("Thread 조인 오류");
			// 아마 닉네임을 입력 받은 후에 서버 실행 시켜야겠지?
		}
		// 닉네임 입력 후 화면 제거
		entryP.setVisible(false);
		cPane.remove(entryP);

		// 맵 패널로 변경, 채팅창 추가
		setSize(1100, 600);
		cPane.add(chatPanel);
		cPane.add(mapP[3]);
		player.setPlayerNickname(userName);
		repaint();

		try{
			socket = new Socket("localhost", 7999);
			System.out.println("[서버와 연결되었습니다.]");
			mapEventSet(socket);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// 클라이언트가 서버에 접속했을 경우
			// 본인외의 서버 혹은 클라이언트의 위치에 맞게 캐릭터 새로 그리기
			// 현재 같은 맵에 있는 유저끼리만 그려야함

			// 클라이언트가 서버로 채팅메세지 보내는 스레드
			UserThread userThread = new UserThread(socket, userName, chatPanel.textField);
			userThread.start();

			while(in != null){
				String inputMsg = in.readLine();
				if(inputMsg.contains("들어오셨습니다.")){
					chatPanel.textArea.append(inputMsg+"\n");
					String name = in.readLine();
					// 만들어야 하는 태스크
					// 1. 처음 접속 시, 본인 제외 플레이어 리스트에 있는 캐릭터 그리기
					// 2. 움직이면 서버로 정보를 보내고, 서버는 모든 클라이언트에게 뿌려줌
					// 3. 뿌려준 정보를 받아서 자기 자신의 정보면 무시, 다른 클라이언트의 정보면 반영
					System.out.println("새로 받아온 이름 " +name);
					int countPlayers = Integer.parseInt(in.readLine());
					System.out.println("prenames " +countPlayers);
					System.out.println("닉네임들");
					System.out.println("player 네임은 : " + player.getName());
					// 유닛 중복때매 위 처럼 클리어 하고 다시 추가 하고 싶었는데
					// 그러면 추가된 애들이 다른 객체가 되어버려서
					// 예전 플레이어들의 실시간 위치 업데이트가 적용되지않음
					for(int i = 0; i<countPlayers; i++){
						boolean canIAddPlayers = true;
						String names = in.readLine();

						for(int j = 0; j<otherPlayers.size(); j++){
							if(otherPlayers.get(j).getName().equals(names)){
								canIAddPlayers = false;
								break;
							}
						}
						if(canIAddPlayers){
							player otherPlayer = new player();
							otherPlayer.setPlayerNickname(names);
							otherPlayers.add(otherPlayer);
							players.add(otherPlayer);
							System.out.println("countPlayer 실행 중 ");
						}
					}
				}
				else if(!inputMsg.matches("^[0-9]+$")){
					if(("["+userName+"]님이 나가셨습니다.").equals(inputMsg)){
						break;
					}
					else if(inputMsg.contains("나가셨습니다.")){
						String outPlayer = in.readLine();
						int outPlayerIndex = 0;
						for(int i = 0; i<otherPlayers.size(); i++){
							if(otherPlayers.get(i).getName().equals(outPlayer)){
								outPlayerIndex = i;
							}
						}
						mapP[curMap].remove(otherPlayers.get(outPlayerIndex));
						otherPlayers.remove(outPlayerIndex);
						mapP[curMap].repaint();
					}
					if(inputMsg.length()>30){ // 너무 길어서 채팅창에 글자 짤리는거 방지
						longString.delete(0, longString.length());
						longString.append(inputMsg);
						longString.insert(30, "\n");
						inputMsg = String.valueOf(longString);
					}
					chatPanel.textArea.append(inputMsg + "\n");
					chatPanel.textArea.setCaretPosition(chatPanel.textArea.getDocument().getLength());
					// textArea 범위를 넘어서는 append가 오면 자동으로 스크롤을 맨 아래로 내림
					System.out.println("From : " + inputMsg);
				}
				else{
					int moveX = Integer.parseInt(inputMsg);
					int moveY = Integer.parseInt(in.readLine());
					int indexOfMap = Integer.parseInt(in.readLine());
					String playerName = in.readLine();
					// 이 정보들로 유저들 위치 새로 그리자

					RealTimeUpdate realTimeUpdate = new RealTimeUpdate(moveX, moveY, indexOfMap, userName, playerName, curMap, this);
					realTimeUpdate.start();

					System.out.println(playerName + "에서 보낸 좌표 정보를 받았음");
				}
			}
		}catch (IOException i){
			chatPanel.textArea.append("서버와 접속이 끊어졌습니다.\n");
			System.out.println("서버와 접속이 끊어졌습니다.");
		} finally {
			try{
				socket.close();
			}catch (IOException j){
				j.printStackTrace();
			}
		}
		chatPanel.textArea.append("서버 연결종료\n");
		System.out.println("서버 연결종료");
	}
	public void setFrame() {
		// 메인 프레임 초기화
		setTitle("소프트웨어학과 IN METAVERSE(클라이언트용)");
		setSize(WIDTH, HEIGHT);
		
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void setPanel() {
		for(int i = 0; i<7; i++) {
			mapP[i] = new map("map"+i);
		}
		entryP.add(setNicknameP);
		cPane.add(entryP);
		repaint();

		//cPane.add(mapP[3]);
		mapP[3].add(player);

		//mapP[0].setLabeltext("102, 101 복도");
		//mapP[1].setLabeltext("디정 후문");
		//mapP[2].setLabeltext("연구실들");
		//mapP[3].setLabeltext("디정 중앙");
		//mapP[4].setLabeltext("디셈 복도");
		//mapP[5].setLabeltext("디정 정문");
		//mapP[6].setLabeltext("116 복도");
		
		mapP[3].setVisible(true);
	}
	public void moveLeftOrRightMap(int target){
		mapP[curMap].setVisible(false); // 이동 전 맵 안보이게
		cPane.remove(mapP[curMap]); // 이동 전 맵 삭제
		curMap = target;
		cPane.add(mapP[curMap]); // 이동하려는 맵 추가
		mapP[curMap].setVisible(true); // 이동하려는 맵 보이게
		player.setLocation(740-player.getX(), player.getY()); // 이동 시 캐릭터 위치
		mapP[curMap].add(player); // 캐릭터 맵 이동시키기
		System.out.println("current map : " + curMap);
		player.setFocusable(true); // 맵을 이동시키면 그 패널에서의 포커서블이 풀리기때문에
		player.requestFocus(); // 다시 설정해줘야함
	}
	public void moveTopOrBottomMap(int target){
		mapP[curMap].setVisible(false); // 이동 전 맵 안보이게
		cPane.remove(mapP[curMap]); // 이동 전 맵 삭제
		curMap = target;
		cPane.add(mapP[curMap]); // 이동하려는 맵 추가
		mapP[curMap].setVisible(true); // 이동하려는 맵 보이게
		player.setLocation(player.getX(), 500 - player.getY()); // 이동 시 캐릭터 위치
		mapP[curMap].add(player); // 캐릭터 맵 이동시키기
		System.out.println("current map : " + curMap);
		player.setFocusable(true);
		player.requestFocus();
	}
	public void mapEventSet(Socket socket) throws IOException {
		PrintStream out = new PrintStream(socket.getOutputStream());
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
					case KeyEvent.VK_SPACE:
						System.out.println("스페이스바 눌림");
						if (curMap == 1) {
							if(player.getX() >= 270 && player.getX() <= 320 && player.getY() >= 180 && player.getY() <= 220){
								try {
									ip = new InformationPanel("102 강의실 : 강의중 컴퓨터를 사용해 실습할수 있는 강의실이다.", "../images/102.jpg");
									mapP[curMap].add(ip);
									ip.setVisible(true);
									mapP[curMap].repaint();
								} catch (IOException ex) {
									ex.printStackTrace();
								}
							}
						} else if (curMap == 0) {
							if(player.getX() >= 390 && player.getX() <= 440 && player.getY() >= 180 && player.getY() <= 260){
								try {
									ip = new InformationPanel("101 강의실 : 강의중 컴퓨터를 사용해 실습할수 있는 강의실이다.", "../images/101.jpg");
									mapP[curMap].add(ip);
									ip.setVisible(true);
									mapP[curMap].repaint();
								} catch (IOException ex) {
									ex.printStackTrace();
								}
							}

						} else if (curMap == 2) {
							if(player.getX() >= 410 && player.getX() <= 470 && player.getY() >= 50 && player.getY() <= 140){
								try {
									ip = new InformationPanel("게시판 : 학과의 중요한 정보를 얻을 수 있다.", "../images/게시판.jpg");
									mapP[curMap].add(ip);
									ip.setVisible(true);
									mapP[curMap].repaint();
								} catch (IOException ex) {
									ex.printStackTrace();
								}
							}
						} else if (curMap == 6) {
							if(player.getX() >= 510 && player.getX() <= 560 && player.getY() >= 0 && player.getY() <= 60){
								try {
									ip = new InformationPanel("116 강의실 : 이론과목을 배울 수 있는 강의실이다.", "../images/116.jpg");
									mapP[curMap].add(ip);
									ip.setVisible(true);
									mapP[curMap].repaint();
								} catch (IOException ex) {
									ex.printStackTrace();
								}
							}
						}
						break;
					// 좌표의 기준이 패널의 좌측하단끝 인것 같음
					default:
						break;
				}
				if (upP) {
					//if(!(player.getX() < 100 && player.getY() < 110)) { } -> 특정 좌표 이동불가 테스트
					if(mControl.CanIGoTopMap(curMap, player.getY())){ // 위쪽 맵으로 이동가능한지 판단
						mControl.MoveToTopMap(); // 현재 위치를 기준으로 어느 위쪽 맵으로 갈건지 판단
						moveTopOrBottomMap(mControl.getTarget()); // 이동할 맵의 인덱스로 맵 이동
					}
					else if(player.getY() != 0){
						player.setLocation(player.getX(), player.getY()-10);
						sendLocationToServer(out, player.getX(), player.getY(),curMap, userName);
					}
				}
				if (downP) {
					if(mControl.CanIGoBottomMap(curMap, player.getY())){
						mControl.MoveToBottomMap();
						moveTopOrBottomMap(mControl.getTarget());
					}
					else if(player.getY() != 500){
						player.setLocation(player.getX(), player.getY()+10);
						sendLocationToServer(out, player.getX(), player.getY(),curMap, userName);
					}
				}
				if (leftP) {
					//if(!(player.getX() < 110 && player.getY() < 100)) { } -> 특정 좌표 이동불가 테스트
					if(mControl.CanIGoLeftMap(curMap, player.getX())) {
						mControl.MoveToLeftMap();
						moveLeftOrRightMap(mControl.getTarget());
					}
					else if(player.getX() != 0) {
						player.setLocation(player.getX()-10, player.getY());
						sendLocationToServer(out, player.getX(), player.getY(),curMap, userName);
					}
				}
				if (rightP) {
					if(mControl.CanIGoRightMap(curMap, player.getX())) {
						mControl.MoveToRightMap();
						moveLeftOrRightMap(mControl.getTarget());
					}
					else if(player.getX() != 740) {
						player.setLocation(player.getX()+10, player.getY());
						sendLocationToServer(out, player.getX(), player.getY(),curMap, userName);
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
		System.out.println(curMap);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				player.setFocusable(true);
				player.requestFocus();
			}
		});
	}
	public void sendLocationToServer(PrintStream out, int x, int y, int curMap, String playerName){
		// x, y는 이동한 위치
		// curMap은 해당 플레이어의 현재 위치
		// playerName은 플레이어 이름
		out.println(player.getX());
		out.println(player.getY());
		out.println(curMap);
		out.println(playerName);
		out.flush();
	}
	public void entryEventSet() {
		tField.addKeyListener(new KeyAdapter(){
			// 쓰레드 끝나면 포커스를 넘기는 식으로 하면 될듯하다.
			// 잘 된다 나이스
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					userName = tField.getText();
					System.out.println(userName);
					entryThread.interrupt(); // entryThread 종료
				}
			}
		});
	}
	class entryRunnable implements Runnable{
		private JLabel userLabel;
		public entryRunnable(){
			objectSettings objSet = new objectSettings();
			userLabel = objSet.getLabel();
		}
		public void run(){
			while(true){
				try{
					Thread.sleep(1000);
					System.out.println("userName = " + userName);
					userLabel.setText(userName);
				}catch(InterruptedException e){
					System.out.println("entryThread 끝");
					return;
				}
			}
		}
	}
	public static void main(String[] args) throws IOException {
		new ClientMain();
	}
}
class UserThread extends Thread{
	Socket socket = null;
	String name;
	Scanner scanner = new Scanner(System.in);
	JTextField textField = new JTextField();
	String outputMsg;
	public UserThread(Socket socket, String name, JTextField textField){
		this.socket = socket;
		this.name = name;
		this.textField = textField;
	}
	@Override
	public void run(){
		try{
			PrintStream out = new PrintStream(socket.getOutputStream());
			out.println(name);
			out.flush();
			textField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JTextField textField1 = (JTextField)e.getSource();
					outputMsg = textField1.getText();

					out.println(outputMsg);
					out.flush();
					textField1.setText("");
				}
			});
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
class RealTimeUpdate extends Thread{
	private int x, y, curMap, receivedMapIndex;
	private String playersName;
	private ClientMain main;
	private player player;
	private String curUser;
	public RealTimeUpdate(int x, int y, int receivedMapIndex,String curUser, String playersName, int curMap, ClientMain main){
		this.x = x;
		this.y = y;
		this.receivedMapIndex = receivedMapIndex; // 정보 준 놈 위치
		this.playersName = playersName; // 정보 준 놈
		this.main = main;
		this.curMap = curMap; // 내위치
		this.player = new player();
		this.curUser = curUser; // 나
	}
	@Override
	public void run(){
		if(main.targetNotAdd.size() == 0){
			player.setPlayerNickname(curUser);
			main.targetNotAdd.add(player);
		}
		for(player player : main.otherPlayers){ // 모든 플레이어의 리스트에서 하나씩 가져온다.
			if(!player.getName().equals(curUser)){ // 플레이어가 이 클라이언트의 유저가 아니라면
				//System.out.println("넘겨받은 이름 : " + playersName + ", 플레이어 리스트의 이름 : "+player.getName());
				if(playersName.equals(player.getName()) && curMap == receivedMapIndex){ // 플레이어 리스트에 있는 플레이어 중 하나가
					// 정보를 보내준 당사자이면서, 현재 클라이언트와 같은 맵에 있는 경우
					if(player.getParent() == null){ // 그 플레이어가 맵에 추가된 적이 없는 경우
						for(int i = 0; i<main.targetNotAdd.size(); i++){ // 이미 추가 된 적 있는 플레이어의 수 만큼 반복
							if(main.targetNotAdd.get(i).getName().equals(player.getName())){ // 이미 있던 플레이어가 위치 정보를 전달 했을 때
								player.setLocation(x, y);
								main.mapP[receivedMapIndex].repaint();
								break;
							}else if(i + 1 == main.targetNotAdd.size()){ // 반복문 다 돌았을 경우
								// => 정보 보내 준 플레이어가 그 맵에 이미 있던 플레이어가 아닌 경우
								main.mapP[receivedMapIndex].add(player);
								main.targetNotAdd.add(player);
								main.mapP[receivedMapIndex].repaint();
							}
						}
						//System.out.println("이 player의 부모는? " + player.getParent());
					}
					player.setLocation(x, y);
					main.mapP[receivedMapIndex].repaint();
				} else if(playersName.equals(player.getName()) && !(curMap == receivedMapIndex)){
					// => 정보를 전달해준 플레이어가 현재 클라이언트랑 다른 맵으로 이동한 경우
					main.mapP[receivedMapIndex].remove(player);
					main.mapP[curMap].remove(player);

					main.targetNotAdd.remove(player);

					main.mapP[curMap].repaint();
					main.mapP[receivedMapIndex].repaint();
					//main.otherPlayers.remove(player);
					break;
				}
			}
		}
	}
}
// 22.11.26 해결해야할 문제
// 1. 이미 있는 캐릭터 다시 그려짐
// 2. 다른 플레이어가 움직이다가 새로운 사람 들어오면 캐릭터 복사됌 뭐임?
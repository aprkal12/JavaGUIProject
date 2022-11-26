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
	public String userName;
	private Thread th1;
	private Thread th2;
	static int curMap = 3;
	static Vector<String> playersName = new Vector<String>();
	static Vector<player> players = new Vector<player>();
	connectServer connectServer = new connectServer();
	static List<player> otherPlayers = Collections.synchronizedList(new ArrayList<player>());
	static List<player> targetNotAdd = Collections.synchronizedList(new ArrayList<player>());

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

		playersName = connectServer.playersName;

		setFrame();
		setPanel();
		entryEventSet();

		// 실행 제어
		entryThread runna = new entryThread();
		th1 = new Thread(runna);
		th1.start();
		try{
			th1.join();
		}catch(InterruptedException e){
			System.out.println("조인 끝");
			// 아마 닉네임을 입력 받은 후인 여기서 서버 실행 시켜야 겠지?
		}
		// 닉네임 입력 후 화면 제거
		entryP.setVisible(false);
		cPane.remove(entryP);

		// 맵 패널로 변경, 채팅창 추가
		setSize(1100, 600);
		JPanel chat = new JPanel();
		chat.setBounds(800, 0, 300,600);
		chat.setBackground(Color.BLACK);
		setVisible(true);
		cPane.add(chat);

		cPane.add(mapP[3]);
		player.setPlayerNickname(userName);

		repaint();
		//player.add(userLabel);

		try{
			socket = new Socket("localhost", 7999);
			System.out.println("[서버와 연결되었습니다.]");
			mapEventSet(socket);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// 클라이언트가 서버에 접속했을 경우
			// 본인외의 서버 혹은 클라이언트의 위치에 맞게 캐릭터 새로 그리기
			// 현재 같은 맵에 있는 유저끼리만 그려야함

			// 클라이언트가 서버로 채팅메세지 보내는 스레드
			userThread userThread = new userThread(socket, userName);
			userThread.start();

			while(in != null){
				String inputMsg = in.readLine();
				//System.out.println("서버에서 보내준 정보 타입은? " + inputMsg.getClass().getSimpleName());
				if(inputMsg.contains("들어오셨습니다.")){
					//System.out.println("입장 체크는 어캐하노");
					String name = in.readLine();


					// 뭔가 문제가 있어
					// 해당 클라이언트 생성 전에 이미 있던 닉네임을 가져오는 법 찾아야함
					// 만들어야 하는 태스크
					// 1. 처음 접속 시, 본인 제외 플레이어 리스트에 있는 캐릭터 그리기
					// 2. 움직이면 서버로 정보를 보내고, 서버는 모든 클라이언트에게 뿌려줌
					// 3. 뿌려준 정보를 받아서 자기 자신의 정보면 무시, 다른 클라이언트의 정보면 반영


					System.out.println("새로 받아온 이름 " +name);
					int countPlayers = Integer.parseInt(in.readLine());
					System.out.println("prenames " +countPlayers);
					System.out.println("닉네임들");
					System.out.print("player 네임은 : " + player.getName() + " 다른 사람들 꺼는 \n");

					otherPlayers.clear();
					players.clear();
					for(int i = 0; i<countPlayers; i++){

						player otherPlayer = new player();
						otherPlayer.setPlayerNickname(in.readLine());
						otherPlayers.add(otherPlayer);
						players.add(otherPlayer);
						System.out.println("countPlayer 실행 중 ");
						//System.out.print("otherPlayers에 저장된 값 : " + otherPlayers.get(i).getName()+" ");
						//System.out.print("players에 저장된 값 : " + players.get(i).getName()+" \n");
					}

					System.out.println();

					AddUserThread addThread = new AddUserThread(name, this);
					addThread.start();
				}
				else if(!inputMsg.matches("^[0-9]+$")){
					if(("["+userName+"]님이 나가셨습니다.").equals(inputMsg)){
						break;
					}
					else if(inputMsg.contains("나가셨습니다.")){
						System.out.println("웨 안됌?");
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
					//Thread.sleep(1000);
					System.out.println("From : " + inputMsg);
				}
				else{
					int moveX = Integer.parseInt(inputMsg);
					int moveY = Integer.parseInt(in.readLine());
					int indexOfMap = Integer.parseInt(in.readLine());
					String playerName = in.readLine();
					// 이 정보들로 유저들 위치 새로 그리자
					// 그 전에 플에이어들 부터 각 클라이언트에도 추가해야함
					// 이제 여기 만들자
					System.out.println(playerName + "에서 정보가 왔는데 지금 나는? " + userName);
					//if(!playerName.equals(userName)){
					RealTimeUpdate realTimeUpdate = new RealTimeUpdate(moveX, moveY, indexOfMap, userName, playerName, curMap, this);
					realTimeUpdate.start();
					//}
					System.out.println(playerName + "에서 보낸 좌표 정보를 받았음");
				}
			}
		}catch (IOException i){
			System.out.println("서버와 접속이 끊어졌습니다.");
		} finally {
			try{
				socket.close();
			}catch (IOException j){
				j.printStackTrace();
			}
		}
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
		System.out.println("초기 유저네임 : " + userName);
		//cPane.add(mapP[3]);

		mapP[3].add(player);
		// 오브젝트 추가 테스트
		//mapP[3].add(t3);
		//tField);
		//mapP[3].add(t2);

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
			// 22.11.18 여기서 만들어서 쓰레드 돌려보자 여기서 리스너 둘 다 만들고
			// 쓰레드 끝나면 포커스를 넘기는 식으로 하면 될듯요~
			// 잘 된다 나이스
			// 클래스 정리좀 하자 ㅋㅋㅋㅋㅋ
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					//th1.interrupt();
					userName = tField.getText();
					System.out.println(userName);
					th1.interrupt();
					//JOptionPane.showMessageDialog(null, userName);
				}
			}
		});

	}
	class entryThread implements Runnable{
		private JLabel userLabel;
		public entryThread(){
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
					System.out.println("thread1 끝");
					return;
				}
			}
		}
	}
	public static void main(String[] args) throws IOException {
		new ClientMain();
	}
}
class userThread extends Thread{
	Socket socket = null;
	String name;
	Scanner scanner = new Scanner(System.in);

	public userThread(Socket socket, String name){
		this.socket = socket;
		this.name = name;
	}
	@Override
	public void run(){
		try{
			PrintStream out = new PrintStream(socket.getOutputStream());
			out.println(name);
			out.flush();
			while(true){
				String outputMsg = scanner.nextLine();
				out.println(outputMsg);
				out.flush();
				if("종료".equals(outputMsg)){
					break;
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
class AddUserThread extends Thread{
	ClientMain main;
	String name;
	player player;
	List<player> targetAdd = Collections.synchronizedList(new ArrayList<player>());
	public AddUserThread(String name, ClientMain main) throws IOException {
		this.name = name;
		this.main = main;
		player = new player();
	}
	@Override
	public void run(){
		System.out.println("Adduser에서의 otherPlayers 길이 : " + main.otherPlayers.size());

		targetAdd.addAll(main.otherPlayers);
		System.out.println("addall 결과 : " + targetAdd);
		System.out.println("targetNotAdd 값 : " + main.targetNotAdd);
		targetAdd.remove(0);
		System.out.println("removeall 결과 : " + targetAdd);

		for(player player : targetAdd){
			if(!player.getName().equals(main.userName)){
				System.out.print("addplayer돌아가는 중 : " + player.getName() + " ");
				if(player.getName().equals(name)){
					System.out.println("새로 생성된 애 추가");
					main.mapP[3].add(player);
					break;
				}
				//player.setPlayerNickname(name);
				//System.out.println("아놔 저장 되긴 허는겨? " + main.targetNotAdd.size());
				// 플레이어 추가를 매번하는게 아니라 없을 때만 추가
				// 집 가서 마저 해보자
			}
			main.repaint();
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
		this.receivedMapIndex = receivedMapIndex;
		this.playersName = playersName;
		this.main = main;
		this.curMap = curMap;
		this.player = new player();
		this.curUser = curUser;
	}
	@Override
	public void run(){
		System.out.println("다른맵에선 왜 안돼 curmap : " + curMap + "receivedindex = " + receivedMapIndex);
		for(player player : main.otherPlayers){
			if(!player.getName().equals(curUser)){
				System.out.println("넘겨받은 이름 : " + playersName + ", 플레이어 리스트의 이름 : "+player.getName());
				if(playersName.equals(player.getName()) && curMap == receivedMapIndex){
					main.mapP[receivedMapIndex].add(player);
					player.setLocation(x, y);
					break;
				}
				else if(playersName.equals(player.getName()) && !(curMap == receivedMapIndex)){
					main.mapP[curMap].remove(player);
					//main.otherPlayers.remove(player);
					break;
				}
			}
			main.mapP[receivedMapIndex].repaint();
			main.mapP[curMap].repaint();
			// 여러 명일 때 왜 자꾸 그려지지? 생각해보자

			// 뭔가 이상함 고쳐야 함
		}
	}
}
// 22.11.26 해결해야할 문제
// 1. 이미 있는 캐릭터 다시 그려짐
// 2. 다른 플레이어가 움직이다가 새로운 사람 들어오면 캐릭터 복사됌 뭐임?
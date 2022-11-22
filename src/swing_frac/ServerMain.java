package swing_frac;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
// 22.11.21
// 서버구현, 멀티실행 intellij 설정(구글링 참고)

// 22.11.18
// 쓰레드를 여기서 만들고 러너블도 여기서 만들어야할것 같다.
// 이벤트 리스너는 포커스 옮겨서 가능하니 그걸 활용하자
// setfocusable과 request포커스로 여러개의 이벤트리스너 구현가능한거 확인
// 구현 목표 :
// entryframe + entryScreen을 Thread1으로,
// ServerAdmin을 Thread2 로 해서
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

public class ServerMain extends JFrame{
    static final int WIDTH = 800, HEIGHT = 600;
    static boolean upP = false;
    static boolean downP = false;
    static boolean leftP = false;
    static boolean rightP = false;
    private Container cPane;
    private moveToMap mControl;

    private objectSettings objTest;
    private map mapP[];
    private player player;
    //static map1Object3 t3 = new map1Object3();
    //statictField = new map1Object1();
    //static map1Object2 t2 = new map1Object2();
    private entryFrame entryP;
    private entryScreen setNicknameP;
    private JTextField tField;
    private String userName;
    private Thread th1;
    private Thread th2;
    static int curMap = 3;

    // 부드러운 캐릭터 이동을 위한 변수선언
    public ServerMain() {
        ServerSocket serverSocket = null;
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
        mapEventSet();
        try{
            serverSocket = new ServerSocket(7999);
            while(true){
                System.out.println("[클라이언트 연결 대기중]");
                socket = serverSocket.accept();

                connectServer connectServer = new connectServer(socket);
                connectServer.start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(serverSocket != null){
                try{
                    serverSocket.close();
                    System.out.println("[서버 종료]");
                }catch (IOException e){
                    e.printStackTrace();
                    System.out.println("[소켓 서버통신 에러]");
                }
            }
        }
    }
    public void setFrame() {
        // 메인 프레임 초기화
        setTitle("소프트웨어학과 IN METAVERSE(서버용)");
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
        //mapP[1].setBackground(Color.WHITE);
        //mapP[2].setBackground(Color.GREEN);
        //mapP[0].setBackground(Color.YELLOW);
        //mapP[4].setBackground(Color.ORANGE);

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
    public void mapEventSet(){
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
                    }
                }
                if (downP) {
                    if(mControl.CanIGoBottomMap(curMap, player.getY())){
                        mControl.MoveToBottomMap();
                        moveTopOrBottomMap(mControl.getTarget());
                    }
                    else if(player.getY() != 500){
                        player.setLocation(player.getX(), player.getY()+10);
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
                    }
                }
                if (rightP) {
                    if(mControl.CanIGoRightMap(curMap, player.getX())) {
                        mControl.MoveToRightMap();
                        moveLeftOrRightMap(mControl.getTarget());
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
        System.out.println(curMap);
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
    class connectServer extends Thread{
        List<PrintWriter> list = Collections.synchronizedList(new ArrayList<PrintWriter>());
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        player player;
        Vector<player> players = new Vector<player>();

        public connectServer(Socket socket){
            this.socket = socket;
            player = new player();
            try{
                out = new PrintWriter(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                list.add(out);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        @Override
        public void run(){
            String name = "";
            //player player2 = new player();
            try{
                name = in.readLine(); // 나중에 캐릭터 닉네임으로 변경해야함
                System.out.println("["+name+" 새연결 생성]");
                sendAll("["+name+"]님이 들어오셨습니다.");

                // 플레이어 접속할 때마다 맵에 캐릭터 추가
                player.setPlayerNickname(name);
                players.add(player);
                for(int i=0; i<players.size(); i++){
                    if(name.equals(players.get(i).getName())){
                        mapP[3].add(players.get(i));
                        players.remove(i);
                    }
                }
                repaint();
                while(in != null){
                    String inputMsg = in.readLine();
                    if("종료".equals(inputMsg)){
                        break;
                    }
                    sendAll(name + ">> " + inputMsg);
                }
            } catch (IOException e) {
                System.out.println("["+name + " 접속 끊김]");
            } finally {
                sendAll("["+name+"]님이 나가셨습니다.");
                list.remove(out);
                try{
                    socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            System.out.println("["+name + " 연결종료]");
        }
        public void sendAll(String s){
            for(PrintWriter out : list){
                out.println(s);
                out.flush();
            }
        }
    }

    public static void main(String[] args) {
        new ServerMain();
    }
}

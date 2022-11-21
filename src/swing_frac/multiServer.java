package swing_frac;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class multiServer {
    public multiServer(){
        ServerSocket serverSocket = null;
        Socket socket = null;
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
    public static void main(String[] args){
        new multiServer();
    }
}
class connectServer extends Thread{
    static List<PrintWriter> list = Collections.synchronizedList(new ArrayList<PrintWriter>());
    Socket socket = null;
    BufferedReader in = null;
    PrintWriter out = null;

    public connectServer(Socket socket){
        this.socket = socket;
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
        try{
            name = in.readLine(); // 나중에 캐릭터 닉네임으로 변경해야함
            System.out.println("["+name+" 새연결 생성]");
            sendAll("["+name+"]님이 들어오셨습니다.");

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

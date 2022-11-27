package swing_frac;

import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatPanel extends JPanel implements Runnable {
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String name;
    private String msg;
    JTextArea textArea = new JTextArea(30, 30);
    JTextField textField = new JTextField(30);

    public ChatPanel(Socket socket, BufferedReader in, PrintWriter out, String name) throws IOException {
        //socket and inputstream and out stream get
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.name = name;

        setBounds(800, 0, 280, 600);
        setLayout(null);
        textField.setBounds(10, 510, 260, 40);
        textArea.setBounds(10, 10, 260, 480);
        textArea.append("chatting string enter");
        textArea.setEditable(false);
        textArea.setVisible(true);
        textField.setVisible(true);
        add(textArea);
        add(textField);

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField tf = (JTextField)e.getSource();
                String text = tf.getText() + "\n";
                System.out.println(text);
                out.println(text);
                tf.setText("");
            }
        });

    }

    @Override
    public void run() {
        while (true) {
            try {
                String serverText = in.readLine();
                if (serverText == null) continue;
                textArea.append(serverText);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

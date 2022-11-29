package swing_frac;

import javax.swing.*;
import java.awt.*;
public class ChatPanel extends JPanel{
    JTextArea textArea = new JTextArea(30, 30);
    JTextField textField = new JTextField(30);
    JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    // 수직 스크롤 항상, 수평 스크롤 X
    public ChatPanel(){
        setBounds(800, 0, 300, 600);
        setLayout(null);
        setVisible(true);

        textField.setBounds(10, 510, 260, 40);
        textArea.setBounds(10, 10, 260, 480);
        scrollPane.setBounds(10, 10, 260, 480);

        textArea.append("@@ SOFTWARE IN METAVERSE @@\n");
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas 굵게", Font.BOLD, 13));
        textField.setFont(new Font("Consolas 굵게", Font.BOLD, 13));

        textArea.setVisible(true);
        textField.setVisible(true);
        add(scrollPane);
        add(textField);
    }
}

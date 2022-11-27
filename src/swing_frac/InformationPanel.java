package swing_frac;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class InformationPanel extends JPanel {

    private int PANEL_WIDTH = 600;
    private int PANEL_HEIGHT = 400;
    private int IMAGE_WIDTH = 600;
    private int IMAGE_HEIGHT = 300;
    private int DETAIL_WIDTH = 500;
    private int DETAIL_HEIGHT = 50;
    private int BUTTON_WIDTH = 120;
    private int BUTTON_HEIGHT = 40;
//    private String image_src = null;

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(15, 15);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        //Draws the rounded opaque panel with borders.
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);//paint background
        graphics.setColor(getForeground());
        graphics.drawRoundRect(0, 0, width - 2, height - 3, arcs.width, arcs.height);//paint border
    }
    public InformationPanel(String detail_text, String src) throws IOException {
        setLayout(null);

        Image img = new ImageIcon(Objects.requireNonNull(player.class.getResource(src))).getImage();

        JLabel imageLabel = new JLabel(new ImageIcon(img));



        setBounds(100,100,PANEL_WIDTH, PANEL_HEIGHT);
        JLabel detail = new JLabel(detail_text);
        JButton exitButton = new JButton("나가기");

        detail.setBounds(20, 5, DETAIL_WIDTH, DETAIL_HEIGHT);
        imageLabel.setBounds(0, 45, IMAGE_WIDTH, IMAGE_HEIGHT);
        exitButton.setBounds(240, 348, BUTTON_WIDTH, BUTTON_HEIGHT);
        add(detail);
        add(imageLabel);
        add(exitButton);
        exitButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                    }
                }
        );

    }

    public static void main(String[] args) throws IOException {
        InformationPanel ip = new InformationPanel("116강의실 : 소프트웨어학과 학생들이 강의듣는 곳", "src/images/ㅁㅁㅁ.jpeg");
        JFrame a = new JFrame();
        Container c = a.getContentPane();
        a.setSize(800, 600);
        a.setVisible(true);
        c.setLayout(null);
        c.add(ip);
    }
}

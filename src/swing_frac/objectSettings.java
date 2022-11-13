package swing_frac;

import javax.swing.*;
import java.awt.*;

public class objectSettings {
    private Font f1;
    private Font f2;
    public objectSettings(){
        this.f1 = new Font("Consolas 굵게", Font.BOLD, 20);
        this.f2 = new Font("Consolas 굵게", Font.BOLD, 10);
    }
    public Font getFont1(){
        return this.f1;
    }
    public Font getFont2(){
        return this.f2;
    }
    public static void main(String [] args){
        new objectSettings();
    }
}

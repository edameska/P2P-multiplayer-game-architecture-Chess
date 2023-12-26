package main;
import javax.swing.JFrame;
public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Chess");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        GamePanel gp=new GamePanel();
        window.add(gp);
        window.pack();//sets the size of the window to the preferred size of the panel


        window.setLocationRelativeTo(null);//shows up at center of monitor
        window.setVisible(true);
    }
}
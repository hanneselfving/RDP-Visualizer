package rdpmain;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Window extends JFrame {
	
	public static OptionsPanel optionsPanel;
	public static Board board;
	private static Window window;
	
	final int initW = 1400, initH = 900;
	
	private Window(int x, int y) {
		board = new Board();
		optionsPanel = new OptionsPanel(board);
		this.setVisible(true);
		this.setPreferredSize(new Dimension(x,y));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(board, BorderLayout.CENTER);
		this.add(optionsPanel, BorderLayout.NORTH);
		this.pack();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ResetWindow();
		
	}
	
	public static void ResetWindow() {
		window = new Window(1400,900);
	}
	
	public static Window GetWindow() {
		return window;
	}
	
	

}

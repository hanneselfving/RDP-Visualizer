package rdpmain;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.BorderFactory;

public class OptionsPanel extends JPanel {
	
	Board board;
	
	public static JLabel scaleText = new JLabel("Scale: " + Board.scale);
	public static JSlider scaleControl = new JSlider(JSlider.HORIZONTAL, 1, 398, 20);
	
	public static JLabel wText = new JLabel("Window Size: " + Board.wSize);
	public static JSlider wControl = new JSlider(JSlider.HORIZONTAL, 1, 50, 5);
	
	public static JLabel errText = new JLabel("Loss Risk: " + Board.lRisk);
	public static JSlider lossControl = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
	
	public static JLabel speedText = new JLabel("Speed: " + Board.speed);
	public static JSlider speedControl = new JSlider(JSlider.HORIZONTAL, 1, 50, 10);
	
	public static JCheckBox toggleSelRep = new JCheckBox("Selective Repeat");
	
	public static JButton startBtn = new JButton("Start");
	
	OptionsPanel(Board board) {
		
		this.board = board;
		
		this.add(scaleText);
		this.add(scaleControl);
		
		this.add(wText);
		this.add(wControl);
		
		this.add(errText);
		this.add(lossControl);
		
		this.add(speedText);
		this.add(speedControl);
		
		//this.add(toggleSelRep);
		this.add(startBtn);
		
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		scaleControl.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
			    if (!source.getValueIsAdjusting()) {
			        Board.scale = (int)source.getValue();
			        Window.board.redraw();
			    }
				
			}
		});
		
		wControl.addChangeListener(new ChangeListener() {
					
					@Override
					public void stateChanged(ChangeEvent e) {
						JSlider source = (JSlider)e.getSource();
					    if (!source.getValueIsAdjusting()) {
					        Board.wSize = (int)source.getValue();
					        Window.board.redraw();
					    }
						
					}
				});
		
		lossControl.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
			    if (!source.getValueIsAdjusting()) {
			        Board.lRisk = (int)source.getValue();
			        Window.board.redraw();
			    }
				
			}
		});
		
		speedControl.addChangeListener(new ChangeListener() {
					
					@Override
					public void stateChanged(ChangeEvent e) {

						JSlider source = (JSlider)e.getSource();
						 if (!source.getValueIsAdjusting()) {
						        Board.speed = (int)source.getValue();
						        Window.board.redraw();
						    }
						
					}
				});
		
		startBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Window.board.redraw();
				board.timer.restart();
				startBtn.setEnabled(false);
				lossControl.setEnabled(false);
				wControl.setEnabled(false);
				scaleControl.setEnabled(false);
				speedControl.setEnabled(false);
				
				
				
			}
		});
		
		
		
	}

}

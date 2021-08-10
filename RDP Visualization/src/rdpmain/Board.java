package rdpmain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
	
	public static Signal sendArr[] = new Signal[400];
	public static Signal rcvArr[] = new Signal[400];
	
	public LinkedList<Packet> packets = new LinkedList<Packet>();
	public static LinkedList<Packet> acks = new LinkedList<Packet>();
	
	public static int frameLineA;
	public static int frameLineB;
	public static int frameDrawSize;
	
	public static int scale = OptionsPanel.scaleControl.getValue();
	public static int wSize = OptionsPanel.wControl.getValue();
	public static int lRisk = OptionsPanel.lossControl.getValue();
	protected static int speed = OptionsPanel.speedControl.getValue();
	public static int base = 0;
	
	//send side
	public static int nextSeqNum = 0;
	//rcv side
	public static int expectedSeqNum = 0;
	
	public static boolean resending = false;
	static int c = 0;
	
	//paint/tick timer
	public Timer timer = new Timer(6, this);
	//
	public static Timer timeoutTm = new Timer(14*1800/speed, new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Resending");
			resend();
			
		}
	});
	
	
	public int spawnTimer = 0;
	public int spawnTimerLimit = 5;
	
	Board() {

		this.setBackground(Color.BLACK);
		redraw();
	}
	
	
	
	protected static void resend() {
		resending = true;
		c = 0;
		
	}



	private void initSignals() {
		
		for(int i = 0; i < scale; i++) {
			sendArr[i] = new Signal(i*(getWidth()/(scale)), getHeight()-400/scale-20, 600/scale);
			rcvArr[i] = new Signal(i*(getWidth()/(scale)), 10,600/scale);
		}
		
	}

	public void redraw() {
		
		packets.clear();
		acks.clear();
		
		base = 0;
		nextSeqNum = 0;
		expectedSeqNum = 0;
		timer = new Timer(6, this);
		timeoutTm = new Timer((15*1800/speed), new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Resending");
				resend();
				
			}
		});
		
		
		OptionsPanel.scaleText.setText("Packets: " + Board.scale);

		
		OptionsPanel.wText.setText("Window Size: " + Board.wSize);

		
		OptionsPanel.errText.setText("Loss Risk: " + Board.lRisk);

		
		OptionsPanel.speedText.setText("Speed: " + Board.speed);

		
		initSignals();
		initFramePos();
		repaint();
		
		
	}

	public static void initFramePos() {
		
		if(base < scale) {
		frameDrawSize = sendArr[base].s;
		frameLineA = sendArr[base].x - sendArr[0].x/2 - frameDrawSize/2 - 2;
		}
	
		if(base + wSize < scale) {
		frameLineB = sendArr[base + wSize].x + sendArr[0].x/2 - frameDrawSize/2 - 2;
		}

	}



	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		
		for(int i = 0; i < scale; i++) {
			sendArr[i].render(g);
			rcvArr[i].render(g);
		}
		
		g.setColor(Color.CYAN);
		if(base < scale) {
		g.fillRect(frameLineA, sendArr[0].y - sendArr[0].s/2, 2, frameDrawSize * 2);
		}
		
		if(base + wSize < scale) {
		g.fillRect(frameLineB, sendArr[0].y - sendArr[0].s/2, 2, frameDrawSize * 2);
		}
		
		for(Packet p : packets) {
			p.render(g);
		}
		for(Packet a : acks) {
			a.render(g);
		}
		
		//g.setColor(Color.LIGHT_GRAY);
		//g.drawString("base: " + base + "\n" + "base + wsize: " + (base+wSize)+ "nextSeqNum: " + nextSeqNum + "\n timeout: " + timeoutTm.getDelay(), 10, 10);
		
	
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		
		//Spawn mechanic
		if(spawnTimer < spawnTimerLimit) {
			spawnTimer++;
		}
		else {
			
			if(resending == false) {
			if(nextSeqNum < base + wSize && nextSeqNum < scale) {
				
				send(nextSeqNum);
				if(base == nextSeqNum) {
					System.out.printf("Timer started");
					timeoutTm.restart();
				
				}
				nextSeqNum++;
			}
			else if(base >= scale) {
				OptionsPanel.startBtn.setEnabled(true);
				OptionsPanel.lossControl.setEnabled(true);
				OptionsPanel.wControl.setEnabled(true);
				OptionsPanel.scaleControl.setEnabled(true);
				OptionsPanel.speedControl.setEnabled(true);
				timer.stop();
				
			}
			}
			else {
				
				if(c == 0) {
				System.out.printf("Timer restarted (c=0)");
				timeoutTm.restart();
				}
				if((base + c) < scale) {
				send(base + c);
				c++;
				}
				if(c >= nextSeqNum-base) {
					System.out.printf("Timer restarted and resending done");
					c = 0;
					resending = false;
					timeoutTm.restart();
				}
				
				
			}
			spawnTimer = 0;
		}
		//
		
		
		//Move packets
		for(Packet p : packets) {
			p.tick();
			if(p.y <= rcvArr[p.seqNum].y) {
				rcvArr[p.seqNum].rcv(p.seqNum);
			}
		}
		
		for(Packet a : acks) {
			a.tick();
			if(a.y >= sendArr[a.seqNum].y && (a.seqNum == 0 || sendArr[a.seqNum-1].color == Color.GREEN)) {
				sendArr[a.seqNum].rcvAck(a.seqNum);
			}
		}
		
		packets.removeIf(p -> p.y <= rcvArr[0].y || (p.color==Color.RED && p.y <= 700));
		acks.removeIf(a -> a.y >= sendArr[0].y);
		
		repaint();
		
	}



	private void send(int seqNum) {
		
		sendArr[seqNum].color = Color.YELLOW;
		Packet p = new Packet(sendArr[seqNum].x, sendArr[seqNum].y - 4, sendArr[seqNum].s, Color.YELLOW, seqNum);
		genError(p);
		packets.add(p);
		
	}
	
	public void genError(Packet p) {
		
		int r = ThreadLocalRandom.current().nextInt(0, 101);
		
		if(r < lRisk) {
			
			p.color = Color.RED;
			
		}
		
		
	}
	

}

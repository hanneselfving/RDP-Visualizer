package rdpmain;

import java.awt.Color;
import java.awt.Graphics;


public class Signal {
	
	//yellow = packet sent, green = ack/packet received, white = waiting to send
	public Color color = Color.WHITE;
	int x, y, s;
	
	Signal(int x, int y, int s) {
		
		this.x = x;
		this.y = y;
		this.s = s;
		
	}
	
	
	public void render(Graphics g) {
		
		g.setColor(color);
		g.fillRect(x, y, s, s);
		g.setColor(Color.BLACK);
		g.drawRect(x - 1, y - 1, s + 2, s+2);
		
	}

	public void rcv(int seqNum) {
		
		//check if expected seq
		if(Board.expectedSeqNum == seqNum) {
		Board.expectedSeqNum++;
		//send ack
		this.color = Color.GREEN;
		Board.acks.add(new Packet(this.x,this.y-4,this.s,Color.GREEN,seqNum));	
		
		}
		
	}


	public void rcvAck(int seqNum) {
		
		this.color = Color.GREEN;
		Board.base = seqNum + 1;
		if(Board.base == Board.nextSeqNum) {
			System.out.println("Stop Timer");
			Board.timeoutTm.stop();
		}
		else {
			System.out.println("Restart Timer");
			Board.timeoutTm.restart();
		}
		Board.initFramePos();
		
		
		
	}
	
	
	

}

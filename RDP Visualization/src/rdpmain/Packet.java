package rdpmain;

import java.awt.Color;
import java.awt.Graphics;

public class Packet {

	//yellow = packet sent, green = ack/packet received, white = waiting to send //red = destruct (packet loss)
	public Color color = Color.WHITE;
	int x, y, s, seqNum;
	
	Packet(int x, int y, int s, Color color, int seqNum) {
		
		this.x = x;
		this.y = y;
		this.s = s;
		this.color = color;
		this.seqNum = seqNum;
	}
	
	
	public void render(Graphics g) {
		
		g.setColor(color);
		g.fillRect(x, y, s, s);
		g.setColor(Color.BLACK);
		g.drawRect(x - 1, y - 1, s + 2, s+2);
		
	}
	
	public void tick() {
		
		if(this.color == Color.YELLOW || this.color == Color.RED) {
			this.y-=Board.speed;
		}
		if(this.color == Color.GREEN) {
			this.y+=Board.speed;
		}
		
	}
	
	
	
}

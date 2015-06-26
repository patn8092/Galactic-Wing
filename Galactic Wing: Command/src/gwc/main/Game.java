package gwc.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{
		
	private static final long serialVersionUID = 1L;

	public static int WIDTH = 800,
			HEIGHT = (WIDTH * 9) / 16,
			SCALE = 1;
	
	private static JFrame frame;
	private BufferStrategy buffer;
	private Graphics g;
	
	private Thread thread;
	
	public Game() {
		requestFocus();
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double ns = 1000000000 / 60;
		double delta = 0;
		long now;
		long timer = System.currentTimeMillis();
		int updates = 0, frames = 0;
		
		while(true) {
			now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1) {
				update();
				delta--;
				updates++;
			}
			
			display();
			frames++;
			
			if(System.currentTimeMillis() - timer >= 1000) {
				timer = System.currentTimeMillis();
				System.out.println("Updates: "+updates+" Frames: "+frames);
				updates = 0;
				frames = 0;
			}
		}
	}
	
	public void update() {
		
	}
	
	public void display() {
		buffer = this.getBufferStrategy();
		
		if(buffer == null) {
			createBufferStrategy(3);
			return;
		}
		
		g = buffer.getDrawGraphics();
		
		//screen.clear(-16777216); 
		//g.drawImage(screen.image, 0, 0, screen.width * SCALE, screen.HEIGHT * SCALE, null);
		
		g.dispose();
		buffer.show();
	}
	
	public static void main(String[] args) {
		frame = new JFrame("Galactic Wing: Command");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Game gwc = new Game();
		Dimension dim = new Dimension(WIDTH, HEIGHT);
		gwc.setMinimumSize(dim);
		gwc.setMaximumSize(dim);
		gwc.setPreferredSize(dim);
		
		frame.add(gwc);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		gwc.start();		
	}
}

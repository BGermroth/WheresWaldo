import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

public class WheresWaldoMan implements MouseInputListener, ActionListener{

	int x,y;//mouse place x,y
	int pic = 0;//number to indicate picture
	boolean clicked = true;//boolean to determine if the button forward or backward was clicked
	boolean trash = false;//boolean to determine whether the graphics should be disposed
	long timer = System.currentTimeMillis()/1000;//time passed since start of the game in seconds
	private static final int FRAME_WIDTH = 1000;//regular frame width and height
	private static final int FRAME_HEIGHT = 800;
	private static final int BFRAME_WIDTH = 300;//remote control frame width and height
	private static final int BFRAME_HEIGHT = 500;
	Point point = MouseInfo.getPointerInfo().getLocation();
	JPanel pane,remPane;//pane, remoteControlPane
	JFrame frame,remFrame;//frame, remoteControlFrame
	//BufferedImage caillou;
	Draw draw = new Draw();
	Rectangle rec1,rec2,rec3;//rectangles for the place of waldo on the screen
	public WheresWaldoMan(){
		frame = new JFrame("Cmon Everybody");
		remFrame = new JFrame("Remote Control");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		remFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pane = draw;
		remPane = new JPanel();
		remPane.setLayout(new GridLayout(2,0));
		pane.addMouseMotionListener(this);
		pane.addMouseListener(this);
		frame.setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));
		pane.setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));
		remFrame.setPreferredSize(new Dimension(BFRAME_WIDTH,BFRAME_HEIGHT));
		remPane.setPreferredSize(new Dimension(BFRAME_WIDTH,BFRAME_HEIGHT));
		remPane.add(buttonMaker("Back"));
		remPane.add(buttonMaker("Forward"));
		BufferedImage cImg = new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB);//makes the cursor disappear
		Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cImg,new Point(0,0),"blank");
		pane.setCursor(blank);
		frame.setContentPane(pane);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		remFrame.setContentPane(remPane);
		remFrame.pack();
		remFrame.setVisible(true);
		remFrame.setLocation(0,30);
		//try {caillou = ImageIO.read(new File("src/lib/Caillou.jpg"));}//caillou knows all
		//catch (IOException e) {e.printStackTrace();}
	}
	public JButton buttonMaker(String name) {//declares each button and sets their preferences
		Font font = new Font("Comic Sans MS",Font.BOLD,25);
		JButton button = new JButton(name);
		button.setFont(font);
		button.setBackground(Color.GREEN);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setForeground(Color.BLUE);
		button.setActionCommand(name.toLowerCase());
		button.addActionListener(this);
		return button;
	}
	public void mouseMoved(MouseEvent e) {//set the mouses position to x and y then repaints the graphics
		x = e.getX();
		y = e.getY();
		//System.out.println(x+" "+y);
		draw.repaint();
	}
	public void mousePressed(MouseEvent e) {//check if clicked mouse is in bounds of a rectangle
		if((x >= rec1.getMinX() && x <= rec1.getMaxX()) && (y >= rec1.getMinY() && y <= rec1.getMaxY()))
			JOptionPane.showMessageDialog(frame, "You found him! Congrats!");
		if((x >= rec2.getMinX() && x <= rec2.getMaxX()) && (y >= rec2.getMinY() && y <= rec2.getMaxY()))
			JOptionPane.showMessageDialog(frame, "You found him! Congrats!");
		if((x >= rec3.getMinX() && x <= rec3.getMaxX()) && (y >= rec3.getMinY() && y <= rec3.getMaxY()))
			JOptionPane.showMessageDialog(frame, "You found him! Congrats!");
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void actionPerformed(ActionEvent e) {
		String event = e.getActionCommand();
		if(event.equals("forward")) {//changes the pic forward by 1, if it was the last pic changes it to 1st one
			if(pic<4)pic++;
			else pic = 0;
			trash = true;
		}
		else if(event.equals("back")) {//changes the pic back by 1, if it was the 1st pic changes it to the last one
			if(pic>0)pic--;
			else pic = 4;
			trash = true;
		}
		clicked = true;
	}
	private static void runGUI() {//starts the gui with decorated look
		JFrame.setDefaultLookAndFeelDecorated(true);
		WheresWaldoMan waldo = new WheresWaldoMan();
	}
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				runGUI();
			}
		});
	}

	class Draw extends JPanel {
		BufferedImage zoomedImg;
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			if(clicked)//when it is started or button is pressed
				setup();
			if(trash) {//disposes of graphics
				g.dispose();
				trash = false;
			}
			long time = (System.nanoTime()/100000000)%100;//timer for repainting
			long clock = (System.currentTimeMillis()/1000) - timer;
			g2.drawImage(zoomedImg,0,0,getWidth(),getHeight(),this);//draws the image onto the screen
			g2.setColor(Color.RED);
			g2.setFont(new Font("Times New Roman", Font.CENTER_BASELINE, 25));
			g2.setColor(Color.WHITE);
			g2.drawString(""+clock,x-(((""+clock).length()-1)*10),y-90);//draws the timer onto the screen
			g2.setColor(Color.RED);
			g2.setStroke(new BasicStroke(4));
			for(int i = 0; i < 4; i++) {//draw line with respect to circles edge and time passed
				g2.drawLine(x,y,(int)(x+(50*Math.cos((time+5.5*i)*6))),(int)(y+(50*Math.sin((time+5.5*i)*6))));
			}
			g2.setStroke(new BasicStroke(4));
			g2.drawOval(x-15,y-15,30,30);//draw smaller circle inside main oval
			g2.setStroke(new BasicStroke(6));
			g2.setColor(Color.RED);
			for(int i = 0; i < 4; i++) {//draw set of red circles on outer edge
				g2.drawOval((int)(x+(72*Math.cos((time+(5.5*i))*6)))-15,(int)(y+(72*Math.sin((time+(5.5*i))*6)))-15,30,30);
			}
			g2.setColor(Color.BLUE);
			for(int i = 0; i < 4; i++) {//draw set of blue circles on outer edge
				g2.drawOval((int)(x+(72*Math.cos((time+(5.5*i+2.75))*6)))-15,(int)(y+(72*Math.sin((time+(5.5*i+2.75))*6)))-15,30,30);
			}
			g2.setStroke(new BasicStroke(12));
			g2.setColor(new Color(0,200,0));
			g2.drawOval(x-50,y-50,100,100);//draws main circle to follow the cursor
			//g2.drawImage(caillou,(int)(x-(((System.currentTimeMillis()/100))%10*100)/2),(int)(y-(((System.currentTimeMillis()/100))%10*50)/2),(int)(300+(System.currentTimeMillis()/100))%10*100,(int)(150+(System.currentTimeMillis()/100))%10*50,this);//caillou is master of all
			if(point.getX()!=x && point.getY()!=y)//repaint if mouse has not moved
				draw.repaint();
		}
		public void setup() {//occurs once button is pressed sets image and rectangles
			switch(pic) {
			case 0:try {zoomedImg = ImageIO.read(new File("src/lib/Waldo.jpg"));}
			catch (Exception e) {e.printStackTrace();};break;
			case 1:try {zoomedImg = ImageIO.read(new File("src/lib/Waldo2.jpg"));}
			catch (Exception e) {e.printStackTrace();};break;
			case 2:try {zoomedImg = ImageIO.read(new File("src/lib/Waldo3.jpg"));}
			catch (Exception e) {e.printStackTrace();};break;
			case 3:try {zoomedImg = ImageIO.read(new File("src/lib/Waldo4.jpg"));}
			catch (Exception e) {e.printStackTrace();};break;
			case 4:try {zoomedImg = ImageIO.read(new File("src/lib/Waldo5.jpg"));}
			catch (Exception e) {e.printStackTrace();};break;
			}
			switch(pic) {
			case 0: rec1 = new Rectangle(683,463,18,20);break;
			case 1: rec1 = new Rectangle(607,603,18,20);break; 
			case 2: rec1 = new Rectangle(758,348,11,15);break;
			case 3: rec1 = new Rectangle(90,369,5,9);break;
			case 4: rec1 = new Rectangle(586,162,8,11);break;
			}
			switch(pic) {
			case 0: rec2 = new Rectangle(679,487,27,22);break;
			case 1: rec2 = new Rectangle(589,623,51,29);break;
			case 2: rec2 = new Rectangle(748,364,19,33);break;
			case 3: rec2 = new Rectangle(86,381,12,18);break;
			case 4: rec2 = new Rectangle(586,174,7,5);break;
			}
			switch(pic) {
			case 0: rec3 = new Rectangle(687,510,21,6);break;
			case 1: rec3 = new Rectangle(594,649,30,30);break;
			case 2: rec3 = new Rectangle(746,396,11,12);break;
			case 3: rec3 = new Rectangle(87,404,8,23);break;
			case 4: rec3 = new Rectangle(583,180,10,7);break;
			}
			clicked = false;
		}
	}
}
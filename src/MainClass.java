import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class MainClass extends JFrame implements MouseListener {

	public static final int SIZE = 500;
	public static final int SPACING = SIZE / 10;

	Token[][] allTokens;
	Token selected;
	int selectedRow,selectedCol;
	boolean redTurn, didJump;
	BufferedImage offscreen;
	Graphics bg;

	public MainClass() {
		offscreen = new BufferedImage(SIZE,SIZE,BufferedImage.TYPE_INT_RGB);
		bg = offscreen.getGraphics();
		didJump = false;
		redTurn = true;
		selected = null;
		allTokens = new Token[10][10];
		for (int col = 0; col < 10; col++) {
			for (int row = 0; row < 3; row++) {
				if ((row + col) % 2 != 0) {
					allTokens[row][col] = new Token(Color.black);
				}
			}
			for (int row = 7; row < 10; row++) {
				if ((row + col) % 2 != 0) {
					allTokens[row][col] = new Token(Color.red);
				}
			}
		}
		this.addMouseListener(this);
	}

	public static void main(String[] args) {
		MainClass mc = new MainClass();
		mc.setSize(SIZE, SIZE + 30);
		mc.setResizable(false);
		mc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mc.setVisible(true);
	}

	public void paint(Graphics g) {
		drawBoard(bg);
		for (int col = 0; col < 10; col++) {
			for (int row = 0; row < 10; row++) {
				if(allTokens[row][col]!=null)allTokens[row][col].draw(bg, row, col);
			}
		}
		if(selected!=null)selected.drawSelected(bg, selectedRow, selectedCol);
		g.drawImage(offscreen, 0, 30, null);
	}

	private void drawBoard(Graphics g) {
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if ((row + col) % 2 == 0)
					g.setColor(new Color(0x8C0000));
				else
					g.setColor(new Color(0x616161));
				g.fillRect(col * SPACING, row * SPACING, SPACING, SPACING);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	
	private boolean isAdjacent(int row, int col){
		int dx = Math.abs(row-selectedRow);
		int dy = Math.abs(col-selectedCol);
		return dx==1 && dy==1;
	}
	
	
	private boolean isForward(int row){
		int dy = row-selectedRow;  // positive if moving down
		return dy>0 != redTurn; // red moves up
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int col = e.getX()/SPACING;
		int row = (e.getY()-30)/SPACING;
		
		if(allTokens[row][col]==selected){
			// deselecting
			selected = null;
			if(didJump){
				didJump = false;
				redTurn = !redTurn;
			}
		}
		else if(selected==null){
			// selecting
			if(redTurn != allTokens[row][col].isRed())return;
			selectedRow = row;
			selectedCol = col;
			selected = allTokens[selectedRow][selectedCol];
		}else{
			// moving
			if(allTokens[row][col]!=null)return;
			if(!selected.hasCrown() && !isForward(row))return;
			if(didJump && !isJump(row,col))return;
			if(!didJump && !isJump(row,col) && !isAdjacent(row,col))return;
			allTokens[row][col] = selected;
			if(isBackRow(row))selected.addCrown();
			allTokens[selectedRow][selectedCol] = null;
			selectedRow = row;
			selectedCol = col;
			if(!didJump){
				selected = null;
				redTurn = !redTurn;
			}
		}
		repaint();
	}
	
	private boolean isBackRow(int row){
		if(redTurn)return row==0;
		return row==9;
	}

	private boolean isJump(int row, int col) {
		// make sure diagonally 2 away
		int dy = row-selectedRow;
		int dx = col-selectedCol;
		if( 4!=dy*dy || dx*dx!=4 )return false;
		
		// make sure token of other color exists between 
		int mX = selectedCol + dx /2;
		int mY = selectedRow +dy /2;
		if(allTokens[mY][mX] == null || allTokens[mY][mX].isRed() == redTurn)return false;
		
		// remove the jumped token, and set jump flag
		allTokens[mY][mX] = null;
		didJump = true;
		return true;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}

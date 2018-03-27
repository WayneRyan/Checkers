import java.awt.Color;
import java.awt.Graphics;


public class Token {
	boolean hasCrown;
	Color myColor;
	
	public Token(Color c){
		myColor = c;
		hasCrown = false;
	}
	
	public void draw(Graphics g, int row, int col){
		int x = col*MainClass.SPACING;
		int y = row*MainClass.SPACING;
		g.setColor(myColor);
		g.fillOval(x, y, MainClass.SPACING, MainClass.SPACING);
		g.setColor(new Color(1,1,1,0.5f));
		if(hasCrown)g.fillOval(x+5, y+5, MainClass.SPACING-10, MainClass.SPACING-10);
	}
	
	public void drawSelected(Graphics g, int row, int col){
		g.setColor(Color.yellow);
		g.fillOval(col*MainClass.SPACING-2, row*MainClass.SPACING-2, MainClass.SPACING+4, MainClass.SPACING+4);
		draw(g,row,col);
	}
	
	public void addCrown(){
		hasCrown = true;
	}
	
	public boolean hasCrown(){
		return hasCrown;
	}

	public boolean isRed() {
		return myColor.equals(Color.red);
	}

}

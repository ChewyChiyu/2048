import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
@SuppressWarnings("serial")

public class _2048Panel extends JPanel{

	Dimension gameDim = new Dimension(600,600);

	int[][] grid = new int[4][4]; 

	final int BORDER_BUFFER = 50;
	final int SPACER = 20;
	final int PANEL_SIZE = 100;

	int score = 0;

	public _2048Panel(){		
		panel();
		keys();
		newGame();
	}

	void newGame(){
		//reset score
		score = 0;
		//clearing grid
		for(int row = 0; row < grid.length; row++){
			for(int col = 0; col < grid[0].length; col++){
				grid[row][col] = 0;
			}
		}
		//summoning two new numbers
		summonNum();
		summonNum();		
		gridToPanel();


	}

	void summonNum(){ //always summoning the number 2
		if(noPossibleMoves()){ return; }
		//summon the number num in a random location 
		Point randLocation = new Point(-1,-1);
		while(randLocation.x==-1&&randLocation.y==-1){
			int randRow = (int)(Math.random()*grid.length);
			int randCol = (int)(Math.random()*grid[0].length);
			if(grid[randRow][randCol]==0){
				randLocation.setLocation(randRow, randCol);
			}
		}
		grid[randLocation.x][randLocation.y] = 2;
	}

	boolean noPossibleMoves(){
		
		//checking if any zeros
		for(int[] row: grid){
			for(int col : row){
				if(col==0){ return false; }
			}
		}
		return true;
		
	}

	void panel(){
		JFrame frame = new JFrame("2048");
		frame.add(this);
		frame.setPreferredSize(gameDim);
		//reset button
		JButton reset = new JButton("reset");
		reset.setFocusable(false);
		reset.addActionListener(e-> {
			newGame();
		});
		this.add(reset);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	void keys(){
		getInputMap().put(KeyStroke.getKeyStroke("A"), "A");
		getInputMap().put(KeyStroke.getKeyStroke("S"), "S");
		getInputMap().put(KeyStroke.getKeyStroke("D"), "D");
		getInputMap().put(KeyStroke.getKeyStroke("W"), "W");
		getActionMap().put("A", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				shift(ShiftDirection.WEST);
			}

		});
		getActionMap().put("S", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				shift(ShiftDirection.SOUTH);
			}

		});
		getActionMap().put("D", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				shift(ShiftDirection.EAST);
			}

		});
		getActionMap().put("W", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				shift(ShiftDirection.NORTH);
			}

		});
	}
	void printGrid(){ //printing the grid
		for(int[] row: grid){
			for(int col: row){
				System.out.print(col + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	void printArr(int[] arr){
		for(int i : arr){ 
			System.out.print(i + " "); 
		}
		System.out.println(); 
	}
	void shift(ShiftDirection d){ //shifting the grid based on key input
		switch(d){
		case EAST:
			// ------> shift
			for(int row = 0; row < grid.length; row++){
				//getting arr to compress
				int[] shift = new int[grid[0].length];
				int reverse = 0;
				for(int col = grid[0].length-1; col >=0  ; col--){
					shift[reverse++] = grid[row][col];
				}
				shift = compressArr(shift);
				//reapplying shifted arr
				reverse = 0;
				for(int col = grid[0].length-1; col >=0  ; col--){
					grid[row][col] = shift[reverse++];
				}
			}
			summonNum();
			gridToPanel();
			printGrid();
			break;
		case NORTH:
			for(int col = 0; col < grid[0].length; col++){
				int[] shift = new int[grid.length];
				for(int row = 0; row < grid.length; row++){
					shift[row] = grid[row][col];
				}
				shift = compressArr(shift);
				for(int row = 0; row < grid.length; row++){
					grid[row][col] = shift[row];
				}
			}
			summonNum();
			gridToPanel();
			printGrid();
			break;
		case SOUTH:
			for(int col = 0; col < grid.length; col++){
				//getting arr to compress
				int[] shift = new int[grid.length];
				int reverse = 0;
				for(int row = grid[0].length-1; row >=0  ; row--){
					shift[reverse++] = grid[row][col];
				}
				shift = compressArr(shift);
				//reapplying shifted arr
				reverse = 0;
				for(int row = grid[0].length-1; row >=0  ; row--){
					grid[row][col] = shift[reverse++];
				}
			}
			summonNum();
			gridToPanel();
			printGrid();
			break;
		case WEST:
			// <------- shift
			for(int row = 0; row < grid.length; row++){
				//getting arr to compress
				int[] shift = new int[grid[0].length];
				for(int col = 0; col < grid[0].length; col++){
					shift[col] = grid[row][col];
				}
				shift = compressArr(shift);
				//reapplying shifted arr
				for(int col = 0; col < grid[0].length; col++){
					grid[row][col] = shift[col];
				}
			}
			summonNum();
			gridToPanel();
			printGrid();
			break;

		}
	}

	//Animation? . . . nah....
	void gridToPanel(){
		repaint();
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setFont(new Font("Aerial",Font.BOLD,30));
		//drawing panel from grid
		int xBuffer = BORDER_BUFFER;
		int yBuffer = BORDER_BUFFER;
		for(int row = 0; row < grid.length; row++){
			for(int col = 0; col < grid[0].length; col++){
				//setting color to value
				g.setColor(getColor(grid[row][col]));
				g.fillRect(xBuffer, yBuffer, PANEL_SIZE, PANEL_SIZE);
				//drawing number
				g.setColor(getNumColor(grid[row][col]));
				g.drawString("" +grid[row][col], xBuffer + (int)(PANEL_SIZE*.47) - (""+grid[row][col]).length()*10, yBuffer + (int) (PANEL_SIZE * .60));
				xBuffer+=(PANEL_SIZE+SPACER);
			}
			xBuffer = BORDER_BUFFER;
			yBuffer+=(PANEL_SIZE+SPACER);
		}
		//drawing score
		g.setColor(new Color(0x776e65));
		g.drawString("Score " + score, BORDER_BUFFER / 2, (int)(BORDER_BUFFER * .75));
	}

	int[] sortZeros(int[] arr){ // [0,2,0,2] --> [2,2,0,0], returns values shifted West  https://www.geeksforgeeks.org/move-zeroes-end-array/
		int count = 0;  
		for (int i = 0; i < arr.length; i++){
			if (arr[i] != 0){
				arr[count++] = arr[i]; 
			}
		}
		while (count < arr.length){
			arr[count++] = 0;
		}
		return arr;
	}
	int[] compressArr(int [] arr){ // [2,2,0,0] --> [4,0,0,0] , [2,2,4,4] , [4,8,0,0]
		int[] sorted = sortZeros(arr); //  [0,2,0,2] --> [2,2,0,0]
		for(int index = 0; index < sorted.length-1; index++){
			if(sorted[index] == sorted[index+1]){
				sorted[index] = sorted[index] * 2;
				//adding to score
				score += sorted[index] * 2;
				sorted[index + 1] = 0; 
			}
		}
		sorted = sortZeros(sorted);
		return(sorted);
	}

	Color getColor(int num){ // standard color choice https://github.com/bulenkov/2048/blob/master/src/com/bulenkov/game2048/Game2048.java
		switch (num) {
		case 2:    return new Color(0xeee4da);
		case 4:    return new Color(0xede0c8);
		case 8:    return new Color(0xf2b179);
		case 16:   return new Color(0xf59563);
		case 32:   return new Color(0xf67c5f);
		case 64:   return new Color(0xf65e3b);
		case 128:  return new Color(0xedcf72);
		case 256:  return new Color(0xedcc61);
		case 512:  return new Color(0xedc850);
		case 1024: return new Color(0xedc53f);
		case 2048: return new Color(0xedc22e);
		}
		return new Color(0xcdc1b4);
	}
	Color getNumColor(int num) {
		if(num < 16)
			return new Color(0x776e65);
		else
			return new Color(0xf9f6f2);
	}
}
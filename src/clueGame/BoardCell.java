package clueGame;

public class BoardCell {

	private int row;
	private int column;
	private char initial;
	private char doorDirection = 'x';
	
	
	
	public BoardCell(int row, int column, char initial) {
		super();
		this.row = row;
		this.column = column;
		this.initial = initial;
	}
	
	public void setDoorDirection(char c){
		doorDirection = c;
	}

	public int getRow() {
		return this.row;
	}


	public int getColumn() {
		return this.column;
	}

	public void setInitial(char i) {
		this.initial = i;
	}


	public char getInitial() {
		return this.initial;
	}

	public boolean isWalkway(){
		if (this.initial == 'W') return true;
		
		return false;
	}
	
	public boolean isRoom(){
		
		return false;
	}
	
	public boolean isDoorway(){
		if (this.doorDirection != 'x') return true;
		return false;
	}

	public DoorDirection getDoorDirection() {
		if (this.isDoorway()){
			switch (this.doorDirection){
			case 'R': return DoorDirection.RIGHT;
			case 'L': return DoorDirection.LEFT;
			case 'D': return DoorDirection.DOWN;
			case 'U': return DoorDirection.UP;
			}
		}
		return DoorDirection.NONE;
	}
}

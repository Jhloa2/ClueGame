package clueGame;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.io.FileNotFoundException;
import java.io.File;
public class Board {
	
private int numRows;
private int numColumns;
public final static int BOARD_SIZE = 50;
private BoardCell[][] board;

private static Map<Character, String> rooms;
public Map<BoardCell, LinkedList<BoardCell>> adjMatrix;
private Set<BoardCell> targets;
private Set<BoardCell> visited;

public String boardConfigFile;
public String roomConfigFile;

public Board(String boardConfigFile, String roomConfigFile) {
	super();
	this.boardConfigFile = boardConfigFile;
	this.roomConfigFile = roomConfigFile;
	
	adjMatrix = new HashMap<BoardCell, LinkedList<BoardCell>>();
	targets = new HashSet<BoardCell>();
	visited = new HashSet<BoardCell>();
	
}

//public Board() {
//	super();
//	//OUR FILES
////	this.boardConfigFile = "layout2.csv";
////	this.roomConfigFile = "legend.txt";
//	
//	//HER FILES
//	this.boardConfigFile = "ClueLayout.csv";
//	this.roomConfigFile = "ClueLegend.txt";
//}



public int getNumRows() {
	return numRows;
}

public int getNumColumns() {
	return numColumns;
}

public LinkedList<BoardCell> getAdjList(int i, int j){
	return adjMatrix.get(getCellAt(i,j));
}

public static Map<Character, String> getRooms(){
	return rooms;
}

public Set<BoardCell> getTargets(){
	return targets;
}

public void initialize(){
	board = new BoardCell[BOARD_SIZE][BOARD_SIZE]; //make a board as big as we could possibly need (50/50)
	
	// makes board above, then call load board && load room
	
	rooms = new HashMap<Character, String>();
	//Board.rooms.clear();
	try {
		this.loadRoomConfig();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		System.out.println("Legend file not found!");
	} catch (BadConfigFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	try {
		this.loadBoardConfig();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (BadConfigFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	calcAdjacencies();
}

public void loadRoomConfig() throws FileNotFoundException, BadConfigFormatException{

	FileReader reader = new FileReader(roomConfigFile);
	Scanner in = new Scanner(reader);
	in.useDelimiter(", |\n");
	//rooms.put(in.next().charAt(0), in.next());
	//in.next();
	do{
		rooms.put(in.next().charAt(0), in.next());
		if(!in.hasNext()) throw new BadConfigFormatException();
		in.next(); //doesn't look at the type (card,other)
		
	} while (in.hasNext());
in.close();

}

@SuppressWarnings("resource")
public void loadBoardConfig() throws FileNotFoundException, BadConfigFormatException{

	File file = new File(boardConfigFile);
	Scanner input = new Scanner(file);
	String s = new String();

//	input.useDelimiter(",|\n");
//	while(input.hasNext()) {
//		board[i][j % numCols].setInitial(input.next());
//		j++;
//		if (j >= numCols) {
//			i++;
//		}
//	}
	
	//determine numCols. How many commas in a line? NumCols = numCommas + 1
    s=input.nextLine();
	int count = 0;
    for (int i=0 ; i < s.length(); i++){
    	if (s.charAt(i)==',') count ++;
    }
    this.numColumns = count+1;
    
    input.close();
    input = new Scanner(file); //reset the scanner so it starts at the beginning of the file
    input.useDelimiter("\r\n|,");
    String temp = new String();
    //use numColumns to produce each row of the array
    int j = 0;
    do{
    	for (int i=0 ; i < numColumns ; i++){
    		temp = input.next();
    		if (temp.length()==2 && temp.charAt(1) != 'N'){
    			board[j][i] = new BoardCell(j,i,temp.charAt(0));
    			board[j][i].setDoorDirection(temp.charAt(1));
    		}
    		else {
    			//if(i > board.length-3 || j > board[i].length-3) throw new BadConfigFormatException();
    			//System.out.println(i + " " + j);
    			try {
					board[j][i] = new BoardCell(j,i,temp.charAt(0));
				} catch (Exception e) {
					throw new BadConfigFormatException();
					//e.printStackTrace();
				}
    			}
    		
    	}
    	j++;
    } while (input.hasNextLine());
    
    this.numRows = j;
  
    
	input.close();
}
		

public void calcAdjacencies(){
	for(int r = 0; r < numRows; r ++) {
		for(int w = 0; w < numColumns; w ++) {
			//BoardCell cur = getCellAt(r,w);
			LinkedList<BoardCell> list = new LinkedList<BoardCell>();
			
			if(getCellAt(r,w).isRoom()) {
				adjMatrix.put(getCellAt(r,w), list);
			}
			if (getCellAt(r,w).isWalkway()) {
				if (r != 0)
					if(getCellAt(r-1, w).isWalkway() || (getCellAt(r-1,w).isDoorway() && getCellAt(r-1,w).getDoorDirection() == DoorDirection.DOWN)) list.add(getCellAt(r-1,w));
				if (w != 0)
					if(getCellAt(r, w-1).isWalkway() || (getCellAt(r,w-1).isDoorway() && getCellAt(r,w-1).getDoorDirection() == DoorDirection.RIGHT)) list.add(getCellAt(r,w-1));
				if (r != getNumRows()-1)
					if(getCellAt(r+1, w).isWalkway() || (getCellAt(r+1,w).isDoorway() && getCellAt(r+1,w).getDoorDirection() == DoorDirection.UP)) list.add(getCellAt(r+1,w));
				if (w != getNumColumns()-1)
					if(getCellAt(r, w+1).isWalkway() || (getCellAt(r,w+1).isDoorway() && getCellAt(r,w+1).getDoorDirection() == DoorDirection.LEFT)) list.add(getCellAt(r,w+1));
			}
			if (getCellAt(r,w).isDoorway()) {
				if(getCellAt(r,w).getDoorDirection() == DoorDirection.UP) list.add(getCellAt(r-1,w));
				if(getCellAt(r,w).getDoorDirection() == DoorDirection.DOWN) list.add(getCellAt(r+1,w));
				if(getCellAt(r,w).getDoorDirection() == DoorDirection.RIGHT) list.add(getCellAt(r,w+1));
				if(getCellAt(r,w).getDoorDirection() == DoorDirection.LEFT) list.add(getCellAt(r,w-1));
			}
			adjMatrix.put(getCellAt(r,w), list);
			//System.out.println(adjMatrix);
		}
	}
}

public void calcTargets(int row, int column, int pathLength){
	visited.clear();
	visited.add(getCellAt(row,column));
	targets.clear();
	findAllTargets(getCellAt(row,column), pathLength);
}

public void findAllTargets(BoardCell startCell, int numSteps) {
	LinkedList<BoardCell> adj = getAdjList(startCell.getRow(),startCell.getColumn());
	LinkedList<BoardCell> unv = unvisitedAdj(adj);
	for(BoardCell b : unv) {
		if (b.isDoorway() && !targets.contains(b)){
			targets.add(b);
		}
	}
	
	for(BoardCell b : unv) {
		visited.add(b);
		if(numSteps == 1) targets.add(b);
		else findAllTargets(b,numSteps-1);
		visited.remove(b);
	}
	
}

private LinkedList<BoardCell> unvisitedAdj(LinkedList<BoardCell> adj) {
	LinkedList<BoardCell> unv = new LinkedList<BoardCell>();
	for(BoardCell b: adj) {
		if(!visited.contains(b)) unv.add(b);
	}
	return unv;
}

public BoardCell getCellAt(int row, int column){
	return board[row][column];
}

public static void printCell(BoardCell b) {
	System.out.println(b.getRow() + " " + b.getColumn());
}

public static void main(String[] args) {
	Board board = new Board("ClueLayout.csv","ClueLegend.txt");

	board.initialize();
	board.calcTargets(12, 7, 3);
}
}
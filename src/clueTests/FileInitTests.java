package clueTests;

// Doing a static import allows me to write assertEquals rather than
// Assert.assertEquals
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.BoardCell;
//import clueGame.ClueGame;
import clueGame.DoorDirection;

public class FileInitTests {
	// Constants that I will use to test whether the file was loaded correctly
	public static final int NUM_ROOMS = 11;
	public static final int NUM_ROWS = 23;
	public static final int NUM_COLUMNS = 24;

	// NOTE: I made Board static because I only want to set it up one 
	// time (using @BeforeClass), no need to do setup before each test.
	// The methods that test invalid config files will have a local 
	// Board variable, so will not use this
	private Board board;
	
	@Before
	public void setUp() {
		// Create a new Board using the valid files. Note that
		// the default filenames must be attributes of the Board class. 
		board = new Board("layout.csv","legend.txt");
		// Initialize will load BOTH config files 
		board.initialize();
	}
	@Test
	public void testRooms() {
		System.out.println("Testing...?");
		// rooms is static, see discussion in lab writeup
		Map<Character, String> rooms = Board.getRooms();
		// Ensure we read the correct number of rooms
		System.out.println(rooms);
		assertEquals(NUM_ROOMS, rooms.size());
		// To ensure data is correctly loaded, test retrieving a few rooms 
		// from the hash, including the first and last in the file and a few others
		assertEquals("Closet", rooms.get('X'));
		assertEquals("Sauna", rooms.get('S'));
		assertEquals("Bathroom", rooms.get('R'));
		assertEquals("Dancing Hall", rooms.get('D'));
		assertEquals("Pantry", rooms.get('C'));
	}
	
	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());		
	}
	
	// Test a doorway in each direction, plus two cells that are not
	// a doorway.
	// These cells are white on the planning spreadsheet
	@Test
	public void FourDoorDirections() {
		// Test one each RIGHT/LEFT/UP/DOWN
		BoardCell room = board.getCellAt(2, 5);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getCellAt(5, 12);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		room = board.getCellAt(5,20);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		room = board.getCellAt(14, 2);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());
		// Test that room pieces that aren't doors know it
		room = board.getCellAt(20,20);
		assertFalse(room.isDoorway());	
		// Test that walkways are not doors
		BoardCell cell = board.getCellAt(14, 14);
		assertFalse(cell.isDoorway());		

	}
	
	// Test that we have the correct number of doors
	@Test
	public void testNumberOfDoorways() 
	{
		int numDoors = 0;
		int totalCells = board.getNumColumns() * board.getNumRows();
		Assert.assertEquals(552, totalCells);
		for (int row=0; row<board.getNumRows(); row++)
			for (int col=0; col<board.getNumColumns(); col++) {
				BoardCell cell = board.getCellAt(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(15, numDoors);
	}

	// Test a few room cells to ensure the room initial is correct.
	@Test
	public void testRoomInitials() {
		assertEquals('B', board.getCellAt(0, 0).getInitial());
		assertEquals('P', board.getCellAt(2, 12).getInitial());
		assertEquals('D', board.getCellAt(18, 21).getInitial());
		assertEquals('R', board.getCellAt(19, 8).getInitial());
		assertEquals('L', board.getCellAt(15,2).getInitial());
	}
	
	// Test that an exception is thrown for a bad config file
	@Test (expected = BadConfigFormatException.class)
	public void testBadColumns() throws BadConfigFormatException, FileNotFoundException {
		// overloaded Game ctor takes config file names. See discussion of 
		// testing exceptions in lab writeup. 
		// Note that we are using a LOCAL Board variable, not the static one 
		// set up by @BeforeClass
		Board board = new Board("ClueLayoutBadColumns.csv", "ClueLegend.txt");
		// Instead of initialize, we call the two load functions directly
		board.loadRoomConfig();
		// This one should throw an exception
		board.loadBoardConfig();
	}
	// Test that an exception is thrown for a bad config file
	@Test (expected = BadConfigFormatException.class)
	public void testBadRoom() throws BadConfigFormatException, FileNotFoundException {
		Board board = new Board("ClueLayoutBadRoom.csv", "ClueLegend.txt");
		board.loadRoomConfig();
		board.loadBoardConfig();
	}
	// Test that an exception is thrown for a bad room config file
	@Test (expected = BadConfigFormatException.class)
	public void testBadRoomFormat() throws BadConfigFormatException, FileNotFoundException {
		Board board = new Board("ClueLayout.csv", "ClueLegendBadFormat.txt");
		board.loadRoomConfig();
	}
}
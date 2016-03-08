package clueTests;

import java.util.LinkedList;
import java.util.Set;

//Doing a static import allows me to write assertEquals rather than
//assertEquals
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTests {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	@BeforeClass
	public static void setUp() {
		board = new Board("layout.csv", "legend.txt");
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesInsideRooms()
	{
		// Test a corner
		LinkedList<BoardCell> testList = board.getAdjList(0, 0);
		assertEquals(0, testList.size());
		// Test one that has walkway underneath
		testList = board.getAdjList(5, 10);
		assertEquals(0, testList.size());
		// Test one that has walkway above
		testList = board.getAdjList(8, 18);
		assertEquals(0, testList.size());
		// Test one that is in middle of room
		testList = board.getAdjList(19, 21);
		assertEquals(0, testList.size());
		// Test one beside a door
		testList = board.getAdjList(5, 11);
		assertEquals(0, testList.size());
		// Test one in a corner of room
		testList = board.getAdjList(7, 5);
		assertEquals(0, testList.size());
	}

	// Ensure that the adjacency list from a doorway is only the
	// walkway. NOTE: This test could be merged with door 
	// direction test. 
	// These tests are PURPLE on the planning spreadsheet
	@Test
	public void testAdjacencyRoomExit()
	{
		// TEST DOORWAY RIGHT 
		LinkedList<BoardCell> testList = board.getAdjList(2, 5);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(2, 6)));
		// TEST DOORWAY LEFT 
		testList = board.getAdjList(5, 20);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(5, 19)));
		//TEST DOORWAY DOWN
		testList = board.getAdjList(5, 12);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(6, 12)));
		//TEST DOORWAY UP
		testList = board.getAdjList(14, 2);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(13,2)));
		
	}
	
	// Test adjacency at entrance to rooms
	// These tests are GREEN in planning spreadsheet
	@Test
	public void testAdjacencyDoorways()
	{
		// Test beside a door direction RIGHT
		LinkedList<BoardCell> testList = board.getAdjList(2, 6);
		assertTrue(testList.contains(board.getCellAt(3, 6)));
		assertTrue(testList.contains(board.getCellAt(1, 6)));
		assertTrue(testList.contains(board.getCellAt(2, 7)));
		assertTrue(testList.contains(board.getCellAt(2, 5)));
		assertEquals(4, testList.size());
		// Test beside a door direction DOWN
		testList = board.getAdjList(12, 18);
		assertTrue(testList.contains(board.getCellAt(12, 17)));
		assertTrue(testList.contains(board.getCellAt(12, 19)));
		assertTrue(testList.contains(board.getCellAt(13, 18)));
		assertTrue(testList.contains(board.getCellAt(11, 18)));
		assertEquals(4, testList.size());
		// Test beside a door direction LEFT
		testList = board.getAdjList(18, 11);
		assertTrue(testList.contains(board.getCellAt(17, 11)));
		assertTrue(testList.contains(board.getCellAt(19, 11)));
		assertTrue(testList.contains(board.getCellAt(18, 10)));
		assertTrue(testList.contains(board.getCellAt(18, 12)));
		assertEquals(4, testList.size());
		// Test beside a door direction UP
		testList = board.getAdjList(13, 21);
		assertTrue(testList.contains(board.getCellAt(13, 20)));
		assertTrue(testList.contains(board.getCellAt(13, 22)));
		assertTrue(testList.contains(board.getCellAt(12, 21)));
		assertTrue(testList.contains(board.getCellAt(14, 21)));
		assertEquals(4, testList.size());
	}

	// Test a variety of walkway scenarios
	// These tests are LIGHT PURPLE on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on top edge of board, just two walkway piece
		LinkedList<BoardCell> testList = board.getAdjList(0, 6);
		assertTrue(testList.contains(board.getCellAt(0, 7)));
		assertTrue(testList.contains(board.getCellAt(1, 6)));
		assertEquals(2, testList.size());
		
		// Test on left edge of board, two walkway pieces
		testList = board.getAdjList(6, 0);
		assertTrue(testList.contains(board.getCellAt(6, 1)));
		assertEquals(1, testList.size());

		// Test between two rooms, walkways right and left
		testList = board.getAdjList(20, 6);
		assertTrue(testList.contains(board.getCellAt(19, 6)));
		assertTrue(testList.contains(board.getCellAt(21, 6)));
		assertEquals(2, testList.size());

		// Test surrounded by 4 walkways
		testList = board.getAdjList(15,7);
		assertTrue(testList.contains(board.getCellAt(15, 8)));
		assertTrue(testList.contains(board.getCellAt(15, 6)));
		assertTrue(testList.contains(board.getCellAt(14, 7)));
		assertTrue(testList.contains(board.getCellAt(16, 7)));
		assertEquals(4, testList.size());
		
		// Test on bottom edge of board, next to 1 room piece
		testList = board.getAdjList(22, 10);
		assertTrue(testList.contains(board.getCellAt(22, 11)));
		assertTrue(testList.contains(board.getCellAt(21, 10)));
		assertEquals(2, testList.size());
		
		// Test on right edge of board, next to 1 room piece
		testList = board.getAdjList(13, 23);
		assertTrue(testList.contains(board.getCellAt(13, 22)));
		assertTrue(testList.contains(board.getCellAt(12, 23)));
		assertEquals(2, testList.size());

		// Test on walkway next to  door that is not in the needed
		// direction to enter
		testList = board.getAdjList(19, 3);
		assertTrue(testList.contains(board.getCellAt(19, 4)));
		assertTrue(testList.contains(board.getCellAt(18, 3)));
		assertEquals(2, testList.size());
	}
	
	
	// Tests of just walkways, 1 step, includes on edge of board
	// and beside room
	// Have already tested adjacency lists on all four edges, will
	// only test two edges here
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsOneStep() {
		board.calcTargets(21, 11, 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(20, 11)));
		assertTrue(targets.contains(board.getCellAt(22, 11)));	
		assertTrue(targets.contains(board.getCellAt(21, 10)));
		
		board.calcTargets(22, 15, 1);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(22, 14)));
		assertTrue(targets.contains(board.getCellAt(22, 16)));	
		assertTrue(targets.contains(board.getCellAt(21, 15)));			
	}
	
	// Tests of just walkways, 2 steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsTwoSteps() {
		board.calcTargets(21, 11, 2);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(19, 11)));
		assertTrue(targets.contains(board.getCellAt(22, 10)));
		assertTrue(targets.contains(board.getCellAt(20, 10)));
		assertTrue(targets.contains(board.getCellAt(21, 9)));
		
		board.calcTargets(22,15, 2);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(21, 14)));
		assertTrue(targets.contains(board.getCellAt(21, 16)));	
		assertTrue(targets.contains(board.getCellAt(20, 15)));			
	}
	
	// Tests of just walkways, 4 steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsFourSteps() {
		board.calcTargets(21, 11, 4);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCellAt(22, 10)));
		assertTrue(targets.contains(board.getCellAt(20, 10)));
		assertTrue(targets.contains(board.getCellAt(19, 9)));
		assertTrue(targets.contains(board.getCellAt(19, 11)));
		assertTrue(targets.contains(board.getCellAt(18, 10)));
		assertTrue(targets.contains(board.getCellAt(17, 11)));
		assertTrue(targets.contains(board.getCellAt(21, 9)));
		assertTrue(targets.contains(board.getCellAt(18, 12)));
		
		// Includes a path that doesn't have enough length
		board.calcTargets(22,15, 4);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCellAt(21, 14)));
		assertTrue(targets.contains(board.getCellAt(21, 16)));	
		assertTrue(targets.contains(board.getCellAt(20, 15)));
		assertTrue(targets.contains(board.getCellAt(19, 14)));	
		assertTrue(targets.contains(board.getCellAt(19, 16)));
		assertTrue(targets.contains(board.getCellAt(18, 15)));	
		assertTrue(targets.contains(board.getCellAt(20, 17)));	
	}	
	
	// Tests of just walkways plus one door, 6 steps
	// These are LIGHT BLUE on the planning spreadsheet

	@Test
	public void testTargetsSixSteps() {
		board.calcTargets(13, 0, 6);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(11, targets.size());
		assertTrue(targets.contains(board.getCellAt(13, 2)));
		assertTrue(targets.contains(board.getCellAt(13, 4)));	
		assertTrue(targets.contains(board.getCellAt(13, 6)));	
		assertTrue(targets.contains(board.getCellAt(12, 1)));	
		assertTrue(targets.contains(board.getCellAt(12, 3)));	
		assertTrue(targets.contains(board.getCellAt(12, 5)));	
		assertTrue(targets.contains(board.getCellAt(11, 4)));
		assertTrue(targets.contains(board.getCellAt(14, 5)));
		assertTrue(targets.contains(board.getCellAt(11, 1)));
		assertTrue(targets.contains(board.getCellAt(11, 2)));
		assertTrue(targets.contains(board.getCellAt(14, 2)));
	}	
	
	// Test getting into a room
	// These are LIGHT BLUE on the planning spreadsheet

	@Test 
	public void testTargetsIntoRoom()
	{
		board.calcTargets(13, 0, 6);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(11, targets.size());
		assertTrue(targets.contains(board.getCellAt(13, 2)));
		assertTrue(targets.contains(board.getCellAt(13, 4)));	
		assertTrue(targets.contains(board.getCellAt(13, 6)));	
		assertTrue(targets.contains(board.getCellAt(12, 1)));	
		assertTrue(targets.contains(board.getCellAt(12, 3)));	
		assertTrue(targets.contains(board.getCellAt(12, 5)));	
		assertTrue(targets.contains(board.getCellAt(11, 4)));
		assertTrue(targets.contains(board.getCellAt(11, 1)));
		assertTrue(targets.contains(board.getCellAt(11, 2))); // exactly 6 away
		assertTrue(targets.contains(board.getCellAt(14, 2)));
		
		
	}
	
	// Test getting into room, doesn't require all steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsIntoRoomShortcut() 
	{
		board.calcTargets(12, 7, 3);
		Set<BoardCell> targets= board.getTargets();

		
		board.calcTargets(6,12, 2);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCellAt(6, 10)));
		assertTrue(targets.contains(board.getCellAt(6, 14)));	
		assertTrue(targets.contains(board.getCellAt(7, 11)));
		assertTrue(targets.contains(board.getCellAt(7, 13)));
		assertTrue(targets.contains(board.getCellAt(8, 12)));
		assertTrue(targets.contains(board.getCellAt(5, 12))); //must shortcut to get in		
		
	}

	// Test getting out of a room
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testRoomExit()
	{
		// Take one step, essentially just the adj list
		board.calcTargets(5, 12, 1);
		Set<BoardCell> targets= board.getTargets();
		// Ensure doesn't exit through the wall
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCellAt(6, 12)));
		// Take two steps
		board.calcTargets(5, 12, 2);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(6, 11)));
		assertTrue(targets.contains(board.getCellAt(6, 13)));
		assertTrue(targets.contains(board.getCellAt(7, 12)));
	}

}

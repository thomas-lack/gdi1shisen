package gdi1shisen.test.pub;


import static gdi1shisen.test.ShisenTest.*;
import static org.junit.Assert.*;
import gdi1shisen.test.*;
import org.junit.*;
import java.io.*;


/**
 * Public tests for the minimal Shisen implementation level
 */
public class ShisenTestPublicMinimal {
	
	/**
	 * The tested Shisen implementation
	 */
	private ShisenTest testee = null;
	
	/**
	 * Variables and constants needed for tests
	 */
	private static final File valLevel	= new File( LVLPUB , "test.valid.lvl" );	 /* Valid level   */
	private static final File invLevel1	= new File( LVLPUB , "test.invalid.1.lvl" ); /* Invalid level */
	private static final File invLevel2	= new File( LVLPUB , "test.invalid.2.lvl" ); /* Invalid level */
	private static final File invLevel3	= new File( LVLPUB , "test.invalid.3.lvl" ); /* Invalid level */
	private static final File invLevel4	= new File( LVLPUB , "test.invalid.4.lvl" ); /* Invalid level */
	private static final File invLevel5	= new File( LVLPUB , "test.invalid.5.lvl" ); /* Invalid level */
	private static final File invLevel6	= new File( LVLPUB , "test.invalid.6.lvl" ); /* Invalid level */
	private static final File invLevel7	= new File( LVLPUB , "test.invalid.7.lvl" ); /* Invalid level */


	@BeforeClass
	public static void init() {
		ShisenTestAdapter.init();
	}

	@Before
	public void before() {
		testee = new ShisenTestAdapter();
	}


	/**
	 * Requirements "parseLevel", "toString" & "checkSyntax"
	 */

	@Test
	public void parseValidLevel() throws Exception {
		testee.loadLevel( valLevel );

		String valLevelString = "OBA7CD9FWNHIJ2LMQPT1DEROZGHI6SPJKAQLBCDWFPGHAOIQ7NJUL675PCDOEQFGHIJMKLS7T9UVGXYZ0RM1N28345BK8VWEXSYZ0CK3A45NTUVMRWXYF89601234B8T2VEXYZ01U93S456R";
		
		assertEquals("The string representation of the parsed level differs from file content", valLevelString , testee.currentLevelToString());
	}

	@Test(expected=Exception.class)
	public void parseInvalidLevel1() throws Exception {
		testee.loadLevel( invLevel1 );
	}

	@Test(expected=Exception.class)
	public void parseInvalidLevel2() throws Exception {
		testee.loadLevel( invLevel2 );
	}

	@Test(expected=Exception.class)
	public void parseInvalidLevel3() throws Exception {
		testee.loadLevel( invLevel3 );
	}

	@Test(expected=Exception.class)
	public void parseInvalidLevel4() throws Exception {
		testee.loadLevel( invLevel4 );
	}

	@Test(expected=Exception.class)
	public void parseInvalidLevel5() throws Exception {
		testee.loadLevel( invLevel5 );
	}

	@Test(expected=Exception.class)
	public void parseInvalidLevel6() throws Exception {
		testee.loadLevel( invLevel6 );
	}

	@Test(expected=Exception.class)
	public void parseInvalidLevel7() throws Exception {
		testee.loadLevel( invLevel7 );
	}


	/**
	 * Requirements "detectSolved"
	 */

	@Test
	public void detectSolved() throws Exception {
		testee.loadLevel( valLevel );
		assertFalse("New level detected as solved.", testee.isSolved());
	}
	
	@Test
	public void testGetLevelWidth() throws Exception {
		testee.loadLevel( valLevel );
		assertEquals( "Level width must be 18." , 18 , testee.getLevelWidth() );
	}
	
	@Test
	public void testGetLevelHeight() throws Exception {
		testee.loadLevel( valLevel );
		assertEquals( "Level height must be 8." , 8 , testee.getLevelHeight() );
	}
	
	@Test
	public void testGetBrickCount() throws Exception {
		testee.loadLevel( valLevel );
		assertEquals( "A level initially contains 144 bricks." , 144 , testee.getBrickCount() );		
	}


	/**
	 * General
	 * */

	@Test
	public void testBrickAt () throws Exception {
		testee.loadLevel( valLevel );

		assertEquals( "O" , testee.getBrickAt( 0, 0) );
		assertEquals( "P" , testee.getBrickAt(17, 0) );
		assertEquals( "8" , testee.getBrickAt( 0, 7) );
		assertEquals( "R" , testee.getBrickAt(17, 7) );
		assertEquals( "D" , testee.getBrickAt( 2, 2) );
		assertEquals( "E" , testee.getBrickAt( 3, 1) );
		assertEquals( "E" , testee.getBrickAt( 5, 5) );
		assertEquals( "C" , testee.getBrickAt( 3, 3) );
	}

	@Test
	public void testCanBeRemoved1 () throws Exception {
		testee.loadLevel( valLevel );

		assertTrue( testee.canBeRemoved(16 , 0 , 16 , 1) );
		assertTrue( testee.canBeRemoved( 0 , 1 ,  0 , 4) );
		assertTrue( testee.canBeRemoved( 0 , 4 ,  0 , 6) );
		assertTrue( testee.canBeRemoved( 0 , 1 ,  0 , 6) );
		assertTrue( testee.canBeRemoved( 0 , 2 ,  0 , 5) );
		assertTrue( testee.canBeRemoved( 6 , 5 ,  6 , 6) );
		assertTrue( testee.canBeRemoved( 2 , 1 ,  2 , 2) );
		assertTrue( testee.canBeRemoved( 3 , 4 ,  3 , 5) );
	}

	@Test
	public void testCanBeRemoved2 () throws Exception {
		testee.loadLevel( valLevel );

		assertFalse( testee.canBeRemoved( 0 , 0 ,  1 , 1) );
		assertFalse( testee.canBeRemoved( 2 , 2 ,  3 , 2) );
		assertFalse( testee.canBeRemoved( 8 , 1 ,  7 , 2) );
		assertFalse( testee.canBeRemoved( 7 , 6 ,  6 , 7) );
	}

	@Test
	public void testRemoveBricks () throws Exception {
		testee.loadLevel( valLevel );

		testee.removeBricks(16 , 0 , 16 , 1); assertEquals( 142 , testee.getBrickCount() );
		testee.removeBricks( 0 , 1 ,  0 , 4); assertEquals( 140 , testee.getBrickCount() );
		testee.removeBricks( 0 , 2 ,  0 , 5); assertEquals( 138 , testee.getBrickCount() );
		testee.removeBricks( 6 , 5 ,  6 , 6); assertEquals( 136 , testee.getBrickCount() );
		testee.removeBricks( 2 , 1 ,  2 , 2); assertEquals( 134 , testee.getBrickCount() );
		testee.removeBricks( 3 , 4 ,  3 , 5); assertEquals( 132 , testee.getBrickCount() );

		assertFalse( testee.getBrickAt(16, 0).equals( "Q" ) );
		assertFalse( testee.getBrickAt(16, 1).equals( "Q" ) );
		assertFalse( testee.getBrickAt( 0, 1).equals( "T" ) );
		assertFalse( testee.getBrickAt( 0, 4).equals( "T" ) );
		assertFalse( testee.getBrickAt( 0, 2).equals( "B" ) );
		assertFalse( testee.getBrickAt( 0, 5).equals( "B" ) );
		assertFalse( testee.getBrickAt( 2, 1).equals( "D" ) );
		assertFalse( testee.getBrickAt( 2, 2).equals( "D" ) );
	}
}
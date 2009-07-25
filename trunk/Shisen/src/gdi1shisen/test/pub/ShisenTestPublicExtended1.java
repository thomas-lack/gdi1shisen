package gdi1shisen.test.pub;


import static gdi1shisen.test.ShisenTest.*;
import static org.junit.Assert.*;
import gdi1shisen.test.*;
import org.junit.*;
import java.util.*;
import java.io.*;


/**
* Public tests for the 1st extended Shisen implementation level
*/
public class ShisenTestPublicExtended1 {

	/**
	 * The tested Shisen implementation
	 */
	private ShisenTest testee = null;

	/**
	 * Variables and constants needed for tests
	 */
	private static final File lvlFile = new File( LVLPUB , "test.valid.lvl" );
	private static final File hscFile = new File( LVLPUB , "test.valid.hsc" );


	@BeforeClass
	public static void init() {
		ShisenTestAdapter.init();
	}
	
	@Before
	public void before() {
		testee = new ShisenTestAdapter();
	}


	/**
	 * Requirement "highScoreDB"
	 * @throws Exception 
	 */

	private void prepare () throws Exception {
		if(hscFile.exists()){
			if(!hscFile.delete()){
				throw new Exception( "Could not delete highscore file \"" + hscFile.getAbsolutePath() + "\"" );
			}
		}

		testee.loadLevel( lvlFile );

		testee.createHighscoreEntry( "Test2" , 10 );
		testee.createHighscoreEntry( "Test3" , 100 );
		testee.createHighscoreEntry( "Test1" , 1 );
		testee.createHighscoreEntry( "Test5" , 10000 );
		testee.createHighscoreEntry( "Test4" , 1000 );

		testee.writeHighScoreFile();
	}

	private ArrayList<String[]> read () throws Exception {
		/* Open file	*/
		BufferedReader in = new BufferedReader(
				new FileReader( hscFile )
		);

		/* Read file	*/
		ArrayList<String[]> list = new ArrayList<String[]>();

		String line;

		while((line = in.readLine()) != null && line.length() > 0){
			list.add(
					line.split(
							";"
					)
			);
		}

		/* Close file	*/
		in.close();

		return(
				list
		);
	}

	@Test
	public void highscoreDbCreate() throws Exception {
		prepare();

		assertTrue( "Highscore file not written." , hscFile.exists() );
		assertTrue( "Could not delete highscore file. Did you leave any Reader/Writer open?" , hscFile.delete() );
	}

	@Test
	public void testHighscoreCounts() throws Exception {
		prepare();

		assertEquals( 5 , testee.getHighscoreCount() );
	}
	
	@Test
	public void testGetBestPlayerName() throws Exception {
		prepare();

		assertEquals( "Test1" , testee.getBestPlayerName() );
	}

	@Test
	public void highscoreDbRead() throws Exception {
		prepare();

		ArrayList<String[]> hsc = read();

		assertEquals( "Test1" , hsc.get( 0 )[0] ); assertEquals( "1" , hsc.get( 0 )[1] );
		assertEquals( "Test2" , hsc.get( 1 )[0] ); assertEquals( "10" , hsc.get( 1 )[1] );
		assertEquals( "Test3" , hsc.get( 2 )[0] ); assertEquals( "100" , hsc.get( 2 )[1] );
		assertEquals( "Test4" , hsc.get( 3 )[0] ); assertEquals( "1000" , hsc.get( 3 )[1] );
		assertEquals( "Test5" , hsc.get( 4 )[0] ); assertEquals( "10000" , hsc.get( 4 )[1] );
	}
}
package gdi1shisen.test.pub;


import static gdi1shisen.test.ShisenTest.*;
import static org.junit.Assert.*;
import gdi1shisen.test.*;
import org.junit.*;
import java.io.*;


/**
* Public tests for the 2nd extended Shisen implementation level
*/
public class ShisenTestPublicExtended2 {

		/**
		 * The tested Shisen implementation
		 */
		private ShisenTest testee = null;

		/**
		 * Variables and constants needed for tests
		 */
		private static final File lvlFile = new File( LVLPUB , "test.valid.lvl" );
		private static final File sveFile = new File( LVLPUB , "test.valid.sve" );


		@BeforeClass
		public static void init() {
			ShisenTestAdapter.init();
		}

		@Before
		public void before() {
			testee = new ShisenTestAdapter();
		}


		/**
		 * Requirement "undo/redo"
		 */

		@Test
		public void undo() throws Exception {
			testee.loadLevel( lvlFile );

			assertEquals( 0 , testee.getUndoCount() );

			testee.removeBricks( 16 , 0 , 16 , 1 );

			assertEquals( 1 , testee.getUndoCount() );

			testee.removeBricks( 0 , 1 , 0 , 4 );

			assertEquals( 2 , testee.getUndoCount() );

			testee.undoLastMove();

			assertEquals( 1 , testee.getUndoCount() );

			testee.undoLastMove();

			assertEquals( 0 , testee.getUndoCount() );
		}

		@Test
		public void redo() throws Exception {
			undo();

			assertEquals( 2 , testee.getRedoCount() );

			testee.redoLastUndoneMove();

			assertEquals( 1 , testee.getRedoCount() );

			testee.redoLastUndoneMove();

			assertEquals( 0 , testee.getRedoCount() );
			assertEquals( 2 , testee.getUndoCount() );
		}


		/**
		 * Requirement "saveGame" & "loadGame"
		 */

		@Test
		public void saveGame() throws Exception {
			if(sveFile.exists()){
				if(!sveFile.delete()){
					throw new Exception( "Could not delete save file \"" + sveFile.getAbsolutePath() + "\"" );
				}
			}

			testee.loadLevel( lvlFile );

			testee.removeBricks( 16 , 0 , 16 , 1 );
			testee.removeBricks(  0 , 1 ,  0 , 4 );

			testee.saveGame( sveFile );

			assertTrue( sveFile.exists() );
		}

		@Test
		public void loadGame() throws Exception {
			saveGame();

			testee.loadGame( sveFile );

			assertEquals( 2 , testee.getUndoCount() );
			assertEquals( 140 , testee.getBrickCount() );			
		}
}
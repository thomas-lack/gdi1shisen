package gdi1shisen.test.pub;


import static gdi1shisen.test.ShisenTest.*;
import static org.junit.Assert.*;
import gdi1shisen.test.*;
import org.junit.*;
import java.io.*;


/**
* Public tests for the 3rd extended Shisen implementation level
*/
public class ShisenTestPublicExtended3 {

		/**
		 * the tested Shisen implementation
		 */
		private ShisenTest testee = null;

		/**
		 * Variables and constants needed for tests
		 */
		private static final File lvlFile = new File( LVLPUB , "test.valid.lvl" );


		@BeforeClass
		public static void init() {
			ShisenTestAdapter.init();
		}

		@Before
		public void before() {
			testee = new ShisenTestAdapter();
		}


		/**
		 * Requirement "deadlock"
		 */

		@Test
		public void testHint () throws Exception {
			testee.loadLevel( lvlFile );

			int[][] hint = testee.getHint();

			assertNotNull( hint );

			assertTrue(
					testee.canBeRemoved(
							hint[0][0] , hint[0][1] ,
							hint[1][0] , hint[1][1]
					)
			);
		}

		@Test
		public void testSolution () throws Exception {
			testee.loadLevel( lvlFile );

			int[][][] solution = testee.getSolution();

			assertNotNull( solution );

			for(int[][] hint : solution){
				assertTrue(
						testee.canBeRemoved(
								hint[0][0] , hint[0][1] ,
								hint[1][0] , hint[1][1]
						)
				);

				testee.removeBricks(
						hint[0][0] , hint[0][1] ,
						hint[1][0] , hint[1][1]
				);
			}

			assertTrue( testee.isSolved() );
			assertEquals( 0 , testee.getBrickCount() );
		}
}
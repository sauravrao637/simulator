/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.camo.CacheSimulatorLib;

import static org.junit.Assert.assertEquals;

import com.camo.csimulator.CS;
import com.camo.csimulator.HelperCS;

import org.junit.Test;

/*
#Start
W 7a2
W 22d
W 17d
W 17e
W 1e7
#comment
W 3f1
W 345
W 189
W 7c2
#eof
*/
public class CSTest {

    String[] arr = { "W 7a2", "W 22d", "W 17d ", "W 17e", "W 1e7", "#comment", "W 3f1", "W 345", "W 189", "W 7c2" };

    @Test
    public void testDM() {
        try {
            CS cs = new CS(2048, 4, 4, arr, 0);
            assertEquals("1f0", cs.getDirectMappingOutput().getDM().get(0).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHtoB() {
        try {
            assertEquals("111110000", HelperCS.htob("1f0", 4));
        } catch (Exception e) {
            assertEquals(false, true);
        }
    }

    @Test
    public void testWhatPower() {
        assertEquals(11, HelperCS.whatPower(2048));
    }

    @Test
    public void testAM() {
        try {
            CS cs = new CS(2048, 4, 4, arr, 0);
            assertEquals("011111100", cs.getAssociativeMappingOutput().getAM().get(0).getTag());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSAM() {
        try {
            CS cs = new CS(2048, 4, 4, arr, 0);
            assertEquals("d1", cs.getSetAssociativeMappingOutput(2).getSAM().get(3).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testML() {
        try {
            CS cs = new CS(2048, 4, 4, arr, 0);
            assertEquals("d1",cs.getMLOutput().getML().get(1).getContent());;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

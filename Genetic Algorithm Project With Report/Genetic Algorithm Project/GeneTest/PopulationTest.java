import org.junit.Test;

import java.math.RoundingMode;

import static org.junit.Assert.*;


public class PopulationTest {
    Population tstObj = new Population(500);
    @Test
    public void initPopulationTest(){
        tstObj.initPopulation();
        assertEquals(500, tstObj.pop.length);
        tstObj = new Population(100);
        assertNotEquals(500, tstObj.pop.length);
        assertEquals(100, tstObj.pop.length);
    }

    @Test
    public void calTotalFitTest(){
        tstObj.initPopulation();
        assertTrue(200000 < tstObj.calTotalFit().intValue() &&  tstObj.calTotalFit().intValue()< 300000);
    }

    @Test
    public void calRelativeFitTest(){
        tstObj.initPopulation();
        assertEquals(500, tstObj.calRelativeFit().length);
        assertTrue(tstObj.calRelativeFit()[200].doubleValue() >= 0 && tstObj.calRelativeFit()[200].doubleValue() < 3);
        double target = tstObj.pop[200].getFitness().divide(tstObj.calTotalFit(), 20, RoundingMode.HALF_UP).doubleValue();
        assertTrue(tstObj.calRelativeFit()[200].doubleValue() == target);
    }

    @Test
    public void crossOverTest(){
        tstObj.initPopulation();
        Individual p0 = tstObj.pop[0];
        Individual p1 = tstObj.pop[1];
        Individual p498 = tstObj.pop[498];
        Individual p499 = tstObj.pop[499];
        tstObj.crossover();
        assertNotEquals(p0, tstObj.pop[0]);
        assertNotEquals(p1, tstObj.pop[1]);
        assertNotEquals(p498, tstObj.pop[498]);
        assertNotEquals(p499, tstObj.pop[499]);
    }

    @Test
    public void evaluateTest(){
        tstObj.initPopulation();
        tstObj.evaluate();
        assertTrue(tstObj.generation != 0);
        for (int i = 0; i<100; i++)
            tstObj.evaluate();
        assertTrue(tstObj.generation == 101);
    }

    @Test
    public void isEvolutionTest(){
        assertFalse(tstObj.isEvolutionDone());
    }
}

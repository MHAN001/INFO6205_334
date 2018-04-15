import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class RosenbrockTest {
    Individual tstObj = new RosenbrockIndividual(20);
    @Test
    public void calTargetTest(){
        BigDecimal tmp = tstObj.getTargetValue();
        tstObj.setChrom(0,19,"00000000000000000000");
        tstObj.calTargetValue();
        assertNotEquals(tmp,  tstObj.getTargetValue());
    }

    @Test
    public void calFitnessTest(){
        tstObj.calFitness();
        assertEquals(tstObj.fitness, tstObj.targetValue);
    }

    @Test
    public void rosenbrockTest(){
        BigDecimal x1 = new BigDecimal(2.048);
        BigDecimal x2 = new BigDecimal(-2.048);
        assertTrue(RosenbrockIndividual.rosenbrock(x1,x2).intValue() == 3897);
        x1 = x2;
        assertEquals(3905,RosenbrockIndividual.rosenbrock(x1,x2).intValue());
    }

}

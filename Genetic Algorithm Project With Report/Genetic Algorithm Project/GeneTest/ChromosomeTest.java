
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChromosomeTest {
    Chromosome tstObj = new Chromosome(20);
    @Test
    public void setGeneTest(){
        assertTrue("failed", tstObj.setGene(0,19,"65446544654465446544"));
        assertTrue("failed", tstObj.setGene(0,19,"65465165146515124654"));
        assertTrue("failed", tstObj.setGene(0,19,"68849812056654984658"));
        assertTrue("failed", tstObj.setGene(0,19,"99999999999999999999"));
        assertTrue("failed", tstObj.setGene(0,19,"00000000000000000000"));
    }

    @Test
    public void getGeneTest(){
        tstObj.setGene(0,19,"00000000000000000000");
        assertEquals("00000000000000000000", tstObj.getGene(0,19));
        assertEquals("000000", tstObj.getGene(0,5));
        assertEquals("00000000000", tstObj.getGene(0,10));
        assertEquals("0000000000000000", tstObj.getGene(0,15));
        tstObj.setGene(0,19,"65446544654465446544");
        assertEquals("65446544654465446544", tstObj.getGene(0,19));
        assertEquals("65446", tstObj.getGene(0,4));
        assertEquals("46544", tstObj.getGene(15,19));
    }

    @Test
    public void cloneTest(){
        assertTrue(tstObj.clone() instanceof Chromosome);
        Chromosome tmp = (Chromosome) tstObj.clone();
        assertEquals(tstObj.getGene(0,5),tmp.getGene(0,5));
        assertEquals(tstObj.getGene(0,9),tmp.getGene(0,9));
        assertEquals(tstObj.getGene(0,15),tmp.getGene(0,15));
        assertEquals(tstObj.getGene(0,19),tmp.getGene(0,19));
    }
}

import java.math.BigDecimal;

public class Mutator implements Runnable {
    private Population pop;
    private int beginIndex;
    private int endIndex;

    public Mutator(Population pop, int beginIndex, int endIndex) {
        this.pop = pop;
        this.beginIndex = beginIndex;
        this.endIndex = Math.min(endIndex, pop.pop.length);
    }

    @Override
    public synchronized void run() {
        for (int i = beginIndex; i < endIndex; i++) {
            for(int j = 0 ;j < pop.getChromlen(); j++){
                if( Main.mutateRate.compareTo(new BigDecimal(Math.random())) > 0){
                    pop.pop[i].mutateSingleBit(j);
                }
            }
        }
    }
}

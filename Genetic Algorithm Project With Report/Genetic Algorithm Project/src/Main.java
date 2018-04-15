import java.io.*;
import java.math.BigDecimal;

class Main {
    public static final BigDecimal crossoverRate;//交叉概率
    public static final BigDecimal mutateRate;//变异概率
    public static final int generationNum;//进化代数
    public static final int groupSize;//群体大小

    static {
        generationNum = 100;
        groupSize = 500;
        crossoverRate = new BigDecimal(0.6);
        mutateRate = new BigDecimal(0.001);
    }

    public static void main(String[] args)throws IOException {

        File outPut = new File("result.txt");
        if(!outPut.exists()) outPut.createNewFile();
            FileWriter fw = new FileWriter(outPut);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            Population pop = new Population(groupSize);
            pop.initPopulation();

            pw.println("First Generation:\n" + pop);
            while(!pop.isEvolutionDone()){
                pop.evolve();
                pw.print("No." + pop.getGeneration() + "Best:" + pop.bestIndividual );
                pw.print("No." + pop.getGeneration()  + "current:" + pop.currentBest );
                pw.println("");
            }
            pw.println();
            pw.println("No"+ pop.getGeneration()  + "group:\n" + pop);

            pw.close();
            System.out.println("Finished");
    }
}


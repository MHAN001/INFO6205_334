import java.math.BigDecimal;
import java.math.RoundingMode;

class Population {
    protected int generation; //种群的代数
    private int size;            //群体大小
    protected Individual[] pop;   //public for the test
    private BigDecimal averageFitness;    //平均适应度
    private BigDecimal[] relativeFitness;    //相对适应度
    private int chromlen;//基因长度
    Individual bestIndividual;//当前代适应度最好的个体
    Individual worstIndividual;//当前代适应度最差的个体
    Individual currentBest;//到目前代为止最好的个体
    private int worstIndex;//bestIndividual对应的数组下标


    public Population(int size){
        this.generation = 0;
        this.size = size;

        this.pop = new Individual[size];
        this.averageFitness = new BigDecimal(0);
        this.relativeFitness = new BigDecimal[size];
        this.chromlen = 20;

        for(int i = 0; i < size; i++){
            pop[i] = new RosenbrockIndividual(chromlen);
        }
    }


    //Initialization
    public void initPopulation(){
        for(int i = 0;i < size;i++){
            pop[i].generateIndividual();
        }
        findBestAndWorstIndividual();
    }

    //----------------------------------------------------
    //比例选择
    public void  select(){
        BigDecimal[] rouletteWheel; //赌盘
        Individual[] childPop = new Individual[size];

        calRelativeFit();

        //产生赌盘
        rouletteWheel  = new BigDecimal[size];
        rouletteWheel[0] = relativeFitness[0];
        for(int i=1;i<size -1;i++){
            rouletteWheel[i] = relativeFitness[i].add(rouletteWheel[i - 1]);
        }
        rouletteWheel[size - 1] = new BigDecimal(1);

        //进行赌盘选择,产生新种群
        for(int i = 0;i < size ; i++){
            BigDecimal rnd = rand();
            for(int j = 0; j < size; j++){
                if(rnd.compareTo(rouletteWheel[j]) < 0 ){
                    childPop[i] = pop[j];
                    break;
                }
            }
        }

        for(int i = 0;i < size; i++){
            pop[i] = childPop[i];
        }

        //return     childPop;
    }

    //CalTotalFitness
    protected BigDecimal calTotalFit(){
        BigDecimal total = new BigDecimal(0);
        for(int i = 0 ; i < size ;i++)
            total  = total.add(pop[i].getFitness());
        return total;
    }

    //relativeFitness
    public BigDecimal[] calRelativeFit(){
        BigDecimal totalFitness = calTotalFit();
        for(int i = 0 ;i < size ; i++){
            relativeFitness[i] = pop[i].getFitness().divide(totalFitness, 20, RoundingMode.HALF_UP);
        }

        return relativeFitness;
    }

    //单点交叉
    public void crossover(){
        for(int i = 0 ; i < size/2*2; i+=2){
            int rnd;
            //random pair
            rnd = rand(i , size);

            if(rnd != i)
                exchange(pop , i , rnd);

            rnd = rand(i , size);
            if(rnd != i+1)
                exchange(pop , i + 1 , rnd);

            //交叉
            BigDecimal random = rand();

            if(random.compareTo(Main.crossoverRate) < 0 ){
                cross(i);
            }
        }
    }

    //crossing
    private void cross(int i){
        String chromFragment1,chromFragment2;//基因片段

        int rnd = rand(0 , getChromlen() - 1);//交叉点为rnd之后,可能的位置有chromlen - 1个.
        chromFragment1 = pop[i].getChrom(rnd + 1 , getChromlen() - 1);
        chromFragment2 = pop[i+1].getChrom(rnd + 1 , getChromlen() - 1);

        pop[i].setChrom(rnd + 1 , getChromlen() - 1 , chromFragment2);
        pop[i+1].setChrom(rnd + 1 , getChromlen() - 1 , chromFragment1);
    }

    // generate Random Number from start to end
    private int rand(int start , int end){
        return rand().multiply(new BigDecimal(end - start)).add(new BigDecimal(start)).intValue();
    }

    //exchange
    private void exchange(Individual[] p ,int src , int dest){
        Individual temp;
        temp = p[src];
        p[src] = p[dest];
        p[dest] = temp;
    }

    //mutating
    public void mutate(){
        int n = 8; // Number of threads
        Thread[] threads = new Thread[n];
        for (int i=0; i<n; i++)
        {
            int beginIndex = size / n * i;
            int endIndex = size / n * (i + 1);
            threads[i] = new Thread(new Mutator(this, beginIndex, endIndex));
            threads[i].start();
        }
        boolean anyAlive = true;
        while (anyAlive) {
            try {
                Thread.sleep(5);
                anyAlive = false;
                for (int i = 0; i < n; i++) {
                    if (threads[i].isAlive()) {
                        anyAlive = true;
                    }
                }
                if (!anyAlive) {
                    break;
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        for(int i = 0 ; i < size;i++){
            for(int j = 0 ;j < getChromlen(); j++){
                if( Main.mutateRate.compareTo(rand()) > 0){
                    pop[i].mutateSingleBit(j);
                    ///System.out.print("变异"+ i +" - "+ j + "  ");///
                }
            }
        }
    }

    //evolution
    public void evolve(){
        select();
        crossover();
        mutate();
        evaluate();
    }

    //get fittest offspring by replacing the worst one using best one
    public void evaluate(){
        //同步目标函数值和适应度
        for(int i = 0; i < size; i++){
            pop[i].calTargetValue();
            pop[i].calFitness();
        }

        //使用最优保存策略(Elitist Model)保存最优个体
        findBestAndWorstIndividual();
        pop[worstIndex] = (Individual)currentBest.clone();

        generation++;
    }
    //找出适应度最大的个体
    public void findBestAndWorstIndividual(){
        bestIndividual = worstIndividual = pop[0];
        for(int i = 1; i <size;i++){
            if(pop[i].fitness.compareTo(bestIndividual.fitness) > 0){
                bestIndividual = pop[i];
            }
            if(pop[i].fitness.compareTo(worstIndividual.fitness) < 0){
                worstIndividual = pop[i];
                worstIndex = i;
            }
        }

        if( generation == 0 ){//初始种群
            currentBest = (Individual)bestIndividual.clone();
        }else{
            if(bestIndividual.fitness.compareTo(currentBest.fitness) > 0)
                currentBest = (Individual)bestIndividual.clone();
        }
    }

    //判断进化是否完成
    public boolean isEvolutionDone(){
        if(generation < Main.generationNum)
            return false;
        return true;
    }

    //产生随机数
    private BigDecimal rand(){
        return new BigDecimal(Math.random());
    }

    public int getChromlen(){
        return chromlen;
    }

    public int getGeneration(){
        return generation;
    }

    public String toString(){
        StringBuffer str= new StringBuffer();
        for(int i = 0 ; i < size ; i++)
            str = str.append(pop[i]);
        return str.toString();
    }

}

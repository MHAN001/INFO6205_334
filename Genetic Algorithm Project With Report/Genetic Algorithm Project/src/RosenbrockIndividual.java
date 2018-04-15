import java.math.BigDecimal;
import java.math.RoundingMode;

class RosenbrockIndividual extends Individual {
    private BigDecimal x1 , x2; // 个体表现型
    //基因型chromosome由 (x1 , x2)编码而成

    RosenbrockIndividual(int chromlen){
        genelen = 10;
        chrom = new Chromosome(chromlen);
    }

    //编码
    public void coding(){
        chrom.setGene(0 , 9 , codingVariable(x1));
        chrom.setGene(10, 19 , codingVariable(x2));
    }

    //解码
    public void decode(){
        String gene1,gene2;

        gene1 = chrom.getGene(0 , 9);
        gene2 = chrom.getGene(10 , 19);

        x1 = decodeGene(gene1);
        x2 = decodeGene(gene2);
    }

    //计算目标函数值
    public  void calTargetValue(){
        decode();
        targetValue = rosenbrock(x1 , x2);
    }

    //计算个体适应度
    public void calFitness(){
        fitness = getTargetValue();
    }

    private String codingVariable(BigDecimal x){
        BigDecimal y = x.add(new BigDecimal(2.048))
                .multiply(new BigDecimal(1023)).divide(new BigDecimal(4.096), 8, RoundingMode.HALF_UP);

        String code = Integer.toBinaryString(y.intValue());

        StringBuffer codeBuf = new StringBuffer(code);
        for(int i = code.length(); i<genelen; i++)
            codeBuf.insert(0,'0');

        return codeBuf.toString();
    }

    private BigDecimal decodeGene(String gene){
        int value ;
        BigDecimal decode;
        value = Integer.parseInt(gene, 2);
        decode = new BigDecimal(value/1023.0*4.096 - 2.048);

        return decode;
    }

    public String toString(){
        StringBuffer str = new StringBuffer();
        str = str.append(rosenbrock(x1 , x2)).append("\n");
        return str.toString();
    }

    /**
     *Rosenbrock函数:
     *f(x1,x2) = 100*(x1**2 - x2)**2 + (1 - x1)**2
     *在当x在[-2.048 , 2.048]内时，
     *函数有两个极大点:
     *f(2.048 , -2.048) = 3897.7342
     *f(-2.048,-2.048) = 3905.926227
     *其中后者为全局最大点。
     *
     */
    public static BigDecimal rosenbrock(BigDecimal x1 , BigDecimal x2){
        BigDecimal fun = new BigDecimal(100*Math.pow(x1.multiply(x1).subtract(x2).doubleValue() , 2)).
                   add(new BigDecimal(Math.pow(x1.negate().add(new BigDecimal(1)).doubleValue() , 2)));

        return fun;
    }

    //随机产生群体
    public void generateIndividual(){
            x1 = new BigDecimal(Math.random() * 4.096 - 2.048);
            x2 = new BigDecimal(Math.random() * 4.096 - 2.048);
            //同步编码和适应度
            coding();
            calTargetValue();
            calFitness();


    }
}

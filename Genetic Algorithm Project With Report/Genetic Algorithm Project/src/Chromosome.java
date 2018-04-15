public class Chromosome implements Cloneable{
    private StringBuffer chromosome;
    private char defaultChar; //默认基因填充字符

    public  Chromosome(int chromosomeLength){
        chromosome = new StringBuffer(chromosomeLength);
        chromosome.setLength(chromosomeLength);
        defaultChar = '0';
//        this.chromosomeLength = chromosomeLength;
    }
    public boolean setGene(int start , int end , String gene){
        int len = gene.length();
        if(len > end - start + 1)
            return false;

        //index => chromosome , idx => gene
        for (int index = start , idx = 0; index <= end; index++ , idx++) {
            if(idx < len)
                chromosome.setCharAt(index , gene.charAt(idx));
            else
                chromosome.setCharAt(index , defaultChar);
        }

        return true;
    }
    public String getGene(int begin , int end){
        char[] dest = new char[end - begin + 1];
        chromosome.getChars(begin , end + 1 , dest , 0);

        return new String(dest);
    }

    public String toString(){
        return chromosome.toString();
    }
    @Override
    public Object clone() {
        Chromosome c = null;
        try{
            c = (Chromosome)super.clone();
            c.chromosome = new StringBuffer(chromosome);
        }catch(CloneNotSupportedException e ){
            System.out.println(e.getMessage());
        }
        return c;
    }
}

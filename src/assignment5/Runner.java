/* CRITTERS Runner.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * Vinay Shah
 * vss452
 * 16205
 * Vignesh Ravi
 * vgr325
 * 16225
 * Slip days used: <0>
 * Spring 2019
 */

/*
 * The Runner critter has one purpose in life: to run to the
 * edges of the world until it dies. It is incredibly terrible at
 * conflict resolution as it runs from all fights in life. As a result,
 * Runners often live very sad, lonely lives. They are unable to reproduce because of this.
 * */



package assignment5;

public class Runner extends Critter {
    @Override
    /**
     * Returns String representation of Runner
     */
    public String toString() { return "R"; }

    private static final int GENE_TOTAL = 24;
    private int[] genes = new int[8];
    private int dir;

    public Runner() {
        for (int k = 0; k < 8; k += 1) {
            genes[k] = GENE_TOTAL / 8;
        }
        dir = Critter.getRandomInt(8);
    }

    @Override
    /**
     * Runs away when confronted in a fight
     * @return false
     */
    public boolean fight(String opponent) {
        run(getRandomInt(8));
        return false;
    }


    @Override
    /**
     * Simulates timestep for Runner, chooses a random direction to run in
     */
    public void doTimeStep() {
        run(dir);

        /* pick a new direction based on our genes */
        int roll = Critter.getRandomInt(GENE_TOTAL);
        int turn = 0;
        while (genes[turn] <= roll) {
            roll = roll - genes[turn];
            turn = turn + 1;
        }
        assert(turn < 8);

        dir = (dir + turn) % 8;
    }

    /**
     * returns the stats for the Runners class
     * @param runners
     */
    public static String runStats(java.util.List<Critter> runners) {
        int total_straight = 0;
        int total_left = 0;
        int total_right = 0;
        int total_back = 0;
        for (Object obj : runners) {
            Runner r = (Runner) obj;
            total_straight += r.genes[0];
            total_right += r.genes[1] + r.genes[2] + r.genes[3];
            total_back += r.genes[4];
            total_left += r.genes[5] + r.genes[6] + r.genes[7];
        }
        String s1 = ("" + runners.size() + " total Runners    ");
        String s2 = ("" + total_straight / (GENE_TOTAL * 0.01 * runners.size()) + "% straight   ");
        String s3 = ("" + total_back / (GENE_TOTAL * 0.01 * runners.size()) + "% back   ");
        String s4 = ("" + total_right / (GENE_TOTAL * 0.01 * runners.size()) + "% right   ");
        String s5 = ("" + total_left / (GENE_TOTAL * 0.01 * runners.size()) + "% left   ");
        
        return s1 + s2 + s3 + s4 + s5;
    }

	@Override
	public CritterShape viewShape() {
		// TODO Auto-generated method stub
		return null;
	}
}

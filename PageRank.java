//******************************************************************************
//
// File:    PageRank.java
//
//******************************************************************************


import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import static edu.rit.pj2.Task.dynamic;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
* This class is used to execute the parallel version to calculate the Page rank of the graph
*
* @author  Arjun Nair (an3395)
* @author  Aditya Advani (aa5394)
* @version 10-Dec-2015
*/

public class PageRank extends Task {

    int n;

    /**
     * Main program
     *
     * @param args
     * @throws java.lang.Exception
     */
    
    public void main(String[] args) {

        // Parse command line arguments.
        if (args.length != 2) usage();

        // number of nodes in the graph
        n = Integer.parseInt(args[1]);


        // variables used for checking convergence of the page rank scores for every node
        int z;
        boolean flag = true;


        // data structure to store the sparse matrix
        // outer arraylist represents the nodes
        // inner arraylist represents the outlinks from the respective node 
        final ArrayList<ArrayList<sparseMatrix>> list = readFile(args[0]);

        //Global reduction variable 
        final PageRankVbl gPRV = new PageRankVbl(n);

        //while convergence criteria is not satisfiyed
        while (flag) {

            parallelFor(0, n - 1).schedule(dynamic).exec(new Loop() {
                PageRankVbl local;

                public void start() {
                    local = threadLocal(gPRV);
                }

  /**
    * Run method is to calculate the page rank in different threads and store it in one array.
    *
    * @param  int i
    *
    * @return null
    */                public void run(int i) throws Exception {

                    local.calculate(i, list.get(i));
                }
            });


            z = 0;
            
		 //check convergence
		 z = converge(gPRV,z);
            
		 //if convergence has been reached, break loop
            if (z == n) {
                flag = false;
                break;
            }

		 // re-initialize array holding page rank scores of the current round
            gPRV.clearCurrent(n);
        }

        //print final page rank scores for every node
        System.out.println("PageRank Scores:");
        for (int i = 0; i < n; i++) {
            System.out.print("PageRank score for node " + (i + 1) + ": \t");
            System.out.printf("%.10g", gPRV.current[i]);
            System.out.println();
        }

    }


    /**
     * Check for convergence
     * @param  gPRV [Object]
     * @param  z    [descrntiption]
     * @return      [int]
     */
    public int converge(PageRankVbl gPRV,int z){
        for (int i = 0; i < n; i++) {
                if (Math.abs(gPRV.prev[i] - gPRV.current[i]) < 0.000001) {
                    z++;
                }
            }

            for (int i = 0; i < n; i++) {
                gPRV.prev[i] = gPRV.current[i];
            }
            return z;   
    }

 /**
   * Reads the input file and stores it in a list
   * @param  file          [Name of the file containing the sparse matrix]
   * @exception  Exception
   * @return [ArrayList of ArrayList containing the sparse matrix]
   */
  
  
    public ArrayList<ArrayList<sparseMatrix>> readFile(String file) {
        String line;
        ArrayList<ArrayList<sparseMatrix>> list = new ArrayList<ArrayList<sparseMatrix>>();
        list.add(new ArrayList<sparseMatrix>());
        try {
            try (Scanner sc = new Scanner(new File(file))) {
                while (sc.hasNext()) {
                    line = sc.nextLine();
                    String split[] = line.split("\\s+");
                    int x = Integer.parseInt(split[0]);
                    int y = Integer.parseInt(split[1]);
                    double z = Double.parseDouble(split[2]);
                    if (list.size() - x > 0) {
                        //same size
                        list.get(list.size() - 1).add(new sparseMatrix(x, y, z));
                    } else {
                        //increment size
                        list.add(new ArrayList<sparseMatrix>());
                        list.get(list.size() - 1).add(new sparseMatrix(x, y, z));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

     /**
      * Print a usage message and exit.
      */
     private static void usage()
     {
      System.err.println ("Invalid Arguments");
      throw new IllegalArgumentException();
    }

}

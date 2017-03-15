//******************************************************************************
//
// File:    PageRankVbl.java
//
//******************************************************************************


import edu.rit.pj2.Vbl;
import java.util.ArrayList;

/**
 * Class PageRankVbl provides a reduction variable for PageRank
 * shared by many threads.
 *
 * @author  Arjun Nair (an3395)
 * @author  Aditya Advani (aa5394)
 * @version 10-Dec-2015
 */
public class PageRankVbl implements Vbl {


    
    double[] current;
    double[] prev;
    double a;
    double vcalc;
    int nodenumber;
    int allnodes;

    /**
     * Constructore to initialize variables and calcuate a value
     * @param  n [number of nodes]
     */

    public PageRankVbl(int n) {
        allnodes = n;
        current = new double[n];
        prev = new double[n];
        a = 0.85;
        vcalc = (double) (1 / (double) n);

        for (int i = 0; i < n; i++) {
            current[i] = vcalc;
        }

        vcalc *= (1 - a);
    }

    /**
    * Create a clone of this shared variable.
    *
    * @return  The cloned object.
    * 
    * @exception  CloneNotSupportedException
    *     Thrown if the Clone is not
    *      supported.
    */
   
    public Object clone() {
        Object vbl = null;
        try {
            vbl = super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("error in cloning\n" + e);
        }
        return vbl;
    }

    /**
    * Set these shared variables to the given shared variables.
    *
    * @param  vbl  Shared variable.
    *
    */
    public void set(Vbl vbl) {
        this.current    = (((PageRankVbl) vbl).current);
        this.prev       = (((PageRankVbl) vbl).prev);
        this.a          = (((PageRankVbl) vbl).a);
        this.vcalc      = (((PageRankVbl) vbl).vcalc);
        this.nodenumber = (((PageRankVbl) vbl).nodenumber);
    }
    
    /**
    * Reduce the shared variables into the shared variables of this class. 
    * <P>
    *
    * @param  vbl  Shared variable.
    *
    */
   
    public void reduce(Vbl vbl) {
        PageRankVbl a = (PageRankVbl) vbl;
    if(this.current[nodenumber] == 0)
        this.current[nodenumber] = a.current[nodenumber];
    
    }


    /**
     * Reinitialize the value to 0
     * @param n [number of nodes]
     */
    public void clearCurrent(int n) {
        for (int i = 0; i < n; i++) {
            current[i] = 0;
        }
    }


/**
 * Calculate the page rank
 * @param k     [thread number]
 * @param nodes [Sparse matrix at k]
 */
    public void calculate(int k, ArrayList<sparseMatrix> nodes) {
        
	int xTemp=0,yTemp=0;
        for (sparseMatrix node : nodes) {
            xTemp = node.x;
            yTemp = node.y;
            double valueTemp = a * node.value;
            current[xTemp] += valueTemp * prev[yTemp];

        }
	nodenumber = xTemp;
	current[xTemp] += vcalc;
    }
}

//******************************************************************************
//
// File:    sparseMatrix.java
//
//******************************************************************************

/**
* This class is used to store the sparse matrix
*
* @author  Arjun Nair (an3395)
* @author  Aditya Advani (aa5394)
* @version 10-Dec-2015
*/
public class sparseMatrix {
	int x;
	int y;
	double value;
	/**
	 * Constructor
	 * @param  x     [x index]
	 * @param  y     [y index]
	 * @param  value 
	 */
	public sparseMatrix(int x,int y,double value) {
		this.x = x;
		this.y = y;
		this.value = value;	
	}

	public sparseMatrix(){}
}

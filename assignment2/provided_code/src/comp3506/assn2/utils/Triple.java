package comp3506.assn2.utils;


/**
 * Simple tuple representing a triple of values.
 * 
 * @author Richard Thomas <richard.thomas@uq.edu.au>
 *
 * @param <L> Type of the first (left) value.
 * @param <C> Type of the second (centre) value.
 * @param <R> Type of the third (right) value.
 */
public class Triple<L, C, R> extends Pair<L, R> {
	
	private C centreValue;

	public Triple(L leftValue, C centreValue, R rightValue) {
		super(leftValue, rightValue);
		this.centreValue = centreValue;
	}

	public C getCentreValue() {
		return centreValue;
	}

	public void setCentreValue(C centreValue) {
		this.centreValue = centreValue;
	}

}

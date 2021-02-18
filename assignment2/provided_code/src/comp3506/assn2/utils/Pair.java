package comp3506.assn2.utils;


/**
 * Simple tuple representing a pair of values.
 * 
 * @author Richard Thomas <richard.thomas@uq.edu.au>
 *
 * @param <L> Type of the first (left) value.
 * @param <R> Type of the second (right) value.
 */
public class Pair<L, R> {
	
	private L leftValue;
	private R rightValue;

	public Pair(L leftValue, R rightValue) {
		this.leftValue = leftValue;
		this.rightValue = rightValue;
	}

	public L getLeftValue() {
		return leftValue;
	}

	public void setLeftValue(L leftValue) {
		this.leftValue = leftValue;
	}

	public R getRightValue() {
		return rightValue;
	}

	public void setRightValue(R rightValue) {
		this.rightValue = rightValue;
	}

}

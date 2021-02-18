package comp3506.assn2.utils;


import java.util.Objects;

import comp3506.assn2.utils.Pair;


public class TestingPair<L, R> extends Pair<L, R> {
	
	public TestingPair(L leftValue, R rightValue) {
		super(leftValue, rightValue);
	}
	
	public TestingPair(Pair<L, R> pair) {
		super(pair.getLeftValue(), pair.getRightValue());
	}
	
	@Override
	public boolean equals(Object o) {
	    // self check
	    if (this == o)
	        return true;
	    // null check
	    if (o == null)
	        return false;
	    // type check and cast
	    if (getClass() != o.getClass())
	        return false;
	    TestingPair<L, R> otherPair = (TestingPair<L, R>) o;
	    // field comparison
	    return Objects.equals(getLeftValue(), otherPair.getLeftValue())
	           && Objects.equals(getRightValue(), otherPair.getRightValue());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getLeftValue(), getRightValue());
	}

}

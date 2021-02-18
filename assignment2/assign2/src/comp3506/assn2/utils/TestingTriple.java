package comp3506.assn2.utils;


import java.util.Objects;


public class TestingTriple<L, C, R> extends Triple<L, C, R> {
	
	public TestingTriple(L leftValue, C centreValue, R rightValue) {
		super(leftValue, centreValue, rightValue);
	}
	
	public TestingTriple(Triple<L, C, R> triple) {
		super(triple.getLeftValue(), triple.getCentreValue(), triple.getRightValue());
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
	    TestingTriple<L, C, R> otherPair = (TestingTriple<L, C, R>) o;
	    // field comparison
	    return Objects.equals(getLeftValue(), otherPair.getLeftValue())
	    	   && Objects.equals(getCentreValue(), otherPair.getCentreValue())
	           && Objects.equals(getRightValue(), otherPair.getRightValue());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getLeftValue(), getCentreValue(), getRightValue());
	}

}

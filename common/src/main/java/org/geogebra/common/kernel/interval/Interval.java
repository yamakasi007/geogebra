package org.geogebra.common.kernel.interval;

import static org.geogebra.common.kernel.arithmetic.MyDouble.isFinite;
import static org.geogebra.common.kernel.interval.IntervalConstants.PI_TWICE_LOW;
import static org.geogebra.common.kernel.interval.IntervalConstants.WHOLE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.geogebra.common.util.DoubleUtil;

/**
 * Class to implement interval arithmetic
 *
 */
public class Interval {
	private final IntervalAlgebra algebra = new IntervalAlgebra(this);
	private final IntervalTrigonometric trigonometric = new IntervalTrigonometric(this);
	private double low;
	private double high;

	/**
	 * Creates a singleton interval [value, value]
	 *
	 * @param value for the singleton interval.
	 */
	public Interval(double value) {
		this(value, value);
	}

	/**
	 * Creates an empty interval.
	 */
	public Interval() {
		setEmpty();
	}

	/**
	 * Copy constructor
	 *
	 * @param other to copy.
	 */
	public Interval(Interval other) {
		this(other.low, other.high);
	}

	/** Empty interval is represented by [∞, -∞]
	 * as in the original lib. */
	void setEmpty() {
		set(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
	}

	/**
	 * Creates an interval with bounds [low, high]
	 *
	 * @param low lower bound of the interval
	 * @param high higher bound of the interval
	 */
	public Interval(double low, double high) {
		if (high < low) {
			setEmpty();
		} else {
			set(low, high);
		}
	}

	/**
	 * Interval addition
	 *
	 * @param other interval to add
	 * @return this as result
	 */
	public Interval add(Interval other) {
		low += other.low;
		high += other.high;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Interval interval = (Interval) o;
		return Double.compare(interval.low, low) == 0
				&& Double.compare(interval.high, high) == 0;
	}

	@Override
	public String toString() {
		String result = "Interval [";
		if (!isEmpty()) {
			result += low;
			if (!isSingleton()) {
				result += ", " + high;
			}
		}

		result += "]";
		return result;
	}

	/**
	 * Interval subtraction
	 *
	 * @param other to subtract from this interval
	 * @return this as result.
	 */
	public Interval subtract(Interval other) {
		low -= other.high;
		high -= other.low;
		return this;
	}

	/**
	 * Interval multiplication
	 *
	 * @param other to multiply this interval with.
	 * @return this as result.
	 */
	public Interval multiply(Interval other) {
		getBoundsFromList(Arrays.asList(low * other.low, low * other.high, high * other.low,
				high * other.high));
		return this;
	}

	protected void getBoundsFromList(List<Double> list) {
		Collections.sort(list);

		// to avoid -0.0, see testMultiplication5
		low = list.get(0) + 0.0;
		high = list.get(3) + 0.0;
	}

	/**
	 * Interval division
	 *
	 * @param other to divide this interval with.
	 * @return this as result.
	 */
	public Interval divide(Interval other) throws IntervalDivisionByZero {
		if (other.hasZero()) {
			throw new IntervalDivisionByZero();
		}
		getBoundsFromList(Arrays.asList(low / other.low, low / other.high, high / other.low,
				high / other.high));
		return this;
	}

	/**
	 *
	 * @return if interval has zero in it.
	 */
	public boolean hasZero() {
		return low <= 0 && high >= 0;
	}

	/**
	 *
	 * @return if interval represents all the real numbers R.
	 */
	public boolean isWhole() {
		return low == Double.NEGATIVE_INFINITY && high == Double.POSITIVE_INFINITY;
	}

	/**
	 *
	 * @return if interval is in the form [n, n] where n is finite.
	 */
	public boolean isSingleton() {
		return isFinite(low) && DoubleUtil.isEqual(high, low, 1E-7);
	}

	@Override
	public int hashCode() {
		return Objects.hash(low, high);
	}

	/**
	 *
	 * @return if interval is empty.
	 */
	public boolean isEmpty() {
		return  low > high;
	}

	/**
	 *
	 * @return lower bound.
	 */
	public double getLow() {
		return low;
	}

	/**
	 *
	 * @return upper bound.
	 */
	public double getHigh() {
		return high;
	}

	/**
	 *
	 * @param other interval to check.
	 * @return if intervals are overlapping
	 */
	public boolean isOverlap(Interval other) {
		if (isEmpty() || other.isEmpty()) {
			return false;
		}
		return (low <= other.low && other.low <= high)
				|| (other.low <= low && low <= other.high);
	}

	/**
	 * Computes x mod y (x - k * y)
	 *
	 * @param other argument.
	 * @return this as result
	 */
	public Interval fmod(Interval other) {
		algebra.fmod(other);
		return this;
	}

	/**
	 *
	 * @param other to compare
	 * @return if the other interval is equal with a precision
	 */
	public boolean almostEqual(Interval other) {
		return DoubleUtil.isEqual(low, other.low, 1E-7)
			&& DoubleUtil.isEqual(high, other.high, 1E-7);
	}

	/**
	 *  Computes "1 / x"
	 * @return this as result.
	 */
	public Interval multiplicativeInverse() {
		if (isEmpty()) {
			return IntervalConstants.EMPTY;
		}

		if (hasZero()) {
			if (low != 0) {
				if (high != 0) {
					// [negative, positive]
					setWhole();
				} else {
					// [negative, zero]
					double d = low;
					low = Double.NEGATIVE_INFINITY;
					high = RMath.divHigh(1.0, d);
				}
			} else {
				if (high != 0) {
					// [zero, positive]
					low = RMath.divLow(1, high);
					high = Double.POSITIVE_INFINITY;
				} else {
					// [zero, zero]
					setEmpty();
				}
			}
		} else {
			// [positive, positive]
			return new Interval(RMath.divLow(1, high), RMath.divHigh(1, low));
		}
		return this;
	}

	void setWhole() {
		set(WHOLE.low, WHOLE.high);
	}

	/**
	 *
	 * @param power of the interval
	 * @return power of the interval
	 */
	public Interval pow(int power) {
		return algebra.pow(power);
	}

	/**
	 * Sets the closed interval bounds as the other interval
	 * @param other interval to set bounds as the same.
	 */
	void set(Interval other) {
		set(other.low, other.high);
	}

	/**
	 * Sets the closed interval bounds.
	 *
	 * @param low lower bound.
	 * @param high higher bound.
	 */
	void set(double low, double high) {
		this.low = low;
		this.high = high;
	}

	/**
	 * Power of an interval where power is also an interval
	 * that must be a singleton, ie [n, n]
	 *
	 * @param other interval power.
	 * @return this as result.
	 * @throws PowerIsNotInteger if other is not a singleton interval.
	 */
	public Interval pow(Interval other) throws PowerIsNotInteger {
		return algebra.pow(other);
	}

	/**
	 * [a, b] -> (a, b]
	 * @return this as result
	 */
	public Interval halfOpenLeft() {
		low = RMath.next(low);
		return this;
	}

	/**
	 * [a, b] -> [a, b)
	 * @return this as result
	 */
	public Interval halfOpenRight() {
		high = RMath.prev(high);
		return this;
	}

	/**
	 *
	 * @return square root of the interval.
	 */
	public Interval sqrt() {
		return algebra.sqrt();
	}

	/**
	 * Computes the nth root of the interval
	 * if other (=n) is a singleton
	 *
	 * @param other interval
	 * @return nth root of the interval.
	 */
	public Interval nthRoot(Interval other) {
		return algebra.nthRoot(other);
	}

	/**
	 * Computes x^(1/n)
	 *
	 * @param n the root
	 * @return nth root of the interval.
	 */
	public Interval nthRoot(double n) {
		return algebra.nthRoot(n);
	}

	public Interval cos() {
		return trigonometric.cos();
	}

	/**
	 * Checks if the interval is
	 * either [-∞, -∞] or [∞, ∞].
	 *
	 * @return true if infinite.
	 */
	public boolean isOnlyInfinity() {
		return Double.isInfinite(low) && low == high;
	}

	public double getWidth() {
		if (isEmpty()) {
			return 0;
		}
		return RMath.subHigh(high, low);
	}

	/**
	 * "Invert" the interval
	 */
	public void negative() {
		set(-high, -low);
	}

	public void handleNegative() {
		if (low < 0) {
			if (low == Double.NEGATIVE_INFINITY) {
				set(0, Double.POSITIVE_INFINITY);
			} else {
      			double n = Math.ceil(-low / PI_TWICE_LOW);
				set(low + PI_TWICE_LOW * n,
					high + PI_TWICE_LOW * n);
			}
		}
	}

	/**
	 *
	 * @return the bounds as array
	 */
	public double[] toArray() {
		return new double[] {low, high};
	}

	/**
	 *
	 * @return sine of the interval.
	 */
	public Interval sin() {
		return trigonometric.sin();
	}

	/**
	 *
	 * @return tangent of the interval.
	 */
	public Interval tan() {
		return trigonometric.tan();
	}

	/**
	 *
	 * @return arc sine of the interval
	 */
	public Interval asin() {
		return trigonometric.asin();
	}

	/**
	 *
	 * @return arc cosine of the interval
	 */
	public Interval acos() {
		return trigonometric.acos();
	}

	/**
	 *
	 * @return arc tangent of the interval
	 */
	public Interval atan() {
		return trigonometric.atan();
	}

	/**
	 *
	 * @return hyperbolic sine of the interval
	 */
	public Interval sinh() {
		return trigonometric.sinh();
	}

	/**
	 *
	 * @return hyperbolic cosine of the interval
	 */
	public Interval cosh() {
		return trigonometric.cosh();
	}
}

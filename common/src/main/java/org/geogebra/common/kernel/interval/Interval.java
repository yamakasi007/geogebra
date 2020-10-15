package org.geogebra.common.kernel.interval;

import static org.geogebra.common.kernel.arithmetic.MyDouble.isFinite;
import static org.geogebra.common.kernel.interval.RMath.powHi;
import static org.geogebra.common.kernel.interval.RMath.powLo;

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
	 * Creates an interval with bounds [low, high]
	 *
	 * @param low lower bound of the interval
	 * @param high higher bound of the interval
	 */
	public Interval(double low, double high) {
		if (high < low) {
			setEmpty();
		} else {
			this.low = low;
			this.high = high;
		}
	}

	/** Empty interval is represented by [+∞, -∞]
	 * as in the original lib. */
	private void setEmpty() {
		low = Double.POSITIVE_INFINITY;
		high = Double.NEGATIVE_INFINITY;
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
		return isFinite(low) && DoubleUtil.isEqual(high, low);
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
		return low == Double.POSITIVE_INFINITY && high == Double.NEGATIVE_INFINITY;
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
		if (isEmpty() || other.isEmpty()) {
			return IntervalConstants.EMPTY;
		}

		double yb = low < 0 ? other.low : other.high;
		double n = low / yb;
		if (n < 0) {
			n = Math.ceil(n);
		} else {
			n = Math.floor(n);
		}
		// x mod y = x - n * y
		subtract(other.multiply(new Interval(n)));
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
					high = RMath.divHi(1.0, d);
				}
			} else {
				if (high != 0) {
					// [zero, positive]
					low = RMath.divLo(1, high);
					high = Double.POSITIVE_INFINITY;
				} else {
					// [zero, zero]
					setEmpty();
				}
			}
		} else {
			// [positive, positive]
			return new Interval(RMath.divLo(1, high), RMath.divHi(1, low));
		}
		return this;
	}

	private void setWhole() {
		low = IntervalConstants.WHOLE.low;
		high = IntervalConstants.WHOLE.high;
	}

	public Interval pow(int power) {
		if (isEmpty()) {
			return this;
		}

		if (power == 0) {
			return powerOfZero();
		} else if (power < 0) {
			set(multiplicativeInverse().pow(-power));
			return this;
		}

		return powOfInteger(power);
	}

	private void set(Interval other) {
		set(other.low, other.high);
	}

	private Interval powOfInteger(int power) {
		if (high < 0) {
			// [negative, negative]
			// assume that power is even so the operation will yield a positive interval
			// if not then just switch the sign and order of the interval bounds
      		double yl = powLo(-high, power);
      		double yh = powHi(-low, power);
			if ((power & 1) == 1) {
				// odd power
				set(-yh, -yl);
			} else {
				// even power
				set(yl, yh);
			}
		} else if (low < 0) {
			// [negative, positive]
			if ((power & 1) == 1) {
				set(-powLo(-low, power), powHi(high, power));
			} else {
				// even power means that any negative number will be zero (min value = 0)
				// and the max value will be the max of x.lo^power, x.hi^power
				set(0, powHi(Math.max(-low, high), power));
			}
		} else {
			// [positive, positive]
			set(powLo(low, power), powHi(high, power));
		}
		return this;
	}

	private void set(double low, double high) {
		this.low = low;
		this.high = high;
	}

	protected Interval powerOfZero() {
		if (low == 0 && high == 0) {
			// 0^0
			setEmpty();
			return this;
		} else {
			// x^0
			set(1, 1);
			return this;
		}
	}

	public Interval pow(Interval other) throws PowerIsNotInteger {
		if (!other.isSingleton()) {
			setEmpty();
			return this;
		}

		if (!DoubleUtil.isInteger(other.low)) {
			throw new PowerIsNotInteger();
		}
		return pow((int)other.low);
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

	public Interval sqrt() {
		return nthRoot(2);
	}

	public Interval nthRoot(Interval other) {
		if (!other.isSingleton()) {
			setEmpty();
			return this;
		}
		return nthRoot(other.low);
	}

	public Interval nthRoot(double n) {
		if (isEmpty() || n < 1) {
			setEmpty();
			return this;
		}
		double power = 1 / n;
		if (high < 0) {
			if (DoubleUtil.isInteger(n) && ((int) n & 1) == 1) {
				set(powHi(-low, power), powLo(-high, power));
				return this;
			}
			setEmpty();
			return this;
		} else if (low < 0) {
			double yp = powHi(high, power);
			if (DoubleUtil.isInteger(n) && ((int) n & 1) == 1) {
				double yn = -powHi(-low, power);
				set(yn, yp);
				return this;
			}
			set(0, yp);
			return this;
		} else {
			set(powLo(low, power), powHi(high, power));
		}
		return this;

	}
}

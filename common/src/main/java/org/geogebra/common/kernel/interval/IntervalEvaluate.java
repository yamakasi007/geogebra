package org.geogebra.common.kernel.interval;

import org.geogebra.common.plugin.Operation;

public class IntervalEvaluate {
	private final Interval interval;

	public IntervalEvaluate(Interval interval) {
		this.interval = interval;
	}

	public Interval evaluate(Operation operation, Interval other)
			throws Exception {
		switch (operation) {
		case PLUS:
			return interval.add(other);
		case MINUS:
			return interval.subtract(other);
		case MULTIPLY:
			return interval.multiply(other);
		case DIVIDE:
			return interval.divide(other);
		case POWER:
			return interval.pow(other);
		case NROOT:
			return interval.nthRoot(other);
		case INTEGRAL:
			break;
		case INVERSE_NORMAL:
			break;
		case DIFF:
			return interval.difference(other);
		}
		return interval;
	}

	public Interval evaluate(Operation operation)
			throws Exception {
		switch (operation) {
		case COS:
			return interval.cos();
		case SIN:
			return interval.sin();
		case TAN:
			return interval.tan();
		case EXP:
			return interval.exp();
		case LOG:
			return interval.log();
		case ARCCOS:
			return interval.acos();
		case ARCSIN:
			return interval.asin();
		case ARCTAN:
			return interval.atan();
		case SQRT:
			return interval.sqrt();
		case SQRT_SHORT:
			break;
		case ABS:
			return interval.abs();
		case COSH:
			return interval.cosh();
		case SINH:
			return interval.sinh();
		case TANH:
			return interval.tanh();
		case ACOSH:
			return interval.acos();
		case LOG10:
			return interval.log10();
		case LOG2:
			return interval.log2();
		case INTEGRAL:
			break;
		case INVERSE_NORMAL:
			break;
		}
		return interval;
	}
}

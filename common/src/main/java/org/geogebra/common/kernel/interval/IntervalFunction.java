package org.geogebra.common.kernel.interval;

import org.geogebra.common.kernel.arithmetic.ExpressionNode;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.plugin.Operation;

public class IntervalFunction {
	private final GeoFunction function;

	public IntervalFunction(GeoFunction function) {
		this.function = function;
	}

	public Interval evaluate(Interval x) throws Exception {
		return evaluateInterval(new Interval(x),
				function.getFunctionExpression());
	}

	public Interval evaluateInterval(Interval x, ExpressionNode node) throws Exception {
		Operation operation = node.getOperation();
		if (node.isLeaf()) {
			if (node.isConstant()) {
				return new Interval(node.evaluateDouble());
			}
			return x.evaluate(operation);
		}
		ExpressionNode leftTree = node.getLeftTree();
		ExpressionNode rightTree = node.getRightTree();
		Interval left = evaluateInterval(x, leftTree);
		Interval right = evaluateInterval(x, rightTree);
		switch (operation) {
		case PLUS:
			return left.add(right);
		case MINUS:
			return left.subtract(right);
		case MULTIPLY:
			return left.multiply(right);
		case DIVIDE:
			return left.divide(right);
		case POWER:
			return x.pow(rightTree.evaluateDouble());
		case SIN:
			return left.sin();
		case COS:
			return left.cos();
		case SQRT:
			return left.sqrt();
		default:
			return IntervalConstants.empty();
		}
	}
}
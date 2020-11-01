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
		return evaluate(new Interval(x),
				function.getFunctionExpression());
	}

	private Interval evaluate(Interval x, ExpressionNode node) throws Exception {
		Operation operation = node.getOperation();
		if (operation == Operation.NO_OPERATION) {
			if (node.isConstant()) {
				return new Interval(node.evaluateDouble());
			}
			return x;
		}
		if (Operation.isSimpleFunction(operation)) {
			return x.evaluate(operation);
		} else {
			return evaluate(x, node.getLeftTree(), operation, node.getRightTree());
		}
	}

	private Interval evaluate(Interval x, ExpressionNode left,
			Operation operation, ExpressionNode right)
			throws Exception {
		Interval leftInterval = evaluate(new Interval(x), left);
		Interval rightInterval = evaluate(new Interval(x), right);
		return leftInterval.evaluate(operation, rightInterval);
	}
}
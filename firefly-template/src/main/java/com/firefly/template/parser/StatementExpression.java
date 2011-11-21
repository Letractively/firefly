package com.firefly.template.parser;

import static com.firefly.template.support.RPNUtils.Type.*;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import com.firefly.template.exception.ExpressionError;
import com.firefly.template.support.RPNUtils;
import com.firefly.template.support.RPNUtils.Fragment;

public class StatementExpression implements Statement {

	@Override
	public void parse(String content, JavaFileBuilder javaFileBuilder) {

	}

	public String parse(String content) {
		List<Fragment> list = RPNUtils.getReversePolishNotation(content);
		Deque<Fragment> d = new LinkedList<Fragment>();
		for (Fragment f : list) {
			if (isSymbol(f.type)) {
				Fragment right = d.pop();
				Fragment left = d.pop();

				Fragment ret = new Fragment();
				switch (f.type) {
				case ARITHMETIC_OPERATOR:
					if (left.type == STRING || right.type == STRING) {
						ret.type = STRING;
						if (f.value.equals("+")) {
							left.value = left.type == VARIABLE ? getVariable(left.value)
									: left.value;
							right.value = right.type == VARIABLE ? getVariable(right.value)
									: right.value;
							if (left.value.charAt(0) == '"'
									&& left.value
											.indexOf("objNav.getValue(model ,\"") < 0
									&& right.value.charAt(0) == '"'
									&& right.value
											.indexOf("objNav.getValue(model ,\"") < 0)
								ret.value = "\""
										+ left.value.substring(1,
												left.value.length() - 1)
										+ right.value.substring(1,
												right.value.length() - 1)
										+ "\"";
							else
								ret.value = left.value + " + " + right.value;
						} else {
							throw new ExpressionError(
									"String only suport '+' operator.");
						}
					} else if (left.type == DOUBLE || right.type == DOUBLE) {
						ret.type = DOUBLE;
						ret.value = getFloatArithmeticResult(left, right,
								f.value, false);
					} else if (left.type == FLOAT || right.type == FLOAT) {
						ret.type = FLOAT;
						ret.value = getFloatArithmeticResult(left, right,
								f.value, true);
					} else if (left.type == LONG || right.type == LONG) {
						ret.type = LONG;
						ret.value = getIntegerArithmeticResult(left, right,
								f.value, false);
					} else if (left.type == INTEGER || right.type == INTEGER) {
						ret.type = INTEGER;
						ret.value = getIntegerArithmeticResult(left, right,
								f.value, true);
					} else {
						throw new ExpressionError(left.type + " and "
								+ right.type + " ​​can not do arithmetic.");
					}
					break;
				case LOGICAL_OPERATOR:
					break;
				case ASSIGNMENT_OPERATOR:
					break;
				case ARITHMETIC_OR_LOGICAL_OPERATOR:
					break;
				case CONDITIONAL_OPERATOR:
					break;
				default:
					break;
				}
				d.push(ret);
			} else {
				d.push(f);
			}
		}
		if (d.size() != 1)
			throw new ExpressionError("RPN error: " + content);
		return d.pop().value;
	}

	private boolean isSymbol(RPNUtils.Type type) {
		return type == ARITHMETIC_OPERATOR || type == LOGICAL_OPERATOR
				|| type == ASSIGNMENT_OPERATOR
				|| type == ARITHMETIC_OR_LOGICAL_OPERATOR
				|| type == CONDITIONAL_OPERATOR;
	}

	private String getVariable(String var) {
		int start = var.indexOf("${") + 2;
		int end = var.indexOf('}');
		return "objNav.getValue(model ,\"" + var.substring(start, end) + "\")";
	}

	private String getVariable(String var, String t) {
		StringBuilder ret = new StringBuilder();
		int start = var.indexOf("${") + 2;
		int end = var.indexOf('}');
		ret.append(var.substring(0, start - 2)).append(
				"objNav.get" + t + "(model ,\"" + var.substring(start, end)
						+ "\")");
		if (end < var.length() - 1)
			throw new ExpressionError("Variable format error: " + var);
		return ret.toString();
	}

	private String getFloatArithmeticResult(Fragment left, Fragment right,
			String s, boolean isFloat) {
		String ret = null;
		if (left.type == VARIABLE || right.type == VARIABLE)
			ret = getVariableFloatArithmeticResult(left, right, s, isFloat);
		else if (left.value.indexOf("objNav") >= 0
				|| right.value.indexOf("objNav") >= 0)
			ret = left.value + " " + s + " " + right.value;
		else {
			ret = getConstFloatArithmeticResult(left, right, s, isFloat);
		}
		return ret;
	}

	private String getVariableFloatArithmeticResult(Fragment lf, Fragment rf,
			String s, boolean isFloat) {
		char f0 = s.charAt(0);
		lf.value = lf.type == VARIABLE ? getVariable(lf.value,
				isFloat ? "Float" : "Double")
				+ " " + s + " " : lf.value;
		rf.value = rf.type == VARIABLE ? " " + s + " "
				+ getVariable(rf.value, isFloat ? "Float" : "Double")
				: rf.value;
		return f0 == '*' || f0 == '/' || f0 == '%' ? lf.value + rf.value : "("
				+ lf.value + rf.value + ")";
	}

	private String getConstFloatArithmeticResult(Fragment lf, Fragment rf,
			String s, boolean isFloat) {
		float l = Float.parseFloat(lf.value), r = Float.parseFloat(rf.value);
		double l0 = Double.parseDouble(lf.value), r0 = Double
				.parseDouble(rf.value);
		String ret = null;
		char f0 = s.charAt(0);
		switch (f0) {
		case '+':
			ret = String.valueOf(isFloat ? l + r : l0 + r0);
			break;
		case '-':
			ret = String.valueOf(isFloat ? l - r : l0 - r0);
			break;
		case '*':
			ret = String.valueOf(isFloat ? l * r : l0 * r0);
			break;
		case '/':
			ret = String.valueOf(isFloat ? l / r : l0 / r0);
			break;
		case '%':
			ret = String.valueOf(isFloat ? l % r : l0 % r0);
			break;
		default:
			throw new ExpressionError(s + "is illegal");
		}
		return ret;
	}

	private String getIntegerArithmeticResult(Fragment left, Fragment right,
			String s, boolean isInteger) {
		String ret = null;
		if (left.type == VARIABLE || right.type == VARIABLE)
			ret = getVariableIntegerArithmeticResult(left, right, s, isInteger);
		else if (left.value.indexOf("objNav") >= 0
				|| right.value.indexOf("objNav") >= 0)
			ret = left.value + " " + s + " " + right.value;
		else {
			ret = getConstIntegerArithmeticResult(left, right, s, isInteger);
		}
		return ret;
	}

	private String getVariableIntegerArithmeticResult(Fragment lf, Fragment rf,
			String s, boolean isInteger) {
		char f0 = s.charAt(0);
		lf.value = lf.type == VARIABLE ? getVariable(lf.value,
				isInteger ? "Integer" : "Long")
				+ " " + s + " " : lf.value;
		rf.value = rf.type == VARIABLE ? " " + s + " "
				+ getVariable(rf.value, "Integer") : rf.value;
		return f0 == '*' || f0 == '/' || f0 == '%' ? lf.value + rf.value : "("
				+ lf.value + rf.value + ")";
	}

	private String getConstIntegerArithmeticResult(Fragment lf, Fragment rf,
			String s, boolean isInteger) {
		int l = Integer.parseInt(lf.value), r = Integer.parseInt(rf.value);
		long l0 = Long.parseLong(lf.value), r0 = Long.parseLong(rf.value);
		String ret = null;
		char f0 = s.charAt(0);
		switch (f0) {
		case '+':
			ret = String.valueOf(isInteger ? l + r : l0 + r0);
			break;
		case '-':
			ret = String.valueOf(isInteger ? l - r : l0 - r0);
			break;
		case '*':
			ret = String.valueOf(isInteger ? l * r : l0 * r0);
			break;
		case '/':
			ret = String.valueOf(isInteger ? l / r : l0 / r0);
			break;
		case '%':
			ret = String.valueOf(isInteger ? l % r : l0 % r0);
			break;
		case '<':
			ret = String.valueOf(isInteger ? l << r : l0 << r0);
			break;
		case '>':
			if (s.length() == 3 && s.charAt(1) == '>' && s.charAt(2) == '>') {
				ret = String.valueOf(isInteger ? l >>> r : l0 >>> r0);
			} else if (s.length() == 2 && s.charAt(1) == '>') {
				ret = String.valueOf(isInteger ? l >> r : l0 >> r0);
			} else {
				throw new ExpressionError(s + "is illegal");
			}
			break;
		case '^':
			ret = String.valueOf(isInteger ? l ^ r : l0 ^ r0);
			break;
		default:
			throw new ExpressionError(s + "is illegal");
		}
		return ret;
	}

}

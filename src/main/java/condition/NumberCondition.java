package condition;

import com.rictin.util.Condition;

public interface NumberCondition {

	Condition<Number> isGreaterThan(Number number);
	Condition<Number> isLessThan(Number number);

}

package bg.uni_sofia.fmi.artificial.inteligence.classifier;

public class Instance {

	private String className;
	private double[] values;

	public Instance(String instance) {
		String[] parts = instance.split(",");
		values = new double[parts.length - 1];
		for (int i = 0; i < values.length; i++) {
			values[i] = Double.valueOf(parts[i]);
		}
		this.className = parts[parts.length - 1];
	}

	public Instance(double[] values, String className) {
		this.values = values;
		this.className = className;
	}

	void normalizeAttributes(double[] minValues, double[] maxValues) {
		for (int i = 0; i < values.length; i++) {
			values[i] = (values[i] - minValues[i]) / (maxValues[i] - minValues[i]);
		}
	}

	double getDistanceTo(Instance other) {
		double squareDistance = 0.0;
		for (int i = 0; i < values.length; i++) {
			double d = other.values[i] - this.values[i];
			squareDistance += d * d;
		}

		return Math.sqrt(squareDistance);
	}

	double getValue(int index) {
		return values[index];
	}

	String getClassName() {
		return className;
	}

	public double[] getAttributes() {
		return values;
	}

	public void setAttributes(double[] attributes) {
		this.values = attributes;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Instance) {
			Instance otherInstance = (Instance) obj;
			if (!this.className.equals(otherInstance.className))
				return false;

			for (int i = 0; i < values.length; i++) {
				if (this.values[i] != otherInstance.values[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}

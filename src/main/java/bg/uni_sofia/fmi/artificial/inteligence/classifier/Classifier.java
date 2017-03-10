package bg.uni_sofia.fmi.artificial.inteligence.classifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Classifier {

	private static final int TESTING_SET_LENGTH = 20;
	private static final int ATTRIBUTES_COUNT = 4;
	private Random randomGenerator;
	private List<Instance> instances;
	private List<Instance> testingSet;
	private List<Instance> teachingSet;
	private int[] indexes;
	private int K;
	double[] minValues = new double[ATTRIBUTES_COUNT];
	double[] maxValues = new double[ATTRIBUTES_COUNT];

	public Classifier(int K, String file) {
		this.K = K;
		instances = new ArrayList<Instance>();
		initializeData(file);
	}

	private void initializeData(String file) {
		
		String line = null;
		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			while ((line = reader.readLine()) != null) {
				instances.add(new Instance(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		indexes = new int[instances.size()];
		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = i;
		}

		testingSet = new ArrayList<Instance>(TESTING_SET_LENGTH);
		randomGenerator = new Random();
		int size = indexes.length;
		for (int i = 0; i < TESTING_SET_LENGTH; i++) {
			int randomPosition = randomGenerator.nextInt(size);
			testingSet.add(instances.get(indexes[randomPosition]));
			indexes[randomPosition] = indexes[size - 1];
			size--;
		}
		teachingSet = new ArrayList<Instance>();
		while (size > 0) {
			int randomPosition = randomGenerator.nextInt(size);
			teachingSet.add(instances.get(indexes[randomPosition]));
			indexes[randomPosition] = indexes[size - 1];
			size--;
		}
		teachingSet.add(instances.get(indexes[0]));

		normalize();
	}

	private void normalize() {
		for (int i = 0; i < ATTRIBUTES_COUNT; i++) {
			minValues[i] = teachingSet.get(0).getValue(i);
			maxValues[i] = teachingSet.get(0).getValue(i);
		}

		for (Instance instance : teachingSet) {
			for (int i = 0; i < ATTRIBUTES_COUNT; i++) {
				if (instance.getValue(i) < minValues[i]) {
					minValues[i] = instance.getValue(i);
				}

				if (instance.getValue(i) > maxValues[i]) {
					maxValues[i] = instance.getValue(i);
				}
			}
		}

		for (Instance instance : teachingSet) {
			instance.normalizeAttributes(minValues, maxValues);
		}

		for (Instance instance : testingSet) {
			instance.normalizeAttributes(minValues, maxValues);
		}
	}

	public void classifyAndShowAlgorithmAccuracy() {

		int numberOfCorrectPredictions = 0;
		for (Instance instance : testingSet) {
			String className = classify(instance);
			if (className.equals(instance.getClassName()))
				numberOfCorrectPredictions++;
			System.out.println("Class:" + className + " " 
				+ Arrays.toString(instance.getAttributes()));
		}
		System.out.println(
				"Algorithm accuracy: " + 
		((double) numberOfCorrectPredictions / TESTING_SET_LENGTH) * 100 + "%");
	}

	private String classify(final Instance currentInstance) {
		teachingSet.sort(new Comparator<Instance>() {

			public int compare(Instance first, Instance second) {
				return Double.compare(first.getDistanceTo(currentInstance), 
						second.getDistanceTo(currentInstance));
			}

		});

		Map<String, Integer> classesOccurrences = new HashMap<String, Integer>();
		for (int i = 0; i < K; i++) {
			String className = teachingSet.get(i).getClassName();
			if (classesOccurrences.containsKey(className)) {
				classesOccurrences.replace(className, classesOccurrences.get(className) + 1);
			} else {
				classesOccurrences.put(className, 1);
			}
		}

		String resultClass = null;
		int maxOccurences = 0;
		for (String key : classesOccurrences.keySet()) {
			int occurences = classesOccurrences.get(key);
			if (occurences > maxOccurences) {
				maxOccurences = occurences;
				resultClass = key;
			}
		}
		return resultClass;
	}
}
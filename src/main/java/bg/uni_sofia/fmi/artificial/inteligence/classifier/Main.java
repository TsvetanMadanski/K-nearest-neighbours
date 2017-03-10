package bg.uni_sofia.fmi.artificial.inteligence.classifier;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	
	private static final int K = 10;
	
	public static void main(String[] args) {
		Path file = Paths.get("D:/Users/iris.txt");
		Classifier classifier = new Classifier(K, file.toString());
		classifier.classifyAndShowAlgorithmAccuracy();
	}
}

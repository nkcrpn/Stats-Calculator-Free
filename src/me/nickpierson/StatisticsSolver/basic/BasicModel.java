package me.nickpierson.StatisticsSolver.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.nickpierson.StatisticsSolver.utils.MyConstants;

import com.thecellutioncenter.mvplib.DataActionHandler;

public class BasicModel extends DataActionHandler {

	public enum Types {
		VALID_INPUT, INVALID_NUMBER, INVALID_FREQUENCY;
	}

	public enum Keys {
		RESULTS, INVALID_ITEM;
	}

	private LinkedHashMap<String, Double> result;

	public BasicModel() {
		result = new LinkedHashMap<String, Double>();
	}

	public LinkedHashMap<String, Double> getEmptyResults() {
		LinkedHashMap<String, Double> emptyMap = new LinkedHashMap<String, Double>();
		emptyMap.put(MyConstants.SIZE, 0.0);
		emptyMap.put(MyConstants.SUM, 0.0);
		emptyMap.put(MyConstants.MEAN, 0.0);
		emptyMap.put(MyConstants.MEDIAN, 0.0);
		emptyMap.put(MyConstants.MODE, null);
		emptyMap.put(MyConstants.RANGE, 0.0);
		emptyMap.put(MyConstants.POP_VAR, 0.0);
		emptyMap.put(MyConstants.SAMPLE_VAR, 0.0);
		emptyMap.put(MyConstants.POP_DEV, 0.0);
		emptyMap.put(MyConstants.SAMPLE_DEV, 0.0);
		return emptyMap;
	}

	public void validateInput(String input) {

	}

	public LinkedHashMap<String, Double> calculateResults(List<Double> numberList) {
		result.put(MyConstants.SIZE, (double) numberList.size());
		result.put(MyConstants.SUM, calculateSum(numberList));
		result.put(MyConstants.MEAN, result.get(MyConstants.SUM) / result.get(MyConstants.SIZE));
		result.put(MyConstants.MEDIAN, calculateMedian(numberList, result.get(MyConstants.SIZE)));
		result.put(MyConstants.MODE, calculateMode(numberList));
		result.put(MyConstants.RANGE, calculateRange(numberList));
		result.put(MyConstants.POP_VAR, calculatePopVariance(numberList, result.get(MyConstants.MEAN), result.get(MyConstants.SIZE)));
		result.put(MyConstants.SAMPLE_VAR, calculateSampleVariance(numberList, result.get(MyConstants.MEAN), result.get(MyConstants.SIZE)));
		result.put(MyConstants.POP_DEV, Math.sqrt(result.get(MyConstants.POP_VAR)));
		result.put(MyConstants.SAMPLE_DEV, Math.sqrt(result.get(MyConstants.SAMPLE_VAR)));

		return result;
	}

	private int previousErrorIndex;

	public ArrayList<Double> convertInput(String input) {
		ArrayList<Double> result = new ArrayList<Double>();
		String[] numbers = input.split(",");
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i].length() == 0) {
				continue;
			}

			if (hasMultiplier(numbers[i]) && isWellFormed(numbers[i])) {
				double num = getNumber(numbers[i]);
				int multiplier = getMultiplier(numbers[i]);

				for (int j = 0; j < multiplier; j++) {
					result.add(num);
				}
			} else if (isWellFormed(numbers[i])) {
				result.add(Double.valueOf(numbers[i]));
			} else {
				previousErrorIndex = i;
				return null;
			}
		}

		if (result.size() == 0) {
			previousErrorIndex = 0;
			return null;
		} else {
			return result;
		}
	}

	private boolean isWellFormed(String string) {
		if (hasMultiplier(string)) {
			String[] values = string.split("x");

			if (values.length != 2 || values[0].length() < 1 || values[1].length() < 1) {
				return false;
			}

			try {
				Double.valueOf(values[0]);
				Integer.valueOf(values[1]);
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			try {
				Double.valueOf(string);
			} catch (NumberFormatException e) {
				return false;
			}
		}

		return true;
	}

	private boolean hasMultiplier(String string) {
		return string.contains("x");
	}

	private double getNumber(String value) {
		return Double.valueOf(value.substring(0, value.indexOf('x')));
	}

	private int getMultiplier(String value) {
		return Integer.valueOf(value.substring(value.indexOf('x') + 1, value.length()));
	}

	public int getPreviousErrorIndex() {
		return previousErrorIndex;
	}

	private double calculateSum(List<Double> numberList) {
		double sum = 0;
		for (double num : numberList) {
			sum += num;
		}
		return sum;
	}

	private double calculateMedian(List<Double> numberList, double length) {
		int size = (int) length;
		Collections.sort(numberList);
		if (size % 2 == 1) {
			int index = (int) Math.floor(size / 2.0);
			return numberList.get(index);
		} else {
			int half = size / 2;
			return (numberList.get(half - 1) + numberList.get(half)) / 2;
		}
	}

	private Double calculateMode(List<Double> numberList) {
		HashMap<Double, Integer> freqs = new HashMap<Double, Integer>();
		for (double num : numberList) {
			Integer freq = freqs.get(num);
			freqs.put(num, freq == null ? 1 : freq + 1);
		}

		double mode = 0;
		int max = 0;
		boolean isSimilarMax = true;
		for (Map.Entry<Double, Integer> entry : freqs.entrySet()) {
			int freq = entry.getValue();
			if (freq > max) {
				isSimilarMax = false;
				max = freq;
				mode = entry.getKey();
			} else if (freq == max) {
				isSimilarMax = true;
			}
		}

		if (isSimilarMax) {
			return null;
		} else {
			return mode;
		}
	}

	private double calculateRange(List<Double> numberList) {
		double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
		for (double num : numberList) {
			if (num > max) {
				max = num;
			}
			if (num < min) {
				min = num;
			}
		}

		return max - min;
	}

	private double calculatePopVariance(List<Double> numberList, double average, double size) {
		double numerator = calculateVarianceNumerator(numberList, average);
		return numerator / size;
	}

	private double calculateSampleVariance(List<Double> numberList, double average, double size) {
		double numerator = calculateVarianceNumerator(numberList, average);
		return numerator / (size - 1);
	}

	private double calculateVarianceNumerator(List<Double> numberList, double average) {
		double sum = 0;
		for (double num : numberList) {
			sum += Math.pow(num - average, 2);
		}
		return sum;
	}

	public LinkedHashMap<String, Double> getResultMap() {
		return result;
	}
}

package me.nickpierson.StatisticsSolve.basic;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import me.nickpierson.StatisticsSolver.basic.BasicModel;
import me.nickpierson.StatisticsSolver.utils.MyConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class BasicModelTest {

	public BasicModel model;
	private double DELTA = .000001;

	@Before
	public void setup() {
		model = new BasicModel();
	}

//	@Test
//	public void whenModelIsCreated_ResultsAreInitializedToZero() {
//		assertEquals(model.getResultMap().get(MyConstants.SIZE), 0.0, DELTA);
//		assertEquals(model.getResultMap().get(MyConstants.SUM), 0.0, DELTA);
//		assertEquals(model.getResultMap().get(MyConstants.MEAN), 0.0, DELTA);
//		assertEquals(model.getResultMap().get(MyConstants.MEDIAN), 0.0, DELTA);
//		assertEquals(model.getResultMap().get(MyConstants.MODE), null);
//		assertEquals(model.getResultMap().get(MyConstants.RANGE), 0.0, DELTA);
//		assertEquals(model.getResultMap().get(MyConstants.POP_VAR), 0.0, DELTA);
//		assertEquals(model.getResultMap().get(MyConstants.SAMPLE_VAR), 0.0, DELTA);
//		assertEquals(model.getResultMap().get(MyConstants.POP_DEV), 0.0, DELTA);
//		assertEquals(model.getResultMap().get(MyConstants.SAMPLE_DEV), 0.0, DELTA);
//	}

	@Test
	public void convertInputReturnsCorrectListOfIntegers_WithCorrectInput() {
		String input1 = "45,89,.334,99.999999,0,-.12,45.00001";
		String input2 = ".39x2,-3x6,-.2x3,1,-112.333x2";
		ArrayList<Double> expectedReturn1 = makeValidList(45, 89, .334, 99.999999, 0, -.12, 45.00001);
		ArrayList<Double> expectedReturn2 = makeValidList(.39, .39, -3, -3, -3, -3, -3, -3, -.2, -.2, -.2, 1, -112.333, -112.333);

		assertEquals(expectedReturn1, model.convertInput(input1));
		assertEquals(expectedReturn2, model.convertInput(input2));
	}

	@Test
	public void convertInputReturnsNull_WhenMoreThanOnePeriodIsInOneNumber() {
		String input = "35.8,34.5.6,71";
		String input1 = "37.89,34..8,2";
		String input2 = "90.1,23,...9";

		assertEquals(null, model.convertInput(input));
		assertEquals(null, model.convertInput(input1));
		assertEquals(null, model.convertInput(input2));
	}

	@Test
	public void convertInputReturnsNull_WhenNegativeSignIsMisplaced() {
		String input = "-45.6,98.1,9-1";
		String input1 = "-32.1,11,.-1";
		String input2 = "34,82,95-";

		assertEquals(null, model.convertInput(input));
		assertEquals(null, model.convertInput(input1));
		assertEquals(null, model.convertInput(input2));
	}
	
	@Test
	public void convertInputReturnsNull_WhenTimesSignIsInputIncorrectly() {
		String input1 = "23x2,45,2x2.0,3";
		String input2 = "4x7,3xx8,7,7,";
		String input3 = "4,6x,3,9";
		String input4 = "99,x7,12.3";
		
		assertEquals(null, model.convertInput(input1));
		assertEquals(null, model.convertInput(input2));
		assertEquals(null, model.convertInput(input3));
		assertEquals(null, model.convertInput(input4));
	}

	@Test
	public void calculateResults_CalculatesCorrectResult() {
		ArrayList<Double> sampleInput = makeValidList(45, 68.1, 29.4, -54, -.19, 3.0001);
		LinkedHashMap<String, Double> expectedResult = new LinkedHashMap<String, Double>();
		expectedResult.put(MyConstants.SUM, 91.3101);
		expectedResult.put(MyConstants.SIZE, 6.0);
		expectedResult.put(MyConstants.MEAN, 15.21835);
		expectedResult.put(MyConstants.MEDIAN, 16.20005);
		expectedResult.put(MyConstants.MODE, null);
		expectedResult.put(MyConstants.RANGE, 122.1);
		expectedResult.put(MyConstants.POP_VAR, 1510.402939);
		expectedResult.put(MyConstants.SAMPLE_VAR, 1812.483527);
		expectedResult.put(MyConstants.POP_DEV, 38.863902);
		expectedResult.put(MyConstants.SAMPLE_DEV, 42.573272);

		LinkedHashMap<String, Double> actualResult = model.calculateResults(sampleInput);
		assertEquals(expectedResult.get(MyConstants.SUM), actualResult.get(MyConstants.SUM));
		assertEquals(expectedResult.get(MyConstants.SIZE), actualResult.get(MyConstants.SIZE));
		assertEquals(expectedResult.get(MyConstants.MEAN), actualResult.get(MyConstants.MEAN), DELTA);
		assertEquals(expectedResult.get(MyConstants.MEDIAN), actualResult.get(MyConstants.MEDIAN), DELTA);
		assertEquals(expectedResult.get(MyConstants.MODE), actualResult.get(MyConstants.MODE));
		assertEquals(expectedResult.get(MyConstants.RANGE), actualResult.get(MyConstants.RANGE), DELTA);
		assertEquals(expectedResult.get(MyConstants.POP_VAR), actualResult.get(MyConstants.POP_VAR), DELTA);
		assertEquals(expectedResult.get(MyConstants.SAMPLE_VAR), actualResult.get(MyConstants.SAMPLE_VAR), DELTA);
		assertEquals(expectedResult.get(MyConstants.POP_DEV), actualResult.get(MyConstants.POP_DEV), DELTA);
		assertEquals(expectedResult.get(MyConstants.SAMPLE_DEV), actualResult.get(MyConstants.SAMPLE_DEV), DELTA);
	}

	private ArrayList<Double> makeValidList(double... args) {
		ArrayList<Double> validList = new ArrayList<Double>();
		for (double val : args) {
			validList.add(val);
		}
		return validList;
	}

}

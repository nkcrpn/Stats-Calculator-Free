package me.nickpierson.StatisticsSolve.basic;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import me.nickpierson.StatisticsSolver.basic.BasicModel;
import me.nickpierson.StatisticsSolver.basic.BasicPresenter;
import me.nickpierson.StatisticsSolver.basic.BasicView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.thecellutioncenter.mvplib.ActionListener;
import com.thecellutioncenter.mvplib.DataActionListener;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class BasicPresenterTest {

	BasicView view;
	BasicModel model;

	ArgumentCaptor<ActionListener> listener;
	private ArgumentCaptor<DataActionListener> dataListener;

	@Before
	public void setup() {
		view = mock(BasicView.class);
		model = mock(BasicModel.class);

		listener = ArgumentCaptor.forClass(ActionListener.class);
		dataListener = ArgumentCaptor.forClass(DataActionListener.class);

		when(model.getEmptyResults()).thenReturn(new LinkedHashMap<String, Double>());
	}

	public void createPresenter() {
		BasicPresenter.create(model, view);
	}

	@Test
	public void viewIsInitializedByPresenter() {
		createPresenter();

		verify(model).getEmptyResults();
		verify(view).showResults(model.getEmptyResults());
	}

	@Test
	public void whenEditTextIsClicked_ThenKeypadIsShown() {
		createPresenter();

		verify(view).addListener(listener.capture(), eq(BasicView.Types.EDITTEXT_CLICKED));

		listener.getValue().fire();

		verify(view).showKeypad();
	}

	@Test
	public void whenDoneIsClicked_ThenInputIsValidated() {
		when(view.getInput()).thenReturn("15,32x4,17.9");
		createPresenter();

		verify(view).addListener(listener.capture(), eq(BasicView.Types.DONE_CLICKED));

		listener.getValue().fire();

		verify(model).validateInput(view.getInput());
	}

	@Test
	public void whenValidInput_ThenInputIsShown() {
		ArrayList<Double> testResults = new ArrayList<Double>();
		HashMap<Enum<?>, ArrayList<Double>> testMap = new HashMap<Enum<?>, ArrayList<Double>>();
		testMap.put(BasicModel.Keys.VALIDATED_LIST, testResults);
		createPresenter();

		verify(model).addListener(dataListener.capture(), eq(BasicModel.Types.VALID_INPUT));

		dataListener.getValue().fire(testMap);

		verify(model).calculateResults(testResults);
		verify(view, times(2)).showResults(model.calculateResults(testResults));
	}

	@Test
	public void whenInvalidNumber_ThenEmptyResultsAreShownAndToastIsDisplayed() {
		HashMap<Enum<?>, Integer> testMap = new HashMap<Enum<?>, Integer>();
		testMap.put(BasicModel.Keys.INVALID_ITEM, 5);
		createPresenter();

		verify(model).addListener(dataListener.capture(), eq(BasicModel.Types.INVALID_NUMBER));

		dataListener.getValue().fire(testMap);

		verify(model, times(2)).getEmptyResults();
		verify(view, times(2)).showResults(model.getEmptyResults());
		verify(view).showErrorToast(5);
	}
}

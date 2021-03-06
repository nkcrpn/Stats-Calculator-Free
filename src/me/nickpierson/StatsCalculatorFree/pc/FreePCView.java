package me.nickpierson.StatsCalculatorFree.pc;

import me.nickpierson.StatsCalculator.R;
import me.nickpierson.StatsCalculator.pc.PCView;
import me.nickpierson.StatsCalculator.utils.Constants;
import me.nickpierson.StatsCalculator.utils.KeypadHelper;
import me.nickpierson.StatsCalculatorFree.utils.FreeDefaultAdapter;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class FreePCView extends PCView {

	private KeypadHelper keypadHelper;

	public FreePCView(Activity activity) {
		super(activity, new FreeDefaultAdapter(activity, R.layout.perm_comb_results_item, R.id.pc_tvResultsTitle, R.id.pc_tvResultsResult));

		lvResults = (ListView) LayoutInflater.from(activity).inflate(R.layout.results_list, null);
		lvResults.setAdapter(resultsAdapter);
		resultsAdapter.addMultiple(Constants.PC_TITLES);
		flFrame.addView(lvResults);

		keypadHelper = new KeypadHelper();
		keypadHelper.disableSoftInputFromAppearing(etNVal);
		keypadHelper.disableSoftInputFromAppearing(etRVal);
		keypadHelper.disableSoftInputFromAppearing(etNVals);

		btnBackspace.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				EditText etSelected = getSelectedEditText();
				if (etSelected != null) {
					keypadHelper.longPressBackspace(etSelected);
				}
				return true;
			}
		});
	}

	@Override
	public void showResults() {
		flFrame.removeAllViews();
		flFrame.addView(lvResults);
	}

	public void keypadPress(Button button) {
		/* Skips MVP */
		EditText etSelected = getSelectedEditText();

		if (etSelected != null) {
			keypadHelper.keypadPress(etSelected, button.getText().charAt(0));
		}
	}

	public void backSpace() {
		/* Skips MVP */
		EditText etSelected = getSelectedEditText();

		if (etSelected != null) {
			keypadHelper.backspace(etSelected);
		}
	}

	private EditText getSelectedEditText() {
		EditText etSelected = null;

		if (etNVal.isFocused()) {
			etSelected = etNVal;
		} else if (etRVal.isFocused()) {
			etSelected = etRVal;
		} else if (etNVals.isFocused()) {
			etSelected = etNVals;
		}

		return etSelected;
	}
}

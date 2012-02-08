package dsbudget.view;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import com.divrep.DivRepEvent;
import com.divrep.DivRepEventListener;
import com.divrep.common.DivRepDialog;
import com.divrep.common.DivRepMoneyAmount;
import com.divrep.common.DivRepTextBox;

import dsbudget.Main;
import dsbudget.i18n.Labels;
import dsbudget.model.Deduction;
import dsbudget.model.Income;

public class DeductionDialog extends DivRepDialog
{
	MainView mainview;
	
	public DivRepMoneyAmount amount;
	public DivRepTextBox description;
	
	Income income;
	Deduction deduction;
	NumberFormat nf = NumberFormat.getCurrencyInstance();
	NumberFormat pnf = NumberFormat.getPercentInstance();
	
	public DeductionDialog(MainView parent) {
		super(parent);
		mainview = parent;
		
		setHeight(250);
		setWidth(450);
		setEnterToSubmit(Main.conf.getProperty("enter_to_submit").equals("true"));
		
		description = new DivRepTextBox(this);
		description.setLabel(Labels.getString(DED_LABEL_DESCRIPTION));
		description.setRequired(true);
		description.setWidth(300);
		
		amount = new DivRepMoneyAmount(this);
		amount.setLabel(Labels.getString(DED_LABEL_AMOUNT));
		amount.setWidth(200);
		amount.setSampleValue(nf.format(Integer.valueOf(Labels.getString(DED_LABEL_AMOUNT_SAMPLE))));
		amount.setRequired(true);
		amount.allowPercentage(true);
	}
	
	public void open(Income _income, Deduction _deduction)
	{
		income = _income;
		deduction = _deduction;
		if(deduction == null) {
			setTitle(Labels.getString(DED_LABEL_NEW_DEDUCTION, income.getName()));
			description.setValue("");
			amount.setValue("");
		} else {
			setTitle(Labels.getString(DED_LABEL_UPDATE_DEDUCTION, income.getName()));
			description.setValue(deduction.description);
			if(deduction.isPercentage()) {
				amount.setValue(pnf.format(deduction.getPercentage()));
			} else {
				amount.setValue(nf.format(deduction.getAmount(null)));
			}
		}
		description.redraw();
		amount.redraw();
		super.open();
	}
	
	public void onCancel() {
		close();
	}
	public void onSubmit() {
		if(validate()) {
			if(deduction == null) {
				//new deduction
				deduction = new Deduction();
				income.deductions.add(deduction);
			}
			
			try {
				//try parsing it as amount
				deduction.setAmount(new BigDecimal(nf.parse(amount.getValue()).toString()));
			} catch (ParseException e1) {
				try {
					//try parsing it as percentage
					deduction.setAmountAsPercentage(new BigDecimal(pnf.parse(amount.getValue()).toString()));
				} catch (ParseException e2) {
					//neither amount nor percentage
					e2.printStackTrace();
				}
			}
			deduction.description = description.getValue();
			
			close();
			mainview.updateIncomeView();
			mainview.save();
		}
	}
	public void renderDialog(PrintWriter out) {
		description.render(out);
		amount.render(out);
	}
	protected Boolean validate()
	{
		Boolean valid = true;
		valid &= description.validate();
		valid &= amount.validate();
		return valid;
	}
	
	public static final String DED_LABEL_DESCRIPTION = "DeductionDialog.LABEL_DESCRIPTION";
	public static final String DED_LABEL_AMOUNT = "DeductionDialog.LABEL_AMOUNT";
	public static final String DED_LABEL_AMOUNT_SAMPLE = "DeductionDialog.LABEL_AMOUNT_SAMPLE";
	public static final String DED_LABEL_NEW_DEDUCTION = "DeductionDialog.LABEL_NEW_DEDUCTION";
	public static final String DED_LABEL_UPDATE_DEDUCTION = "DeductionDialog.LABEL_UPDATE_DEDUCTION";

};

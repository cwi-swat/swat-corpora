package dsbudget.view;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;
import com.divrep.DivRepEventListener;
import com.divrep.common.DivRepDate;
import com.divrep.common.DivRepDialog;
import com.divrep.common.DivRepSelectBox;
import com.divrep.common.DivRepTextBox;
import com.divrep.validator.DivRepIValidator;

import dsbudget.Main;
import dsbudget.i18n.Labels;
import dsbudget.model.Budget;
import dsbudget.model.Category;
import dsbudget.model.Expense;
import dsbudget.model.Income;
import dsbudget.model.Page;

public class PageDialog extends DivRepDialog
{
	Budget budget;
	Page current_page; //current page
	Boolean newpage;
	
	DivRepTextBox title;
	DivRepDate cdate;
	NewPageStuff newpage_stuff;
	
	LinkedHashMap<Integer, String> pages_kv = new LinkedHashMap<Integer, String>();

	class NewPageStuff extends DivRep {
		
		public Boolean hidden = false;
		public DivRepSelectBox copy_from;
		public DivRepSelectBox balance_handling;
		
		public NewPageStuff(DivRep parent) {
			super(parent);
			
			for(Page page : budget.pages) {
				//allow to select copying from the page itself
				pages_kv.put(page.getID(), page.name);
			}
			copy_from = new DivRepSelectBox(this, pages_kv);
			copy_from.setNullLabel(Labels.getString(PAD_LABEL_CREATE_EMPTY_PAGE));
			copy_from.setLabel(Labels.getString(PAD_LABEL_COPY_INCOME_BUDGETTING));
			copy_from.addEventListener(new DivRepEventListener() {
				public void handleEvent(DivRepEvent e) {
					usebalanceShowHide();
				}});

			TreeMap<Integer, String> kv = new TreeMap<Integer, String>();
			kv.put(1, Labels.getString(PAD_LABEL_SUM_UP_AND_ADD_AS_INCOME));
			kv.put(2, Labels.getString(PAD_LABEL_ADD_TO_EACH_CATEGORY_AS_NEGATIVE_EXPENSE));
			balance_handling = new DivRepSelectBox(this, kv);
			balance_handling.setNullLabel(Labels.getString(PAD_LABEL_DO_NOTHING));
			balance_handling.setLabel(Labels.getString(PAD_LABEL_WHAT_DO_YOU_DO_WITH_BALANCE));
		}
		protected void onEvent(DivRepEvent e) {
			// TODO Auto-generated method stub
			
		}
		public void render(PrintWriter out) {
			out.write("<div id=\""+getNodeID()+"\">");
			if(!hidden) {
				out.write("<br/>");
				copy_from.render(out);
				balance_handling.render(out);
			}
			out.write("</div>");
		}

		public void usebalanceShowHide()
		{
			Integer id = copy_from.getValue();
			if(id == null || id.equals("")) {
				balance_handling.setHidden(true);
				balance_handling.setValue(null);
			} else {
				balance_handling.setHidden(false);	
			}
			redraw();
		}
	
	};
	
	public void open() {
		throw new RuntimeException(Labels.getString(PAD_MESSAGE_DONT_USE_OPEN));
	}
	
	public void open(Boolean _newpage) {
		newpage = _newpage;
		
		if(newpage) {
			setTitle(Labels.getString(PAD_LABEL_CREATE_NEW_PAGE));
			
			//set new name
			Date today = new Date();     
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");  
			String monthName = dateFormat.format(today);  
			String newname = monthName;
			title.setValue(newname);
			cdate.setValue(new Date()); //use today's date
			
			newpage_stuff.copy_from.setValue(current_page.getID()); //copy from current page by default
			newpage_stuff.hidden = false;
		} else {
			//update current page settings
			setTitle(Labels.getString(PAD_LABEL_PAGE_SETTINGS));	
			
			title.setValue(current_page.name);
			cdate.setValue(current_page.created);
			
			newpage_stuff.hidden = true;
		}
		title.redraw();
		cdate.redraw();
		newpage_stuff.redraw();
		
		super.open();
	}
	
	public PageDialog(DivRep parent, Budget _budget, Page _current_page) {
		super(parent);
		
		setHeight(350);
		setWidth(400);
		setHasCancelButton(true);
		setEnterToSubmit(Main.conf.getProperty("enter_to_submit").equals("true"));
		
		budget = _budget;
		current_page = _current_page;

		title = new DivRepTextBox(this);
		title.setLabel(Labels.getString(PAD_LABEL_TITLE));
		title.setWidth(220);
				
		title.setRequired(true);
		title.addValidator(new DivRepIValidator<String>() {
			public String getErrorMessage() {
				return Labels.getString(PAD_MESSAGE_PAGE_TITLE_ALREADY_EXISTS);
			}

			public Boolean isValid(String value) {
				if(newpage) {
					for(Page page : budget.pages) {
						if(page.name.equals(value)) {
							return false;
						}
					}
				}
				return true;
			}
		});
		
		cdate = new DivRepDate(this);
		cdate.setLabel(Labels.getString(PAD_LABEL_GRAPH_BEGINNING_DATE));
		cdate.setRequired(true);

		newpage_stuff = new NewPageStuff(this);
	}
	
	public void onSubmit() {
		if(isValid()) {
			if(newpage) {
				createNewPage();
			} else {
				updatePage();
			}
			budget.save();
		}
	}
	
	public void onCancel() {
		cdate.close();
		close();	
	}
	
	private void updatePage()
	{
		current_page.name = title.getValue();
		current_page.created = cdate.getValue();
		redirect("?page="+current_page.getID());
	}
	
	private void createNewPage()
	{
		Page newpage;
		
		Integer id = newpage_stuff.copy_from.getValue();
		if(id != null) {
			//copy from the original page
			Page original = budget.findPage(id);
			newpage = original.clone();
			
			
			//clear balance income
			ArrayList<Income> non_balance_incomes = new ArrayList<Income>();
			for(Income income : newpage.incomes) {
				if(income.balance_from == null)  {
					non_balance_incomes.add(income);
				}
			}
			newpage.incomes = non_balance_incomes;
			
			
			//add balance as income
			Integer action = newpage_stuff.balance_handling.getValue();
			if(action == null) {
				//do nothing.. just clear all expenses
				for(Category category : newpage.categories) {
					category.expenses = new ArrayList<Expense>();
				}
			} else if(action.equals(1)) {
				//add as income
				Income income = new Income(newpage);
				income.balance_from = original;
				newpage.incomes.add(income);
				
				//then clear all expenses
				for(Category category : newpage.categories) {
					category.expenses = new ArrayList<Expense>();
				}
			} else if(action.equals(2)) {
				//as negative income
				for(Category category : newpage.categories) {
					BigDecimal balance = category.getAmount().subtract(category.getTotalExpense());
					
					category.expenses = new ArrayList<Expense>();
					Expense balance_expense = new Expense();
					balance_expense.amount = balance.negate();
					balance_expense.date = newpage.created;
					balance_expense.where = Labels.getString(PAD_LABEL_BALANCE_FROM, original.name);
					balance_expense.description = "";
					balance_expense.tentative = false;
					category.expenses.add(balance_expense);
				}
			}
				
		} else {
			//empty page
			newpage = new Page(budget);
		}
		newpage.name = title.getValue();
		newpage.created = cdate.getValue();
		budget.pages.add(newpage);

		redirect("?page="+newpage.getID());
	}
	
	public Boolean isValid()
	{
		Boolean valid = true;
		valid &= title.validate();
		return valid;
	}

	public void renderDialog(PrintWriter out) {
		title.render(out);
		cdate.render(out);
		newpage_stuff.render(out);
	}
	
	/**
	 * Labels
	 */
	public static final String PAD_LABEL_CREATE_EMPTY_PAGE = "PageDialog.LABEL_CREATE_EMPTY_PAGE";
	public static final String PAD_LABEL_COPY_INCOME_BUDGETTING = "PageDialog.LABEL_COPY_INCOME_BUDGETTING";
	public static final String PAD_LABEL_SUM_UP_AND_ADD_AS_INCOME = "PageDialog.LABEL_SUM_UP_AND_ADD_AS_INCOME";
	public static final String PAD_LABEL_ADD_TO_EACH_CATEGORY_AS_NEGATIVE_EXPENSE = "PageDialog.LABEL_ADD_TO_EACH_CATEGORY_AS_NEGATIVE_EXPENSE";
	public static final String PAD_LABEL_DO_NOTHING = "PageDialog.LABEL_DO_NOTHING";
	public static final String PAD_LABEL_WHAT_DO_YOU_DO_WITH_BALANCE = "PageDialog.LABEL_WHAT_DO_YOU_DO_WITH_BALANCE";
	public static final String PAD_LABEL_CREATE_NEW_PAGE = "PageDialog.LABEL_CREATE_NEW_PAGE";
	public static final String PAD_LABEL_PAGE_SETTINGS = "PageDialog.LABEL_PAGE_SETTINGS";
	public static final String PAD_LABEL_TITLE = "PageDialog.LABEL_TITLE";
	public static final String PAD_MESSAGE_PAGE_TITLE_ALREADY_EXISTS = "PageDialog.MESSAGE_PAGE_TITLE_ALREADY_EXISTS";
	public static final String PAD_LABEL_GRAPH_BEGINNING_DATE = "PageDialog.LABEL_GRAPH_BEGINNING_DATE";
	public static final String PAD_LABEL_BALANCE_FROM = "PageDialog.LABEL_BALANCE_FROM";
	public static final String PAD_MESSAGE_DONT_USE_OPEN = "PageDialog.MESSAGE_DONT_USE_OPEN";

}
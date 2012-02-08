package dsbudget.view;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;

import dsbudget.Main;
import dsbudget.i18n.Labels;
import dsbudget.model.Budget;
import dsbudget.model.Category;
import dsbudget.model.Expense;
import dsbudget.model.Income;
import dsbudget.model.Page;

public class MainView extends DivRep {

	static Logger logger = Logger.getLogger(MainView.class);
	
	public Budget budget;
	public Page page;
	
	public IncomeView incomeview;
	public BudgetingView budgettingview;
	public ExpenseView expenseview;
	public BalanceView balanceview;
	
	//dialogs
	IncomeDialog income_dialog;
	CategoryDialog category_dialog;
	ExpenseDialog expense_dialog;
	DeductionDialog deduction_dialog;
	RemoveIncomeDialog removeincomedialog;
	RemoveCategoryDialog removecategorydialog;
	
	public MainView(DivRep parent, Budget _budget, Page _page) {
		super(parent);
		page = _page;
		budget = _budget;

		income_dialog = new IncomeDialog(this); 
		category_dialog = new CategoryDialog(this); 
		expense_dialog = new ExpenseDialog(this); 
		deduction_dialog = new DeductionDialog(this);
		removeincomedialog = new RemoveIncomeDialog(this);
		removecategorydialog = new RemoveCategoryDialog(this);
		
		initView();
	}
	public void initView()
	{
		incomeview = new IncomeView(this);
		budgettingview = new BudgetingView(this);
		expenseview = new ExpenseView(this);
		balanceview = new BalanceView(this);
	}

	protected void onEvent(DivRepEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void render(PrintWriter out) {
		out.write("<div id=\""+getNodeID()+"\">");
		
		//display each section in the order specified in the configuration
		String section_order = Main.conf.getProperty("section_order");
		String[] items = section_order.split(",");
		for(String item : items) {
			item = item.trim();
			if(item.equals("income")) {
				incomeview.render(out);
			} else if(item.equals("budget")) {
				budgettingview.render(out);
			} else if(item.equals("expense")) {
				expenseview.render(out);
			} else if(item.equals("balance")) {
				balanceview.render(out);
			} else {
				logger.error(Labels.getHtmlEscapedString(MAV_MESSAGE_UNKNOWN_SECTION_ORDER, item));
			}
		}
		
		income_dialog.render(out);
		category_dialog.render(out);
		expense_dialog.render(out);
		deduction_dialog.render(out);
		removeincomedialog.render(out);
		removecategorydialog.render(out);
		
		out.write("</div>");
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	//view updaters -- TODO -- the design of this is all wrong.... we should be dealing with
	//update the views - not actually changing the model.
	public void addCategory(Category cat) {
		page.categories.add(cat);
		initView();
		redraw();
	}
	public void removeCategory(Category cat) {
		page.categories.remove(cat);
		initView();
		redraw();
	}
	public void removeIncome(Income in) {
		page.incomes.remove(in);
		incomeview.redraw();
		budgettingview.redraw();
		balanceview.redraw();
	}
	public void removeExpense(Category cat, Expense ex) {
		cat.removeExpense(ex);
		expenseview.updateExpenseCategory(cat);
		balanceview.redraw();
	}
	public void updateCategory(Category cat) {
		budgettingview.redraw();
		expenseview.redraw();
		balanceview.redraw();
	}
	public void updateExpenseCategory(Category cat) {
		expenseview.updateExpenseCategory(cat);
		balanceview.redraw();
	}
	public void updateIncomeView()
	{
		incomeview.redraw();
		budgettingview.redraw();
		balanceview.redraw();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	//getter / setter proxy
	public ArrayList<Income> getIncomes() {
		return page.incomes;
	}
	public void changeIncomeOrder(ArrayList<Income> list) {
		page.incomes = list;
	}
	public ArrayList<Category> getCategories() {
		return page.categories;
	}
	public void setCategories(ArrayList<Category> list) {
		page.categories = list;
		expenseview.initView();
		expenseview.redraw();
	}
	public Integer getPageID() {
		return page.getID();
	}
	public BigDecimal getTotalIncomeDeduction() {
		return page.getTotalIncomeDeduction();
	}
	public BigDecimal getTotalBudgetted() {
		return page.getTotalBudgetted();
	}
	public BigDecimal getTotalUnBudgetted() {
		BigDecimal total_free_income = getTotalIncome();
		total_free_income = total_free_income.subtract(getTotalIncomeDeduction());
		return total_free_income.subtract(getTotalBudgetted());
	}
	public BigDecimal getTotalIncome() {
		return page.getTotalIncome();
	}
	
	public BigDecimal getTotalNetIncome() {
		BigDecimal income = getTotalIncome();
		income = income.subtract(getTotalIncomeDeduction());
		return income;
	}
	
	public BigDecimal getTotalExpense() {
		BigDecimal total = BigDecimal.ZERO;
		for(Category cat : page.categories) {
			total = total.add(cat.getTotalExpense());
		}
		return total;
	}
	public BigDecimal getBalance() {
		return page.getBalance();
	}
	public ArrayList<Page> getPages() {
		return budget.pages;
	}
	public Page findPage(Integer pageid) {
		return budget.findPage(pageid);
	}
	public void save() {
		budget.save();
	}
	
	public static final String MAV_MESSAGE_UNKNOWN_SECTION_ORDER = "MainView.MESSAGE_UNKNOWN_SECTION_ORDER";

}
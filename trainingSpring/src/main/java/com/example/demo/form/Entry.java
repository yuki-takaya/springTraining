package com.example.demo.form;

public class Entry {
	private String btnFlg;
	private String date;
	private String expense_item;
	private String amount;
	private String remark;
	
	private String[] id;
	private String[] detailDate;
	private String[] detailExpense_item;
	private String[] detailAmount;
	private String[] detailRemark;
	
	public String[] getId() {
		return id;
	}
	public void setId(String[] id) {
		this.id = id;
	}
	public String[] getDetailDate() {
		return detailDate;
	}
	public void setDetailDate(String[] detailDate) {
		this.detailDate = detailDate;
	}
	public String[] getDetailExpense_item() {
		return detailExpense_item;
	}
	public void setDetailExpense_item(String[] detailExpense_item) {
		this.detailExpense_item = detailExpense_item;
	}
	public String[] getDetailAmount() {
		return detailAmount;
	}
	public void setDetailAmount(String[] detailAmount) {
		this.detailAmount = detailAmount;
	}
	public String[] getDetailRemark() {
		return detailRemark;
	}
	public void setDetailRemark(String[] detailRemark) {
		this.detailRemark = detailRemark;
	}
	public String getBtnFlg() {
		return btnFlg;
	}
	public void setBtnFlg(String btnFlg) {
		this.btnFlg = btnFlg;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getExpense_item() {
		return expense_item;
	}
	public void setExpense_item(String expense_item) {
		this.expense_item = expense_item;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}

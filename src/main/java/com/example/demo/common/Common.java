package com.example.demo.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.demo.form.ItemList;
import com.example.demo.form.SearchData;
import com.example.demo.sql.SqlList;

public class Common {
	
	// kyoutsuパラメタセット
	protected DbDataAccess da = new DbDataAccess();
	protected SqlList sql = new SqlList();
	protected SearchData sd = new SearchData();
	public final static String strNull = "NULL";
	protected final String constSearchForm = "searchForm";
	protected final String constId = "id";
	
	protected static String getYmd(Date nengetsu, boolean firstdayFlg) {
		
		Calendar cal = Calendar.getInstance();
        
        cal.setTime(nengetsu);
         
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = 1;
        
        cal.set(year, month, date, 0, 0, 0);
         
        // saishuu年月日を取得する
        if(!firstdayFlg){
        	// getumatuを取得する
        	date = cal.getActualMaximum(Calendar.DATE);
            cal.set(year, month, date, 0, 0, 0);
        }
        // yyyymmdd形式に変換
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sdf.setLenient(false);
        return sdf.format(cal.getTime()); 
	}
	// a、今月日付取得 
	protected Date getDate(boolean flg) {
		Date retDate = null;
		Calendar cal = Calendar.getInstance();
		if(flg) {
			Date now = cal.getTime();
			retDate = now;
		}else {
	        cal.add(Calendar.MONTH, -1);
	        Date last = cal.getTime();
	        retDate = last;
		}
		return retDate; 
	}
	// searchパラメタセット
	protected String getPrm(String value) {
		String retValue = value;
		if(value == null || value.equals("")) {
			retValue = strNull;
		}
		return retValue;
	}
	// nullを空白に変換
	protected String blank(String value) {
		String retValue = value;
		if(value == null) {
			retValue = " ";
		}
		return retValue;
	}
	// nullを0に変換
	protected String zero(String value) {
		String retValue = value;
		if(value == null) {
			retValue = "0";
		}
		return retValue;
	}
	// 1収支合計取得
    protected String getTotalAmount(List<SearchData> data) {
    	long totalAmount = 0;
    	String strTotalAmount = "0";
    	for(SearchData a : data) {
    		long amount = Long.parseLong(a.getDetailAmount().replace(",", "").trim());
    		// 2支払の場合はマイナスする
    		if(a.getDetailExpense_item_val().equals("2")) {
    			amount = amount * -1; 
    		}
    		totalAmount += amount;
    	}
    	strTotalAmount = String.valueOf(totalAmount); 
    	return strTotalAmount.length() > 3 ?  String.format("%1$,3d",totalAmount) : strTotalAmount;
    }
    // gethimokuMaster
    protected List<ItemList> getItemList(){
    	return getItemList(false);
    }
    protected List<ItemList> getItemList(boolean allFlg){
    	List<ItemList> list = new ArrayList<ItemList>();
    	ItemList il = new ItemList();
    	if(allFlg) {
    		il.setValue("");
        	il.setName("全て");
        	list.add(il);
        	il = new ItemList();
    	}
    	il.setValue("1");
    	il.setName("収入");
    	list.add(il);
    	il = new ItemList();
    	il.setValue("2");
    	il.setName("支出");
    	list.add(il);
    	
    	return list;
    }
    // aカンマ付加処理
    protected String comma(String data) {
    	return data.length() > 3 ? String.format("%1$,3d",Long.parseLong(data)) : data;
    }
}

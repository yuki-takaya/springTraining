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
	
	// コンストラクタ
	protected DbDataAccess da = new DbDataAccess();
	protected SqlList sql = new SqlList();
	protected SearchData sd = new SearchData();
	public final static String strNull = "NULL";
	protected final String constSearchForm = "searchForm";
	protected final String constId = "id";
	
	protected static String getYmd(Date nengetsu, boolean firstdayFlg) {
		
		Calendar cal = Calendar.getInstance();
        
        //年月をセットする
        cal.setTime(nengetsu);
         
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = 1;
        // 月初取得
        cal.set(year, month, date, 0, 0, 0);
         
        //終了年月日を取得する
        if(!firstdayFlg){
            //月末日を取得する
            date = cal.getActualMaximum(Calendar.DATE);
            cal.set(year, month, date, 0, 0, 0);
        }
        
        //日付チェック
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sdf.setLenient(false);
        return sdf.format(cal.getTime()); 
	}
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
	// 検索パラメタセット
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
	// 収支合計金額取得
    protected String getTotalAmount(List<SearchData> data) {
    	long totalAmount = 0;
    	String strTotalAmount = "0";
    	for(SearchData a : data) {
    		totalAmount += Long.parseLong(a.getDetailAmount().replace(",", "").trim());
    	}
    	strTotalAmount = String.valueOf(totalAmount); 
    	return strTotalAmount.length() > 3 ?  String.format("%1$,3d",totalAmount) : strTotalAmount;
    }
    // 費目リストの作成
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
}

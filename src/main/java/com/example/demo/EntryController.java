package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.demo.common.Common;
import com.example.demo.form.Entry;
import com.example.demo.form.SearchData;

@Controller
public class EntryController extends Common {
	
	// localhostをブラウザから起動時に動くメソッド
    @RequestMapping(value="/entry", method=RequestMethod.GET)
    public ModelAndView index(
            ModelAndView mav) {
    	mav.addObject("itemList", getItemList());
    	mav.addObject("amount", "0");
    	mav.addObject("summary", "0");
        // 戻り値
        mav.setViewName("entry");
        return mav;
    }
    
    // 家計簿記入ボタン、家計簿メニューボタン押下後に起動するメソッド
    @RequestMapping(value="/entry", method=RequestMethod.POST)
    @Transactional(readOnly=false)
    public ModelAndView submit(
    		@ModelAttribute("formModel") Entry form,
            ModelAndView mav) throws SQLException {
    	String btnFlg = form.getBtnFlg();
    	String screen = "";
    	switch(btnFlg) {
    		case "insert":
    			screen = "entry";
    			// 登録処理実行
    			List<SearchData> detail = insert(form);
    			// 収支合計金額取得
    	    	String totalAmount = getTotalAmount(detail);
    			// 検索結果データセット
    			mav.addObject("detail", detail);
    			mav.addObject("date", form.getDate());
    	    	mav.addObject("expense_item", form.getExpense_item());
    	    	mav.addObject("itemList", getItemList());
    	    	mav.addObject("amount", form.getAmount());
    	    	mav.addObject("remark", form.getRemark());
    	    	mav.addObject("summary", totalAmount);
    			break;
    		case "menu":
    			screen = "redirect:/";
    			break;
    		default:
    			screen = "redirect:/";
    	    	break;
    	}
    	mav.setViewName(screen);
        return mav; 
    }
    
    // 取消ボタン押下時の処理
    @RequestMapping(value="/entry/{id}", method=RequestMethod.POST)
    @Transactional(readOnly=false)
    public ModelAndView submitDel(
    		@PathVariable String id,
    		@ModelAttribute("formModel") Entry form,
            ModelAndView mav) throws SQLException {
    	// 削除処理実行
    	List<SearchData> detail = delete(form, id);
    	// 収支合計金額取得
    	String totalAmount = getTotalAmount(detail);
    	mav.addObject("detail", detail);
    	mav.addObject("date", form.getDate());
    	mav.addObject("expense_item", form.getExpense_item());
    	mav.addObject("itemList", getItemList());
    	mav.addObject("amount", form.getAmount());
    	mav.addObject("remark", form.getRemark());
    	mav.addObject("summary", totalAmount);
    	mav.setViewName("entry");
        return mav; 
	}
    
    // 削除本処理
    private List<SearchData> delete(Entry form, String frmId) throws SQLException {
    	// 削除処理
    	da.execute(sql.deleteHouseholdAccounts(), new String[] {frmId});
    	// listデータに格納
		List<SearchData> data = new ArrayList<SearchData>();
		// 画面データをセッターに格納
		if(form.getId() != null) {
			for (int i=0; i< form.getId().length; i++ ) {
				sd = new SearchData();
				String id = form.getId()[i];
				// 削除対象Id以外を画面に再描写する
				if(!id.equals(frmId)) {
					String date = form.getDetailDate()[i];
					String expense_item = form.getDetailExpense_item()[i];
					String amount = form.getDetailHiddenAmount()[i];
					String remark = form.getDetailRemark()[i];
					sd.setId(id);
					sd.setDetailDate(date);
					sd.setDetailExpense_item(expense_item);
					sd.setDetailAmount(comma(amount));
					sd.setDetailHiddenAmount(amount);
					sd.setDetailRemark(remark);
					data.add(sd);
				}
			}
		}
		return data;
    }
    
    // 登録本処理
    private List<SearchData> insert(Entry form) throws SQLException {
    	// MAXID取得
		List<HashMap<String, String>> listMaxId 
    	= da.select(sql.getId(), new String[] {"MAX_ID"}, new String[] {});
		String maxId = listMaxId.get(0).get("MAX_ID") == null ? "1" : listMaxId.get(0).get("MAX_ID").toString();
		// INSERT処理
		da.execute(sql.insertHouseholdAccounts()
				, new String[] {
						 maxId
						,getPrm(form.getDate())
						,getPrm(form.getExpense_item())
						,getPrm(zero(form.getAmount()))
						,getPrm(form.getRemark())
						});
		// 検索処理
		List<HashMap<String, String>> listData
    	= da.select(sql.entryHouseholdAccounts()
    			, new String[] {
    					 "ID"
    					,"INPUT_DATE"
    					,"EXPENSE_ITEM"
    					,"AMOUNT"
    					,"REMARK"
    					}, new String[] {maxId});
		// listデータに格納
		List<SearchData> data = new ArrayList<SearchData>();
		// 画面データをセッターに格納
		if(form.getId() != null) {
			int loopcnt = form.getId().length;
			for (int i=0; i < loopcnt; i++ ) {
				sd = new SearchData();
				String id = form.getId()[i];
				String date = form.getDetailDate()[i];
				String expense_item = form.getDetailExpense_item()[i];
				String amount = form.getDetailHiddenAmount()[i];
				String remark = form.getDetailRemark()[i];
				sd.setId(id);
				sd.setDetailDate(date);
				sd.setDetailExpense_item(expense_item);
				sd.setDetailAmount(comma(amount));
				sd.setDetailHiddenAmount(amount);
				sd.setDetailRemark(remark);
				data.add(sd);
			}
		}
		// 検索データをセッターに格納
		listData.forEach(
				list -> {
					sd = new SearchData();
					sd.setId(list.get("ID").toString());
					sd.setDetailDate(list.get("INPUT_DATE").toString());
					sd.setDetailExpense_item(list.get("EXPENSE_ITEM").toString());
					sd.setDetailAmount(comma(list.get("AMOUNT").toString()));
					sd.setDetailHiddenAmount(list.get("AMOUNT").toString());
					sd.setDetailRemark(blank(list.get("REMARK")));
					data.add(sd);
				});
    	return data;
    }
}

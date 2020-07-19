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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.example.demo.common.Common;
import com.example.demo.form.SearchData;
import com.example.demo.form.Update;

@Controller
public class UpdateController extends Common {
	
	// localhostをブラウザから起動時に動くメソッド
    @RequestMapping(value="/update", method=RequestMethod.GET)
    public ModelAndView index(
    		HttpServletRequest request,
            ModelAndView mav) throws SQLException {
    	// セッションよりIDの取得
    	HttpSession session = request.getSession();
    	String id = (String) session.getAttribute(constId);
    	// 検索処理実行
    	List<SearchData> detail = search(id);
    	// 収支合計金額取得
    	String totalAmount = getTotalAmount(detail);
    	mav.addObject("detail", detail);
    	mav.addObject("id", detail.get(0).getId());
    	mav.addObject("date", detail.get(0).getDetailDate().replace("/", ""));
    	mav.addObject("expense_item", detail.get(0).getDetailExpense_item_val());
    	mav.addObject("itemList", getItemList());
    	mav.addObject("amount", detail.get(0).getDetailAmount());
    	mav.addObject("remark", detail.get(0).getDetailRemark());
    	mav.addObject("summary", totalAmount);
        // 戻り画面
        mav.setViewName("update");
        return mav;
    }
    // 家計簿の更新ボタン押下時の処理
    @RequestMapping(value="/update", method=RequestMethod.POST)
    @Transactional(readOnly=false)
    public ModelAndView submitUpd(
    		@ModelAttribute("formModel") Update form,
            ModelAndView mav) throws SQLException {
    	// idの取得
    	String id = form.getId();
    	// ボタン変数セット
    	String btnFlg = form.getBtnFlg();
    	String screen = "";
    	switch(btnFlg) {
			case "update":
				// 更新処理実行
		    	update(form, id);
				screen = "redirect:/search/update";
				break;
			case "search":
				screen = "redirect:/search/back";
				break;
			default:
    			screen = "redirect:/";
    	    	break;
    	}
    	mav.setViewName(screen);
        return mav; 
	}
    // 検索本処理
    private List<SearchData> search(String id) throws SQLException {
    	// 検索処理
		List<HashMap<String, String>> listData
    	= da.select(sql.entryHouseholdAccounts()
    			, new String[] {
    					 "ID"
    					,"INPUT_DATE"
    					,"EXPENSE_ITEM"
    					,"AMOUNT"
    					,"REMARK"
    					,"EXPENSE_ITEM_VAL"
    					}, new String[] {getPrm(id)});
		// listデータに格納
		List<SearchData> data = new ArrayList<SearchData>();
		// 検索データをセッターに格納
		listData.forEach(
				list -> {
					sd = new SearchData();
					sd.setId(list.get("ID").toString());
					sd.setDetailDate(list.get("INPUT_DATE").toString());
					sd.setDetailExpense_item(list.get("EXPENSE_ITEM").toString());
					sd.setDetailAmount(list.get("AMOUNT").toString().length() > 3 ? String.format("%1$,3d",Long.parseLong(list.get("AMOUNT").toString())) : list.get("AMOUNT").toString());
					sd.setDetailRemark(blank(list.get("REMARK")));
					sd.setDetailExpense_item_val(blank(list.get("EXPENSE_ITEM_VAL")));
					data.add(sd);
				});
    	return data;
    }
    // 更新本処理
    private void update(Update form, String id) throws SQLException {
    	// 更新処理
    	da.execute(sql.updateHouseholdAccounts()
    			, new String[] {
    					 getPrm(form.getDate())
    					,getPrm(form.getExpense_item())
    					,getPrm(zero(form.getAmount()))
    					,getPrm(form.getRemark())
    					,getPrm(id)
    					});
    }
}

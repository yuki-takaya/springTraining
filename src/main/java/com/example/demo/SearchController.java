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
import com.example.demo.form.Search;
import com.example.demo.form.SearchData;

@Controller
public class SearchController extends Common {
	
	// localhostをブラウザから起動時に動くメソッド
    @RequestMapping(value="/search/{mode}", method=RequestMethod.GET)
    public ModelAndView index(
    		@PathVariable String mode,
    		HttpServletRequest request,
            ModelAndView mav) throws SQLException {
    	HttpSession session = request.getSession();
    	Search form = new Search();
    	List<SearchData> detail = new ArrayList<SearchData>();
    	// 更新画面から遷移した場合
    	if(!mode.equals("init")) {
	    	form = (Search) session.getAttribute(constSearchForm);
	    	// 検索処理実行
			detail = search(form);
    	}else {
        // メニューから遷移した場合
    		form.setStrDate(getYmd(getDate(true), true));
    		form.setEndDate(getYmd(getDate(true), false));
    		form.setExpense_item("");
    		form.setStrAmount("-999999999");
    		form.setEndAmount("9999999999");
    	}
		// 画面セット
		setScreen(mav, form, detail, request);
        // 戻り値
        mav.setViewName("search");
        return mav;
    }
    
    // 家計履歴検索ボタン、家計簿メニューボタン押下後に起動するメソッド
    @RequestMapping(value="/search", method=RequestMethod.POST)
    @Transactional(readOnly=false)
    public ModelAndView submitSearch(
    		HttpServletRequest request,
    		@ModelAttribute("formModel") Search form,
            ModelAndView mav) throws SQLException {
    	String btnFlg = form.getBtnFlg();
    	String screen = "";
    	switch(btnFlg) {
    		case "search":
    			screen = "search";
    			// 検索処理実行
    			List<SearchData> detail = search(form);
    			// 画面セット
    			setScreen(mav, form, detail, request);
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
    
    // 削除、更新ボタン押下時の処理
    @RequestMapping(value="/search/{id}", method=RequestMethod.POST)
    @Transactional(readOnly=false)
    public ModelAndView submitUpDel(
    		HttpServletRequest request,
    		@PathVariable String id,
    		@ModelAttribute("formModel") Search form,
            ModelAndView mav) throws SQLException {
    	String btnFlg = form.getBtnFlg();
    	String screen = "";
    	// IDをセッションに格納
    	HttpSession session = request.getSession();
    	session.removeAttribute(constId);
    	session.setAttribute(constId, id);
    	// 明細セット用List
    	List<SearchData> detail = new ArrayList<SearchData>();
    	switch(btnFlg) {
			case "update":
				screen = "redirect:/update";
				break;
			case "delete":
				screen = "search" ;
				// 削除処理実行
		    	delete(id);
		    	// 検索処理
		    	detail = search(form);
		    	// 画面セット
    			setScreen(mav, form, detail);
				break;
			default:
				screen = "redirect:/";
		    	break;
		}
    	mav.setViewName(screen);
        return mav; 
	}
    // 削除本処理
    private void delete(String frmId) throws SQLException {
    	// 削除処理
    	da.execute(sql.deleteHouseholdAccounts(), new String[] {frmId});
    }
    // 検索本処理
    private List<SearchData> search(Search form) throws SQLException {
    	// 検索処理
		List<HashMap<String, String>> listData
    	= da.select(sql.searchHouseholdAccounts()
    			, new String[] {
    					 "ID"
    					,"INPUT_DATE"
    					,"EXPENSE_ITEM"
    					,"AMOUNT"
    					,"REMARK"
    					}
    			, new String[] {
    					 getPrm(form.getStrDate())
    					,getPrm(form.getStrDate())
    					,getPrm(form.getEndDate())
    					,getPrm(form.getEndDate())
    					,getPrm(form.getExpense_item())
    					,getPrm(form.getExpense_item())
    					,getPrm(form.getStrAmount())
    					,getPrm(form.getStrAmount())
    					,getPrm(form.getEndAmount())
    					,getPrm(form.getEndAmount())
    					,getPrm(form.getRemark()).equals(strNull) ? strNull : "%" + form.getRemark() + "%" 
    					,getPrm(form.getRemark()).equals(strNull) ? strNull : "%" + form.getRemark() + "%"
    					});
		// listデータに格納
		List<SearchData> data = new ArrayList<SearchData>();
		// 検索データをセッターに格納
		listData.forEach(
				list -> {
					sd = new SearchData();
					sd.setId(list.get("ID").toString());
					sd.setDetailDate(list.get("INPUT_DATE").toString());
					sd.setDetailExpense_item(list.get("EXPENSE_ITEM").toString());
					sd.setDetailAmount(comma(list.get("AMOUNT").toString()));
					sd.setDetailRemark(blank(list.get("REMARK")));
					data.add(sd);
				});
    	return data;
    }
    // 検索結果データセット
    private void setScreen(ModelAndView mav, Search form, List<SearchData> detail) {
    	setScreen(mav, form, detail, null);
    }
    private void setScreen(ModelAndView mav, Search form, List<SearchData> detail, HttpServletRequest request) {
    	// 収支合計金額取得
    	String totalAmount = getTotalAmount(detail);
		// 検索結果データセット
		mav.addObject("detail", detail);
		mav.addObject("strDate", form.getStrDate());
		mav.addObject("endDate", form.getEndDate());
    	mav.addObject("expense_item", form.getExpense_item());
    	mav.addObject("itemList", getItemList(true));
    	mav.addObject("strAmount", form.getStrAmount());
    	mav.addObject("endAmount", form.getEndAmount());
    	mav.addObject("remark", form.getRemark());
    	mav.addObject("summary", totalAmount);
    	// セッション情報へ書込み
    	if(request != null) {    		
	    	HttpSession session = request.getSession();
	    	session.removeAttribute(constSearchForm);
	    	session.setAttribute(constSearchForm, form);
    	}
    }
}

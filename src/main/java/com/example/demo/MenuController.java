package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.example.demo.common.Common;
import com.example.demo.form.Menu;

@Controller
public class MenuController extends Common {
	
	// localhostをブラウザから起動時に動くメソッド
    @RequestMapping(value="/", method=RequestMethod.GET)
    public ModelAndView index(
            ModelAndView mav) throws SQLException {
        // メモデータ取得
        List<HashMap<String, String>> list 
        	= da.select(sql.getNoteData(), new String[] {"NOTE_DATA"}, new String[] {});
        Menu note = new Menu();
        if(list.size() > 0) {
        	note.setNote_data(list.get(0).get("NOTE_DATA").toString());
        }
        
        // 今月取得
        String strMonth = getYmd(getDate(true), true);
        String endMonth = getYmd(getDate(true), false);
        // 前月取得
        String strLastMonth = getYmd(getDate(false), true);
        String endLastMonth = getYmd(getDate(false), false);
        // 今月収支合計の取得
        List<HashMap<String, String>> listMonth 
    	= da.select(sql.getIncomeExpenditure(), new String[] {"MONTH_SUM_AMOUNT"}, new String[] {strMonth, endMonth});
        String monthMoney = zero(listMonth.get(0).get("MONTH_SUM_AMOUNT")); 
        // 前月収支合計の取得
        List<HashMap<String, String>> listLastMonth 
    	= da.select(sql.getIncomeExpenditure(), new String[] {"MONTH_SUM_AMOUNT"}, new String[] {strLastMonth, endLastMonth});
        String lastMonthMoney = zero(listLastMonth.get(0).get("MONTH_SUM_AMOUNT"));
        // 差額取得
        String diffMoney = String.valueOf(Long.parseLong(monthMoney) - Long.parseLong(lastMonthMoney)); 
        // カンマ付加
        monthMoney = comma(monthMoney);
        lastMonthMoney = comma(lastMonthMoney);
        diffMoney = comma(diffMoney);
        // 戻り値
        mav.setViewName("menu");
        mav.addObject("note_data", note.getNote_data());
        mav.addObject("month_shushi", "￥" + monthMoney);
        mav.addObject("lastmonth_shushi", "￥" + lastMonthMoney);
        mav.addObject("diff_amount", "￥" + diffMoney);
        return mav;
    }
    
    // 送信ボタン押下後に起動するメソッド
    @RequestMapping(value="/", method=RequestMethod.POST)
    @Transactional(readOnly=false)
    public ModelAndView submit(
    		@ModelAttribute("formModel") Menu form,
            ModelAndView mav) throws SQLException {
    	String btnFlg = form.getBtnFlg();
    	String screen = "";
    	switch(btnFlg) {
    		case "search":
    			screen = "redirect:/" + btnFlg + "/init";
    			break;
    		case "entry":
    			screen = "redirect:/" + btnFlg;
    			break;
    		case "logoff":
    			screen = "redirect:/";
    			break;
    		case "note":
    	    	// データ削除
    	    	da.execute(sql.deleteNote(), new String[] {});
    	    	// データ登録
    	    	da.execute(sql.insertNote(), new String[] {form.getNote_data()});
    			screen = "redirect:/";
    			break;
    		default:
    			screen = "redirect:/";
    			break;
    	}
        return new ModelAndView(screen);
    }
}

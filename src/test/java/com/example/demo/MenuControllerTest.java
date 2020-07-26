package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.form.Menu;

@SpringBootTest
public class MenuControllerTest {
	
	private MockMvc mockMvc;
	
	private final String note_data = "UT メモテスト";
	
	@Autowired
	private MenuController target;
	
	@BeforeEach
	public void setup() {
	    mockMvc = MockMvcBuilders.standaloneSetup(target).build();
	}
	
	// getリクエストのUnit Test
	@Test
	public void getReqTest() throws Exception {
		// テスト結果
		mockMvc.perform(get("/"))
					.andExpect(status().isOk())
					.andExpect(view().name("menu"))
					.andExpect(model().attribute("note_data", note_data))
					.andExpect(model().attribute("month_shushi", "￥6,000,000"))
					.andExpect(model().attribute("lastmonth_shushi", "￥0"))
					.andExpect(model().attribute("diff_amount", "￥6,000,000"))
				;
	}
	// postリクエストのUnit Test
	@Test
	void postReqTest() throws Exception {
		
		String[] screen = new String[] {
				 "entry"
				,"search"
				,"logoff"
				,"note"
		};
		List<String> sList = Arrays.asList(screen);
		
		sList.forEach( s ->  {
			// フォーム内にインプットセット
			Menu form = new Menu();
			form.setNote_data(note_data);
			form.setBtnFlg(s);
			String viewname = "";
	
			switch(s) {
				case "entry":
					viewname = "redirect:/entry";
					break;
				case "search":
					viewname = "redirect:/search/init";
					break;
				case "logoff":
					viewname = "redirect:/";
					break;
				case "note":
					viewname = "redirect:/";
					break;
			}
			// テスト結果
			try {
				mockMvc.perform(post("/").flashAttr("formModel", form))
							.andExpect(view().name(viewname))
						;
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		});
	}
}

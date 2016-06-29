package com.xiyuan.template.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiyuan.template.bo.TestBo;
import com.xiyuan.template.dao.Dao;

@Controller
public class TestController {

	@Autowired
	private Dao dao;
	
	@RequestMapping(value="/test")
	@ResponseBody
	public Map<String, Object> test() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("message", "test");
		return result;
	}
	
	@RequestMapping(value="/test/list")
	@ResponseBody
	public Map<String, Object> testList() {
		Map<String, Object> result = new HashMap<String, Object>();
		ArrayList<TestBo> list = dao.all(TestBo.class);
		result.put("list", list);
		result.put("success", true);
		result.put("message", "test");
		return result;
	}
	
}

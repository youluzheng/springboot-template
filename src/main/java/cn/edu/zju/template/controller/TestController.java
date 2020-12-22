package cn.edu.zju.template.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.zju.template.interceptor.TokenNotRequire;
import cn.edu.zju.template.service.TestService;
import cn.edu.zju.template.util.ResponseUtil;

@RestController
@RequestMapping("test")
public class TestController {
	
	@Autowired
	private TestService testService;
	
	@GetMapping("test")
	@TokenNotRequire
	public ResponseUtil test() {
		return ResponseUtil.getDefaultResponse();
	}
	
	@GetMapping("test1")
	@TokenNotRequire
	public ResponseUtil test1() {
		return new ResponseUtil("result", this.testService.test());
	}
}

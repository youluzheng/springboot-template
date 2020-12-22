package cn.edu.zju.template.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.zju.template.dao.TestDao;

@Service
public class TestService {
	
	@Autowired
	private TestDao testDao;
	
	public String test() {
		String daoMsg = this.testDao.test();
		return daoMsg + ", service";
	}
}

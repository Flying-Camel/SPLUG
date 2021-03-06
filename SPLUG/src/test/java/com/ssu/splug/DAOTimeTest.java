package com.ssu.splug;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.ssu.persistence.TimeDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring"
		+ "/**/*-context.xml" })
public class DAOTimeTest {

	@Inject
	private TimeDAO dao;
	
	@Test
	public void timeTest() throws Exception{
		System.out.println(dao.getTime());
	}// timeTest()

}// class

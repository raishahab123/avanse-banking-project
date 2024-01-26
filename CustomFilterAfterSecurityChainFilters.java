package com.avanse.springboot.configuration;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import com.avanse.springboot.model.Page;
import com.avanse.springboot.service.PageService;



@Component
public class CustomFilterAfterSecurityChainFilters extends GenericFilterBean {
	
	@Autowired
	TemplateEngine templateEngine;
	
	@Autowired
	PageService pageService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//System.out.println("Entering Our Custom Filter");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		String requestUrl = req.getRequestURI();
		List<String[]> allUrisForPages = pageService.getAllUrisForPages();
		boolean check = false;
		long pageId=0;
		for(String[] pageDetails : allUrisForPages) {
			
			if(pageDetails[1].equals(requestUrl)) {
				check=true;
				pageId = Long.valueOf(pageDetails[0]);
			}
		}
		if(check) {
			Page page = pageService.getPageById(pageId).get();
			res.setContentType("text/html");
			String renderedHtml=templateEngine.process("addedPages/"+page.getExtractedFileName(), new IContext() {
				
				@Override
				public Set<String> getVariableNames() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Object getVariable(String name) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Locale getLocale() {
					// TODO Auto-generated method stub
					return Locale.ENGLISH;
				}
				
				@Override
				public boolean containsVariable(String name) {
					// TODO Auto-generated method stub
					return false;
				}
			});
			res.getWriter().println(renderedHtml);
		} else {
			chain.doFilter(request, response);
		}		
	}

}

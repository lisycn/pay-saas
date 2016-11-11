package cn.vfinance.open.saas.web.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public interface VerificationCodeService {
	
	public void generate( HttpServletRequest request, HttpServletResponse response ) throws Exception;

}

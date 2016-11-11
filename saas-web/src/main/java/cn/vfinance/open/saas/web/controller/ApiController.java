package cn.vfinance.open.saas.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping
public class ApiController {

	 /**
	  * API统一下单
	  * @return
	  */
	 @RequestMapping("all_san_pay")
     public ModelAndView allSanPay(){
    	 return new ModelAndView("/views/api/paySan");
     }
     
     /**
      * API交易查询
      * @return
      */
	 @RequestMapping("query_trade")
     public ModelAndView queryTrade(){
         return new ModelAndView("/views/api/tradeQuery");	 
     }
	 
	 /**
	  * 用户协议
	  * @return
	  */
	 @RequestMapping("user_agreement")
	 public ModelAndView userAgreement(){
		 return new ModelAndView("/views/api/Agreement");
	 }
	
}

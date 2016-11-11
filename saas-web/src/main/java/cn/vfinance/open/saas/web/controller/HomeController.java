package cn.vfinance.open.saas.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by qiuwei on 2016/9/28.
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String toHome() {
        return "views/home";
    }
}

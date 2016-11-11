package cn.vfinance.open.saas.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sean Weng
 */
@RestController
@RequestMapping
public class HealthCheckController {

    @RequestMapping(value = "_health_check")
    public String healthCheck() {
        return "OK";
    }
}

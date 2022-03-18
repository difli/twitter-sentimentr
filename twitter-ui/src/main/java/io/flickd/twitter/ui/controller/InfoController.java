package io.flickd.twitter.ui.controller;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.flickd.twitter.ui.model.ApplicationInfo;

@Controller
public class InfoController {
	
	@Value("${twitter_query:#}")
	String twitter_query;

    private Environment springEnvironment;

    @Autowired
    public InfoController(Environment springEnvironment) {
        this.springEnvironment = springEnvironment;
    }

    @RequestMapping(value = "/appinfo")
    public @ResponseBody ApplicationInfo info(HttpServletRequest request) {
    	ApplicationInfo ai = new ApplicationInfo(springEnvironment.getActiveProfiles(), getServiceNames(), getTwitterQuery(), request.getLocalAddr(), request.getLocalPort());
    	return ai;
    }

    @RequestMapping(value = "/env")
    @ResponseBody
    public Map<String, String> showEnvironment() {
        return System.getenv();
    }

//    @RequestMapping(value = "/service")
//    @ResponseBody
//    public List<ServiceInfo> showServiceInfo() {
//            return new ArrayList<ServiceInfo>();
//    }
 
    private String[] getServiceNames() {
            return new String[]{};
    }
    
    private String getTwitterQuery()
    {
		return "'"+twitter_query+"'";
    }
}
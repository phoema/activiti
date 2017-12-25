package phoema.activiti.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(value="/http",method={RequestMethod.POST,RequestMethod.GET})
public class HttpController {
 
	private Environment env;
	/**
	 * Oauth认证信息，需要跳转页面
	 * @param request
	 * @param response
	 * @throws OAuthSystemException
	 * @throws IOException
	 */
    @RequestMapping(value="/test")
    public String authorize(HttpServletRequest request, HttpServletResponse response) throws  IOException {
 

    	return "Hello World!";

 
    }

    
}
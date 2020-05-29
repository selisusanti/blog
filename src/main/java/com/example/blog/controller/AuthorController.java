package com.example.blog.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.dto.MyPage;
import com.example.blog.dto.MyPageable;
import com.example.blog.dto.request.AuthorDTO;
import com.example.blog.dto.request.AuthorPasswordDTO;
import com.example.blog.dto.request.DeleteDTO;
import com.example.blog.dto.response.ResponseAuthorDTO;
import com.example.blog.dto.response.ResponseBaseDTO;
import com.example.blog.dto.util.PageConverter;
import com.example.blog.model.Author;
import com.example.blog.service.AuthorService;
import com.example.blog.dto.response.ResponseOauthDTO;


import javax.sql.DataSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.security.oauth2.provider.ClientDetailsService;


@RestController
public class AuthorController {
    
    @Autowired
    AuthorService AuthorService;

    @Autowired
	private DataSource dataSource;

	@Autowired
    private ClientDetailsService clientDetailsStore;

    @Autowired
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
	}

    @Autowired
	private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/authors", method = RequestMethod.POST)
    public ResponseBaseDTO createAuthors(@Valid @RequestBody Author request) {
        return ResponseBaseDTO.ok(AuthorService.save(request));
    }

    @RequestMapping(value = "/authors/{id}", method = RequestMethod.GET)
    public ResponseBaseDTO<ResponseAuthorDTO> getOne(@PathVariable Long id) {
        return ResponseBaseDTO.ok(AuthorService.findById(id));
    }

    @RequestMapping(value = "/authors", method = RequestMethod.DELETE)
    public ResponseBaseDTO<ResponseAuthorDTO> deleteComment(@Valid @RequestBody DeleteDTO request) {
        return ResponseBaseDTO.ok(AuthorService.deleteById(request));
    }

    @RequestMapping(value = "/authors/{id}", method = RequestMethod.PUT)
    public ResponseBaseDTO updateAuthors(@PathVariable Long id,@Valid @RequestBody AuthorDTO request) {
        return ResponseBaseDTO.ok(AuthorService.update(id,request));
    }

    @RequestMapping(value = "/authors/{id}/password", method = RequestMethod.PUT)
    public ResponseBaseDTO updateAuthors(@PathVariable Long id,@Valid @RequestBody AuthorPasswordDTO request) {
        return ResponseBaseDTO.ok(AuthorService.updatePassword(id,request));
    }


    @RequestMapping(value = "/authors", method = RequestMethod.GET)
    public ResponseBaseDTO getAuthors(MyPageable pageable, 
        @RequestParam(required = false) String param, HttpServletRequest request
    ){

        Page<ResponseAuthorDTO> author;

        if(param != null){
            author = AuthorService.findByName(MyPageable.convertToPageable(pageable),param);
        }else{
            author = AuthorService.findAll(MyPageable.convertToPageable(pageable)); 
        }
        PageConverter<ResponseAuthorDTO> converter = new PageConverter<>();
       
        String search = ""; 
        String url = String.format("%s://%s:%d/authors/",request.getScheme(),  request.getServerName(), request.getServerPort());

 
        if(param != null){
            search += "&param="+param;
        }
 
        MyPage<ResponseAuthorDTO> response = converter.convert(author, url, search);
 
        return ResponseBaseDTO.ok(response);

    }


    
    //Normal Login
	@RequestMapping(value="/api/login", method = RequestMethod.POST)
	public  ResponseEntity<ResponseOauthDTO> login(@RequestParam HashMap<String, String> params) throws Exception
	{
		ResponseOauthDTO response = new ResponseOauthDTO();
		Author checkUser =  AuthorService.findByUsername(params.get("username"));

	    if (checkUser != null)
		{
			try {
				OAuth2AccessToken token = this.getToken(params);
			
				response.setStatus(true);
				response.setCode("200");
				response.setMessage("success");
				response.setData(token);

				return new ResponseEntity<>(response, HttpStatus.OK);
			} catch (Exception exception) {
				
                    response.setStatus(false);
                    response.setCode("500");
                    response.setMessage(exception.getMessage());
			}
		} else {
			throw new Exception();
		}
		

		return new ResponseEntity<ResponseOauthDTO>(response, HttpStatus.UNAUTHORIZED);
    }
    
    private OAuth2AccessToken getToken(HashMap<String, String> params) throws HttpRequestMethodNotSupportedException {
		if (params.get("username") == null ) {
			throw new UsernameNotFoundException("username not found");
		}

		if (params.get("password") == null) {
			throw new UsernameNotFoundException("password not found");
		}

		if (params.get("client_id") == null) {
			throw new UsernameNotFoundException("client_id not found");
		}

		if (params.get("client_secret") == null) {
			throw new UsernameNotFoundException("client_secret not found");
		}

		DefaultOAuth2RequestFactory defaultOAuth2RequestFactory = new DefaultOAuth2RequestFactory(clientDetailsStore);

		AuthorizationRequest authorizationRequest = defaultOAuth2RequestFactory.createAuthorizationRequest(params);
		authorizationRequest.setApproved(true);

		OAuth2Request oauth2Request = defaultOAuth2RequestFactory.createOAuth2Request(authorizationRequest);
		
		final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
				params.get("username"), params.get("password"));
		Authentication authentication = authenticationManager.authenticate(loginToken);

		OAuth2Authentication authenticationRequest = new OAuth2Authentication(oauth2Request, authentication);
		authenticationRequest.setAuthenticated(true);

		OAuth2AccessToken token = tokenServices().createAccessToken(authenticationRequest);


        Map<String, Object> adInfo = new HashMap<>();

        adInfo.put("role", null);
        
        try {

            Author author = (Author) authentication.getPrincipal();
            
			adInfo.put("role", author.getRole());

		} catch (Exception e) {
			e.printStackTrace();
		}

		((DefaultOAuth2AccessToken) token).setAdditionalInformation(adInfo);

		return token;
    } 
    
    @Autowired
	public AuthorizationServerTokenServices tokenServices() {
		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setAccessTokenValiditySeconds(-1);

		defaultTokenServices.setTokenStore(tokenStore());
		return defaultTokenServices;
	}

}
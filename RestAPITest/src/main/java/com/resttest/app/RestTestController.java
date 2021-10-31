package com.resttest.app;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resttest.app.message.RestTestRequestMessage;
import com.resttest.domain.business.RestTestBusinessLogic;

@Path("/Test")
@ApplicationScoped
public class RestTestController {
	
	
	@Inject
	private RestTestBusinessLogic logic;
	
	@Path("/hello")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response hello() throws Exception{
		
		
		try {
			return Response.ok("THIS IS REST-API TEST APPLICATION. HELLO WORLD!!!").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	
	/*
	 * おそらくこのPOSTで受け取るjson文字列によって
	 * キャスト先のクラスを判断するロジックを本番では入れないといけないイメージです
	 * 以下ではJAX-RS標準でキャストさせてしまっているのでどのようにやるかあまりイメージついていないです
	 */
	@Path("/postjson")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public Response postjson(@Valid RestTestRequestMessage message) throws Exception{
		
		System.out.println(message.getId());
		System.out.println(message.getName());
		
		try {
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	
	@Path("/id")
	@Produces(MediaType.APPLICATION_JSON)
    @GET
	public Response info(@QueryParam("id")final String id) throws Exception{
		
    	try {
    		return Response.ok(logic.getName(id)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
    	
	}
	
	@Path("/test")
	@Produces()
	@GET
	public Response test() throws Exception{
		
		try {
		
			RestTestRequestMessage reqMessage = new RestTestRequestMessage();
			reqMessage.setId("test");
			reqMessage.setName("testName");
			ObjectMapper mapper = new ObjectMapper();
			
			System.out.println(mapper.writeValueAsString(reqMessage));
			
			return Response.ok().build();
		
		}catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

}

package com.resttest.app;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.transaction.RollbackException;
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

import com.resttest.app.message.RestTestRequest2Message;
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
	 * (2021/11/21 追記:MessageBodyWriterなる実装が入っているようです。これを独自実装するときはinterfaceとしての
	 * MessageBodyWriterをimplementsし、実装をしたあとに@Providerをつける形になります。
	 * interfaceのまま参照実装をそのまま使えるのはjava1.8以降のコンパイラ機能です)
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
	
	@Path("/postjson2")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public Response postjson2(@Valid RestTestRequest2Message message) throws Exception{
		
		
		Jsonb jsonb = JsonbBuilder.create();
		System.out.println(jsonb.toJson(message));
		
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
			
			return Response.ok().build();
		
		}catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testjsonb")
	@Produces()
	@GET
	public Response testjsonb() throws Exception{
		
		try {
		
			RestTestRequest2Message reqMessage = new RestTestRequest2Message();
			reqMessage.setId("test");
			reqMessage.setName("testName");
			reqMessage.setMessage(new RestTestRequestMessage());
			Jsonb jsonb = JsonbBuilder.create();
			
			//System.out.println(mapper.writeValueAsString(reqMessage));
			System.out.println(jsonb.toJson(reqMessage));
			
			return Response.ok().build();
		
		}catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testtimeout")
	@Produces()
	@GET
	//タイムアウトを狙います。wildflyのdefault-timeout設定を60秒にした状態で、このメソッドを実行します
	public Response testtimeout() throws Exception{
		
		try {
			System.out.println("started!!!");
			String test = logic.getTimeOutException("this is transaction-timeout test");
			System.out.println(test);
			return Response.ok().build();
		

		}catch (RollbackException e) {
			System.out.println("success transaction-timout test");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
			
		}catch (Exception e) {
			System.out.println("failed transaction-timout test");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

}

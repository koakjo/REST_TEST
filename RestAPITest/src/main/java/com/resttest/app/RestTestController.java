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
		

		} catch (RollbackException e) {
			System.out.println("success transaction-timout test");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
			
		} catch (Exception e) {
			System.out.println("failed transaction-timout test");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testsoamethod1")
	@Produces()
	@GET
	public Response testSOAmethod1() throws Exception{
		
		try {
			System.out.println("testSOAmethod1 started!!!");
			String test = logic.testSOAmethod1();
			System.out.println(test);
			return Response.ok().build();
		
		} catch (RollbackException e) {
			System.out.println("RollbackException testSOAmethod1 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
			
		} catch (Exception e) {
			System.out.println("Exception testSOAmethod1 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testsoamethod2")
	@Produces()
	@GET
	public Response testSOAmethod2() throws Exception{
		
		try {
			System.out.println("testSOAmethod2 started!!!");
			String test = logic.testSOAmethod2();
			System.out.println(test);
			return Response.ok().build();
		
		} catch (RollbackException e) {
			System.out.println("RollbackException testSOAmethod2 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
			
		} catch (Exception e) {
			System.out.println("Exception testSOAmethod2 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testsoamethod3")
	@Produces()
	@GET
	public Response testSOAmethod3() throws Exception{
		
		try {
			System.out.println("testSOAmethod3 started!!!");
			String test = logic.testSOAmethod3();
			System.out.println(test);
			return Response.ok().build();
		
		} catch (RollbackException e) {
			System.out.println("RollbackException testSOAmethod3 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
			
		} catch (Exception e) {
			System.out.println("Exception testSOAmethod3 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testsoamethod4")
	@Produces()
	@GET
	public Response testSOAmethod4() throws Exception{
		
		try {
			System.out.println("testSOAmethod4 started!!!");
			String test = logic.testSOAmethod4();
			System.out.println(test);
			return Response.ok().build();
		
		} catch (RollbackException e) {
			System.out.println("RollbackException testSOAmethod4 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
			
		} catch (Exception e) {
			System.out.println("Exception testSOAmethod4 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testsoamethod5")
	@Produces()
	@GET
	public Response testSOAmethod5() throws Exception{
		
		try {
			System.out.println("testSOAmethod5 started!!!");
			String test = logic.testSOAmethod5();
			System.out.println(test);
			return Response.ok().build();
		
		} catch (RollbackException e) {
			System.out.println("RollbackException testSOAmethod5 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
			
		} catch (Exception e) {
			System.out.println("Exception testSOAmethod5 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testsoamethod6")
	@Produces()
	@GET
	public Response testSOAmethod6() throws Exception{
		
		try {
			System.out.println("testSOAmethod6 started!!!");
			String test = logic.testSOAmethod6();
			System.out.println(test);
			return Response.ok().build();
		
		} catch (RollbackException e) {
			System.out.println("RollbackException testSOAmethod6 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
			
		} catch (Exception e) {
			System.out.println("Exception testSOAmethod6 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testsoamethod7")
	@Produces()
	@GET
	public Response testSOAmethod7() throws Exception{
		
		try {
			System.out.println("testSOAmethod7 started!!!");
			String test = logic.testSOAmethod7();
			System.out.println(test);
			return Response.ok().build();
		
		} catch (RollbackException e) {
			System.out.println("RollbackException testSOAmethod7 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
			
		} catch (Exception e) {
			System.out.println("Exception testSOAmethod7 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testusertransaction8")
	@Produces()
	@GET
	public Response testUserTransaction8() throws Exception{
		
		try {
			System.out.println("testUserTransaction8 started!!!");
			String test = logic.testUserTransaction8();
			System.out.println(test);
			return Response.ok().build();
		
		} catch (RollbackException e) {
			System.out.println("RollbackException testUserTransaction8 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
			
		} catch (Exception e) {
			System.out.println("Exception testUserTransaction8 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testusertransaction9")
	@Produces()
	@GET
	public Response testUserTransaction9() throws Exception{
		
		try {
			System.out.println("testUserTransaction9 started!!!");
			String test = logic.testUserTransaction9();
			System.out.println(test);
			return Response.ok().build();
		
		} catch (RollbackException e) {
			System.out.println("RollbackException testUserTransaction9 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
			
		} catch (Exception e) {
			System.out.println("Exception testUserTransaction9 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testusertransaction10")
	@Produces()
	@GET
	public Response testUserTransaction10() throws Exception{
		
		try {
			System.out.println("testUserTransaction10 started!!!");
			String test = logic.testUserTransaction10();
			System.out.println(test);
			return Response.ok().build();
		
		} catch (RollbackException e) {
			System.out.println("RollbackException testUserTransaction10 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
			
		} catch (Exception e) {
			System.out.println("Exception testUserTransaction10 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testusertransaction11")
	@Produces()
	@GET
	public Response testUserTransaction11() throws Exception{
		
		try {
			System.out.println("testUserTransaction11 started!!!");
			String test = logic.testUserTransaction11();
			System.out.println(test);
			return Response.ok().build();
		
		} catch (RollbackException e) {
			System.out.println("RollbackException testUserTransaction11 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
			
		} catch (Exception e) {
			System.out.println("Exception testUserTransaction11 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testusertransaction12")
	@Produces()
	@GET
	public Response testUserTransaction12() throws Exception{
		
		try {
			System.out.println("testUserTransaction12 started!!!");
			String test = logic.testUserTransaction12();
			System.out.println(test);
			return Response.ok().build();
		
		} catch (RollbackException e) {
			System.out.println("RollbackException testUserTransaction12 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
			
		} catch (Exception e) {
			System.out.println("Exception testUserTransaction12 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testusertransaction13")
	@Produces()
	@GET
	public Response testUserTransaction13() throws Exception{
		
		try {
			System.out.println("testUserTransaction13 started!!!");
			String test = logic.testUserTransaction13();
			System.out.println(test);
			return Response.ok().build();
		
		} catch (RollbackException e) {
			System.out.println("RollbackException testUserTransaction13 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
			
		} catch (Exception e) {
			System.out.println("Exception testUserTransaction13 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testtimelimiter14")
	@Produces()
	@GET
	public Response testTimeLimiter14() throws Exception{
		
		try {
			System.out.println("testTimeLimiter14 started!!!");
			String test = logic.testTimeLimiter14();
			System.out.println(test);
			return Response.ok().build();
		/*
		} catch (RollbackException e) {
			System.out.println("RollbackException testUserTransaction13 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		*/
		} catch (Exception e) {
			System.out.println("Exception testUserTransaction13 controller");
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testrestclient15")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public Response testRestClient15(com.resttest.domain.model15.RestAPITestmethodInputDto request) {
		
		com.resttest.domain.model15.RestAPITestmethodOutputDto methodOutput = 
				new com.resttest.domain.model15.RestAPITestmethodOutputDto();
		com.resttest.domain.model15.OutputTestDto outputdetail = 
				new com.resttest.domain.model15.OutputTestDto();
		
		Jsonb jsonb = JsonbBuilder.create();
		
		
		try {
			
			System.out.println("RestTestApp testRestClient15 started.");
			methodOutput.setName(logic.testRestMethod15(request.getId()));
			
			//検証
			outputdetail.setId(request.getId());
			outputdetail.setName(methodOutput.getName());
			outputdetail.setExplanation("ちゃんと入るかチェック");
			
			methodOutput.setOutputTestDto(outputdetail);
			
			System.out.println("RestTestApp testRestClient15 return.");
			System.out.println(jsonb.toJson(methodOutput)); 
			return Response.ok(jsonb.toJson(methodOutput)).build();
		
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testrestclient16")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public Response testRestClient16(com.resttest.domain.model16.RestAPITestmethodInputDto request) {
		
		com.resttest.domain.model16.RestAPITestmethodOutputDto methodOutput = 
				new com.resttest.domain.model16.RestAPITestmethodOutputDto();
		com.resttest.domain.model16.OutputTestDto outputdetail = 
				new com.resttest.domain.model16.OutputTestDto();
		
		Jsonb jsonb = JsonbBuilder.create();
		
		try {
			
			System.out.println("RestTestApp testRestClient16 started.");
			methodOutput.setName(logic.testRestMethod16(request.getId()));
			
			//検証
			outputdetail.setId(request.getId());
			outputdetail.setName(methodOutput.getName());
			outputdetail.setExplanation("ちゃんと入るかチェック");
			
			methodOutput.setOutputTest(outputdetail);
			
			System.out.println("RestTestApp testRestClient16 return.");
			System.out.println(jsonb.toJson(methodOutput)); 
			
			return Response.ok(jsonb.toJson(methodOutput)).build();
		
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Path("/testrestclient17")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public Response testRestClient17(com.resttest.domain.model17.RestAPITestmethodInputDto request) {
		
		com.resttest.domain.model17.RestAPITestmethodOutputDto methodOutput = 
				new com.resttest.domain.model17.RestAPITestmethodOutputDto();
		com.resttest.domain.model17.OutputTestDto outputdetail = 
				new com.resttest.domain.model17.OutputTestDto();
		
		Jsonb jsonb = JsonbBuilder.create();
		
		
		try {
			
			System.out.println("RestTestApp testRestClient17 started.");
			
			methodOutput.setName(logic.testRestMethod17(request.getId()));
			
			//検証
			outputdetail.setId(request.getId());
			outputdetail.setName(methodOutput.getName());
			outputdetail.setExplanation("ちゃんと入るかチェック");
			
			methodOutput.setOutputTestDto(outputdetail);
			
			System.out.println("RestTestApp testRestClient17 return.");
			System.out.println(jsonb.toJson(methodOutput)); 
			
			return Response.ok(jsonb.toJson(methodOutput)).build();
		
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
}

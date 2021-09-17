package br.com.rmv.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI= "http://localhost:8080/tasks-backend/";
	}
	
	@Test
	public void deveRetornarTarefas() {
		RestAssured.given()
		.when()
			.get("/todo")			
		.then()
			.statusCode(200)
		;
	}
	
	@Test
	public void deveAdicionarTarefaComSucesso() {
		RestAssured.given()
			.body("{\n"
					+ "	\"task\" : \"Teste via API\",\n"
					+ "	\"dueDate\" : \"2030-01-01\" \n"
					+ "} ")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")			
		.then()
			.statusCode(201)
		;
	}
	
	@Test
	public void deveRemoverTarefaComSucesso() {
		Integer id = RestAssured.given()
			.body("{\n"
					+ "	\"task\" : \"Teste via API\",\n"
					+ "	\"dueDate\" : \"2030-01-01\" \n"
					+ "} ")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")			
		.then()
			.statusCode(201)
			.extract().path("id")
		;
		
		RestAssured.given()
		.when()
			.delete("/todo/"+id)
		.then()
			.statusCode(204)
		;
	}
	
	@Test
	public void naoDeveAdicionarTarefaInvalida() {
		RestAssured.given()
			.body("{\n"
					+ "	\"task\" : \"Teste via API\",\n"
					+ "	\"dueDate\" : \"2000-01-01\" \n"
					+ "} ")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")			
		.then()
			.statusCode(400)
			.body("message", CoreMatchers.is("Due date must not be in past"))
		;
	}

}

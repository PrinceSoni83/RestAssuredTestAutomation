package stepDefinitions;



import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.json.simple.ItemList;
import org.json.simple.JSONObject;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;



public class CoinMarketCapSteps {

	String server = "https://pro-api.coinmarketcap.com";
	String accessToken = "8646d49f-5dfd-4481-8bb8-848ae58d2bff";
	RequestSpecification reqSpec = null;
	Response resp = null;
	String symbol_bitcoin,symbol_tether,symbol_ethereum;

	@Given("The coin market cap api is up and running")
	public void the_coin_market_cap_api_is_up_and_running() {
		reqSpec = given().baseUri(server)
				.accept(ContentType.JSON)
				.header("X-CMC_PRO_API_KEY", accessToken);      
	}

	@When("I hit the api with get request and end point as {string}")
	public void i_hit_the_api_with_get_request_and_end_point_as(String endpoint) {
		resp = reqSpec.when().get(endpoint);
		//System.out.println("Response return as : " + resp.prettyPrint());
	}

	@Then("I should get a response code as {int}")
	public void i_should_get_a_response_code_as(Integer statusCode) {
		resp.then().assertThat().statusCode(statusCode);

	}

	@Then("I should get the ID for Bitcoin")
	public void i_should_get_the_id_for_bitcoin() {
		// passe the json to iterate into the json response
		List<JSONObject> dataObjects = resp.jsonPath().getList("data");
		for (int i = 0; i < 5; i++) {
			Map<String,String> obj = dataObjects.get(i);
			String currencyName = obj.get("name");
			if(currencyName.equals("Bitcoin")) {
				symbol_bitcoin = obj.get("symbol");
				System.out.println("Symbol for Bitcoin : "+ symbol_bitcoin);
				break;
			}
		}	

	}

	@Then("I should get the ID for Tether")
	public void i_should_get_the_id_for_tether() {
		// passe the json to iterate into the json response
		List<JSONObject> dataObjects = resp.jsonPath().getList("data");
		for (int i = 0; i < dataObjects.size(); i++) {
			Map<String,String> obj = dataObjects.get(i);
			String currencyName = obj.get("name");
			if(currencyName.equals("Tether")) {
				symbol_tether = obj.get("symbol");
				System.out.println("Symbol for Bitcoin : "+ symbol_tether);
				break;
			}
		}	

	}

	@Then("I should get the ID for Ethereum")
	public void i_should_get_the_id_for_ethereum() {
		// passe the json to iterate into the json response
		List<JSONObject> dataObjects = resp.jsonPath().getList("data");
		for (int i = 0; i < dataObjects.size(); i++) {
			Map<String,String> obj = dataObjects.get(i);
			String currencyName = obj.get("name");
			if(currencyName.equals("Ethereum")) {
				symbol_ethereum = obj.get("symbol");
				System.out.println("Symbol for Bitcoin : "+ symbol_ethereum);
				break;
			}
		}	

	}

	@When("I convert the currencies to Bolivian Boliviano using endpoint {string}")
	public void i_convert_the_currencies_to_bolivian_boliviano_using_endpoint(String endpoint) {
		reqSpec = given().baseUri(server)
				.accept(ContentType.JSON)
				.header("X-CMC_PRO_API_KEY", accessToken)
				.queryParam("symbol",symbol_bitcoin)
				.queryParam("amount",10) // Amount is mandatory field 
				.queryParam("convert","BOB"); // BOB is the currency code for Bolivian Boliviano,
		resp = reqSpec.when().get(endpoint);
		resp.then().assertThat().statusCode(200);
		System.out.println("Response of the coverted currency" + resp.asPrettyString());

	}

	@Then("I get the converted price of these currencies")
	public void i_get_the_converted_price_of_these_currencies() {
		resp.then().assertThat().body("data.amount",equalTo(10));
		resp.then().assertThat().body("data.quote.BOB", hasKey("price"));
		resp.then().assertThat().body("data.quote.BOB", hasKey("last_updated"));

	}	

	@When("I hit the api with get request and endpoint as {string} with id {int}")
	public void i_hit_the_api_with_get_request_and_end_point_as_with_id(String endpoint, Integer id) {
		reqSpec = given().baseUri(server)
				.accept(ContentType.JSON)
				.header("X-CMC_PRO_API_KEY", accessToken)
				.queryParam("id",id); // this is Ethereum id
		resp = reqSpec.when().get(endpoint);
		System.out.println("Response :" + resp.asPrettyString());
	}	

	@Then("Response should have a logo url {string}")
	public void response_should_have_a_logo_url(String logo) {
		resp.then().assertThat().body("data.1027.logo",equalTo(logo));
	}
	@Then("Response should have Technical Doc field {string}")
	public void response_should_have_technical_doc_field(String techDoc) {
		resp.then().assertThat().body("data.1027.urls.technical_doc[0]", equalTo(techDoc));
	}
	@Then("Response should have Symbol of currency should as {string}")
	public void response_should_have_symbol_of_currency_should_as(String currencySymbol) {
		resp.then().assertThat().body("data.1027.symbol",equalTo(currencySymbol));
	}
	@Then("Response should have date added field as {string}")
	public void response_should_have_date_added_field_as(String dateAdded) {
		resp.then().assertThat().body("data.1027.date_added",equalTo(dateAdded));
	}
	@Then("Response should have mineable tag")
	public void response_should_have_mineable_tag() {
		resp.then().assertThat().body("data.1027.tag-names[0]",equalTo("Mineable"));
	}

	@When("I hit the api with get request and endpoint as {string} with first {int} id's")
	public void i_hit_the_api_with_get_request_and_endpoint_as_with_first_id_s(String endpoint, Integer reqFrequency) {
		for (int idCounter=1; idCounter<=10; idCounter++) {
			reqSpec = given().baseUri(server)
					.accept(ContentType.JSON)
					.header("X-CMC_PRO_API_KEY", accessToken)
					.queryParam("id","1,2,3,4,5,6,7,8,9,10"); // as per the api docs we can add the comma seperated id's
			resp = reqSpec.when().get(endpoint);
		}   
	}

	@Then("The correct cryptocurrencies should have been printed out")
	public void the_correct_cryptocurrencies_should_have_been_printed_out() {
		for (int idCounter=1; idCounter<=10; idCounter++) {
			ArrayList<String> dataObjects = resp.jsonPath().getJsonObject("data." + idCounter + ".tags");
			System.out.println("for id : " + idCounter + " tag is : "+ dataObjects.get(0));
		}
	}

}


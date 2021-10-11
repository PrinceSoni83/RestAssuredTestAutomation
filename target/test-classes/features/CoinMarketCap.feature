Feature: Validating CoinMarketCap API


	@Test
  Scenario:  Retrieve ID's for BTC , USDT and ETH
    Given The coin market cap api is up and running
    When I hit the api with get request and end point as "/v1/cryptocurrency/map"
    Then I should get a response code as 200
    And I should get the ID for Bitcoin
    And I should get the ID for Tether
	 	And I should get the ID for Ethereum
    When I convert the currencies to Bolivian Boliviano using endpoint "/v1/tools/price-conversion"
    Then I get the converted price of these currencies
  
    @Test
   Scenario:  Retrieve the Ethereum technical documentation
    Given The coin market cap api is up and running
    When I hit the api with get request and endpoint as "/v1/cryptocurrency/info" with id 1027
    Then I should get a response code as 200
    And Response should have a logo url "https://s2.coinmarketcap.com/static/img/coins/64x64/1027.png" 
	  And Response should have Symbol of currency should as "ETH"
    And Response should have date added field as "2015-08-07T00:00:00.000Z"
    And Response should have mineable tag
	 	And Response should have Technical Doc field "https://github.com/ethereum/wiki/wiki/White-Paper"
	   
	 @Test
	 Scenario:  Retrieve the first 10 currencies
	  Given The coin market cap api is up and running
	  When I hit the api with get request and endpoint as "/v1/cryptocurrency/info" with first 10 id's
	  Then The correct cryptocurrencies should have been printed out
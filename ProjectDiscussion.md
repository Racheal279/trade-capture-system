Step 2: 
When testing the initial failing tests, generate cashflows method was not working so had to add the schedule repository and set it to 1m and now cashflows are 24 since each tradeleg has 12 cashflows. 

Step 3: 
I intially used RSQL parser  for the rsql method but later switched to 
import io.github.perplexhub.rsql.RSQLJPASupport;  instead of import cz.jirutka.rsql.parser.RSQLParser; so I added these to the pom file and included the error methods and updated the trade repository
filter, search and rsql are all the same endpoint using GetMapping.
My filter method takes multiple parameters
The search has 5 end points, filter has only one endpoint. The filter method uses search. Mock trade entity objects (trade1 and trade2) were created to make an entitylist and trade mapper was imported. This fixed the error where I was expecting 2 trade entities but tests ran and they were 0. 


public ValidationResult validateTradeBusinessRules(TradeDTO tradeDTO); checks if the trade data follows important business rules before the trade is created or updated 
created a validationresult class
I included the import jakarta.persistence.EntityManager; The create and amend methods also need to be updated according to the logic  added in the public ValidationResult validateTradeBusinessRules(TradeDTO tradeDTO) and validateUserPrivilege method. 

I also included the tests in tradeservicetest for each method and the tradecontrollertest; trade controller test (mocks Service); this is unit testing, 
The unit-under-test is the TradeService this is mainly the intergration tests, this is when we use a REAL TradeService, but we mock out the Database. Unit Testing, focuses on only one layer at a time. For the controller tests, a fake trade was used. 
The Unit-under-test is the TradeController - the behaviour of the TradeController is what we focus on testing. 
I had initially only added tradeservicetests and later realised I need to include tradecontrollertests too. 

Integration Testing, focuses on testing all the layers/ multiple layers

Added search, filter, rsql, mytrades, book id trades, summary and daily summary over to trade controller class and the tests over in the tradeservicetest class, I also included additional integration tests in the tradecontrollertest class to test the functionality of rsql and search methods. 

Created dailysummaryDTO and tradesummary DTO classes including constructors, getters and setters
Included validationresult class for the public validation methods, realised it needed adderror methods for validation purposes
added getters and setters for application user and user profile files for improved encapsulation

Optional<ApplicationUser> findByLoginIdIgnoreCase(String loginId); was added to applicationuser repository to allow case insensitivity, this is a Spring JPA query method 

Used JPA Specification Executor was added to the TradeRepository for the multi search criteria and RSQL end points and then find by tradeuser, bookid and trade date  methods are added  as derived query methods to allow easy access by tradeservice class. 
The findall specification method is used to build the query dynamically and makes the search process more efficient so the rsql jpa works by parsing through the specification object which then passes over to findall() method. import jakarta.persistence.EntityManager was also imported to build the RSQL query. 
added autowired for book mapping 
added trademapper, domain specification and list imports for tradeservice test 


Step 4: fixing calculatecashflowvalue 

The issue for the error is because double result = (notional * rate * months) / 12; does not divide the rate by 100 and this leads to 8,750,000 instead of 87,500. The bigdecimal leads to precision issues. We have to refactor the bigdecimal method for all calculations while also using the bigdecimal divide () while also using roundingmode for accuracy
BigDecimal decimalRate = rate.divide(ONE_HUNDRED, 10, RoundingMode.HALF_UP); //this divides and rounds to 2 decimal places  
make public BigDecimal calculateCashflowValue(TradeLeg leg, int monthsInterval) changed it from private to public


Front end and Swagger testing
I initially had an empty placeholder when running swagger testing
my profile page front end  was not properly running, had unsupported exception in applicationuser class so included getters to allow encapsulation 
when using swagger, I had the wrong information showing up for the results so imported the  trade mapper, fixed urls and made sure to test all the methods. 
package UserAccount;

	import org.testng.annotations.Test;
	import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

	public class TwitterUser {
		
		static Properties Property = new Properties();
		static String RootFolder = System.getProperty("user.dir");
		static Logger logger;
		static ExtentHtmlReporter htmlReporter;
		static ExtentReports extrepo = new ExtentReports();
		ExtentTest Extst;
		ExtentTest scenario;
		Response SerResp;
		
		String GettweetId = "";
		static String TweetMessage = "";
		static String UpdateStatus = "";
		static String Resource = "";
		static String ShowStatus = "";
		static String consumerKey = "";
		static String consumnerSecret = "";
		static String accessToken ="";
		static String accessTSecret = "";
		
		
		@BeforeSuite
		public static void StatupConfig() throws IOException {
		
		// Initialize Logger 
			logger = Logger.getLogger(TwitterUser.class);
			
		// Initialize Property File 
			PropertyConfigurator.configure("Log4j.properties");
			FileInputStream PropPath = new FileInputStream(RootFolder +"\\src\\test\\java\\GenericRepository.properties");
			Property.load(PropPath);
			
		// Initialize ExtentReport
			htmlReporter = new ExtentHtmlReporter(RootFolder + Property.getProperty("ReportPath"));
			extrepo.attachReporter(htmlReporter);
			htmlReporter.getSystemAttributeContext();
			htmlReporter.setAppendExisting(true);
			htmlReporter.getStartTime();
			htmlReporter.getEndTime();
			htmlReporter.config().setReportName("API_Automation_Report");
			htmlReporter.config().setDocumentTitle("TwitterAPiReport");
			
		// Initialize RestAssured 	
			RestAssured.baseURI = Property.getProperty("Entrypoint");
			Resource = Property.getProperty("Resource");
		
		// Get Value From property file
			UpdateStatus = Property.getProperty("UpdateStatus");
			ShowStatus = Property.getProperty("ShowStatus");
			TweetMessage = Property.getProperty("TweetMessage");
			consumerKey = Property.getProperty("consumerKey");
			consumnerSecret = Property.getProperty("consumnerSecret");
			accessToken = Property.getProperty("accessToken");
			accessTSecret = Property.getProperty("accessTSecret");

		}
		
		@Test (priority=1)
		public void CreateTweets() throws IOException{
		
		  Extst = extrepo.createTest("CreateTwitts", "");
		  SerResp = given().
					auth().
					oauth(consumerKey, consumnerSecret, accessToken, accessTSecret).
					when().log().all().
					post(Resource+UpdateStatus+TweetMessage).
					then().assertThat().statusCode(200).
					extract().response();
		  Extst.createNode("API Request URL","" ).info(Property.getProperty("Entrypoint")+Resource+UpdateStatus+TweetMessage);
		  Extst.createNode("Server Response ", "").info(SerResp.prettyPrint());
		  logger.info("CreateTwitts : Server Response ------------");
		  logger.info(SerResp.getBody().jsonPath().prettify());
		  
					JsonPath json = SerResp.jsonPath();
					GettweetId=json.get("id_str");
					
		}
		
		@Test (priority=2)
		public void getTweet() throws IOException{
			Extst = extrepo.createTest("GetTwitts", "");
			     SerResp =	given().
							auth().
							oauth(consumerKey, consumnerSecret, accessToken, accessTSecret).
							params("screen_name","twitterapi","count", 1).log().all().
							when().
							get(Resource+"user_timeline.json").
							then().assertThat().statusCode(200).
							extract().response();
			     Extst.createNode("API Request URL","" ).info(Property.getProperty("Entrypoint")+Resource+"user_timeline.json");
				 Extst.createNode("Server Response ", "").info(SerResp.prettyPrint());
			     logger.info("getTweet : Server Response ------------");
				 logger.info(SerResp.getBody().jsonPath().prettify());
		}
		
		@Test (priority=3)
		public void ShowStatusTweets() throws IOException{
			Extst = extrepo.createTest("ShowStatusTweets", "");	
		  SerResp = given().
					auth().
					oauth(consumerKey, consumnerSecret, accessToken, accessTSecret).
					params("id", GettweetId).log().all().
					when().
					get(Resource+ShowStatus).
					then().assertThat().statusCode(200).
					extract().response();
		     Extst.createNode("API Request URL","" ).info(Property.getProperty("Entrypoint")+Resource+ShowStatus);
			 Extst.createNode("Server Response ", "").info(SerResp.prettyPrint());
		  	 logger.info("ShowStatusTwitts : Server Response ------------");
			 logger.info(SerResp.getBody().jsonPath().prettify());
		}		
				
		@Test (priority=4)
		public void ValidatewithExistingTweet() throws IOException{
			     Extst = extrepo.createTest("ValidatewithExistingTweet", "");	
				 SerResp = given().
							auth().
							oauth(consumerKey, consumnerSecret, accessToken, accessTSecret).
							when().
							post(Resource+UpdateStatus+TweetMessage).
							then().assertThat().statusCode(403).log().all().
							extract().response();
				     Extst.createNode("API Request URL","" ).info(Property.getProperty("Entrypoint")+Resource+UpdateStatus+TweetMessage);
					 Extst.createNode("Server Response ", "").info(SerResp.prettyPrint());
				     logger.info("ValidatewithExistingTweet : Server Response ------------");
					 logger.info(SerResp.getBody().jsonPath().prettify());	
			
				}
				
		@Test (priority=5)
		public void DeleteTweet() throws IOException{
			Extst = extrepo.createTest("DeleteTweet", "");	
				  SerResp = given().
							auth().
							oauth(consumerKey, consumnerSecret, accessToken, accessTSecret).
							when().
							post(Resource+"destroy/"+GettweetId+".json").
					    	then().assertThat().statusCode(200).log().all().
							extract().response();
				     Extst.createNode("API Request URL","" ).info(Property.getProperty("Entrypoint")+Resource+"destroy/"+GettweetId+".json");
					 Extst.createNode("Server Response ", "").info(SerResp.prettyPrint());
				     logger.info("DeleteTweet : Server Response ------------");
					 logger.info(SerResp.getBody().jsonPath().prettify());	
				}
				
		@Test (priority=6)
		public void CheckTweet() throws IOException{
		Extst = extrepo.createTest("CheckTweet", "");	
		  SerResp = given().
					auth().
					oauth(consumerKey, consumnerSecret, accessToken, accessTSecret).
					params("id", GettweetId).log().all().
					when().
					get(Resource+ShowStatus).
					then().assertThat().statusCode(404).log().all().
					extract().response();
		     Extst.createNode("API Request URL","" ).info(Property.getProperty("Entrypoint")+Resource+ShowStatus);
			 Extst.createNode("Server Response ", "").info(SerResp.prettyPrint());
		     logger.info("CheckTweet : Server Response ------------");
			 logger.info(SerResp.getBody().jsonPath().prettify());	
		}	
		
		
		@AfterSuite		
		public static void TearDown() throws IOException {

			extrepo.flush();
		
		}		

		
	}


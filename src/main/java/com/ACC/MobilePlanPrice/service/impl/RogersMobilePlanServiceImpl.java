package com.ACC.MobilePlanPrice.service.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import com.ACC.MobilePlanPrice.model.MobilePlan;
import com.ACC.MobilePlanPrice.service.MobilePlanService;


@Service
public class RogersMobilePlanServiceImpl implements MobilePlanService {

	private WebDriver driver;
    private WebDriverWait wait;


    public void initializeDriver(String url) {
    	ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        
    	driver = new ChromeDriver(options);
    	wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(url);
        driver.manage().window().maximize();
    }
    
    
   
	@Override
	public List<MobilePlan> getMobilePlan() {
		
		List<MobilePlan> rogersPlanList= new ArrayList<>();

		initializeDriver("https://www.rogers.com/plans?icid=R_WIR_CMH_6WMCMZ");		
        List<WebElement> totalplans = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//ds-tile//div[contains(@class,'dsa-vertical-tile d-flex')]")));
        
        
        for(int i=0;i<totalplans.size();i++) {
        	MobilePlan plan= new MobilePlan();        	
        	JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

            jsExecutor.executeScript("window.scrollBy(0,1000);");      	
        	
        	List<WebElement> planName = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//ds-tile//div[contains(@class,'dsa-vertical-tile d-flex')]//p[contains(@class,'heading')]")));
            
        	List<WebElement> monthlyCost=wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//ds-tile//div[contains(@class,'dsa-vertical-tile d-flex')]//ds-price//div[@class='ds-price']")));
        	
        	
        	List<WebElement> dataAllowance= wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//ds-tile//div[contains(@class,'dsa-vertical-tile d-flex')]//li//b")));
            
        	
        	List<WebElement>networkCoverage=wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//ds-tile//div[contains(@class,'dsa-vertical-tile d-flex')]//li[contains(text(),'network')]")));
            
        	
        	List<WebElement> callAndTextAllowance=wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//ds-tile//div[contains(@class,'dsa-vertical-tile d-flex')]//*[contains(text(),'talk') or contains(text(),'Calling')]")));
            
        	
        	
        	plan.setDataAllowance(dataAllowance.get(i).getText());
        	plan.setMonthlyCost(monthlyCost.get(i).getAttribute("aria-label"));
        	plan.setPlanName(planName.get(i).getText());
        	plan.setCallAndTextAllowance(callAndTextAllowance.get(i).getText());
        	plan.setNetworkCoverage(networkCoverage.get(i).getText());
        	plan.setProvider("Rogers");

        	rogersPlanList.add(plan);
        }
        
        driver.quit();		
		return rogersPlanList;
	}

}

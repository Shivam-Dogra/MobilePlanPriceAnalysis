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
public class FreedomMobilePlanServiceImpl implements MobilePlanService {

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
		
		List<MobilePlan> freedomPlanList= new ArrayList<>();
		initializeDriver("https://shop.freedommobile.ca/en-CA/plans?isByopPlanFirstFlow=true");		
        
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        jsExecutor.executeScript("window.scrollBy(0,1200);");      	
    	
        List<WebElement> totalplans = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//li[@data-testid='planComponent']//p[@data-testid='plan-eyebrow']")));
        
        try {
        	for(int i=0;i<totalplans.size();i++) {
            	MobilePlan plan= new MobilePlan();
            	
            	List<WebElement> planNameA = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//li[@data-testid='planComponent']//p[@data-testid='plan-eyebrow']")));
                
            	List<WebElement> planNameB = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//li[@data-testid='planComponent']//p[@data-testid='plan-title']")));
                
            			
            	   
            	List<WebElement> monthlyCost=wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//li[@data-testid='planComponent']//span[@data-testid='plan-monthly-price']")));
                
            	
            	List<WebElement> dataAllowance= wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//p[@data-testid='plan-title']")));
                
            	
            	List<WebElement>networkCoverage=wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//p[@data-testid='plan-tail-text']")));
                
            	
            	List<WebElement> callAndTextAllowance=wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@data-testid='data-talk-text-description']//p[contains(text(),'talk')]")));
                
            	plan.setPlanName(planNameA.get(i).getText()+" "+planNameB.get(i).getText());
            	plan.setMonthlyCost(monthlyCost.get(i).getText());
            	plan.setDataAllowance(dataAllowance.get(i).getText());
            	
            	
            
            	//if(i==4)
            		plan.setNetworkCoverage("5G");
            	//else
            		//plan.setNetworkCoverage(networkCoverage.get(i).getText());
            	
            	plan.setCallAndTextAllowance(callAndTextAllowance.get(i).getText());       	
            	plan.setProvider("Freedom");

            	freedomPlanList.add(plan);
            }
            
            driver.quit();	
            System.out.println("completed freedom ");
    	
        }
        catch(Exception e) {
        	e.printStackTrace();
        	System.out.println(e);
        }
        return freedomPlanList;
                
	}

}

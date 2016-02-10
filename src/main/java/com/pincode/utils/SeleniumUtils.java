package com.pincode.utils;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class SeleniumUtils {
	
	/**
	 * Convert Options to List
	 * @param driver
	 * @param id - elementId
	 * @return
	 */
	public List<String> labelsFromWebElement(WebDriver driver, String id){
		WebElement element = driver.findElement(By.id(id));
		Select select = new Select(element);
		List<WebElement> options = select.getOptions();

		List<String> labels = new ArrayList<String>();
		
		// get text from options
		for (WebElement option : options) {
			if(!option.getText().trim().equals("Select Locality")){
				labels.add(option.getText());
			}
		}
		
		return labels;
	}
	
	/**
	 * Check if element is appear
	 * @param driver
	 * @param by
	 * @return
	 * @throws InterruptedException
	 */
	public Boolean findStaleElement(WebDriver driver, By by) throws InterruptedException{
		boolean result = false;
		int attemps = 0;
		while (attemps < 5) {
			try{
				driver.findElement(by).click();
				result = true;
				break;
			}catch(StaleElementReferenceException e){
			}
			Thread.sleep(1000);
			attemps++;
		}
		return result;
	}
	
	/**
	 * Check if element is appear
	 * @param element
	 * @return
	 * @throws InterruptedException
	 */
	public Boolean findStaleElement(WebElement element) throws InterruptedException{
		boolean result = false;
		int attemps = 0;
		while (attemps < 5) {
			try{
				element.click();
				result = true;
				break;
			}catch(StaleElementReferenceException e){
			}
			Thread.sleep(1000);
			attemps++;
		}
		return result;
	}
}

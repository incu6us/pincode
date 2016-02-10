

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pincode.utils.FWriter;
import com.pincode.utils.SeleniumUtils;

public class Runner {
	
	// use an empty variable for recursive search
	private static final String STATE = "ANDAMAN & NICOBAR ISLANDS";
	private static final String DIRECTION = "NORTH AND MIDDLE ANDAMAN";

	private static final String FILE = "PATH-TO-SAVE";
	private static final String SEPARATOR = "|";

	/*
	 * Main method
	 */
	private static final Logger LOG = Logger.getLogger(SeleniumUtils.class);

	public static void main(String[] args) throws InterruptedException, IOException {
		SeleniumUtils seleniumUtils = new SeleniumUtils();
		FWriter fw = new FWriter(FILE);

		List<String> stateLabels = null;
		List<String> distLabels = null;
		String strToShow = null;
		
		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
		WebDriver driver = new FirefoxDriver(dc);
		driver.get("http://cept.gov.in/lbpsd/placesearch.aspx");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Include All
		driver.findElement(By.id("rbnIncludeAll")).click();
		Thread.sleep(1000);

		// search for each state option
		if(STATE.isEmpty()){
			stateLabels = seleniumUtils.labelsFromWebElement(driver, "ddlState");
		}else{
			stateLabels = Arrays.asList(STATE);
		}
		for (String state : stateLabels) {
			new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='ddlState']/option[normalize-space(text())='" + state + "']")));
			driver.findElement(By.xpath(".//*[@id='ddlState']/option[normalize-space(text())='" + state + "']")).click();

			// search for each district
			if(DIRECTION.isEmpty()){
				distLabels = seleniumUtils.labelsFromWebElement(driver, "ddlDist");
			}else{
				distLabels = Arrays.asList(DIRECTION);
			}
			for (String dist : distLabels) {
				new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='ddlDist']/option[normalize-space(text())='" + dist + "']")));
				driver.findElement(By.xpath(".//*[@id='ddlDist']/option[normalize-space(text())='" + dist + "']")).click();

				// City/town/village (ddlVCT)
				List<String> vctLabels = seleniumUtils.labelsFromWebElement(driver, "ddlVCT");
				for (String vct : vctLabels) {
					new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='ddlVCT']/option[normalize-space(text())=\"" + vct + "\"]")));
					driver.findElement(By.xpath(".//*[@id='ddlVCT']/option[normalize-space(text())=\"" + vct + "\"]")).click();

					// Locality (ddlLocality)
					List<String> ddlLocalityLabels = seleniumUtils.labelsFromWebElement(driver, "ddlLocality");
					for (String ddlLocality : ddlLocalityLabels) {
						new WebDriverWait(driver, 30)
								.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='ddlLocality']/option[normalize-space(text())=\"" + ddlLocality + "\"]")));
						driver.findElement(By.xpath(".//*[@id='ddlLocality']/option[normalize-space(text())=\"" + ddlLocality + "\"]")).click();

						// getting pin code
						try {
							new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='txtPincode']")));
							String pinCode = driver.findElement(By.xpath(".//*[@id='txtPincode']")).getAttribute("value");

							if (!pinCode.equals("")) {
								strToShow = state + SEPARATOR + dist + SEPARATOR + vct + SEPARATOR + ddlLocality + SEPARATOR + pinCode;

								LOG.info(strToShow);
								fw.write(strToShow);
							}
						} catch (TimeoutException | NoSuchElementException e) {
							// "Server Error in '/lbpsd' Application
							LOG.warn("Server error appears -> " + strToShow);
							driver.navigate().back();
						}
						Thread.sleep(1000);
					}
				}
			}
		}
		
	}
}

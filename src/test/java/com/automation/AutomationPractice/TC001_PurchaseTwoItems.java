package com.automation.AutomationPractice;

import org.testng.annotations.Test;

import com.automation.AutomationPractice.BasePage.BasePage;
import com.automation.AutomationPractice.PageUI.LoginPage;
import com.aventstack.extentreports.Status;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TC001_PurchaseTwoItems extends BasePage {

	public LoginPage page;
	double total_products, total_shipping, total_tax, total;
	public String timeStamp;
	public static String colorOfProduct;

	@Test(priority = 0)
	public void customerLogin() {
		logger = report.createTest("User Login");
		page = new LoginPage(driver);
		page.customerLogin("qatest@gmail.com", "password");
	}

	@Test(priority = 1)
	public void purchaseTwoItems() {
		logger = report.createTest("Purchase Products");
		String beforeSize = null;
		List<WebElement> products = driver
				.findElements(By.xpath("//div[@id='center_column']/div/ul[1]/li/div/div/div[1]/a[2]"));
		for (int i = 1; i <= products.size(); i++) {

			if (i == 1 || i == 3) {

				try {
					WebElement product = driver.findElement(
							By.xpath("//div[@id='center_column']/div/ul[1]/li[" + i + "]/div/div/div[1]/a[2]"));
					domClick(product);
					WebElement productSize = driver.findElement(By.xpath("//select[@id='group_1']"));
					selectOption(productSize, "M");
					beforeSize = productSize.getText().replace('S', ' ').replace('L', ' ').trim();
					driver.findElement(By.xpath("//p[@id='add_to_cart']")).click();
					elementVisible(10, driver.findElement(By.xpath("//span[@title='Continue shopping']")));
					driver.findElement(By.xpath("//span[@title='Continue shopping']")).click();
					page.homeLink.click();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		domClick(driver.findElement(By.xpath("//a[@id='button_order_cart']")));
		List<WebElement> checkoutSize = driver.findElements(By.xpath("//table[@id='cart_summary']/tbody/tr"));
		Double productsCount = new Double(0.00);

		for (int j = 1; j <= checkoutSize.size(); j++) {

			String sizeOfProduct = driver
					.findElement(By.xpath("//table[@id='cart_summary']/tbody/tr[" + j + "]/td[2]/small[2]")).getText()
					.substring(23).trim();
			colorOfProduct = driver
					.findElement(By.xpath("//table[@id='cart_summary']/tbody/tr[" + j + "]/td[2]/small[2]")).getText()
					.substring(0,10).trim();
			String color = driver
			.findElement(By.xpath("//table[@id='cart_summary']/tbody/tr[" + j + "]/td[2]/small[2]")).getText();
			
			System.out.println(colorOfProduct + color);
			String e = driver.findElement(By.xpath("//table[@id='cart_summary']/tbody/tr[" + j + "]/td[4]/span/span"))
					.getText().replace('$', ' ').trim();
			double productCost = Double.parseDouble(e);
			productsCount = productsCount + productCost;
			productsCount = Double.parseDouble(new DecimalFormat("##.##").format(productsCount));

			if (sizeOfProduct.equals(beforeSize)) {

				logger.log(Status.PASS, "Size of Products are Matching");
			} else

				logger.log(Status.PASS, "Size of Products are not matching");
		}

		total_products = getdouble(convertDouble(
				driver.findElement(By.xpath("//td[@id='total_product']")).getText().replace('$', ' ').trim()));
		total_shipping = getdouble(convertDouble(
				driver.findElement(By.xpath("//td[@id='total_shipping']")).getText().replace('$', ' ').trim()));
		total_tax = getdouble(convertDouble(
				driver.findElement(By.xpath("//td[@id='total_tax']")).getText().replace('$', ' ').trim()));
		total = getdouble(convertDouble(
				driver.findElement(By.xpath("//td[@id='total_price_container']")).getText().replace('$', ' ').trim()));
		if (productsCount == total_products && productsCount == (total - total_shipping - total_tax)) {

			logger.log(Status.PASS, "Cost of the Products is equal to Total Cost");
		} else {

			logger.log(Status.PASS, "Cost of the Products is not equal to Total Cost");
		}

		driver.findElement(By.xpath("//body[@id='order']")).click();
		driver.findElement(By.xpath("//*[@id='center_column']/p[2]/a[1]/span")).click();
		driver.findElement(By.xpath("//*[@id='center_column']/form/p/button/span")).click();
		driver.findElement(By.xpath("//input[@id='cgv']")).click();
		driver.findElement(By.xpath("//*[@id='form']/p/button/span")).click();
		driver.findElement(By.xpath("//a[@class='bankwire']")).click();
		driver.findElement(By.xpath("//*[@id='cart_navigation']/button/span")).click();
		WaitForJStoLoad();
		if (driver.findElement(By.xpath("//*[@id='center_column']/div/p/strong")).getText()
				.equals("Your order on My Store is complete.")) {

			logger.log(Status.PASS, "Order has been places successfully");
		}
		timeStamp = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
		logger.log(Status.INFO, "Current At the time of Placing an Order "+timeStamp);
		driver.findElement(By.xpath("//a[@class='logout']")).click();

	}

	public static Double getdouble(double value) {

		value = Double.parseDouble(new DecimalFormat("##.##").format(value));
		return value;
	}

	public static Double convertDouble(String value) {

		return Double.parseDouble(value);

	}

	

}

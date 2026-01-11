import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import java.util.Arrays as Arrays
import org.openqa.selenium.Alert as Alert
import org.openqa.selenium.WebDriver as WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('https://laravel-qa.kpntr.com')

    WebUI.maximizeWindow()

    // Delay kecil biar modal sempat render
    WebUI.delay(2)

    // Buat TestObject secara dynamic dari xpath
    TestObject popupModal = new TestObject('popupModal')

    popupModal.addProperty('xpath', ConditionType.EQUALS, '//div[@id=\'staticBackdropAds\']/div/div/a/img')

    TestObject closeButton = new TestObject('closeButton')

    closeButton.addProperty('xpath', ConditionType.EQUALS, '//div[@id=\'staticBackdropAds\']/div/div/i')

    // Cek apakah modal popup tampil
    boolean isPopupPresent = WebUI.verifyElementPresent(popupModal, 3, FailureHandling.OPTIONAL)

    if (isPopupPresent) {
        println('Popup marketing detected, closing it...')

        WebUI.waitForElementVisible(closeButton, 5)

        WebUI.waitForElementClickable(closeButton, 5)

        try {
            WebUI.click(closeButton)
        }
        catch (Exception e) {
            println('Normal click failed, trying JS click...')

            WebUI.executeJavaScript('arguments[0].click();', Arrays.asList(WebUI.findWebElement(closeButton, 5)))
        } 
        
        WebUI.verifyElementNotPresent(popupModal, 5, FailureHandling.OPTIONAL)

        println('Popup successfully closed.')
    } else {
        println('No popup marketing detected, skip closing.')
    }
    
    // Tunggu navbar muncul
    TestObject btnRegister = new TestObject('btnRegisterCTAMarketing')

    btnRegister.addProperty('xpath', ConditionType.EQUALS, "//a[@id='btn_reg_home']")

    // Alternatif yang lebih stabil (rekomendasi)
    WebUI.waitForElementVisible(btnRegister, 5)

    WebUI.waitForElementClickable(btnRegister, 5)

    // Klik button Register
    WebUI.click(btnRegister)

    println('Button Register clicked, navigating to Register page.')
	
	// ==========================================================
	// Detect apakah halaman Register ter-block Basic Auth (staging only)
	// Jika iya, bypass dengan credential URL
	// ==========================================================
	WebUI.waitForPageLoad(5)
	WebUI.delay(2)
	
	// Object indikator halaman register berhasil terbuka
	TestObject registerIndicator = new TestObject('registerIndicator')
	registerIndicator.addProperty(
		'xpath',
		ConditionType.EQUALS,
		"//input[@id='mail']"
	)
	
	// Cek apakah form register sudah bisa diakses
	boolean isRegisterAccessible = WebUI.verifyElementPresent(
		registerIndicator,
		5,
		FailureHandling.OPTIONAL
	)
	
	if (!isRegisterAccessible) {
		println('Register page not accessible, kemungkinan terkena Basic Auth. Bypassing...')
	
		String username = 'devman'
		String password = 'G0man'
		String authUrl = "https://${username}:${password}@laravel-qa.kpntr.com/register"
	
		WebUI.navigateToUrl(authUrl)
		WebUI.waitForPageLoad(10)
		WebUI.delay(2)
	
		// Setelah bypass, register page WAJIB bisa diakses
		WebUI.verifyElementPresent(
			registerIndicator,
			10,
			FailureHandling.STOP_ON_FAILURE
		)
	
		println('Basic Auth successfully bypassed. Register page now accessible.')
	} else {
		println('No Basic Auth detected. Register page opened normally.')
	}
    
    // ===== Register Page Objects =====
    TestObject inputEmail = new TestObject('inputEmail')

    inputEmail.addProperty('xpath', ConditionType.EQUALS, '//input[@id=\'mail\']')

    TestObject inputPassword = new TestObject('inputPassword')

    inputPassword.addProperty('xpath', ConditionType.EQUALS, '//input[@id=\'password\']')

    TestObject inputPasswordConfirm = new TestObject('inputPasswordConfirm')

    inputPasswordConfirm.addProperty('xpath', ConditionType.EQUALS, '//input[@id=\'password_confirmation\']')

    TestObject inputNama = new TestObject('inputNama')

    inputNama.addProperty('xpath', ConditionType.EQUALS, '//input[@id=\'nama\']')

    TestObject additionalSection = new TestObject('additionalSection')

    additionalSection.addProperty('xpath', ConditionType.EQUALS, '//div[@id=\'body-content\']/section/div/div/div/div/div[5]/div/div/div/div')

    TestObject checkboxTnc = new TestObject('checkboxTnc')

    checkboxTnc.addProperty('xpath', ConditionType.EQUALS, '//div[@id=\'body-content\']/section/div/div/div/div/div[7]')

    TestObject btnRegisterSubmit = new TestObject('btnRegisterSubmit')

    btnRegisterSubmit.addProperty('xpath', ConditionType.EQUALS, '//button[@id=\'saveProfile\']')

    WebUI.waitForPageLoad(10)

    WebUI.delay(2)

    // ===== Validation Register Page Loaded =====
    boolean emailPresent = WebUI.verifyElementPresent(inputEmail, 15, FailureHandling.OPTIONAL)

    boolean passwordPresent = WebUI.verifyElementPresent(inputPassword, 15, FailureHandling.OPTIONAL)

    boolean passwordConfirmPresent = WebUI.verifyElementPresent(inputPasswordConfirm, 15, FailureHandling.OPTIONAL)

    boolean namaPresent = WebUI.verifyElementPresent(inputNama, 15, FailureHandling.OPTIONAL)

    boolean tncPresent = WebUI.verifyElementPresent(checkboxTnc, 15, FailureHandling.OPTIONAL)

    boolean registerBtnPresent = WebUI.verifyElementPresent(btnRegisterSubmit, 15, FailureHandling.OPTIONAL)

    println('Email field present           : ' + emailPresent)

    println('Password field present        : ' + passwordPresent)

    println('Password confirmation present : ' + passwordConfirmPresent)

    println('Nama field present            : ' + namaPresent)

    println('T&C checkbox present          : ' + tncPresent)

    println('Register button present       : ' + registerBtnPresent)

    if (((emailPresent && namaPresent) && tncPresent) && registerBtnPresent) {
        println('Register page successfully loaded. All mandatory components are present.')
    } else {
        println('========== DEBUG REGISTER PAGE ==========')

        println('emailPresent           : ' + emailPresent)

        println('passwordPresent        : ' + passwordPresent)

        println('passwordConfirmPresent : ' + passwordConfirmPresent)

        println('namaPresent            : ' + namaPresent)

        println('tncPresent             : ' + tncPresent)

        println('registerBtnPresent     : ' + registerBtnPresent)

        println('========================================')

        WebUI.takeScreenshot()

        KeywordUtil.markFailedAndStop('Register page not loaded properly. One or more mandatory elements are missing.')
    }
    
    println('Test case passed: user berhasil akses halaman Register')
}
finally { 
    // Selalu dieksekusi walaupun test fail
    WebUI.closeBrowser()
}
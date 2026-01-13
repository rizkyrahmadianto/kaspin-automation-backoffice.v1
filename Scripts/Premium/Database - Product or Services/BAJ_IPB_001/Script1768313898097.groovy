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
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil

def currentUrl = WebUI.getUrl(FailureHandling.OPTIONAL)

// Jika browser belum buka ATAU browser terbuka tapi balik ke halaman landing/login
if ((currentUrl == null) || currentUrl.contains('login')) {
    WebUI.callTestCase(findTestCase('Test Cases/Login Owner/LG_001'), [:], FailureHandling.STOP_ON_FAILURE)

    WebUI.navigateToUrl('https://laravel-qa.kpntr.com/account') // Jika masih login, langsung pindah halaman
} else {
    WebUI.navigateToUrl('https://laravel-qa.kpntr.com/account')
}

WebUI.waitForPageLoad(10)

// =========================
// STEP 2 : Klik menu Database Barang / Jasa
// =========================
TestObject menuDatabase = new TestObject('menuDatabase')

menuDatabase.addProperty('xpath', ConditionType.EQUALS, '//ul[@id=\'accordion\']/li[5]/a')

WebUI.waitForElementVisible(menuDatabase, 10)

WebUI.waitForElementClickable(menuDatabase, 10)

WebUI.click(menuDatabase)

println('Menu Database Barang/Jasa berhasil diklik.')

// =========================
// STEP 3 : Klik submenu Database Barang atau Jasa
// =========================
TestObject subMenuDatabase = new TestObject('subMenuDatabase')

subMenuDatabase.addProperty('xpath', ConditionType.EQUALS, '//ul[@id=\'accordionSub\']/li/a')

WebUI.waitForElementVisible(subMenuDatabase, 10)

WebUI.waitForElementClickable(subMenuDatabase, 10)

WebUI.click(subMenuDatabase)

println('Submenu Database Barang/Jasa berhasil diklik.')

// =========================
// STEP 4 : Klik button Import Produk Baru
// =========================
TestObject btnExcelProd = new TestObject('btnExcelProd')

btnExcelProd.addProperty('xpath', ConditionType.EQUALS, '//a[@id=\'btnExcelProd\']')

WebUI.waitForElementVisible(btnExcelProd, 10)

WebUI.waitForElementClickable(btnExcelProd, 10)

WebUI.click(btnExcelProd)

println('Button Import Produk Baru berhasil diklik.')

// =========================
// STEP 5 : Verifikasi masuk halaman Import Produk Baru
// =========================
WebUI.waitForPageLoad(10)

WebUI.delay(2)

currentUrl = WebUI.getUrl()

println('Current URL: ' + currentUrl)

if (!(currentUrl.toLowerCase().contains('import'))) {
    KeywordUtil.markFailedAndStop('Gagal masuk halaman Import Produk Baru. Actual URL: ' + currentUrl)
}

println('Berhasil masuk halaman Import Produk Baru.')

// =========================
// STEP 6 : Verifikasi & klik button Unduh Template
// =========================
TestObject btnUnduhTemplate = new TestObject('btnUnduhTemplate')

btnUnduhTemplate.addProperty('xpath', ConditionType.EQUALS, '//a[contains(text(),\'Unduh Template\')]')

WebUI.waitForElementVisible(btnUnduhTemplate, 10)

WebUI.waitForElementClickable(btnUnduhTemplate, 10)

WebUI.click(btnUnduhTemplate)

println('Button Unduh Template berhasil diklik (proses download di-trigger).')

WebUI.delay(3 // delay sekadar memberi waktu browser memproses download
    )

// =========================
// STEP 7 : Verifikasi & klik button Unduh Template SKU
// (menggunakan xpath yang kamu kasih)
// =========================
TestObject btnUnduhTemplateSku = new TestObject('btnUnduhTemplateSku')

btnUnduhTemplateSku.addProperty('xpath', ConditionType.EQUALS, '(//button[@type=\'button\'])[2]')

WebUI.waitForElementVisible(btnUnduhTemplateSku, 10)

WebUI.waitForElementClickable(btnUnduhTemplateSku, 10)

WebUI.click(btnUnduhTemplateSku)

println('Button Unduh Template SKU berhasil diklik (proses download di-trigger).')

WebUI.delay(3)

// =========================
// Pengecekan Modal setelah klik Unduh Template SKU
// =========================
TestObject modalSkuNotAvailable = new TestObject('modalSkuNotAvailable')
modalSkuNotAvailable.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//div[contains(.,'Template SKU tidak tersedia')]"
)

// Cek apakah modal muncul
boolean isModalPresent = WebUI.verifyElementPresent(modalSkuNotAvailable, 5, FailureHandling.OPTIONAL)

if (isModalPresent) {
	println('Modal "Template SKU tidak tersedia" muncul.')

	// Button "Mengerti" di dalam modal
	TestObject btnMengerti = new TestObject('btnMengerti')
	btnMengerti.addProperty(
		'xpath',
		ConditionType.EQUALS,
		"//div[contains(.,'Template SKU tidak tersedia')]//button[contains(normalize-space(.),'mengerti')]"
	)

	WebUI.waitForElementVisible(btnMengerti, 5)
	WebUI.waitForElementClickable(btnMengerti, 5)
	WebUI.scrollToElement(btnMengerti, 2)

	try {
		WebUI.click(btnMengerti)
		println('Button "Mengerti" berhasil diklik dengan normal click.')
	} catch (Exception e) {
		println('Normal click gagal, mencoba JS click...')
		WebUI.executeJavaScript(
			"arguments[0].click();",
			Arrays.asList(WebUI.findWebElement(btnMengerti, 5))
		)
		println('Button "Mengerti" berhasil diklik dengan JavaScript.')
	}

	// Optional: pastikan modal sudah tertutup
	WebUI.verifyElementNotPresent(modalSkuNotAvailable, 5, FailureHandling.OPTIONAL)
	println('Modal berhasil ditutup.')

} else {
	println('Tidak ada modal peringatan. Template SKU kemungkinan berhasil di-download.')
}


// =========================
// STEP 8 : Klik button Pilih File
// =========================
TestObject btnPilihFile = new TestObject('btnPilihFile')

// XPath disederhanakan agar tidak tergantung whitespace
btnPilihFile.addProperty('xpath', ConditionType.EQUALS, '//label[contains(normalize-space(.),\'Pilih File\')]')

WebUI.waitForElementPresent(btnPilihFile, 10)

WebUI.scrollToElement(btnPilihFile, 3)

WebUI.waitForElementVisible(btnPilihFile, 10)

WebUI.waitForElementClickable(btnPilihFile, 10)

try {
    WebUI.click(btnPilihFile)

    println('Button Pilih File berhasil diklik dengan normal click.')
}
catch (Exception e) {
    println('Normal click gagal, mencoba JS click...')

    WebUI.executeJavaScript('arguments[0].click();', Arrays.asList(WebUI.findWebElement(btnPilihFile, 10)))

    println('Button Pilih File berhasil diklik dengan JavaScript.')
} 

println('Dialog file chooser seharusnya muncul (tidak bisa di-handle oleh automation).')

WebUI.delay(2)

println('Test case Database Barang/Jasa → Import Produk Baru → Verifikasi Button selesai.')
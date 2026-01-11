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

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('https://laravel-qa.kpntr.com')

    WebUI.maximizeWindow()

    WebUI.waitForPageLoad(10)

    // =========================
    // STEP 1 : Klik menu Harga di navbar
    // =========================
    TestObject btnHargaNavbar = new TestObject('btnHargaNavbar')

    btnHargaNavbar.addProperty('xpath', ConditionType.EQUALS, '//a[@id=\'btn_menu_price\']')

    WebUI.waitForElementVisible(btnHargaNavbar, 10)

    WebUI.waitForElementClickable(btnHargaNavbar, 10)

    WebUI.click(btnHargaNavbar)

    println('Button Harga di navbar berhasil diklik.')

    // =========================
    // STEP 2 : Validasi masuk halaman Harga
    // =========================
    WebUI.waitForPageLoad(10)

    WebUI.delay(2)

    String currentUrl = WebUI.getUrl()

    println('Current URL after click Harga: ' + currentUrl)

    if (!(currentUrl.contains('/harga'))) {
        KeywordUtil.markFailedAndStop('Gagal menuju halaman Harga. Actual URL: ' + currentUrl)
    }
    
    println('Berhasil masuk ke halaman Harga.')

    // =========================
    // STEP 3 : Cari button Pro Price (button ke-2)
    // =========================
    TestObject btnProPrice2 = new TestObject('btnProPrice2')

    btnProPrice2.addProperty('xpath', ConditionType.EQUALS, '//a[@id=\'btn_pro_price_2\']')

    WebUI.waitForElementVisible(btnProPrice2, 10)

    WebUI.waitForElementClickable(btnProPrice2, 10)

    println('Button Pro Price ke-2 ditemukan di halaman Harga.')

    // =========================
    // STEP 4 : Klik button Pro Price 2 (akan membuka new tab)
    // =========================
    WebUI.click(btnProPrice2)

    println('Button Pro Price 2 diklik, menunggu tab baru terbuka...')

    // Simpan window index sekarang
    int currentWindowIndex = WebUI.getWindowIndex()

    WebUI.delay(3)

    // Switch ke tab baru
    WebUI.switchToWindowIndex(currentWindowIndex + 1)

    WebUI.waitForPageLoad(10)

    // =========================
    // STEP 5 : Validasi redirect URL
    // =========================
    String newTabUrl = WebUI.getUrl()

    println('New tab URL: ' + newTabUrl)

    // Karena kpntr.link adalah tracking link dan akan redirect,
    // kita validasi tujuan akhirnya (Play Store)
    if ((newTabUrl.contains('kpntr.link/download_pro_website') || newTabUrl.contains('play.google.com/store/apps/details')) || 
    newTabUrl.contains('org.owline.kasirpintarpro')) {
        println('Berhasil redirect ke halaman download Kasir Pintar Pro.')
    } else {
        KeywordUtil.markFailedAndStop('Redirect URL tidak sesuai. Actual: ' + newTabUrl)
    }
    
    println('Test case passed: Landing Page → Harga → Pro Price Button 2 → Redirect ke Download Pro.')
}
finally { 
    WebUI.closeBrowser()
}
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('https://laravel-qa.kpntr.com')

    WebUI.maximizeWindow()

    WebUI.waitForPageLoad(10)

    // ===== Object Navbar Produk =====
    TestObject menuProduk = new TestObject('menuProduk')

    menuProduk.addProperty('xpath', ConditionType.EQUALS, '//a[@id=\'listProduk\']')

    // ===== Object Dropdown Item Free =====
    TestObject btnFreeNavbar = new TestObject('btnFreeNavbar')

    btnFreeNavbar.addProperty('xpath', ConditionType.EQUALS, '//a[@id=\'btn_free_navbar\']')

    // ===== Step 1: Pastikan menu Produk ada =====
    WebUI.waitForElementVisible(menuProduk, 10)

    WebUI.waitForElementClickable(menuProduk, 10)

    println('Menu Produk ditemukan di navbar.')

    // ===== Step 2: Hover / klik agar dropdown muncul =====
    WebUI.mouseOver(menuProduk)

    WebUI.delay(1)

    // Beberapa UI butuh click, bukan hover
    if (!(WebUI.verifyElementPresent(btnFreeNavbar, 3, FailureHandling.OPTIONAL))) {
        WebUI.click(menuProduk)

        WebUI.delay(1)
    }
    
    // ===== Step 3: Pastikan item dropdown muncul =====
    WebUI.waitForElementVisible(btnFreeNavbar, 10)

    WebUI.waitForElementClickable(btnFreeNavbar, 10)

    println('Dropdown Produk tampil, item Free ditemukan.')

    // ===== Step 4: Klik menu Free (akan membuka tab baru) =====
    WebUI.click(btnFreeNavbar)

    println('Button Free diklik, menunggu tab baru terbuka...')

    // Simpan window index awal
    int currentWindowIndex = WebUI.getWindowIndex()

    WebUI.delay(3)

    // Pindah ke tab baru
    WebUI.switchToWindowIndex(currentWindowIndex + 1)

    WebUI.waitForPageLoad(10)

    // Validasi URL
    String currentUrl = WebUI.getUrl()

    println('New tab URL: ' + currentUrl)

    if (currentUrl.contains('org.owline.kasirpintarpro')) {
        println('Berhasil membuka halaman Kasir Pintar Pro di Play Store.')
    } else {
        KeywordUtil.markFailedAndStop('Redirect URL tidak sesuai. Actual: ' + currentUrl)
    }
    
    println('Test case passed: Landing page → Navbar Produk → Free → membuka halaman download di tab baru.')
}
finally { 
    WebUI.closeBrowser()
}
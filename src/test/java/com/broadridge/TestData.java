package com.broadridge;

import org.testng.annotations.DataProvider;

public class TestData {


    @DataProvider(name = "broadridgePages")
    public Object[][] livePageUrl() {
        return new Object[][]{

                {"https://broadridgedigital:broadridge1@www-dev.broadridge.com/2024-digital-transformation-study", "https://www-dev.broadridge.com/2024-digital-transformation-study"},
                {"https://broadridgedigital:broadridge1@www-dev.broadridge.com/insight-pages/bpo-case-study", "https://www-dev.broadridge.com/insight-pages/bpo-case-study"},
                {"https://broadridgedigital:broadridge1@www-dev.broadridge.com/article/asset-management/summary-of-latest-guidance-from-the-fca-on-consumer-duty-implementation", "https://www-dev.broadridge.com/article/asset-management/summary-of-latest-guidance-from-the-fca-on-consumer-duty-implementation"},
                {"https://broadridgedigital:broadridge1@www-dev.broadridge.com/insight-pages/governance-regulatory-change","https://www-dev.broadridge.com/insight-pages/governance-regulatory-change"},
                {"https://broadridgedigital:broadridge1@www-dev.broadridge.com/campaign/all-in-the-family-active-etf", "https://www-dev.broadridge.com/campaign/all-in-the-family-active-etf"},
                {"https://broadridgedigital:broadridge1@www-dev.broadridge.com/who-we-serve/asset-management",    "https://www-dev.broadridge.com/who-we-serve/asset-management"},
                {"https://broadridgedigital:broadridge1@www-dev.broadridge.com/who-we-serve/wealth-management",    "https://www-dev.broadridge.com/who-we-serve/wealth-management"},
                {"https://broadridgedigital:broadridge1@www-dev.broadridge.com/who-we-serve/consumer-industries",   "https://www-dev.broadridge.com/who-we-serve/consumer-industries"},
                {"https://broadridgedigital:broadridge1@www.broadridge.com/insight-pages/broadridge-insights",   "https://www.broadridge.com/insight-pages/broadridge-insights"},
                {"https://broadridgedigital:broadridge1@www-dev.broadridge.com/financial-services/asset-management/retirement-services/fiduciary-software-and-technology/fiduciary-focus-toolkit",   "https://www-dev.broadridge.com/financial-services/asset-management/retirement-services/fiduciary-software-and-technology/fiduciary-focus-toolkit"}


        };
    }




}

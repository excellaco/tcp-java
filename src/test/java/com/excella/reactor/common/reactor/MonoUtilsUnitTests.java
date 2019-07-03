package com.excella.reactor.common.reactor;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MonoUtilsUnitTests {
    private MonoUtils monoUtils;

    @BeforeMethod
    private void beforeEach() {
        monoUtils = new MonoUtils();
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(monoUtils);
    }
}

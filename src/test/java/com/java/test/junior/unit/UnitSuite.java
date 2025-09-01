package com.java.test.junior.unit;

import com.java.test.junior.unit.test.ProductTest;
import com.java.test.junior.unit.test.UserTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        UserTest.class,
        ProductTest.class
})
class UnitSuite {

}


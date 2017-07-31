package vn.khtt.datastore.test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import com.googlecode.objectify.ObjectifyService;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class ResetPerClassTest {
    private static final LocalServiceTestHelper helperForClass =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private static com.googlecode.objectify.util.Closeable closeableForClass;

    @BeforeClass
    public static void setUpForClass() throws Exception {
        helperForClass.setUp();
        closeableForClass = ObjectifyService.begin();
    }
    @AfterClass
    public static void tearDownForClass() throws Exception {
        closeableForClass.close();
        helperForClass.tearDown();
    }
}

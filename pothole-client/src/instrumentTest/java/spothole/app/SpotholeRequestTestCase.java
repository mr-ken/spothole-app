package spothole.app;

import android.test.AndroidTestCase;

import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;

public class SpotholeRequestTestCase extends AndroidTestCase {

    protected static final String TEST_BASE_URL = "http://spotholes.herokuapp.com/api";

    protected final BasicNetwork basicNetwork = new BasicNetwork(new HurlStack());

}

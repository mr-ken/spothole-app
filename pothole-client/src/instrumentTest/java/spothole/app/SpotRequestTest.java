package spothole.app;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import spothole.client.SpotRequest;
import spothole.client.SpotResponse;

public class SpotRequestTest extends SpotholeRequestTestCase {

    public void testShouldSpot() throws Exception {
        SpotRequest request = new SpotRequest(TEST_BASE_URL, "SMALL", 0d, 0d, null, null);

        Response<SpotResponse> response;

        try {
            response = request.parseNetworkResponse(basicNetwork.performRequest(request));

        } catch (VolleyError volleyError) {
            throw request.parseNetworkError(volleyError);
        }

        if (response.result == null) throw response.error;

        // TODO nasty test until we have a stub frontend
        assertNotNull(response.result);
    }

}


package spothole.client;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.IOException;

public abstract class SpotholeRequest<T> extends JsonRequest<T> {

    abstract protected Class<T> getRequestType();

    public SpotholeRequest(int method, String url, String requestBody, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }

    @Override
    public Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(
                    getRequestType().newInstance(),
                    HttpHeaderParser.parseCacheHeaders(response)
            );
        } catch (InstantiationException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    protected static String getRequestBody(Object request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationConfig.Feature.WRITE_ENUMS_USING_INDEX);
            return objectMapper.writeValueAsString(request);
        } catch (IOException e) {
            throw new RuntimeException("unable to serialize request", e);
        }
    }

    @Override
    public VolleyError parseNetworkError(VolleyError volleyError) {
        try {
            if (volleyError.networkResponse == null || volleyError.networkResponse.data == null) {
                return volleyError;
            }

            return null;

        } catch (IllegalArgumentException e) {
            return volleyError;
        }
    }

}

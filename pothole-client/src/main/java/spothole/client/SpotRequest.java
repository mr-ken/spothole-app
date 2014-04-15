package spothole.client;

import com.android.volley.Response;
import com.google.common.base.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

public class SpotRequest extends SpotholeRequest<SpotResponse> {

    private static final String PATH = "/pothole";

    public SpotRequest(String baseUrl,
                       String description,
                       double longitude,
                       double latitude,
                       Response.Listener<SpotResponse> listener,
                       Response.ErrorListener errorListener) {
        super(
            Method.POST,
            baseUrl + PATH, 
            getRequestBody(new SpotReq(description, longitude, latitude)),
            listener,
            errorListener);
    }

    @Override
    protected Class<SpotResponse> getRequestType() {
        return SpotResponse.class;
    }

    private static class SpotReq {

        private final String description;
        private final double longitude;
        private final double latitude;

        public SpotReq(String description,
                       double longitude,
                       double latitude) {
            this.description = description;
            this.longitude = longitude;
            this.latitude = latitude;
        }

        public String getDescription() {
            return description;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("description", description)
                    .add("longitude", longitude)
                    .add("latitude", latitude)
                    .toString();
        }

    }
}

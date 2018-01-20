package us.zonix.anticheat.requests;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import us.zonix.core.shared.api.request.Request;
import us.zonix.core.util.MapUtil;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class MemeFetchRequest implements Request {

    private final String path;

    @Override
    public String getPath() {
        return "/anticheat/" + this.path;
    }

    @Override
    public Map<String, Object> toMap() {
        return null;
    }

    public static final class FetchByUuidRequest extends MemeFetchRequest {

        public FetchByUuidRequest(UUID uuid) {
            super("fetch_by_uuid/" + uuid.toString());
        }

    }

    public static final class InsertRequest extends MemeFetchRequest {

        private JsonObject data;

        public InsertRequest(JsonObject data) {
            super("insert");

            this.data = data;
        }

        @Override
        public Map<String, Object> toMap() {
            return MapUtil.of(
                    "data", this.data
            );
        }

    }
}

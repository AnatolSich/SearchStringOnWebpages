package app.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@RequiredArgsConstructor
public class Statictic {

    private final int currentDepth;
    private final List<String> proceededUrls;
    private final List<String> searchingResults;
    private final Map<String, String> errorUrls;
}

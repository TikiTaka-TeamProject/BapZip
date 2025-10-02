package com.sparta.bapzip.ai.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Getter
public class GeminiRequest {

    public static final String CHARACTER_LIMIT = "\n 답변을 최대한 간결하게 50자 이하로";
    private List<Content> contents;

    public GeminiRequest(String text) {
        Part part = new TextPart(text+CHARACTER_LIMIT);
        Content content = new Content(Collections.singletonList(part));
        this.contents = List.of(content);
    }

    public GeminiRequest(String text, InlineData inlineData) {
        List<Content> contents = List.of(
                new Content(
                        List.of(
                                new TextPart(text),
                                new InlineDataPart(inlineData)
                        )
                )
        );

        this.contents = contents;
    }

    @Getter
    @AllArgsConstructor
    private static class Content {
        private List<Part> parts;
    }

    interface Part {}

    @Getter
    @AllArgsConstructor
    private static class TextPart implements Part {
        public String text;
    }

    @Getter
    @AllArgsConstructor
    private static class InlineDataPart implements Part {
        public InlineData inlineData;
    }

    @Getter
    @AllArgsConstructor
    public static class InlineData {
        private String mimeType;
        private String data;
    }
}

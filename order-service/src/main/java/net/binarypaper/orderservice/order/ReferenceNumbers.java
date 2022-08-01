package net.binarypaper.orderservice.order;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArgument;
import net.logstash.logback.argument.StructuredArguments;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Slf4j
public class ReferenceNumbers {

    @NonNull
    private final String publicReference;

    @NonNull
    private final String privateReference;

    public StructuredArgument exportStructuredArgument() {
        HashMap<String, Object> export = new HashMap<>();
        export.put("public-reference", publicReference);
        if (log.isDebugEnabled()) {
            export.put("private-reference", privateReference);
        }
        return StructuredArguments.entries(export);
    }
}
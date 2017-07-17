package com.dipegroup.spring.es.services.es;

import com.dipegroup.spring.es.models.es.EsHighlightedField;
import com.dipegroup.spring.es.models.objects.EsModel;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public abstract class ElasticsearchHighlightingService<E extends EsModel<K>, K> {

    private final HighlightBuilder highlightBuilder;

    protected ElasticsearchHighlightingService(HighlightBuilder highlightBuilder) {
        this.highlightBuilder = highlightBuilder;
    }

    public void highlight(E item, String key, HighlightField field) {
        highlight(item, key, read(field));
    }

    protected void highlight(E item, String key, String content) {
        getFields().stream()
                .filter(field -> field.getName().equals(key) && field.getConsumer() != null)
                .forEach(field -> field.getConsumer().accept(item, content));
    }

    protected List<String> highlightArray(List<String> list, String content) {
        String clearHi = cleanHighlighting(content);
        return list.stream().map(ctx -> {
            if (ctx.equals(clearHi)) {
                return content;
            } else {
                return ctx;
            }
        }).collect(Collectors.toList());
    }

    protected String cleanHighlighting(String content) {
        content = StringUtils.removeStart(content, highlightBuilder.preTags()[0]);
        return StringUtils.removeEnd(content, highlightBuilder.postTags()[0]);
    }

    protected abstract Collection<EsHighlightedField<E, K>> getFields();

    private String read(HighlightField value) {
        return Arrays.asList(value.fragments())
                .stream().map(Text::string).collect(Collectors.joining());
    }
}

package com.example.lucky7postservice.utils.kafka;

import com.example.lucky7postservice.src.command.post.domain.Post;
import com.example.lucky7postservice.utils.kafka.dto.Field;
import com.example.lucky7postservice.utils.kafka.dto.KafkaPostDto;
import com.example.lucky7postservice.utils.kafka.dto.PostPayload;
import com.example.lucky7postservice.utils.kafka.dto.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    List<Field> fields = Arrays.asList(
            new Field("int64", false, "id"),
            new Field("int64", false, "member_id"),
            new Field("int64", false, "blog_id"),
            new Field("string", true, "post_type"),
            new Field("string", false, "title"),
            new Field("string", false, "content"),
            new Field("string", true, "thumbnail"),
            new Field("string", false, "main_hashtag"),
            new Field("string", false, "post_state"),
            new Field("int64", true, "created_at") {public String name="org.apache.kafka.connect.data.Timestamp";},
            new Field("int64", true, "updated_at") {public String name="org.apache.kafka.connect.data.Timestamp";}
    );

    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("post")
            .build();

    public void send(String topic, Post post) {
        PostPayload payload = PostPayload.of(
                post.getId(), post.getBlogId(), post.getMemberId(),
                post.getPostType(), post.getTitle(), post.getContent(),
                post.getThumbnail(), post.getMainHashtag(), post.getPostState(),
                post.getCreatedAt(), post.getUpdatedAt()
        );

        KafkaPostDto kafkaPostDto = new KafkaPostDto(schema, payload);

        String jsonInString = "";

        try {
            JSONObject jsonObject = new JSONObject(kafkaPostDto);
            jsonInString = jsonObject.toString();
            System.out.println("JSON String: " + jsonInString);
        } catch (Exception e) {
            System.err.println("Error converting object to JSON: " + e.getMessage());
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Post Producer sent data from the PostService : " + kafkaPostDto);
    }
}

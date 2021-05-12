package ru.kermilov.targetaudience.vkadapter.event.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetAudience implements Serializable {

    public TargetAudience(Integer externalId, String firstName, String lastName, String city) {
        this.externalId = externalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
    }

    @JsonProperty
    private Integer externalId;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String city;
    @JsonProperty
    private List<TargetAudience> friends;
}

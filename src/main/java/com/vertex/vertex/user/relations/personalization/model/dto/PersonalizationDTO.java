package com.vertex.vertex.user.relations.personalization.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalizationDTO {

    private Long id;
    private Long primaryColor;
    private Long secondColor;
    private Long fontSize;
    private Long fontFamily;
    private Boolean voiceCommand;
    private Boolean listeningText;
    private Long user;

}

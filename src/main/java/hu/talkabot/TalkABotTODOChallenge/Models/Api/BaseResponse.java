package hu.talkabot.TalkABotTODOChallenge.Models.Api;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseResponse {

    private String msg;

    private String type;
}

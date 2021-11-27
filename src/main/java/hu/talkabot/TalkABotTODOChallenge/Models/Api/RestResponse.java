package hu.talkabot.TalkABotTODOChallenge.Models.Api;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse extends BaseResponse {

    private boolean status;

    private String desc;

    private Object response;

    private List<Object> responseList;

    public RestResponse(String msg, String type) {
        super(msg, type);
    }

    public RestResponse(String msg, String type, boolean status, String desc) {
        super(msg, type);
        this.status = status;
        this.desc = desc;
    }

    public RestResponse(String msg, String type, Object response) {
        super(msg, type);
        this.response = response;
    }

    public RestResponse(String msg, String type, List<Object> responseList) {
        super(msg, type);
        this.responseList = responseList;
    }

}

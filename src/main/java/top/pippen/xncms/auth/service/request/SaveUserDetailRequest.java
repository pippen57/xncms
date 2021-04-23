package top.pippen.xncms.auth.service.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author pippen
 */
@Getter
@Setter
public class SaveUserDetailRequest {


    private String name;

    private String email;

    private String telephone;

    private String avatar;
    @NotBlank(message = "登录失败")
    private String openId;

    private String description;

}

package top.pippen.xncms.auth.web.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author Pippen
 * @since 2021/04/15 09:30
 */
@Getter
@Setter
public class AuthParameter {
    @NotBlank
    private String openId;
    @NotBlank
    private String uuid;
}

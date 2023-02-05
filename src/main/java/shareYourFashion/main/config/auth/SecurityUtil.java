package shareYourFashion.main.config.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


public class SecurityUtil {
    public static String getLoginNickname(){


        // TODO: 이곳에, 로그인한 유저가 있으면 반환, 없으면 예외 발생

       PrincipalDetails user = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return user.getNickname();  // 추후 로그인 적용시 삭제 필수!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

}

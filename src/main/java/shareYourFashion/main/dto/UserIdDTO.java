//package shareYourFashion.main.dto;
//
//import org.springframework.security.core.GrantedAuthority;
//
//import java.util.Collection;
//import java.util.stream.Collectors;
//
//public class UserIdDTO {
//
//    public UserIdDTO toDto() {
//        return UserIdDTO.builder()
//                .id(id)
//                .password(password)
//                .email(email)
//                .nickname(nickname)
//                .roles(authorities.stream().map(GrantedAuthority::getAuthority).map(UserAccountRole::valueOf).collect(Collectors.toSet()))
//                .profileImg(profileImg)
//                .build();
//    }
//
//
//
//    @Override public String getUsername() { return username; }
//    @Override public String getPassword() { return password; }
//    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
//
//    @Override public boolean isAccountNonExpired() { return true; }
//    @Override public boolean isAccountNonLocked() { return true; }
//    @Override public boolean isCredentialsNonExpired() { return true; }
//    @Override public boolean isEnabled() { return true; }
//
//}
//}
//
//

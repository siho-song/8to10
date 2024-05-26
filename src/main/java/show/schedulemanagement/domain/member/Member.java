package show.schedulemanagement.domain.member;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.baseEntity.BaseEntity;
import show.schedulemanagement.dto.signup.SignUpRequestDto;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Table(name = "MEMBER")
@DynamicInsert
@ToString
@Builder
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private Mode mode;

    @Column(nullable = false)
    @ColumnDefault(value = "xxx") //TODO 기본 이미지 파일 생성 후 , value 값 수정
    private String imageFile;

    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Double score;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean authEmail;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean authPhone;

    @Default
    @OneToMany(mappedBy = "member" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberRole> roles = new ArrayList<>();

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public static Member of(SignUpRequestDto signUpRequestDto){
        return Member.builder()
                .username(signUpRequestDto.getUsername())
                .nickname(signUpRequestDto.getNickname())
                .email(signUpRequestDto.getEmail())
                .password(signUpRequestDto.getPassword())
                .phoneNumber(signUpRequestDto.getPhoneNumber())
                .gender(signUpRequestDto.getGender())
                .mode(signUpRequestDto.getMode())
                .authEmail(signUpRequestDto.getAuthEmail())
                .authPhone(signUpRequestDto.getAuthPhone())
                .build();
    }
}

package com.eighttoten.notification.domain;

import static com.eighttoten.member.domain.Mode.MILD;
import static com.eighttoten.member.domain.Mode.SPICY;

import com.eighttoten.member.domain.Mode;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;

@Getter
public enum FeedbackMessage {
    MILD_FEEDBACK_MESSAGE_1(MILD, "오늘은 일정을 많이 소화하지 못하셨네요 ㅠㅠ 내일은 더 알찬 하루를 기대해봐요!", 10),
    MILD_FEEDBACK_MESSAGE_2(MILD, "힘든 하루였나 봐요. 내일은 조금 더 가벼운 마음으로 시작해보세요!", 10),
    MILD_FEEDBACK_MESSAGE_3(MILD, "때로는 쉬는 것도 필요하죠. 내일은 더 나아질 거예요!", 10),
    MILD_FEEDBACK_MESSAGE_4(MILD, "오늘의 작은 실천도 소중합니다. 내일은 조금 더 도전해봐요!", 10),
    MILD_FEEDBACK_MESSAGE_5(MILD, "오늘 하루는 점검의 시간이었어요. 내일은 새롭게 시작해볼까요?", 10),
    MILD_FEEDBACK_MESSAGE_6(MILD, "목표를 채우지 못했지만, 실망하지 말아요! 내일이 있잖아요.", 10),
    MILD_FEEDBACK_MESSAGE_7(MILD, "느린 하루도 의미가 있어요. 내일은 더 나은 하루를 만들어봐요.", 10),
    MILD_FEEDBACK_MESSAGE_8(MILD, "결과보다 과정이 중요합니다. 오늘도 고생 많으셨어요.", 10),
    MILD_FEEDBACK_MESSAGE_9(MILD, "오늘은 조금 힘든 날이었지만, 내일은 더 밝게 빛날 거예요!", 10),
    MILD_FEEDBACK_MESSAGE_10(MILD, "오늘의 부족함은 내일의 기회입니다. 내일 더 나아져봐요!", 10),

    MILD_FEEDBACK_MESSAGE_11(MILD, "오늘은 약간 부족했지만, 조금만 더 하면 목표에 가까워질 거예요!", 20),
    MILD_FEEDBACK_MESSAGE_12(MILD, "한 걸음씩 나아가면 됩니다. 내일은 조금 더 도전해봐요.", 20),
    MILD_FEEDBACK_MESSAGE_13(MILD, "시작은 늘 어렵죠. 오늘을 발판 삼아 내일 더 해봐요!", 20),
    MILD_FEEDBACK_MESSAGE_14(MILD, "그래도 열심히 하셨죠? 내일은 더 큰 성취를 기대해요!", 20),
    MILD_FEEDBACK_MESSAGE_15(MILD, "조금만 더 하면 목표에 다가갈 수 있어요. 힘내세요!", 20),
    MILD_FEEDBACK_MESSAGE_16(MILD, "오늘의 성과는 작지만, 내일의 기회는 커질 거예요!", 20),
    MILD_FEEDBACK_MESSAGE_17(MILD, "처음부터 완벽할 수는 없어요. 내일은 조금 더 나아져봐요.", 20),
    MILD_FEEDBACK_MESSAGE_18(MILD, "오늘의 작은 성과도 칭찬받아 마땅해요. 내일은 더 노력해봐요.", 20),
    MILD_FEEDBACK_MESSAGE_19(MILD, "조금씩 성장하는 중이에요. 내일은 더 나아질 겁니다.", 20),
    MILD_FEEDBACK_MESSAGE_20(MILD, "오늘 하루 수고하셨어요. 내일은 더 밝게 시작해봐요!", 20),

    MILD_FEEDBACK_MESSAGE_21(MILD, "아직 갈 길이 있지만, 시작은 했으니 반은 성공이에요!", 30),
    MILD_FEEDBACK_MESSAGE_22(MILD, "오늘은 조금 아쉬웠지만, 내일은 더 나아질 거예요!", 30),
    MILD_FEEDBACK_MESSAGE_23(MILD, "성취가 조금씩 쌓이고 있어요. 내일은 더 높이 도전해봐요!", 30),
    MILD_FEEDBACK_MESSAGE_24(MILD, "작은 발전도 큰 의미가 있습니다. 오늘도 수고했어요!", 30),
    MILD_FEEDBACK_MESSAGE_25(MILD, "오늘의 경험은 내일의 밑거름이 될 거예요. 계속 노력해봐요!", 30),
    MILD_FEEDBACK_MESSAGE_26(MILD, "오늘의 점수는 시작점일 뿐이에요. 더 높이 올라갈 준비되셨죠?", 30),
    MILD_FEEDBACK_MESSAGE_27(MILD, "한 걸음씩 목표에 가까워지고 있어요. 내일은 더 힘내봐요!", 30),
    MILD_FEEDBACK_MESSAGE_28(MILD, "천천히 하지만 꾸준히! 내일은 더 큰 성과를 만들어봐요.", 30),
    MILD_FEEDBACK_MESSAGE_29(MILD, "작은 도전도 의미가 있어요. 오늘도 고생하셨습니다.", 30),
    MILD_FEEDBACK_MESSAGE_30(MILD, "오늘은 준비의 날! 내일은 더 큰 도약을 기대해요.", 30),

    MILD_FEEDBACK_MESSAGE_31(MILD, "절반 가까이 오셨어요. 지금까지도 잘하셨습니다!", 40),
    MILD_FEEDBACK_MESSAGE_32(MILD, "조금만 더 하면 목표에 도달할 수 있어요. 화이팅!", 40),
    MILD_FEEDBACK_MESSAGE_33(MILD, "꾸준히 해나가는 모습이 좋아요. 계속 노력해봐요!", 40),
    MILD_FEEDBACK_MESSAGE_34(MILD, "목표를 향해 한 걸음씩 나아가고 있어요. 잘하고 계세요!", 40),
    MILD_FEEDBACK_MESSAGE_35(MILD, "앞으로 조금만 더 집중하면 멋진 성과가 있을 거예요.", 40),
    MILD_FEEDBACK_MESSAGE_36(MILD, "오늘의 노력은 내일의 성과로 돌아올 거예요!", 40),
    MILD_FEEDBACK_MESSAGE_37(MILD, "절반을 넘어서기 위한 첫걸음을 내딛었어요. 잘하고 있어요!", 40),
    MILD_FEEDBACK_MESSAGE_38(MILD, "이 속도라면 충분히 목표에 도달할 수 있습니다!", 40),
    MILD_FEEDBACK_MESSAGE_39(MILD, "계속 도전하면 원하는 바를 이룰 수 있을 거예요. 힘내세요!", 40),
    MILD_FEEDBACK_MESSAGE_40(MILD, "오늘도 열심히 하셨어요. 내일은 더 좋은 결과를 기대해요!", 40),

    MILD_FEEDBACK_MESSAGE_41(MILD, "꾸준히 목표를 향해 가고 있어요. 잘하고 계십니다!", 50),
    MILD_FEEDBACK_MESSAGE_42(MILD, "오늘의 성취가 내일의 더 큰 동기부여가 될 거예요.", 50),
    MILD_FEEDBACK_MESSAGE_43(MILD, "목표의 절반을 향해 가고 있어요. 계속 나아가 봐요!", 50),
    MILD_FEEDBACK_MESSAGE_44(MILD, "한 걸음씩 성과를 내는 모습이 인상적이에요.", 50),
    MILD_FEEDBACK_MESSAGE_45(MILD, "여기까지 온 것도 대단합니다. 조금 더 힘내봐요!", 50),
    MILD_FEEDBACK_MESSAGE_46(MILD, "작은 성취가 쌓여 큰 결과를 만듭니다. 계속 도전하세요!", 50),
    MILD_FEEDBACK_MESSAGE_47(MILD, "오늘의 성과는 내일을 위한 준비입니다. 잘하셨어요!", 50),
    MILD_FEEDBACK_MESSAGE_48(MILD, "더 나아질 수 있는 자신감을 가지세요. 잘하고 있어요!", 50),
    MILD_FEEDBACK_MESSAGE_49(MILD, "꾸준히 노력하는 모습이 훌륭합니다. 계속하세요!", 50),
    MILD_FEEDBACK_MESSAGE_50(MILD, "오늘 하루 잘 보내셨습니다. 내일도 힘내세요!", 50),

    MILD_FEEDBACK_MESSAGE_51(MILD, "점점 목표에 가까워지고 있어요! 멋지게 해내고 있습니다!", 60),
    MILD_FEEDBACK_MESSAGE_52(MILD, "오늘도 훌륭한 성과를 내셨습니다. 내일도 기대할게요!", 60),
    MILD_FEEDBACK_MESSAGE_53(MILD, "꾸준히 쌓이는 성과가 인상적이에요. 잘하고 있어요!", 60),
    MILD_FEEDBACK_MESSAGE_54(MILD, "목표를 향한 여정에서 중요한 진전을 이루고 있어요!", 60),
    MILD_FEEDBACK_MESSAGE_55(MILD, "오늘 하루의 성과에 스스로도 뿌듯하셨을 거예요!", 60),
    MILD_FEEDBACK_MESSAGE_56(MILD, "멋진 하루였습니다. 내일은 더 높은 곳을 향해 도전해봐요!", 60),
    MILD_FEEDBACK_MESSAGE_57(MILD, "꾸준히 해나가는 모습이 정말 인상적이에요. 계속하세요!", 60),
    MILD_FEEDBACK_MESSAGE_58(MILD, "목표의 반 이상을 넘어서며 순항 중이에요. 힘내세요!", 60),
    MILD_FEEDBACK_MESSAGE_59(MILD, "오늘의 성과가 내일의 큰 동기부여가 될 거예요!", 60),
    MILD_FEEDBACK_MESSAGE_60(MILD, "여기까지 온 것도 대단합니다. 내일도 최선을 다해봐요!", 60),

    MILD_FEEDBACK_MESSAGE_61(MILD, "목표에 점점 가까워지고 있어요! 스스로를 믿어보세요!", 70),
    MILD_FEEDBACK_MESSAGE_62(MILD, "오늘 하루 정말 멋졌습니다. 성취가 대단하네요!", 70),
    MILD_FEEDBACK_MESSAGE_63(MILD, "꾸준히 쌓이는 노력의 결실이 보입니다. 잘하고 있어요!", 70),
    MILD_FEEDBACK_MESSAGE_64(MILD, "이대로라면 금방 목표에 도달할 거예요. 계속 도전하세요!", 70),
    MILD_FEEDBACK_MESSAGE_65(MILD, "오늘 하루 성취가 대단합니다. 멋지게 해내셨네요!", 70),
    MILD_FEEDBACK_MESSAGE_66(MILD, "목표를 향해 달려가는 모습이 정말 멋있어요!", 70),
    MILD_FEEDBACK_MESSAGE_67(MILD, "오늘의 성과는 큰 발전을 이루기 위한 중요한 과정이에요!", 70),
    MILD_FEEDBACK_MESSAGE_68(MILD, "여기까지 오느라 정말 수고 많으셨어요. 잘하고 있습니다!", 70),
    MILD_FEEDBACK_MESSAGE_69(MILD, "오늘의 결과는 내일을 위한 큰 힘이 될 거예요. 힘내세요!", 70),
    MILD_FEEDBACK_MESSAGE_70(MILD, "멋진 성취입니다! 내일은 더 큰 목표를 향해 나아가 봅시다!", 70),

    MILD_FEEDBACK_MESSAGE_71(MILD, "목표에 거의 다 왔습니다. 조금만 더 힘내세요!", 80),
    MILD_FEEDBACK_MESSAGE_72(MILD, "오늘 정말 대단한 성취를 이루셨어요. 너무 멋집니다!", 80),
    MILD_FEEDBACK_MESSAGE_73(MILD, "목표를 거의 다 이뤘습니다. 자신감을 가지세요!", 80),
    MILD_FEEDBACK_MESSAGE_74(MILD, "꾸준한 노력이 큰 결과를 만들어내고 있어요. 잘하고 있습니다!", 80),
    MILD_FEEDBACK_MESSAGE_75(MILD, "오늘 하루의 성과는 정말 놀라워요. 수고 많으셨습니다!", 80),
    MILD_FEEDBACK_MESSAGE_76(MILD, "목표를 향한 걸음이 정말 멋집니다. 계속 나아가세요!", 80),
    MILD_FEEDBACK_MESSAGE_77(MILD, "오늘 하루 정말 많은 것을 해내셨네요. 잘하고 있어요!", 80),
    MILD_FEEDBACK_MESSAGE_78(MILD, "목표와 거의 가까워지고 있어요. 계속 집중하세요!", 80),
    MILD_FEEDBACK_MESSAGE_79(MILD, "대단한 성취를 이루셨습니다. 자신감을 가지세요!", 80),
    MILD_FEEDBACK_MESSAGE_80(MILD, "이 성과는 당신의 노력의 결과입니다. 자랑스러워하세요!", 80),

    MILD_FEEDBACK_MESSAGE_81(MILD, "목표에 도달하기까지 정말 가까워졌어요! 멋집니다!", 90),
    MILD_FEEDBACK_MESSAGE_82(MILD, "오늘 하루 정말 놀라운 성과를 이루셨네요. 축하합니다!", 90),
    MILD_FEEDBACK_MESSAGE_83(MILD, "목표를 거의 다 이뤘습니다. 멋진 결과입니다!", 90),
    MILD_FEEDBACK_MESSAGE_84(MILD, "꾸준한 노력이 오늘의 멋진 성과를 만들어냈어요!", 90),
    MILD_FEEDBACK_MESSAGE_85(MILD, "오늘 하루 정말 훌륭하게 해내셨습니다. 자랑스러워요!", 90),
    MILD_FEEDBACK_MESSAGE_86(MILD, "목표를 향한 모든 노력이 빛을 발하고 있어요. 대단합니다!", 90),
    MILD_FEEDBACK_MESSAGE_87(MILD, "오늘의 결과는 정말 최고입니다. 잘하고 있어요!", 90),
    MILD_FEEDBACK_MESSAGE_88(MILD, "이 성과는 노력의 산물입니다. 내일도 더 큰 목표를 향해 나아가 봅시다!", 90),
    MILD_FEEDBACK_MESSAGE_89(MILD, "목표를 거의 다 이루셨네요. 너무나 멋집니다!", 90),
    MILD_FEEDBACK_MESSAGE_90(MILD, "오늘의 성과는 모든 노력의 보람입니다. 축하합니다!", 90),

    MILD_FEEDBACK_MESSAGE_91(MILD, "축하합니다! 목표를 완벽히 달성하셨습니다. 최고입니다!", 100),
    MILD_FEEDBACK_MESSAGE_92(MILD, "오늘의 성취는 정말 놀랍습니다. 대단한 하루였어요!", 100),
    MILD_FEEDBACK_MESSAGE_93(MILD, "목표를 완벽히 이루셨네요. 자랑스러운 결과입니다!", 100),
    MILD_FEEDBACK_MESSAGE_94(MILD, "당신의 노력은 오늘 최고의 결과로 보상받았습니다. 훌륭해요!", 100),
    MILD_FEEDBACK_MESSAGE_95(MILD, "오늘 하루 정말 최고였습니다. 멋진 성과를 이루셨습니다!", 100),
    MILD_FEEDBACK_MESSAGE_96(MILD, "목표를 완벽히 이루다니, 정말 대단합니다. 축하해요!", 100),
    MILD_FEEDBACK_MESSAGE_97(MILD, "이 성취는 당신의 열정과 노력을 증명합니다. 잘하셨어요!", 100),
    MILD_FEEDBACK_MESSAGE_98(MILD, "오늘 하루의 성과는 정말 최고입니다. 스스로를 칭찬해 주세요!", 100),
    MILD_FEEDBACK_MESSAGE_99(MILD, "목표를 이루기 위한 모든 과정이 완벽했습니다. 축하합니다!", 100),
    MILD_FEEDBACK_MESSAGE_100(MILD, "당신의 오늘은 최고였습니다. 내일도 이 기세를 이어가 봅시다!", 100),

    SPICY_FEEDBACK_MESSAGE_1(SPICY, "이럴 거면 계획은 왜 세웠어요? 내일은 자기 자신과의 약속 지키실 거죠?", 10),
    SPICY_FEEDBACK_MESSAGE_2(SPICY, "오늘은 계획의 그림자도 안 보이네요. 내일은 제대로 해봐요.", 10),
    SPICY_FEEDBACK_MESSAGE_3(SPICY, "이 점수로 만족한다면 큰일입니다. 내일은 변명 없이 실천하세요!", 10),
    SPICY_FEEDBACK_MESSAGE_4(SPICY, "오늘처럼 하면 꿈은커녕 기본도 못 이룹니다. 내일은 제대로 해보세요!", 10),
    SPICY_FEEDBACK_MESSAGE_5(SPICY, "계획을 세우고 이렇게 끝낼 거라면 하지 않는 게 나았을 겁니다.", 10),
    SPICY_FEEDBACK_MESSAGE_6(SPICY, "오늘은 진짜 너무 했어요. 내일은 더 치열하게 하세요!", 10),
    SPICY_FEEDBACK_MESSAGE_7(SPICY, "실망스럽네요. 스스로 부끄럽지 않아요? 내일은 달라지길 기대합니다.", 10),
    SPICY_FEEDBACK_MESSAGE_8(SPICY, "노력의 흔적이 없네요. 내일은 마음부터 다잡아야겠어요.", 10),
    SPICY_FEEDBACK_MESSAGE_9(SPICY, "계획을 장식용으로 만든 건 아니겠죠? 내일은 꼭 지켜보세요.", 10),
    SPICY_FEEDBACK_MESSAGE_10(SPICY, "오늘 하루를 이렇게 보낼 거라면 앞으로는 어떨 건가요?", 10),

    SPICY_FEEDBACK_MESSAGE_11(SPICY, "이 점수로 만족한다고요? 정말로 그렇게 생각하나요?", 20),
    SPICY_FEEDBACK_MESSAGE_12(SPICY, "조금 하긴 했지만, 이게 최선인가요? 내일은 더 열심히 해보세요.", 20),
    SPICY_FEEDBACK_MESSAGE_13(SPICY, "겨우 이 정도라면 더 노력해야겠죠? 내일은 다르게 해보세요!", 20),
    SPICY_FEEDBACK_MESSAGE_14(SPICY, "계획을 세웠으면 좀 제대로 실천해봅시다!", 20),
    SPICY_FEEDBACK_MESSAGE_15(SPICY, "노력 없이 결과를 기대하지 마세요. 내일은 더 분발하세요.", 20),
    SPICY_FEEDBACK_MESSAGE_16(SPICY, "이 속도로는 목표 달성이 멀어 보입니다. 집중하세요!", 20),
    SPICY_FEEDBACK_MESSAGE_17(SPICY, "조금만 더 집중했다면 훨씬 나았을 텐데요. 아쉽네요.", 20),
    SPICY_FEEDBACK_MESSAGE_18(SPICY, "내일은 더 큰 목표를 향해 제대로 실천하세요. 지금은 부족합니다.", 20),
    SPICY_FEEDBACK_MESSAGE_19(SPICY, "이 점수에 안주한다면 앞으로는 더 힘들 겁니다.", 20),
    SPICY_FEEDBACK_MESSAGE_20(SPICY, "내일은 변명 없이 실천만 하는 하루로 만들어 보세요!", 20),

    SPICY_FEEDBACK_MESSAGE_21(SPICY, "조금 나아졌지만, 아직 멀었어요. 내일은 두 배로 노력하세요.", 30),
    SPICY_FEEDBACK_MESSAGE_22(SPICY, "이 점수로 만족하긴 이릅니다. 더 열심히 해야죠!", 30),
    SPICY_FEEDBACK_MESSAGE_23(SPICY, "목표를 위해 더 강한 의지가 필요해 보입니다. 내일은 달라지길.", 30),
    SPICY_FEEDBACK_MESSAGE_24(SPICY, "꾸준히 하려면 더 치열하게 몰입해야 합니다. 지금은 부족해요.", 30),
    SPICY_FEEDBACK_MESSAGE_25(SPICY, "계획을 지키지 않는다면, 결과도 없습니다. 내일은 꼭 해내세요!", 30),
    SPICY_FEEDBACK_MESSAGE_26(SPICY, "오늘 하루는 그저 그런 날이었어요. 내일은 반전의 날로 만드세요.", 30),
    SPICY_FEEDBACK_MESSAGE_27(SPICY, "이 점수는 목표와 거리가 멀어요. 내일은 더 노력하세요.", 30),
    SPICY_FEEDBACK_MESSAGE_28(SPICY, "오늘의 실천은 너무 부족했어요. 내일은 제대로 된 하루로 만들어봐요.", 30),
    SPICY_FEEDBACK_MESSAGE_29(SPICY, "자기 자신에게 조금 더 엄격해질 필요가 있어요.", 30),
    SPICY_FEEDBACK_MESSAGE_30(SPICY, "이대로는 원하는 바를 이루기 어렵습니다. 더 치열해지세요.", 30),

    SPICY_FEEDBACK_MESSAGE_31(SPICY, "절반도 못 갔습니다. 이대로 괜찮을까요?", 40),
    SPICY_FEEDBACK_MESSAGE_32(SPICY, "오늘의 성과는 아쉬움만 남네요. 내일은 달라져야 합니다.", 40),
    SPICY_FEEDBACK_MESSAGE_33(SPICY, "목표를 위해 더 높은 집중력이 필요합니다. 힘내세요!", 40),
    SPICY_FEEDBACK_MESSAGE_34(SPICY, "오늘은 조금 아쉬웠습니다. 내일은 계획을 꼭 지키세요.", 40),
    SPICY_FEEDBACK_MESSAGE_35(SPICY, "꾸준함이 부족해 보입니다. 내일은 몰입해 보세요!", 40),
    SPICY_FEEDBACK_MESSAGE_36(SPICY, "오늘 하루는 정말 아쉬운 날이었어요. 내일은 달라지길.", 40),
    SPICY_FEEDBACK_MESSAGE_37(SPICY, "더 나아지려면 더 많은 노력이 필요합니다. 힘내세요.", 40),
    SPICY_FEEDBACK_MESSAGE_38(SPICY, "오늘 이 점수로는 부족합니다. 내일은 더 강하게 도전하세요.", 40),
    SPICY_FEEDBACK_MESSAGE_39(SPICY, "목표를 위해 스스로를 다시 점검해 보세요. 내일은 다르게!", 40),
    SPICY_FEEDBACK_MESSAGE_40(SPICY, "오늘 하루는 실패가 아닙니다. 다만, 내일은 더 해보세요.", 40),

    SPICY_FEEDBACK_MESSAGE_41(SPICY, "절반 정도 해냈지만, 여기서 만족하면 안 돼요. 더 나아갑시다!", 50),
    SPICY_FEEDBACK_MESSAGE_42(SPICY, "오늘의 성과는 나쁘지 않지만, 이 정도에 멈추지 마세요.", 50),
    SPICY_FEEDBACK_MESSAGE_43(SPICY, "목표를 이루려면 더 큰 노력이 필요합니다. 내일은 더 치열하게!", 50),
    SPICY_FEEDBACK_MESSAGE_44(SPICY, "여기까지 온 것도 좋지만, 아직 멀었어요. 계속 노력하세요!", 50),
    SPICY_FEEDBACK_MESSAGE_45(SPICY, "절반의 성공으로는 만족할 수 없습니다. 내일은 더 해보세요!", 50),
    SPICY_FEEDBACK_MESSAGE_46(SPICY, "오늘은 좋았지만, 내일은 더 나은 결과를 만들어야 합니다.", 50),
    SPICY_FEEDBACK_MESSAGE_47(SPICY, "목표는 절반이 아니라 100%입니다. 내일은 집중하세요!", 50),
    SPICY_FEEDBACK_MESSAGE_48(SPICY, "이 정도로는 부족해요. 더 높은 성취를 위해 도전하세요!", 50),
    SPICY_FEEDBACK_MESSAGE_49(SPICY, "절반의 성공도 좋지만, 만족하기엔 아직 이릅니다.", 50),
    SPICY_FEEDBACK_MESSAGE_50(SPICY, "오늘 결과에 안주하지 말고, 내일은 더 크게 도약하세요!", 50),

    SPICY_FEEDBACK_MESSAGE_51(SPICY, "목표에 가까워지고 있지만, 아직 부족합니다. 더 도전하세요!", 60),
    SPICY_FEEDBACK_MESSAGE_52(SPICY, "오늘의 성과는 인상적이지만, 여기에 멈추지 마세요!", 60),
    SPICY_FEEDBACK_MESSAGE_53(SPICY, "목표를 이루려면 지금보다 더 집중해야 합니다. 노력하세요!", 60),
    SPICY_FEEDBACK_MESSAGE_54(SPICY, "좋은 출발이지만, 내일은 더 큰 성취를 이루세요.", 60),
    SPICY_FEEDBACK_MESSAGE_55(SPICY, "오늘도 잘했지만, 더 나은 결과를 위해 내일은 노력해봅시다.", 60),
    SPICY_FEEDBACK_MESSAGE_56(SPICY, "이 정도로 만족한다면 안 돼요. 더 큰 목표를 세우세요!", 60),
    SPICY_FEEDBACK_MESSAGE_57(SPICY, "잘하고 있지만, 아직 최선을 다한 모습은 아닙니다.", 60),
    SPICY_FEEDBACK_MESSAGE_58(SPICY, "목표를 이루기 위해 더 집중하고 노력하세요!", 60),
    SPICY_FEEDBACK_MESSAGE_59(SPICY, "오늘 성과는 나쁘지 않지만, 만족하기엔 아직 이릅니다.", 60),
    SPICY_FEEDBACK_MESSAGE_60(SPICY, "이제부터가 시작입니다. 내일은 더 멀리 나아가세요!", 60),

    SPICY_FEEDBACK_MESSAGE_61(SPICY, "목표에 절반 이상 도달했지만, 아직 끝이 아닙니다!", 70),
    SPICY_FEEDBACK_MESSAGE_62(SPICY, "좋은 흐름입니다. 그러나 더 큰 도약이 필요합니다!", 70),
    SPICY_FEEDBACK_MESSAGE_63(SPICY, "오늘은 훌륭했지만, 내일은 더 멋진 하루로 만들어 보세요.", 70),
    SPICY_FEEDBACK_MESSAGE_64(SPICY, "목표를 위해 조금 더 몰입하세요. 지금도 훌륭합니다!", 70),
    SPICY_FEEDBACK_MESSAGE_65(SPICY, "목표를 향한 좋은 진전입니다. 그러나 아직 멀었어요.", 70),
    SPICY_FEEDBACK_MESSAGE_66(SPICY, "오늘의 성과에 만족하지 말고 내일은 더 나아가세요!", 70),
    SPICY_FEEDBACK_MESSAGE_67(SPICY, "좋은 결과지만, 내일은 더 나은 자신을 기대합니다!", 70),
    SPICY_FEEDBACK_MESSAGE_68(SPICY, "목표에 다가가고 있지만, 더 큰 집중이 필요합니다.", 70),
    SPICY_FEEDBACK_MESSAGE_69(SPICY, "오늘은 잘했지만, 내일은 더욱 치열하게 도전하세요.", 70),
    SPICY_FEEDBACK_MESSAGE_70(SPICY, "목표를 이루려면 한 걸음 더 나아가야 합니다. 계속하세요!", 70),

    SPICY_FEEDBACK_MESSAGE_71(SPICY, "이제 목표에 거의 다 왔습니다. 조금 더 힘내세요!", 80),
    SPICY_FEEDBACK_MESSAGE_72(SPICY, "목표를 이루기까지 아주 근접했습니다. 내일은 완벽을 추구하세요!", 80),
    SPICY_FEEDBACK_MESSAGE_73(SPICY, "오늘은 멋진 하루였지만, 내일은 더 나아가길 기대합니다!", 80),
    SPICY_FEEDBACK_MESSAGE_74(SPICY, "목표를 거의 달성했습니다. 하지만 아직 여유부릴 때는 아닙니다.", 80),
    SPICY_FEEDBACK_MESSAGE_75(SPICY, "훌륭한 결과입니다. 그러나 더 큰 도전을 위해 내일도 노력하세요.", 80),
    SPICY_FEEDBACK_MESSAGE_76(SPICY, "목표에 도달하려면 마지막까지 집중하세요. 조금만 더!", 80),
    SPICY_FEEDBACK_MESSAGE_77(SPICY, "대단한 성과지만, 내일은 더 큰 성취를 기대해봅니다.", 80),
    SPICY_FEEDBACK_MESSAGE_78(SPICY, "목표를 이루기 위해 조금만 더 달려봅시다!", 80),
    SPICY_FEEDBACK_MESSAGE_79(SPICY, "멋진 결과입니다. 하지만 여기에 멈추면 안 됩니다.", 80),
    SPICY_FEEDBACK_MESSAGE_80(SPICY, "오늘의 성과를 내일의 동력으로 삼아 더 멀리 나아가세요!", 80),

    SPICY_FEEDBACK_MESSAGE_81(SPICY, "거의 다 왔습니다! 내일은 완벽한 마무리를 기대합니다.", 90),
    SPICY_FEEDBACK_MESSAGE_82(SPICY, "목표를 눈앞에 두고 있습니다. 조금만 더 집중하세요!", 90),
    SPICY_FEEDBACK_MESSAGE_83(SPICY, "오늘 하루는 멋졌습니다. 내일은 더 큰 성과를 만들어 보세요.", 90),
    SPICY_FEEDBACK_MESSAGE_84(SPICY, "목표를 이루기 직전입니다. 끝까지 집중하세요!", 90),
    SPICY_FEEDBACK_MESSAGE_85(SPICY, "대단한 성과입니다. 내일은 더 큰 도약을 기대합니다.", 90),
    SPICY_FEEDBACK_MESSAGE_86(SPICY, "여기까지 온 것만으로도 훌륭하지만, 끝은 아닙니다.", 90),
    SPICY_FEEDBACK_MESSAGE_87(SPICY, "마무리가 중요합니다. 내일은 완벽한 하루로 만들어 보세요.", 90),
    SPICY_FEEDBACK_MESSAGE_88(SPICY, "이제 목표는 손에 닿을 듯합니다. 끝까지 집중하세요!", 90),
    SPICY_FEEDBACK_MESSAGE_89(SPICY, "멋진 결과입니다. 그러나 더 나은 자신을 위해 내일도 노력하세요!", 90),
    SPICY_FEEDBACK_MESSAGE_90(SPICY, "오늘의 결과는 훌륭했지만, 내일은 더 나아갑시다.", 90),

    SPICY_FEEDBACK_MESSAGE_91(SPICY, "축하합니다! 목표를 완벽히 달성했습니다. 정말 최고예요!", 100),
    SPICY_FEEDBACK_MESSAGE_92(SPICY, "오늘의 성과는 완벽했습니다. 자신감을 가지세요!", 100),
    SPICY_FEEDBACK_MESSAGE_93(SPICY, "목표를 완벽히 이루셨습니다. 더 높은 목표를 세워보세요!", 100),
    SPICY_FEEDBACK_MESSAGE_94(SPICY, "당신의 노력은 최고입니다. 앞으로도 계속 도전하세요!", 100),
    SPICY_FEEDBACK_MESSAGE_95(SPICY, "이 결과는 대단합니다. 하지만 더 멀리 나아갈 수 있습니다.", 100),
    SPICY_FEEDBACK_MESSAGE_96(SPICY, "오늘은 완벽했습니다. 내일도 이 기세를 이어가세요!", 100),
    SPICY_FEEDBACK_MESSAGE_97(SPICY, "목표를 이루느라 고생 많으셨습니다. 정말 자랑스럽습니다!", 100),
    SPICY_FEEDBACK_MESSAGE_98(SPICY, "오늘의 성과는 최고의 결과입니다. 스스로를 칭찬하세요!", 100),
    SPICY_FEEDBACK_MESSAGE_99(SPICY, "축하합니다! 오늘의 노력이 빛을 발했습니다.", 100),
    SPICY_FEEDBACK_MESSAGE_100(SPICY, "목표 달성을 축하합니다! 하지만 내일도 도전을 멈추지 마세요.", 100);

    private final Mode mode;
    private final String message;
    private final int achievementRange;

    FeedbackMessage(Mode mode, String message, int achievementRange) {
        this.mode = mode;
        this.message = message;
        this.achievementRange = achievementRange;
    }

    public static FeedbackMessage selectRandomMessage(Mode mode, double achievementRate) {
        FeedbackMessage[] values = values();
        List<FeedbackMessage> filteredMessages = Arrays.stream(values).filter(value ->
                        isEqualMode(mode, value) && isValidRange(achievementRate, value))
                .toList();

        if (!filteredMessages.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(filteredMessages.size());
            return filteredMessages.get(randomIndex);
        }
        return null;
    }

    private static boolean isEqualMode(Mode mode, FeedbackMessage value) {
        return value.getMode().equals(mode);
    }

    private static boolean isValidRange(double achievementRate, FeedbackMessage value) {
        return value.getAchievementRange() + 10 > achievementRate && achievementRate >= value.getAchievementRange();
    }
}
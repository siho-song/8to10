import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {useState} from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import PropTypes from "prop-types";

function ChangeNickname({ nickname, changeNickname, onBack }) {

    const [newNickname, setNewNickname] = useState("");
    const [submitError, setSubmitError] = useState("");
    const [isDuplicate, setIsDuplicate] = useState(null);
    const [isValidNickname, setIsValidNickname] = useState(true);
    let validNickname = "";

    const isNicknameValid = (nickname) => {
        const regex = /^(?:[가-힣a-zA-Z0-9_.]{2,12}|[a-zA-Z0-9_.]{2,20})$/;
        return regex.test(nickname);
    };

    const handleNewNicknameInput = (e) => {
        const inputValue = e.target.value;
        setNewNickname(inputValue);

        if (!isNicknameValid(inputValue)) {
            setIsValidNickname(false);
        } else {
            setIsValidNickname(true);
        }

        if (inputValue !== validNickname) {
            setIsDuplicate(null);
        }
    };

    const handleCheckDuplicate = async () => {

        try {
            const url = `/signup/nickname/exists?nickname=${newNickname}`;
            const response = await authenticatedApi.get(
                url,
                {apiEndPoint:API_ENDPOINT_NAMES.SIGNUP_NICKNAME_EXISTS,}
            );

            setIsDuplicate(response.data);
            validNickname = newNickname;
        } catch (error) {
            setIsDuplicate(true);
            validNickname="";
        }
    };

    const handleNewNicknameSubmit = async () => {
        if (!isNicknameValid(newNickname)) {
            setSubmitError("닉네임 조건을 충족하지 못했습니다. 다시 입력해주세요.");
            return;
        }

        if (isDuplicate === null) {
            setSubmitError("닉네임 중복확인을 해주세요.");
            return;
        }

        try {
            const url = "/mypage/account/nickname";
            const response = await authenticatedApi.put(
                url,
                {nickname: newNickname,},
                {apiEndPoint: API_ENDPOINT_NAMES.PUT_USER_NICKNAME,}
            );

            changeNickname(newNickname);
            onBack();
        } catch (error) {
            setSubmitError("서버 오류가 발생했습니다. 다시 시도해주세요.");
            console.error(error.toString());
            console.error(error);
        }
    }

    return (
        <div className="mypage-section">
            <div className="section-header">
                <button className="back-button" onClick={onBack}>
                    <ArrowBackIcon /> 뒤로
                </button>
                <h2>닉네임 변경</h2>
            </div>

            <div className="form-group">
                <label htmlFor="nickname">새 닉네임</label>
                <div className="nickname-input-group">
                    <input
                        type="text"
                        id="nickname"
                        name="nickname"
                        value={newNickname}
                        onChange={handleNewNicknameInput}
                        placeholder={nickname}
                    />
                    <button
                        type="button"
                        className="check-duplicate-button"
                        onClick={handleCheckDuplicate}
                        disabled={newNickname.length === 0 || !isValidNickname || isDuplicate===false}
                    >
                        중복확인
                    </button>
                </div>
                {!isValidNickname && newNickname.length > 0 && <p className="error-message">닉네임은 2~12자의 한글 또는 20자의 영어 및 숫자, 특수문자는 _와 .만 허용됩니다.</p>}
                {isDuplicate && <p className="error-message">이미 사용 중인 닉네임입니다. 다른 닉네임을 입력해주세요.</p>}
                {isDuplicate === false && <p className="normal-message">사용할 수 있는 닉네임입니다.</p>}
            </div>
            <button
                className="save-button"
                disabled={newNickname.length === 0 || isDuplicate===true}
                onClick={handleNewNicknameSubmit}
            >
                저장
            </button>
            {submitError && <p className="submit-error-message">{submitError}</p>}
        </div>
    );
}

ChangeNickname.propTypes = {
    nickname: PropTypes.string.isRequired,
    changeNickname: PropTypes.func.isRequired,
    onBack: PropTypes.func.isRequired,
}

export default ChangeNickname;

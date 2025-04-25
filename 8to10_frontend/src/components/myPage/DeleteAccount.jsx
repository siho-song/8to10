import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { useState } from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import { API_ENDPOINT_NAMES } from "@/constants/ApiEndPoints.js";
import PropTypes from "prop-types";
import logoIcon from '@/assets/images/logo.png';
import {useNavigate} from "react-router-dom";

function DeleteAccount({ onBack, closeModal }) {
    const navigate = useNavigate();
    const [isConfirming, setIsConfirming] = useState(false);

    const handleAccountDelete = async () => {
        const url = "/mypage/account";
        const response = await authenticatedApi.delete(
            url,
            {apiEndPoint: API_ENDPOINT_NAMES.DELETE_ACCOUNT}
        )

        if (response.status >= 200 && response.status < 300) {
            localStorage.clear();
            alert("회원 탈퇴 되었습니다. 감사합니다.")
            setTimeout(()=>navigate('/'), 100);
        } else {
            alert("죄송합니다. 회원 탈퇴가 정상적으로 진행되지 않았습니다. 다시 한번 시도해주세요.");
        }
    };

    return (
        <div className="delete-account-section">
            <div className="section-header">
                <button className="back-button" onClick={onBack}>
                    <ArrowBackIcon /> 뒤로
                </button>
                <h2>회원 탈퇴</h2>
            </div>
            <div className="delete-account-description">
                <img src={logoIcon} alt="로고"/>
                <p className="delete-account-head">8TO10 탈퇴 전 확인하세요</p>
                <p className="delete-account-explanation">
                    탈퇴하시면 8TO10의 서비스를 더이상 사용할 수 없으며<br/>
                    모든 데이터는 복구가 불가능 합니다
                </p>
            </div>
            <div className="delete-account">
                <br/>
                <div className="delete-account-confirm">
                    <input
                        type="checkbox"
                        id="agree"
                        className="mr-2"
                        checked={isConfirming}
                        onChange={() => setIsConfirming(!isConfirming)}
                    />
                    <label htmlFor="agree" className="text-sm text-gray-700">
                        안내사항을 확인하였으며, 이에 동의합니다.
                    </label>
                </div>
                <button
                    className="delete-account-button"
                    disabled={!isConfirming}
                    onClick={handleAccountDelete}
                >
                    회원 탈퇴
                </button>
            </div>
        </div>
    );
}

DeleteAccount.propTypes = {
    onBack: PropTypes.func.isRequired,
    closeModal: PropTypes.func.isRequired,
};

export default DeleteAccount;

import {useEffect, useState} from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES, DEFAULT_URL} from "@/constants/ApiEndPoints.js";
import {USER_ROLE} from "@/constants/UserRole.js";
import {DEFAULT_PROFILE_IMAGE_PATH, PROFILE_IMAGE_PATH} from "@/constants/ImagePaths.js";

import "@/styles/myPage/MyPage.css";
import ChangeNickname from "@/components/myPage/ChangeNickname.jsx";
import ChangePassword from "@/components/myPage/ChangePassword.jsx";
import ChangeProfileImage from "@/components/myPage/ChangeProfileImage.jsx";
import MyBoards from "@/components/myPage/MyBoards.jsx";
import PropTypes from "prop-types";
import MyReplies from "@/components/myPage/MyReplies.jsx";
import MyScrappedBoards from "@/components/myPage/MyScrappedBoards.jsx";

import {useNavigate} from "react-router-dom";
import {useAuth} from "@/context/auth/UseAuth.jsx";
import DeleteAccount from "@/components/myPage/DeleteAccount.jsx";

function MyPage({closeModal}) {

    const navigate = useNavigate();
    const {logout} = useAuth();

    const [userNickname, setUserNickname] = useState("");
    const [userEmail, setUserEmail] = useState("");
    const [userRole, setUserRole] = useState("");
    const [userProfileImageUrl, setUserProfileImageUrl] = useState("");
    const [currentView, setCurrentView] = useState("mypage");

    const [profileImageUpdated, setProfileImageUpdated] = useState(false);

    useEffect(() => {
        const loadUserProfile = async () => {
            try {
                const url = "/mypage";
                const response = await authenticatedApi.get(url, {
                    apiEndPoint: API_ENDPOINT_NAMES.GET_USER_PROFILE,
                })

                setUserNickname(response.data.nickname);
                setUserEmail(response.data.email);
                setUserRole(response.data.role);

                if (response.data.profileImageUrl) {
                    const imageFileName = response.data.profileImageUrl.split(PROFILE_IMAGE_PATH).pop();
                    const imageSource = DEFAULT_URL + PROFILE_IMAGE_PATH + imageFileName;
                    setUserProfileImageUrl(imageSource);
                } else {
                    if (response.data.role === USER_ROLE.FAITHFUL_USER) {
                        setUserProfileImageUrl(DEFAULT_PROFILE_IMAGE_PATH.FAITHFUL_USER);
                    } else if (response.data.role === USER_ROLE.NORMAL_USER) {
                        setUserProfileImageUrl(DEFAULT_PROFILE_IMAGE_PATH.NORMAL_USER);
                    } else {
                        setUserProfileImageUrl(DEFAULT_PROFILE_IMAGE_PATH.ADMIN);
                    }
                }
            } catch(error) {
                console.error(error.toString());
                console.error(error);
            }
        }

        loadUserProfile();
    }, [profileImageUpdated]);

    const changeUserNickname = (nickname) => {
        if (nickname.length > 0) {
            setUserNickname(nickname);
        }
    }

    const changeProfileImageUrl = (url) => {
        setUserProfileImageUrl(url);
    }

    const changeProfileImageUpdated = () => {
        setProfileImageUpdated(!profileImageUpdated);
    }

    const handleLogout = () => {
        logout(() => navigate("/"));
    }

    return (
        <div className="mypage-container">
            {currentView === "mypage" && (
                <div className={"mypage-content"}>
                    <div className="mypage-section">
                        <div className="section-header">
                            <h2>내 정보</h2>
                            <button
                                className="logout-button"
                                onClick={handleLogout}
                            >로그아웃
                            </button>
                        </div>
                        <div className="profile-info">
                            <img src={userProfileImageUrl} alt="Profile Picture" className="profile-picture"/>
                            <div className="user-info">
                                <strong>{userNickname}</strong>
                                <span>{userRole} / {userEmail}</span>
                            </div>
                        </div>
                    </div>
                    <div className="mypage-section">
                        <h2>계정</h2>
                        <ul>
                            <li
                                className="clickable-item"
                                onClick={() => setCurrentView("changeNickname")}
                            >닉네임 변경
                            </li>
                            <li
                                className="clickable-item"
                                onClick={() => setCurrentView("changePassword")}
                            >비밀번호 변경
                            </li>
                            <li
                                className="clickable-item"
                                onClick={() => setCurrentView("changeProfileImage")}
                            >프로필 사진 변경
                            </li>
                        </ul>
                    </div>
                    <div className="mypage-section">
                        <h2>커뮤니티</h2>
                        <ul>
                            <li
                                className="clickable-item"
                                onClick={() => setCurrentView("myPosts")}
                            >내가 쓴 게시글 보기</li>
                            <li
                                className="clickable-item"
                                onClick={() => setCurrentView("myComments")}
                            >내가 쓴 댓글 보기</li>
                            <li
                                className="clickable-item"
                                onClick={() => setCurrentView("myScrappedPosts")}
                            >스크랩한 게시글 보기</li>
                        </ul>
                    </div>
                    <div className="mypage-section">
                        <h2>기타</h2>
                        <ul>
                            <li
                                className="clickable-item"
                                onClick={() => setCurrentView("deleteAccount")}
                            >회원 탈퇴</li>
                        </ul>
                    </div>
                </div>
                )}

                {currentView === "changeNickname" && (
                    <ChangeNickname
                        nickname={userNickname}
                        changeNickname={changeUserNickname}
                        onBack={() => setCurrentView("mypage")}
                    />
                )}

                {currentView === "changePassword" && (
                    <ChangePassword
                        onBack={() => setCurrentView("mypage")}
                    />
                )}

                {currentView === "changeProfileImage" && (
                    <ChangeProfileImage
                        userProfileImageUrl={userProfileImageUrl}
                        userRole={userRole}
                        profileImageUpdated={profileImageUpdated}
                        changeProfileImageUpdated={changeProfileImageUpdated}
                        changeProfileUrl={changeProfileImageUrl}
                        onBack={() => setCurrentView("mypage")}
                    />
                )}

                {currentView === "myPosts" && (
                    <MyBoards
                        onBack={() => setCurrentView("mypage")}
                        closeModal={closeModal}
                    />
                )}

                {currentView === "myComments" && (
                    <MyReplies
                        onBack={() => setCurrentView("mypage")}
                        closeModal={closeModal}
                    />
                )}
                {currentView === "myScrappedPosts" && (
                    <MyScrappedBoards
                        onBack={() => setCurrentView("mypage")}
                        closeModal={closeModal}
                    />
                )}
                {currentView === "deleteAccount" && (
                    <DeleteAccount
                        onBack={()=> setCurrentView('mypage')}
                        closeModal={closeModal}
                    />
                )}
            </div>
    );
}

MyPage.propTypes = {
    closeModal: PropTypes.func,
}

export default MyPage;

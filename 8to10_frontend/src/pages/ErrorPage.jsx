import { Container, Typography, Button } from "@mui/material";
import { ErrorOutline } from "@mui/icons-material";
import { useNavigate } from "react-router-dom";
import {useAuth} from "@/context/auth/UseAuth.jsx";

const ErrorPage = () => {
    const navigate = useNavigate();
    const {isAuthenticated} = useAuth();

    return (
        <Container
            maxWidth="sm"
            sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                height: "100vh",
                textAlign: "center",
            }}
        >
            {/* 에러 아이콘 */}
            <ErrorOutline sx={{ fontSize: 80, color: "red", mb: 2 }} />

            {/* 에러 메시지 */}
            <Typography variant="h4" fontWeight="bold" gutterBottom>
                죄송합니다. 요청하신 페이지를 찾을 수 없습니다.
            </Typography>
            <Typography variant="body1" color="textSecondary" gutterBottom>
                방문하시려는 페이지의 주소가 잘못 입력되었거나, 페이지의 주소가 변경 혹은 삭제되어 요청하신 페이지를 찾을 수 없습니다. 입력하신 주소가 정확한지 다시 한번 확인해 주시기 바랍니다. 감사합니다.
            </Typography>

            {/* 홈으로 돌아가기 버튼 */}
            {isAuthenticated ? (
                <Button
                    variant="contained"
                    color="primary"
                    sx={{ mt: 3 }}
                    onClick={() => navigate("/home")}
                >
                    홈으로 돌아가기
                </Button>
            ) : (
                <Button
                    variant="contained"
                    color="primary"
                    sx={{ mt: 3 }}
                    onClick={() => navigate("/")}
                >
                    로그인 페이지로 돌아가기
                </Button>
            )}
        </Container>
    );
};

export default ErrorPage;

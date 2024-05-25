// 예시 데이터 (여러 개의 게시글)
const exampleData = [
    {
        "board_id": 1,
        "title": "지속 가능한 농업 기술",
        "content": "식량 생산을 위한 지속 가능한 농업 기술의 발전에 대한 설명.",
        "member_nickname": "사용자1",
        "created_at": "2024-03-30T11:36:02.808066",
        "updated_at": "2024-03-31T13:36:02.808066",
        "total_like": 81,
        "total_scrap": 6
    },
    {
        "board_id": 2,
        "title": "기후 변화와 대응",
        "content": "기후 변화의 심각성과 이에 대한 전 세계적인 대응 전략에 대해 분석.",
        "member_nickname": "사용자2",
        "created_at": "2024-05-10T11:36:02.808097",
        "updated_at": "2024-05-11T05:36:02.808097",
        "total_like": 22,
        "total_scrap": 29
    },
    {
        "board_id": 3,
        "title": "전기차의 미래",
        "content": "전기차 기술의 최신 동향과 미래 전망에 대한 토론.",
        "member_nickname": "사용자3",
        "created_at": "2024-05-14T11:36:02.808115",
        "updated_at": "2024-05-16T11:36:02.808115",
        "total_like": 170,
        "total_scrap": 10
    },
    {
        "board_id": 4,
        "title": "스마트 홈 기술",
        "content": "가정 자동화를 위한 최신 스마트 홈 기술의 개요.",
        "member_nickname": "사용자4",
        "created_at": "2024-05-05T11:36:02.808125",
        "updated_at": "2024-05-06T19:36:02.808125",
        "total_like": 9,
        "total_scrap": 30
    },
    {
        "board_id": 5,
        "title": "재생 에너지의 이점",
        "content": "태양열, 풍력 및 기타 재생 가능 에너지 소스의 이점을 탐구합니다.",
        "member_nickname": "사용자5",
        "created_at": "2024-04-02T11:36:02.808136",
        "updated_at": "2024-04-03T23:36:02.808136",
        "total_like": 199,
        "total_scrap": 41
    },
    {
        "board_id": 6,
        "title": "전기차의 미래",
        "content": "전기차 기술의 최신 동향과 미래 전망에 대한 토론.",
        "member_nickname": "사용자6",
        "created_at": "2024-04-03T11:36:02.808151",
        "updated_at": "2024-04-03T18:36:02.808151",
        "total_like": 136,
        "total_scrap": 29
    },
    {
        "board_id": 7,
        "title": "스마트 홈 기술",
        "content": "가정 자동화를 위한 최신 스마트 홈 기술의 개요.",
        "member_nickname": "사용자7",
        "created_at": "2024-03-23T11:36:02.808164",
        "updated_at": "2024-03-25T00:36:02.808164",
        "total_like": 59,
        "total_scrap": 40
    },
    {
        "board_id": 8,
        "title": "바이오매스 에너지",
        "content": "바이오매스 에너지의 잠재력과 환경적 영향에 대한 분석.",
        "member_nickname": "사용자8",
        "created_at": "2024-04-05T11:36:02.808176",
        "updated_at": "2024-04-05T22:36:02.808176",
        "total_like": 197,
        "total_scrap": 47
    },
    {
        "board_id": 9,
        "title": "지속 가능한 농업 기술",
        "content": "식량 생산을 위한 지속 가능한 농업 기술의 발전에 대한 설명.",
        "member_nickname": "사용자9",
        "created_at": "2024-04-21T11:36:02.808188",
        "updated_at": "2024-04-22T23:36:02.808188",
        "total_like": 128,
        "total_scrap": 16
    },
    {
        "board_id": 10,
        "title": "도시 숲의 중요성",
        "content": "도시 환경에서 숲이 어떻게 기후 조절에 기여하는지에 대한 연구.",
        "member_nickname": "사용자10",
        "created_at": "2024-03-31T11:36:02.808198",
        "updated_at": "2024-04-01T04:36:02.808198",
        "total_like": 52,
        "total_scrap": 43
    },
    {
        "board_id": 11,
        "title": "바이오매스 에너지",
        "content": "바이오매스 에너지의 잠재력과 환경적 영향에 대한 분석.",
        "member_nickname": "사용자11",
        "created_at": "2024-04-07T11:36:02.808207",
        "updated_at": "2024-04-08T04:36:02.808207",
        "total_like": 49,
        "total_scrap": 50
    },
    {
        "board_id": 12,
        "title": "재생 에너지의 이점",
        "content": "태양열, 풍력 및 기타 재생 가능 에너지 소스의 이점을 탐구합니다.",
        "member_nickname": "사용자12",
        "created_at": "2024-03-22T11:36:02.808215",
        "updated_at": "2024-03-23T16:36:02.808215",
        "total_like": 176,
        "total_scrap": 45
    },
    {
        "board_id": 13,
        "title": "기후 변화와 대응",
        "content": "기후 변화의 심각성과 이에 대한 전 세계적인 대응 전략에 대해 분석.",
        "member_nickname": "사용자13",
        "created_at": "2024-03-29T11:36:02.808224",
        "updated_at": "2024-03-31T08:36:02.808224",
        "total_like": 154,
        "total_scrap": 4
    },
    {
        "board_id": 14,
        "title": "미래 도시 설계",
        "content": "스마트 도시 기술과 지속 가능한 개발에 대한 논의.",
        "member_nickname": "사용자14",
        "created_at": "2024-05-01T11:36:02.808232",
        "updated_at": "2024-05-02T19:36:02.808232",
        "total_like": 144,
        "total_scrap": 23
    },
    {
        "board_id": 15,
        "title": "전기차의 미래",
        "content": "전기차 기술의 최신 동향과 미래 전망에 대한 토론.",
        "member_nickname": "사용자15",
        "created_at": "2024-04-24T11:36:02.808240",
        "updated_at": "2024-04-25T10:36:02.808240",
        "total_like": 136,
        "total_scrap": 28
    },
    {
        "board_id": 16,
        "title": "바이오매스 에너지",
        "content": "바이오매스 에너지의 잠재력과 환경적 영향에 대한 분석.",
        "member_nickname": "사용자16",
        "created_at": "2024-04-24T11:36:02.808248",
        "updated_at": "2024-04-25T11:36:02.808248",
        "total_like": 118,
        "total_scrap": 49
    },
    {
        "board_id": 17,
        "title": "미래 도시 설계",
        "content": "스마트 도시 기술과 지속 가능한 개발에 대한 논의.",
        "member_nickname": "사용자17",
        "created_at": "2024-03-27T11:36:02.808257",
        "updated_at": "2024-03-27T20:36:02.808257",
        "total_like": 36,
        "total_scrap": 11
    },
    {
        "board_id": 18,
        "title": "환경 보호를 위한 새로운 기술",
        "content": "이 기사는 최신 환경 기술과 그 기술이 어떻게 지구를 보호하는 데 도움이 될 수 있는지에 대해 설명합니다.",
        "member_nickname": "사용자18",
        "created_at": "2024-04-19T11:36:02.808280",
        "updated_at": "2024-04-21T07:36:02.808280",
        "total_like": 158,
        "total_scrap": 12
    },
    {
        "board_id": 19,
        "title": "미래 도시 설계",
        "content": "스마트 도시 기술과 지속 가능한 개발에 대한 논의.",
        "member_nickname": "사용자19",
        "created_at": "2024-04-03T11:36:02.808295",
        "updated_at": "2024-04-04T23:36:02.808295",
        "total_like": 140,
        "total_scrap": 0
    },
    {
        "board_id": 20,
        "title": "미래 도시 설계",
        "content": "스마트 도시 기술과 지속 가능한 개발에 대한 논의.",
        "member_nickname": "사용자20",
        "created_at": "2024-04-18T11:36:02.808307",
        "updated_at": "2024-04-20T09:36:02.808307",
        "total_like": 62,
        "total_scrap": 39
    },
    {
        "board_id": 21,
        "title": "환경 보호를 위한 새로운 기술",
        "content": "이 기사는 최신 환경 기술과 그 기술이 어떻게 지구를 보호하는 데 도움이 될 수 있는지에 대해 설명합니다.",
        "member_nickname": "사용자21",
        "created_at": "2024-04-03T11:36:02.808315",
        "updated_at": "2024-04-04T23:36:02.808315",
        "total_like": 136,
        "total_scrap": 11
    },
    {
        "board_id": 22,
        "title": "전기차의 미래",
        "content": "전기차 기술의 최신 동향과 미래 전망에 대한 토론.",
        "member_nickname": "사용자22",
        "created_at": "2024-04-08T11:36:02.808326",
        "updated_at": "2024-04-10T05:36:02.808326",
        "total_like": 66,
        "total_scrap": 21
    },
    {
        "board_id": 23,
        "title": "기후 변화와 대응",
        "content": "기후 변화의 심각성과 이에 대한 전 세계적인 대응 전략에 대해 분석.",
        "member_nickname": "사용자23",
        "created_at": "2024-03-23T11:36:02.808334",
        "updated_at": "2024-03-25T10:36:02.808334",
        "total_like": 170,
        "total_scrap": 24
    },
    {
        "board_id": 24,
        "title": "환경 보호를 위한 새로운 기술",
        "content": "이 기사는 최신 환경 기술과 그 기술이 어떻게 지구를 보호하는 데 도움이 될 수 있는지에 대해 설명합니다.",
        "member_nickname": "사용자24",
        "created_at": "2024-05-02T11:36:02.808343",
        "updated_at": "2024-05-04T05:36:02.808343",
        "total_like": 18,
        "total_scrap": 34
    },
    {
        "board_id": 25,
        "title": "바이오매스 에너지",
        "content": "바이오매스 에너지의 잠재력과 환경적 영향에 대한 분석.",
        "member_nickname": "사용자25",
        "created_at": "2024-05-05T11:36:02.808351",
        "updated_at": "2024-05-07T02:36:02.808351",
        "total_like": 170,
        "total_scrap": 38
    },
    {
        "board_id": 26,
        "title": "바이오매스 에너지",
        "content": "바이오매스 에너지의 잠재력과 환경적 영향에 대한 분석.",
        "member_nickname": "사용자26",
        "created_at": "2024-04-09T11:36:02.808360",
        "updated_at": "2024-04-10T15:36:02.808360",
        "total_like": 5,
        "total_scrap": 27
    },
    {
        "board_id": 27,
        "title": "미래 도시 설계",
        "content": "스마트 도시 기술과 지속 가능한 개발에 대한 논의.",
        "member_nickname": "사용자27",
        "created_at": "2024-05-15T11:36:02.808368",
        "updated_at": "2024-05-17T07:36:02.808368",
        "total_like": 139,
        "total_scrap": 50
    },
    {
        "board_id": 28,
        "title": "재생 에너지의 이점",
        "content": "태양열, 풍력 및 기타 재생 가능 에너지 소스의 이점을 탐구합니다.",
        "member_nickname": "사용자28",
        "created_at": "2024-04-09T11:36:02.808376",
        "updated_at": "2024-04-11T07:36:02.808376",
        "total_like": 117,
        "total_scrap": 42
    },
    {
        "board_id": 29,
        "title": "도시 숲의 중요성",
        "content": "도시 환경에서 숲이 어떻게 기후 조절에 기여하는지에 대한 연구.",
        "member_nickname": "사용자29",
        "created_at": "2024-05-04T11:36:02.808384",
        "updated_at": "2024-05-06T10:36:02.808384",
        "total_like": 90,
        "total_scrap": 29
    },
    {
        "board_id": 30,
        "title": "도시 숲의 중요성",
        "content": "도시 환경에서 숲이 어떻게 기후 조절에 기여하는지에 대한 연구.",
        "member_nickname": "사용자30",
        "created_at": "2024-05-07T11:36:02.808393",
        "updated_at": "2024-05-07T13:36:02.808393",
        "total_like": 60,
        "total_scrap": 38
    }
];

document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const mode = urlParams.get('mode');
    const postId = urlParams.get('postId');
    const title = urlParams.get('title');
    const content = urlParams.get('content');

    if (mode === 'update') {
        document.querySelector('.create-post-header h2').textContent = '글 수정';
        document.getElementById('post-title').value = decodeURIComponent(title);
        document.getElementById('post-content').value = decodeURIComponent(content);
        document.querySelector('.post-submit-btn').textContent = '수정';
        document.querySelector('.post-submit-btn').onclick = function() {
            updatePost(postId);
        };
    }
});

function submitPost() {
    const title = document.getElementById('post-title').value.trim();
    const content = document.getElementById('post-content').value.trim();
    let isValid = true;

    if (!title) {
        document.getElementById('title-error').textContent = '글 제목을 입력하세요.';
        document.getElementById('title-error').style.display = 'block';
        isValid = false;
    } else {
        document.getElementById('title-error').style.display = 'none';
    }

    if (!content) {
        document.getElementById('content-error').textContent = '글 내용을 입력하세요.';
        document.getElementById('content-error').style.display = 'block';
        isValid = false;
    } else {
        document.getElementById('content-error').style.display = 'none';
    }

    if (isValid) {
        // Form data to be added to exampleData
        const postData = {
            board_id: exampleData.length + 1,
            title: title,
            content: content,
            member_nickname: '사용자', // Example user
            created_at: new Date().toISOString(),
            updated_at: new Date().toISOString(),
            total_like: 0,
            total_scrap: 0
        };

        exampleData.push(postData);
        console.log(exampleData);
        // For now, simulate successful submission
        console.log('Post submitted:', postData);
        window.location.href = '/community';
    }
}

function updatePost(postId) {
    const title = document.getElementById('post-title').value.trim();
    const content = document.getElementById('post-content').value.trim();
    let isValid = true;

    if (!title) {
        document.getElementById('title-error').textContent = '글 제목을 입력하세요.';
        document.getElementById('title-error').style.display = 'block';
        isValid = false;
    } else {
        document.getElementById('title-error').style.display = 'none';
    }

    if (!content) {
        document.getElementById('content-error').textContent = '글 내용을 입력하세요.';
        document.getElementById('content-error').style.display = 'block';
        isValid = false;
    } else {
        document.getElementById('content-error').style.display = 'none';
    }

    if (isValid) {
        // Find the post in exampleData and update it
        const postIndex = exampleData.findIndex(post => post.board_id == postId);
        if (postIndex !== -1) {
            exampleData[postIndex].title = title;
            exampleData[postIndex].content = content;
            exampleData[postIndex].updated_at = new Date().toISOString();

            // For now, simulate successful update
            window.location.href = '/community';
        }
    }
}

// 게시글 보기 -> 서버랑 통신할 수 있게 바꿔야 하는 함수
function showPostDetail(postId) {
    const postContent = document.getElementById('post-content');

    const defaultProfileImageUrl = "https://via.placeholder.com/100"; // 기본 이미지 URL

    // 해당 게시글 데이터 가져오기 (예제 데이터 사용)
    const post = exampleData.find(post => post.board_id === postId);

    if (post) {
        postContent.innerHTML = `
            <div class="post-detail">
                <div class="post-header">
                    <h3 class="post-title">${post.title}</h3>
                    <div class="post-header-button">
                        <button onclick="editPost(${postId})">수정</button>
                        <button onclick="history.back()">글 목록</button>
                        <button class="delete-button" onclick="deletePost(this)">삭제</button>
                    </div>
                </div>
                <div class="post-info">
                    <img src="${defaultProfileImageUrl}" alt="프로필 사진" class="post-profile-image">
                    <div class="post-author-date">
                        <span class="post-author">${post.member_nickname}</span>
                        <span class="post-date">${new Date(post.created_at).toLocaleDateString()}</span>
                    </div>
                </div>
                <p class="post-body">${post.content}</p>
                <div class="post-stats">
                    <div class="post-likes">
                        <button id="like-button" class="like-button">
                            <span class="heart">좋아요</span> 
                            <span id="like-count">${post.total_like}</span>
                        </button>
                    </div>
                    <div class="post-scraps">
                        <button id="scrap-button" onclick="toggleScrap(${postId})">스크랩</button>
                        <span id="scrap-count">${post.total_scrap}</span>
                    </div>
                </div>
            </div>
        `;
        // Like button functionality
        document.getElementById('like-button').addEventListener('click', function() {
            toggleLike(this);
        });
    } else {
        postContent.innerHTML = '<p>게시글을 찾을 수 없습니다.</p>';
    }
}

// 글 수정 페이지로 이동하는 함수
function editPost(postId) {
    const post = exampleData.find(post => post.board_id === postId);
    if (post) {
        const url = `/community/post?mode=update&postId=${postId}&title=${encodeURIComponent(post.title)}&content=${encodeURIComponent(post.content)}`;
        window.location.href = url;
    }
}